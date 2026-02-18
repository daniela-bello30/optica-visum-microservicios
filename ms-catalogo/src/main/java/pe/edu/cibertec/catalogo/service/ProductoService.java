package pe.edu.cibertec.catalogo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.catalogo.dto.ProductoRequest;
import pe.edu.cibertec.catalogo.dto.ProductoResponse;
import pe.edu.cibertec.catalogo.exception.BusinessException;
import pe.edu.cibertec.catalogo.exception.ResourceNotFoundException;
import pe.edu.cibertec.catalogo.model.Categoria;
import pe.edu.cibertec.catalogo.model.Marca;
import pe.edu.cibertec.catalogo.model.Producto;
import pe.edu.cibertec.catalogo.repository.CategoriaRepository;
import pe.edu.cibertec.catalogo.repository.MarcaRepository;
import pe.edu.cibertec.catalogo.repository.ProductoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;

    /**
     * Lista todos los productos activos
     */
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        log.debug("Listando todos los productos activos");

        return productoRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por ID
     *
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        log.debug("Obteniendo producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        return convertirAResponse(producto);
    }
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorCategoria(Long idCategoria) {
        log.debug("Buscando productos por categoría: {}", idCategoria);

        return productoRepository.findByCategoriaIdCategoriaAndActivoTrue(idCategoria)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorMarca(Long idMarca) {
        log.debug("Buscando productos por marca: {}", idMarca);

        return productoRepository.findByMarcaIdMarcaAndActivoTrue(idMarca)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
    /**
     * Crea un nuevo producto con validaciones de negocio
     *
     * @throws ResourceNotFoundException si la categoría o marca no existen
     * @throws BusinessException si hay problemas con los datos de negocio
     */
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        log.info("Creando nuevo producto: {}", request.getNombre());

        // Validar que categoría existe
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría", "id", request.getIdCategoria()));

        // Validar que marca existe
        Marca marca = marcaRepository.findById(request.getIdMarca())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Marca", "id", request.getIdMarca()));

        // Validar SKU único (si se proporciona)
        if (request.getSku() != null && !request.getSku().isBlank()) {
            if (productoRepository.existsBySku(request.getSku())) {
                throw new BusinessException(
                        "Ya existe un producto con el SKU: " + request.getSku(),
                        "DUPLICATE_SKU");
            }
        }

        // Validar código de barras único (si se proporciona)
        if (request.getCodigoBarras() != null && !request.getCodigoBarras().isBlank()) {
            if (productoRepository.existsByCodigoBarras(request.getCodigoBarras())) {
                throw new BusinessException(
                        "Ya existe un producto con el código de barras: " + request.getCodigoBarras(),
                        "DUPLICATE_BARCODE");
            }
        }


        // Crear producto
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setDescuento(request.getDescuento() != null ? request.getDescuento() : BigDecimal.ZERO);
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setColor(request.getColor());
        producto.setForma(request.getForma());
        producto.setMaterial(request.getMaterial());
        producto.setGenero(request.getGenero());
        producto.setDestacado(request.getDestacado() != null ? request.getDestacado() : false);
        producto.setEsNuevo(request.getEsNuevo() != null ? request.getEsNuevo() : false);
        producto.setSku(request.getSku());
        producto.setCodigoBarras(request.getCodigoBarras());
        producto.setActivo(true);
        producto.setFechaCreacion(LocalDateTime.now());

        Producto productoGuardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getIdProducto());

        return convertirAResponse(productoGuardado);
    }

    /**
     * Actualiza un producto existente
     *
     * @throws ResourceNotFoundException si el producto, categoría o marca no existen
     * @throws BusinessException si hay conflictos con SKU o código de barras
     */
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        // Validar categoría
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría", "id", request.getIdCategoria()));

        // Validar marca
        Marca marca = marcaRepository.findById(request.getIdMarca())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Marca", "id", request.getIdMarca()));

        // Validar SKU único (excluyendo el producto actual)
        if (request.getSku() != null && !request.getSku().isBlank()) {
            productoRepository.findBySku(request.getSku())
                    .ifPresent(p -> {
                        if (!p.getIdProducto().equals(id)) {
                            throw new BusinessException(
                                    "Ya existe otro producto con el SKU: " + request.getSku(),
                                    "DUPLICATE_SKU");
                        }
                    });
        }

        // Validar código de barras único
        if (request.getCodigoBarras() != null && !request.getCodigoBarras().isBlank()) {
            productoRepository.findByCodigoBarras(request.getCodigoBarras())
                    .ifPresent(p -> {
                        if (!p.getIdProducto().equals(id)) {
                            throw new BusinessException(
                                    "Ya existe otro producto con el código de barras: " + request.getCodigoBarras(),
                                    "DUPLICATE_BARCODE");
                        }
                    });
        }

        // Actualizar campos
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setDescuento(request.getDescuento() != null ? request.getDescuento() : BigDecimal.ZERO);
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setColor(request.getColor());
        producto.setForma(request.getForma());
        producto.setMaterial(request.getMaterial());
        producto.setGenero(request.getGenero());
        producto.setDestacado(request.getDestacado() != null ? request.getDestacado() : producto.getDestacado());
        producto.setEsNuevo(request.getEsNuevo() != null ? request.getEsNuevo() : producto.getEsNuevo());
        producto.setSku(request.getSku());
        producto.setCodigoBarras(request.getCodigoBarras());
        producto.setFechaModificacion(LocalDateTime.now());

        Producto productoActualizado = productoRepository.save(producto);
        log.info("Producto actualizado exitosamente: {}", id);

        return convertirAResponse(productoActualizado);
    }

    /**
     * Elimina (soft delete) un producto
     *
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        producto.setActivo(false);
        producto.setFechaModificacion(LocalDateTime.now());
        productoRepository.save(producto);

        log.info("Producto eliminado (soft delete): {}", id);
    }

    /**
     * Busca productos por texto en nombre o descripción
     */
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorTexto(String texto) {
        log.debug("Buscando productos con texto: {}", texto);

        return productoRepository.buscarPorTexto(texto)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lista productos destacados
     */
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarDestacados() {
        log.debug("Listando productos destacados");

        return productoRepository.findByDestacadoTrueAndActivoTrue()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el stock de un producto
     *
     * @param id ID del producto
     * @param cantidad Cantidad a agregar (positivo) o restar (negativo)
     * @throws ResourceNotFoundException si el producto no existe
     * @throws BusinessException si el stock resultante sería negativo
     */
    @Transactional
    public ProductoResponse actualizarStock(Long id, Integer cantidad) {
        log.info("Actualizando stock del producto {}: {}", id, cantidad);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        Integer nuevoStock = producto.getStock() + cantidad;

        if (nuevoStock < 0) {
            throw new BusinessException(
                    String.format("Stock insuficiente. Stock actual: %d, cantidad solicitada: %d",
                            producto.getStock(), Math.abs(cantidad)),
                    "INSUFFICIENT_STOCK");
        }

        producto.setStock(nuevoStock);
        producto.setFechaModificacion(LocalDateTime.now());

        Producto productoActualizado = productoRepository.save(producto);
        log.info("Stock actualizado. Nuevo stock: {}", nuevoStock);

        return convertirAResponse(productoActualizado);
    }

    /**
     * Convierte una entidad Producto a ProductoResponse
     */
    private ProductoResponse convertirAResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();

        response.setIdProducto(producto.getIdProducto());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setDescuento(producto.getDescuento() != null ? producto.getDescuento() : BigDecimal.ZERO);
        response.setStock(producto.getStock());
        response.setColor(producto.getColor());
        response.setForma(producto.getForma());
        response.setMaterial(producto.getMaterial());
        response.setGenero(producto.getGenero());
        response.setDestacado(producto.getDestacado() != null ? producto.getDestacado() : false);
        response.setEsNuevo(producto.getEsNuevo() != null ? producto.getEsNuevo() : false);
        response.setSku(producto.getSku());
        response.setCodigoBarras(producto.getCodigoBarras());
        response.setActivo(producto.getActivo() != null ? producto.getActivo() : true);
        response.setFechaCreacion(producto.getFechaCreacion());
        response.setFechaModificacion(producto.getFechaModificacion());

        // Calcular precio final con descuento
        BigDecimal precioFinal = producto.getPrecio();
        if (producto.getDescuento() != null && producto.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal factorDescuento = BigDecimal.ONE.subtract(
                    producto.getDescuento().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
            );
            precioFinal = producto.getPrecio().multiply(factorDescuento)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        response.setPrecioFinal(precioFinal);

        // Mapear categoría
        if (producto.getCategoria() != null) {
            ProductoResponse.CategoriaDto categoriaDto = ProductoResponse.CategoriaDto.builder()
                    .idCategoria(producto.getCategoria().getIdCategoria())
                    .nombre(producto.getCategoria().getNombreCategoria())
                    .descripcion(producto.getCategoria().getDescripcion())
                    .build();
            response.setCategoria(categoriaDto);
        }

        // Mapear marca
        if (producto.getMarca() != null) {
            ProductoResponse.MarcaDto marcaDto = ProductoResponse.MarcaDto.builder()
                    .idMarca(producto.getMarca().getIdMarca())
                    .nombre(producto.getMarca().getNombreMarca())
                    .build();
            response.setMarca(marcaDto);
        }

        return response;
    }
}