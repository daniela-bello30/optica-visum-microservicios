package pe.edu.cibertec.citas.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequest {
    private Long idUsuario;
    private Long idSucursal;
    private Long idTipoServicio;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String notas;
}