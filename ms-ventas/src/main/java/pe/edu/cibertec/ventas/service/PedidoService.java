package pe.edu.cibertec.ventas.service;

import pe.edu.cibertec.ventas.client.CarritoClient;
import pe.edu.cibertec.ventas.client.CatalogoClient;
import pe.edu.cibertec.ventas.dto.PedidoRequest;
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
        // Obtener carrito del usuario
        Map<String, Object> carritoData = carritoClient.obtenerCarrito(request.getIdUsuario());
        List<Map<String, Object>> items = (List<Map<String, Object>>) carritoData.get("items");

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(generarNumeroPedido());
        pedido.setIdUsuario(request.getIdUsuario());
        pedido.setTipoEntrega(request.getTipoEntrega());
        pedido.setIdSucursalRecojo(request.getIdSucursalRecojo());
        pedido.setNotas(request.getNotas());

        // Asignar dirección de envío si es envío a domicilio
        if ("Envio".equals(request.getTipoEntrega())) {
            DireccionEnvio direccion = direccionEnvioRepository.findById(request.getIdDireccionEnvio())
                    .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
            pedido.setDireccionEnvio(direccion);
            pedido.setCostoEnvio(new BigDecimal("15.00"));
        }

        // Asignar método de pago
        MetodoPago metodoPago = metodoPagoRepository.findById(request.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        pedido.setMetodoPago(metodoPago);

        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            BigDecimal itemSubtotal = new BigDecimal(item.get("subtotal").toString());
            subtotal = subtotal.add(itemSubtotal);
        }

        pedido.setSubtotal(subtotal);
        pedido.setTotal(subtotal.add(pedido.getCostoEnvio()).subtract(pedido.getDescuento()));

        // Guardar pedido
        pedido = pedidoRepository.save(pedido);

        // Crear detalles del pedido y actualizar stock
        for (Map<String, Object> item : items) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setIdProducto(Long.parseLong(item.get("idProducto").toString()));
            detalle.setCantidad((Integer) item.get("cantidad"));
            detalle.setPrecioUnitario(new BigDecimal(item.get("precioUnitario").toString()));
            detalle.setSubtotal(new BigDecimal(item.get("subtotal").toString()));

            detallePedidoRepository.save(detalle);

            // Actualizar stock en MS-CATALOGO
            catalogoClient.actualizarStock(detalle.getIdProducto(), detalle.getCantidad());
        }

        // Vaciar carrito
        carritoClient.vaciarCarrito(request.getIdUsuario());

        return pedido;
    }

    public List<Pedido> obtenerPedidosPorUsuario(Long idUsuario) {
        return pedidoRepository.findByIdUsuarioOrderByFechaPedidoDesc(idUsuario);
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstadoPedido(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    private String generarNumeroPedido() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "PED-" + timestamp;
    }
}