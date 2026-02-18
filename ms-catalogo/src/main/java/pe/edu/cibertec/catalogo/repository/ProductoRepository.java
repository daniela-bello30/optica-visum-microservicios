package pe.edu.cibertec.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.catalogo.model.Producto;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Producto
 * Define queries personalizadas para operaciones específicas
 *
 * @author VISUM Team
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Encuentra todos los productos activos
     */
    List<Producto> findByActivoTrue();

    /**
     * Encuentra productos destacados y activos
     */
    List<Producto> findByDestacadoTrueAndActivoTrue();

    /**
     * Encuentra productos nuevos y activos
     */
    List<Producto> findByEsNuevoTrueAndActivoTrue();

    /**
     * Busca productos por texto usando búsqueda en nombre o descripción
     * Busca en nombre y descripción
     */
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
            "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))")
    List<Producto> buscarPorTexto(@Param("texto") String texto);

    // Agregar al ProductoRepository.java

    List<Producto> findByCategoriaIdCategoriaAndActivoTrue(Long idCategoria);
    List<Producto> findByMarcaIdMarcaAndActivoTrue(Long idMarca);
    /**
     * Encuentra productos por categoría
     */
    @Query("SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria AND p.activo = true")
    List<Producto> findByCategoria(@Param("idCategoria") Long idCategoria);

    /**
     * Encuentra productos por marca
     */
    @Query("SELECT p FROM Producto p WHERE p.marca.idMarca = :idMarca AND p.activo = true")
    List<Producto> findByMarca(@Param("idMarca") Long idMarca);

    /**
     * Encuentra productos con stock bajo (menos de cierta cantidad)
     */
    @Query("SELECT p FROM Producto p WHERE p.stock < :cantidad AND p.activo = true")
    List<Producto> findByStockBajo(@Param("cantidad") Integer cantidad);

    /**
     * Encuentra productos por género
     */
    List<Producto> findByGeneroAndActivoTrue(String genero);

    /**
     * Verifica si existe un producto con el SKU dado
     */
    boolean existsBySku(String sku);

    /**
     * Verifica si existe un producto con el código de barras dado
     */
    boolean existsByCodigoBarras(String codigoBarras);

    /**
     * Encuentra un producto por SKU
     */
    Optional<Producto> findBySku(String sku);

    /**
     * Encuentra un producto por código de barras
     */
    Optional<Producto> findByCodigoBarras(String codigoBarras);

    /**
     * Encuentra productos con descuento
     */
    @Query("SELECT p FROM Producto p WHERE p.descuento > 0 AND p.activo = true")
    List<Producto> findProductosConDescuento();
}