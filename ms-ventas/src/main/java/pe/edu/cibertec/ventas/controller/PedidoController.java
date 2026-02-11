package pe.edu.cibertec.ventas.controller;

import pe.edu.cibertec.ventas.dto.PedidoRequest;
import pe.edu.cibertec.ventas.model.Pedido;
import pe.edu.cibertec.ventas.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody PedidoRequest request) {
        try {
            Pedido pedido = pedidoService.crearPedido(request);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoService.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        try {
            return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}