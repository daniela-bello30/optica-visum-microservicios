package pe.edu.cibertec.catalogo.repository;

import pe.edu.cibertec.catalogo.model.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {
    List<ImagenProducto> findByProducto_IdProductoOrderByOrdenAsc(Long idProducto);
}