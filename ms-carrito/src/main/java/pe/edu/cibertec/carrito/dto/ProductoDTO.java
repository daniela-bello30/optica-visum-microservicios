package pe.edu.cibertec.carrito.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class ProductoDTO {

    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal precioFinal;
    private Integer stock;
}