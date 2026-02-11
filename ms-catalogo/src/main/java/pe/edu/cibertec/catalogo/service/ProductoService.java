package pe.edu.cibertec.catalogo.service;

import pe.edu.cibertec.catalogo.model.Producto;
import pe.edu.cibertec.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findByEstadoTrue();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        Producto existente = obtenerPorId(id);

        existente.setNombreProducto(producto.getNombreProducto());
        existente.setDescripcion(producto.getDescripcion());
        existente.setCategoria(producto.getCategoria());
        existente.setMarca(producto.getMarca());
        existente.setPrecioUnitario(producto.getPrecioUnitario());
        existente.setPrecioDescuento(producto.getPrecioDescuento());
        existente.setStock(producto.getStock());
        existente.setColor(producto.getColor());
        existente.setForma(producto.getForma());
        existente.setMaterial(producto.getMaterial());
        existente.setGenero(producto.getGenero());

        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setEstado(false);
        productoRepository.save(producto);
    }

    public List<Producto> buscarPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria);
    }

    public List<Producto> buscarPorMarca(Long idMarca) {
        return productoRepository.findByMarca_IdMarca(idMarca);
    }

    public List<Producto> buscarPorNombre(String texto) {
        return productoRepository.buscarPorNombre(texto);
    }

    public List<Producto> productosDestacados() {
        return productoRepository.findByEsDestacadoTrue();
    }

    public void actualizarStock(Long id, Integer cantidad) {
        Producto producto = obtenerPorId(id);
        producto.setStock(producto.getStock() - cantidad);

        if (producto.getStock() < 0) {
            throw new RuntimeException("Stock insuficiente");
        }

        productoRepository.save(producto);
    }
}