package pe.edu.cibertec.citas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.citas.dto.ApiResponse;
import pe.edu.cibertec.citas.model.TipoServicio;
import pe.edu.cibertec.citas.repository.TipoServicioRepository; // Puedes usar el repo directo o crear un service

import java.util.List;

@RestController
@RequestMapping("/tipos-servicio")
@CrossOrigin(origins = "*")
@Tag(name = "Servicios", description = "Endpoints para los tipos de exámenes visuales")
public class TipoServicioController {

    @Autowired
    private pe.edu.cibertec.citas.repository.TipoServicioRepository tipoServicioRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoServicio>>> listarTodos() {
        List<TipoServicio> servicios = tipoServicioRepository.findByEstadoTrue();
        return ResponseEntity.ok(ApiResponse.success(servicios));
    }
}