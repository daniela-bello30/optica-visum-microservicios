package pe.edu.cibertec.ventas.repository;

import pe.edu.cibertec.ventas.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByIdUsuarioOrderByFechaPedidoDesc(Long idUsuario);
}