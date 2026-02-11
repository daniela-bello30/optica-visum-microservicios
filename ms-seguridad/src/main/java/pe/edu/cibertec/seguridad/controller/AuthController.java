package pe.edu.cibertec.seguridad.controller;

import pe.edu.cibertec.seguridad.dto.LoginRequest;
import pe.edu.cibertec.seguridad.dto.LoginResponse;
import pe.edu.cibertec.seguridad.dto.RegisterRequest;
import pe.edu.cibertec.seguridad.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String mensaje = authService.register(request);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/validar/{idUsuario}")
    public ResponseEntity<Boolean> validarUsuario(@PathVariable Long idUsuario) {
        Boolean existe = authService.validarUsuario(idUsuario);
        return ResponseEntity.ok(existe);
    }
}