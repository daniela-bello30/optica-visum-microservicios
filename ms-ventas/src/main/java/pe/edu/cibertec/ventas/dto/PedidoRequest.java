package pe.edu.cibertec.ventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequest {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    private Long idDireccionEnvio;

    @NotBlank(message = "El tipo de entrega es obligatorio")
    private String tipoEntrega; // "Envio" o "Recojo en Tienda"

    private Long idSucursalRecojo;

    @NotNull(message = "El método de pago es obligatorio")
    private Long idMetodoPago;

    private String notas;
}