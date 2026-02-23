package pe.edu.cibertec.citas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.cibertec.citas.dto.ApiResponse;
import pe.edu.cibertec.citas.model.Sucursal;
import pe.edu.cibertec.citas.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sucursales")
@CrossOrigin(origins = "*")
@Tag(name = "Sucursales", description = "Endpoints para consultar las sedes de la óptica")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Operation(summary = "Listar todas las sucursales")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Sucursal>>> listarTodas() {
        return ResponseEntity.ok(ApiResponse.success(sucursalService.listarTodas()));
    }

    @Operation(summary = "Obtener sucursal por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sucursal>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sucursalService.obtenerPorId(id)));
    }
}