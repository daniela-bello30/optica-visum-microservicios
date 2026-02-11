package pe.edu.cibertec.carrito.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleCarritoDTO {
    private Long idDetalle;
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}