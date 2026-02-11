package pe.edu.cibertec.ventas.dto;

import lombok.Data;

@Data
public class PedidoRequest {
    private Long idUsuario;
    private Long idDireccionEnvio;
    private String tipoEntrega; // "Envio" o "Recojo en Tienda"
    private Long idSucursalRecojo;
    private Long idMetodoPago;
    private String notas;
}