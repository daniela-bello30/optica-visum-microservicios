package pe.edu.cibertec.seguridad.service;

import pe.edu.cibertec.seguridad.dto.LoginRequest;
import pe.edu.cibertec.seguridad.dto.LoginResponse;
import pe.edu.cibertec.seguridad.dto.RegisterRequest;
import pe.edu.cibertec.seguridad.exception.BusinessException;
import pe.edu.cibertec.seguridad.model.Rol;
import pe.edu.cibertec.seguridad.model.Usuario;
import pe.edu.cibertec.seguridad.repository.RolRepository;
import pe.edu.cibertec.seguridad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Contraseña incorrecta", "AUTH_ERROR");
        }

        if (!usuario.getEstado()) {
            throw new RuntimeException("Usuario inactivo");
        }

        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return new LoginResponse(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getRol().getNombreRol(),
                "Login exitoso"
        );
    }

    public String register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (usuarioRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad())) {
            throw new RuntimeException("El documento de identidad ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setDocumentoIdentidad(request.getDocumentoIdentidad());
        usuario.setTipoDocumento(request.getTipoDocumento());

        // Asignar rol de CLIENTE por defecto
        Rol rolCliente = rolRepository.findByNombreRol("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        usuario.setRol(rolCliente);

        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
    }

    public Boolean validarUsuario(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }
}