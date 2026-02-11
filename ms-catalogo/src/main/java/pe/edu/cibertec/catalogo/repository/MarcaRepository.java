package pe.edu.cibertec.catalogo.repository;

import pe.edu.cibertec.catalogo.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    List<Marca> findByEstadoTrue();
}