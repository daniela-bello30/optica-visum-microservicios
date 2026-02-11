package pe.edu.cibertec.carrito.controller;

import pe.edu.cibertec.carrito.dto.CarritoDTO;
import pe.edu.cibertec.carrito.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<CarritoDTO> obtenerCarrito(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(carritoService.obtenerCarritoActivo(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{idUsuario}/agregar")
    public ResponseEntity<CarritoDTO> agregarProducto(
            @PathVariable Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam(defaultValue = "1") Integer cantidad) {
        try {
            return ResponseEntity.ok(carritoService.agregarProducto(idUsuario, idProducto, cantidad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idUsuario}/actualizar")
    public ResponseEntity<CarritoDTO> actualizarCantidad(
            @PathVariable Long idUsuario,
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad) {
        try {
            return ResponseEntity.ok(carritoService.actualizarCantidad(idUsuario, idProducto, cantidad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idUsuario}/eliminar/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto) {
        try {
            carritoService.eliminarProducto(idUsuario, idProducto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idUsuario}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long idUsuario) {
        try {
            carritoService.vaciarCarrito(idUsuario);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}