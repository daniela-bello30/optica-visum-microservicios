package pe.edu.cibertec.ventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.edu.cibertec.ventas.dto.ApiResponse;
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
@Tag(name = "Pedidos", description = "Endpoints para la gestión de órdenes de compra")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Crear nuevo pedido", description = "Procesa el carrito y genera una orden de compra")
    @PostMapping
    public ResponseEntity<ApiResponse<Pedido>> crearPedido(@Valid @RequestBody PedidoRequest request) {
        Pedido pedido = pedidoService.crearPedido(request);
        return ResponseEntity.ok(ApiResponse.success("Pedido creado exitosamente", pedido));
    }

    @Operation(summary = "Listar pedidos por usuario", description = "Obtiene el historial de compras de un cliente")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<Pedido>>> obtenerPedidosPorUsuario(@PathVariable Long idUsuario) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @Operation(summary = "Obtener detalle de un pedido", description = "Busca un pedido por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pedido>> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(pedido));
    }

    @Operation(summary = "Actualizar estado", description = "Cambia el estado de preparación/envío del pedido")
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Pedido>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        Pedido pedido = pedidoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado a: " + estado, pedido));
    }
}