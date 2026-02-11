package pe.edu.cibertec.ventas.repository;

import pe.edu.cibertec.ventas.model.DireccionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {
    List<DireccionEnvio> findByIdUsuarioAndEstadoTrue(Long idUsuario);
}