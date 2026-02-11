package pe.edu.cibertec.seguridad.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String telefono;
    private String documentoIdentidad;
    private String tipoDocumento;
}