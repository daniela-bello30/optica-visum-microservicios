package pe.edu.cibertec.carrito.repository;

import pe.edu.cibertec.carrito.model.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    List<DetalleCarrito> findByCarrito_IdCarrito(Long idCarrito);

    Optional<DetalleCarrito> findByCarrito_IdCarritoAndIdProducto(Long idCarrito, Long idProducto);

    void deleteByCarrito_IdCarrito(Long idCarrito);
}