package pe.edu.cibertec.citas.repository;

import pe.edu.cibertec.citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByIdUsuarioOrderByFechaCitaDesc(Long idUsuario);

    List<Cita> findBySucursal_IdSucursalAndFechaCitaOrderByHoraCitaAsc(Long idSucursal, LocalDate fechaCita);

    @Query("SELECT c FROM Cita c WHERE c.fechaCita >= :fechaInicio ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasProximas(LocalDate fechaInicio);
}