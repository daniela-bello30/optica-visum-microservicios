package pe.edu.cibertec.carrito.service;

import pe.edu.cibertec.carrito.client.CatalogoClient;
import pe.edu.cibertec.carrito.dto.CarritoDTO;
import pe.edu.cibertec.carrito.dto.DetalleCarritoDTO;
import pe.edu.cibertec.carrito.dto.ProductoDTO;
import pe.edu.cibertec.carrito.model.Carrito;
import pe.edu.cibertec.carrito.model.DetalleCarrito;
import pe.edu.cibertec.carrito.repository.CarritoRepository;
import pe.edu.cibertec.carrito.repository.DetalleCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private CatalogoClient catalogoClient;

    public CarritoDTO obtenerCarritoActivo(Long idUsuario) {
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(idUsuario, "Activo")
                .orElseGet(() -> crearCarritoNuevo(idUsuario));

        return convertirADTO(carrito);
    }

    private Carrito crearCarritoNuevo(Long idUsuario) {
        Carrito carrito = new Carrito();
        carrito.setIdUsuario(idUsuario);
        carrito.setEstado("Activo");
        return carritoRepository.save(carrito);
    }

    @Transactional
    public CarritoDTO agregarProducto(Long idUsuario, Long idProducto, Integer cantidad) {
        // Obtener o crear carrito
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(idUsuario, "Activo")
                .orElseGet(() -> crearCarritoNuevo(idUsuario));

        // Obtener info del producto desde MS-CATALOGO
        ProductoDTO producto = catalogoClient.obtenerProducto(idProducto);

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Verificar si el producto ya está en el carrito
        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdCarritoAndIdProducto(carrito.getIdCarrito(), idProducto)
                .orElse(null);

        if (detalle != null) {
            // Actualizar cantidad
            detalle.setCantidad(detalle.getCantidad() + cantidad);
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad())));
        } else {
            // Crear nuevo detalle
            detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setIdProducto(idProducto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setSubtotal(producto.getPrecioUnitario().multiply(new BigDecimal(cantidad)));
        }

        detalleCarritoRepository.save(detalle);
        return convertirADTO(carrito);
    }

    @Transactional
    public CarritoDTO actualizarCantidad(Long idUsuario, Long idProducto, Integer nuevaCantidad) {
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(idUsuario, "Activo")
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdCarritoAndIdProducto(carrito.getIdCarrito(), idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        if (nuevaCantidad <= 0) {
            detalleCarritoRepository.delete(detalle);
        } else {
            detalle.setCantidad(nuevaCantidad);
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(new BigDecimal(nuevaCantidad)));
            detalleCarritoRepository.save(detalle);
        }

        return convertirADTO(carrito);
    }

    @Transactional
    public void eliminarProducto(Long idUsuario, Long idProducto) {
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(idUsuario, "Activo")
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdCarritoAndIdProducto(carrito.getIdCarrito(), idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        detalleCarritoRepository.delete(detalle);
    }

    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        Carrito carrito = carritoRepository.findByIdUsuarioAndEstado(idUsuario, "Activo")
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        detalleCarritoRepository.deleteByCarrito_IdCarrito(carrito.getIdCarrito());
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setIdCarrito(carrito.getIdCarrito());
        dto.setIdUsuario(carrito.getIdUsuario());

        List<DetalleCarrito> detalles = detalleCarritoRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());

        List<DetalleCarritoDTO> itemsDTO = detalles.stream().map(detalle -> {
            DetalleCarritoDTO itemDTO = new DetalleCarritoDTO();
            itemDTO.setIdDetalle(detalle.getIdDetalle());
            itemDTO.setIdProducto(detalle.getIdProducto());

            // Obtener nombre del producto desde MS-CATALOGO
            try {
                ProductoDTO producto = catalogoClient.obtenerProducto(detalle.getIdProducto());
                itemDTO.setNombreProducto(producto.getNombreProducto());
            } catch (Exception e) {
                itemDTO.setNombreProducto("Producto no disponible");
            }

            itemDTO.setCantidad(detalle.getCantidad());
            itemDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            itemDTO.setSubtotal(detalle.getSubtotal());

            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(itemsDTO);
        dto.setCantidadItems(itemsDTO.size());

        BigDecimal total = itemsDTO.stream()
                .map(DetalleCarritoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotal(total);

        return dto;
    }
}