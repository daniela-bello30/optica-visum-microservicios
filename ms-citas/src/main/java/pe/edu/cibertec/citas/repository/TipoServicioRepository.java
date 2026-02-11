package pe.edu.cibertec.citas.repository;

import pe.edu.cibertec.citas.model.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {
    List<TipoServicio> findByEstadoTrue();
}