package pe.edu.cibertec.ventas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "numero_pedido", nullable = false, unique = true, length = 20)
    private String numeroPedido;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_direccion_envio")
    private DireccionEnvio direccionEnvio;

    @Column(name = "tipo_entrega", nullable = false, length = 50)
    private String tipoEntrega;

    @Column(name = "id_sucursal_recojo")
    private Long idSucursalRecojo;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "costo_envio", precision = 10, scale = 2)
    private BigDecimal costoEnvio = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado_pedido", length = 50)
    private String estadoPedido = "Pendiente";

    @Column(name = "estado_pago", length = 50)
    private String estadoPago = "Pendiente";

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_pedido", nullable = false, updatable = false)
    private LocalDateTime fechaPedido = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}