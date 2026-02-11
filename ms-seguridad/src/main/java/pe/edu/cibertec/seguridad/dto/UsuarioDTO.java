package pe.edu.cibertec.seguridad.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioDTO {
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String documentoIdentidad;
    private String tipoDocumento;
    private LocalDate fechaNacimiento;
    private String nombreRol;
    private Boolean estado;
}