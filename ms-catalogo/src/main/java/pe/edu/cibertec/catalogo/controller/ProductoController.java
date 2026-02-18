package pe.edu.cibertec.catalogo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.catalogo.dto.ProductoRequest;
import pe.edu.cibertec.catalogo.dto.ProductoResponse;
import pe.edu.cibertec.catalogo.service.ProductoService;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Listar todos los productos activos
     * GET /productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        log.info("Listando todos los productos");
        List<ProductoResponse> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtener un producto por ID
     * GET /productos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        ProductoResponse producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Crear un nuevo producto
     * POST /productos
     */
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        log.info("Creando nuevo producto: {}", request.getNombre());
        ProductoResponse producto = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    /**
     * Actualizar un producto existente
     * PUT /productos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        log.info("Actualizando producto con ID: {}", id);
        ProductoResponse producto = productoService.actualizar(id, request);
        return ResponseEntity.ok(producto);
    }

    /**
     * Eliminar un producto (soft delete)
     * DELETE /productos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando producto con ID: {}", id);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar productos por texto (nombre o descripción)
     * GET /productos/buscar?texto=...
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscarPorNombre(@RequestParam String texto) {
        log.info("Buscando productos por texto: {}", texto);
        List<ProductoResponse> productos = productoService.buscarPorTexto(texto);
        return ResponseEntity.ok(productos);
    }

    /**
     * Listar productos destacados
     * GET /productos/destacados
     */
    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoResponse>> productosDestacados() {
        log.info("Listando productos destacados");
        List<ProductoResponse> productos = productoService.listarDestacados();
        return ResponseEntity.ok(productos);
    }

    /**
     * Actualizar stock de un producto
     * PUT /productos/{id}/stock?cantidad=...
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        log.info("Actualizando stock del producto {}: {}", id, cantidad);
        ProductoResponse producto = productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ProductoResponse>> buscarPorCategoria(@PathVariable Long idCategoria) {
        log.info("Buscando productos por categoría: {}", idCategoria);
        List<ProductoResponse> productos = productoService.buscarPorCategoria(idCategoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/marca/{idMarca}")
    public ResponseEntity<List<ProductoResponse>> buscarPorMarca(@PathVariable Long idMarca) {
        log.info("Buscando productos por marca: {}", idMarca);
        List<ProductoResponse> productos = productoService.buscarPorMarca(idMarca);
        return ResponseEntity.ok(productos);
    }
}