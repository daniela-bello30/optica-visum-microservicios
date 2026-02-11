package pe.edu.cibertec.catalogo.repository;

import pe.edu.cibertec.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEstadoTrue();

    List<Producto> findByCategoria_IdCategoria(Long idCategoria);

    List<Producto> findByMarca_IdMarca(Long idMarca);

    List<Producto> findByEsDestacadoTrue();

    List<Producto> findByEsNuevoTrue();

    List<Producto> findByEnPromocionTrue();

    @Query("SELECT p FROM Producto p WHERE p.estado = true AND " +
            "LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Producto> buscarPorNombre(String texto);

    @Query("SELECT p FROM Producto p WHERE p.estado = true AND " +
            "p.precioUnitario BETWEEN :min AND :max")
    List<Producto> buscarPorRangoPrecio(BigDecimal min, BigDecimal max);
}