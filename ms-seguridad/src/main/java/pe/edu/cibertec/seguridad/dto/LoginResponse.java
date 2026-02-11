package pe.edu.cibertec.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String rol;
    private String mensaje;
}