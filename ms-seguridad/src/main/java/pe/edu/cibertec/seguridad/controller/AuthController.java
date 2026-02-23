package pe.edu.cibertec.seguridad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.edu.cibertec.seguridad.dto.ApiResponse;
import pe.edu.cibertec.seguridad.dto.LoginRequest;
import pe.edu.cibertec.seguridad.dto.LoginResponse;
import pe.edu.cibertec.seguridad.dto.RegisterRequest;
import pe.edu.cibertec.seguridad.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para login y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve datos del usuario")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        // Ya no hay try-catch. Si hay error, el GlobalExceptionHandler lo atrapa.
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario cliente en el sistema")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        String mensaje = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(mensaje));
    }

    @Operation(summary = "Validar si usuario existe", description = "Retorna true si el ID pertenece a un usuario registrado")
    @GetMapping("/validar/{idUsuario}")
    public ResponseEntity<ApiResponse<Boolean>> validarUsuario(@PathVariable Long idUsuario) {
        Boolean existe = authService.validarUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.success("Validación exitosa", existe));
    }
}