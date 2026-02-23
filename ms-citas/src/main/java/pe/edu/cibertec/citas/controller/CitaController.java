package pe.edu.cibertec.citas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.edu.cibertec.citas.dto.ApiResponse;
import pe.edu.cibertec.citas.dto.CitaRequest;
import pe.edu.cibertec.citas.model.Cita;
import pe.edu.cibertec.citas.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "*")
@Tag(name = "Citas", description = "Endpoints para agendar y gestionar citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Operation(summary = "Agendar nueva cita")
    @PostMapping
    public ResponseEntity<ApiResponse<Cita>> agendarCita(@Valid @RequestBody CitaRequest request) {
        Cita cita = citaService.agendarCita(request);
        return ResponseEntity.ok(ApiResponse.success("Cita agendada con éxito", cita));
    }

    @Operation(summary = "Listar citas de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<Cita>>> obtenerCitasPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(ApiResponse.success(citaService.obtenerCitasPorUsuario(idUsuario)));
    }

    @Operation(summary = "Obtener cita por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cita>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(citaService.obtenerPorId(id)));
    }

    @Operation(summary = "Reprogramar cita")
    @PutMapping("/{id}/reprogramar")
    public ResponseEntity<ApiResponse<Cita>> reprogramarCita(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        Cita cita = citaService.reprogramarCita(id, fecha, hora);
        return ResponseEntity.ok(ApiResponse.success("Cita reprogramada", cita));
    }

    @Operation(summary = "Cancelar cita")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<Void>> cancelarCita(@PathVariable Long id) {
        citaService.cancelarCita(id);
        return ResponseEntity.ok(ApiResponse.success("Cita cancelada exitosamente"));
    }
}