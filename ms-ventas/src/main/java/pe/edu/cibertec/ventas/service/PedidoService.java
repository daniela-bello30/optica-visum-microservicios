package pe.edu.cibertec.ventas.service;

import pe.edu.cibertec.ventas.client.CarritoClient;
import pe.edu.cibertec.ventas.client.CatalogoClient;
import pe.edu.cibertec.ventas.dto.PedidoRequest;
import pe.edu.cibertec.ventas.exception.BusinessException;
import pe.edu.cibertec.ventas.exception.ResourceNotFoundException;
import pe.edu.cibertec.ventas.model.DetallePedido;
import pe.edu.cibertec.ventas.model.DireccionEnvio;
import pe.edu.cibertec.ventas.model.MetodoPago;
import pe.edu.cibertec.ventas.model.Pedido;
import pe.edu.cibertec.ventas.repository.DetallePedidoRepository;
import pe.edu.cibertec.ventas.repository.DireccionEnvioRepository;
import pe.edu.cibertec.ventas.repository.MetodoPagoRepository;
import pe.edu.cibertec.ventas.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private CarritoClient carritoClient;

    @Autowired
    private CatalogoClient catalogoClient;

    @Transactional
    public Pedido crearPedido(PedidoRequest request) {
        // Obtener carrito del usuario comunicándonos con MS-CARRITO
        Map<String, Object> carritoData = carritoClient.obtenerCarrito(request.getIdUsuario());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) carritoData.get("items");

        if (items == null || items.isEmpty()) {
            throw new BusinessException("El carrito está vacío. No se puede generar un pedido.", "EMPTY_CART");
        }

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(generarNumeroPedido());
        pedido.setIdUsuario(request.getIdUsuario());
        pedido.setTipoEntrega(request.getTipoEntrega());
        pedido.setIdSucursalRecojo(request.getIdSucursalRecojo());
        pedido.setNotas(request.getNotas());

        // Asignar dirección de envío si el cliente eligió envío a domicilio
        if ("Envio".equalsIgnoreCase(request.getTipoEntrega())) {
            if (request.getIdDireccionEnvio() == null) {
                throw new BusinessException("Debe especificar una dirección de envío", "MISSING_ADDRESS");
            }
            DireccionEnvio direccion = direccionEnvioRepository.findById(request.getIdDireccionEnvio())
                    .orElseThrow(() -> new ResourceNotFoundException("Dirección de Envío", "id", request.getIdDireccionEnvio()));
            pedido.setDireccionEnvio(direccion);
            pedido.setCostoEnvio(new BigDecimal("15.00")); // Costo fijo por ahora
        }

        // Asignar método de pago
        MetodoPago metodoPago = metodoPagoRepository.findById(request.getIdMetodoPago())
                .orElseThrow(() -> new ResourceNotFoundException("Método de Pago", "id", request.getIdMetodoPago()));
        pedido.setMetodoPago(metodoPago);

        // Calcular totales basándonos en los items del carrito
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            BigDecimal itemSubtotal = new BigDecimal(item.get("subtotal").toString());
            subtotal = subtotal.add(itemSubtotal);
        }

        pedido.setSubtotal(subtotal);
        // Total = (Subtotal + CostoEnvio) - Descuento
        pedido.setTotal(subtotal.add(pedido.getCostoEnvio()).subtract(pedido.getDescuento()));

        // Guardar la cabecera del pedido en BD
        pedido = pedidoRepository.save(pedido);

        // Crear detalles del pedido y actualizar stock en MS-CATALOGO
        for (Map<String, Object> item : items) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setIdProducto(Long.parseLong(item.get("idProducto").toString()));
            detalle.setCantidad((Integer) item.get("cantidad"));
            detalle.setPrecioUnitario(new BigDecimal(item.get("precioUnitario").toString()));
            detalle.setSubtotal(new BigDecimal(item.get("subtotal").toString()));

            detallePedidoRepository.save(detalle);

            // Actualizar stock en MS-CATALOGO (Se enviará en negativo para restar, o puedes ajustarlo según la lógica de tu endpoint)
            // Asumiendo que tu endpoint en catálogo suma la cantidad que le envíes, enviamos en negativo:
            catalogoClient.actualizarStock(detalle.getIdProducto(), -detalle.getCantidad());
        }

        // Vaciar el carrito en MS-CARRITO una vez creado el pedido
        carritoClient.vaciarCarrito(request.getIdUsuario());

        return pedido;
    }

    public List<Pedido> obtenerPedidosPorUsuario(Long idUsuario) {
        return pedidoRepository.findByIdUsuarioOrderByFechaPedidoDesc(idUsuario);
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
    }

    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);

        // Validación básica de estado (opcional, pero buena práctica)
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new BusinessException("El nuevo estado no puede estar vacío", "INVALID_STATUS");
        }

        pedido.setEstadoPedido(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    private String generarNumeroPedido() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "PED-" + timestamp;
    }
}