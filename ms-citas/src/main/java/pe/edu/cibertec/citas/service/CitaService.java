package pe.edu.cibertec.citas.service;

import pe.edu.cibertec.citas.dto.CitaRequest;
import pe.edu.cibertec.citas.model.Cita;
import pe.edu.cibertec.citas.model.Sucursal;
import pe.edu.cibertec.citas.model.TipoServicio;
import pe.edu.cibertec.citas.repository.CitaRepository;
import pe.edu.cibertec.citas.repository.SucursalRepository;
import pe.edu.cibertec.citas.repository.TipoServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    public Cita agendarCita(CitaRequest request) {
        // Validar que la fecha no sea pasada
        if (request.getFechaCita().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede agendar una cita en fecha pasada");
        }

        // Obtener sucursal y tipo de servicio
        Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        TipoServicio tipoServicio = tipoServicioRepository.findById(request.getIdTipoServicio())
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));

        // Crear cita
        Cita cita = new Cita();
        cita.setNumeroCita(generarNumeroCita());
        cita.setIdUsuario(request.getIdUsuario());
        cita.setSucursal(sucursal);
        cita.setTipoServicio(tipoServicio);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraCita(request.getHoraCita());
        cita.setNotas(request.getNotas());
        cita.setEstadoCita("Programada");

        return citaRepository.save(cita);
    }

    public List<Cita> obtenerCitasPorUsuario(Long idUsuario) {
        return citaRepository.findByIdUsuarioOrderByFechaCitaDesc(idUsuario);
    }

    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public Cita reprogramarCita(Long id, LocalDate nuevaFecha, java.time.LocalTime nuevaHora) {
        Cita cita = obtenerPorId(id);

        if (!cita.getEstadoCita().equals("Cancelada") && !cita.getEstadoCita().equals("Completada")) {
            cita.setFechaCita(nuevaFecha);
            cita.setHoraCita(nuevaHora);
            return citaRepository.save(cita);
        } else {
            throw new RuntimeException("No se puede reprogramar una cita cancelada o completada");
        }
    }

    public Cita actualizarEstado(Long id, String nuevoEstado) {
        Cita cita = obtenerPorId(id);
        cita.setEstadoCita(nuevoEstado);
        return citaRepository.save(cita);
    }

    public void cancelarCita(Long id) {
        Cita cita = obtenerPorId(id);
        cita.setEstadoCita("Cancelada");
        citaRepository.save(cita);
    }

    public List<Cita> obtenerCitasPorSucursalYFecha(Long idSucursal, LocalDate fecha) {
        return citaRepository.findBySucursal_IdSucursalAndFechaCitaOrderByHoraCitaAsc(idSucursal, fecha);
    }

    private String generarNumeroCita() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "CITA-" + timestamp;
    }
}