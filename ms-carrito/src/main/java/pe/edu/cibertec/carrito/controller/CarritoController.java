package pe.edu.cibertec.carrito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import pe.edu.cibertec.carrito.dto.ApiResponse;
import pe.edu.cibertec.carrito.dto.CarritoDTO;
import pe.edu.cibertec.carrito.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")
@Validated // Permite validar @RequestParam y @PathVariable
@Tag(name = "Carrito", description = "Endpoints para la gestión del carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Obtener carrito", description = "Obtiene el carrito activo de un usuario")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<CarritoDTO>> obtenerCarrito(@PathVariable Long idUsuario) {
        CarritoDTO carrito = carritoService.obtenerCarritoActivo(idUsuario);
        return ResponseEntity.ok(ApiResponse.success(carrito));
    }

    @Operation(summary = "Agregar producto", description = "Agrega un producto al carrito del usuario")
    @PostMapping("/{idUsuario}/agregar")
    public ResponseEntity<ApiResponse<CarritoDTO>> agregarProducto(
            @PathVariable Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "La cantidad debe ser al menos 1") Integer cantidad) {

        CarritoDTO carrito = carritoService.agregarProducto(idUsuario, idProducto, cantidad);
        return ResponseEntity.ok(ApiResponse.success("Producto agregado al carrito", carrito));
    }

    @Operation(summary = "Actualizar cantidad", description = "Actualiza la cantidad de un producto en el carrito")
    @PutMapping("/{idUsuario}/actualizar")
    public ResponseEntity<ApiResponse<CarritoDTO>> actualizarCantidad(
            @PathVariable Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam @Min(value = 0, message = "La cantidad no puede ser negativa") Integer cantidad) {

        CarritoDTO carrito = carritoService.actualizarCantidad(idUsuario, idProducto, cantidad);
        return ResponseEntity.ok(ApiResponse.success("Cantidad actualizada", carrito));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del carrito")
    @DeleteMapping("/{idUsuario}/eliminar/{idProducto}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto) {

        carritoService.eliminarProducto(idUsuario, idProducto);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado del carrito"));
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los productos del carrito activo")
    @DeleteMapping("/{idUsuario}/vaciar")
    public ResponseEntity<ApiResponse<Void>> vaciarCarrito(@PathVariable Long idUsuario) {

        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.ok(ApiResponse.success("Carrito vaciado exitosamente"));
    }
}