package pe.edu.cibertec.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.ventas.model.DetallePedido;

public interface DetallePedidoRepository
    extends JpaRepository<DetallePedido, Long> {

}
