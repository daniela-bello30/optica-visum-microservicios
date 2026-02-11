package pe.edu.cibertec.carrito.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CarritoDTO {
    private Long idCarrito;
    private Long idUsuario;
    private List<DetalleCarritoDTO> items;
    private BigDecimal total;
    private Integer cantidadItems;
}