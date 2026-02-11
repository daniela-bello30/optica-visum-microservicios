package pe.edu.cibertec.carrito.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long idProducto;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precioUnitario;
    private BigDecimal precioDescuento;
    private Integer stock;
    private String color;
    private String marca;
    private String categoria;
}