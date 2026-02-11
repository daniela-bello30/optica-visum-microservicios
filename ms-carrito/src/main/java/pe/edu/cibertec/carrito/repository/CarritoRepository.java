package pe.edu.cibertec.carrito.repository;

import pe.edu.cibertec.carrito.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByIdUsuarioAndEstado(Long idUsuario, String estado);
}