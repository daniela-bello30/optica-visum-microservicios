package pe.edu.cibertec.citas.service;

import pe.edu.cibertec.citas.dto.CitaRequest;
import pe.edu.cibertec.citas.exception.BusinessException;
import pe.edu.cibertec.citas.exception.ResourceNotFoundException;
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
            throw new BusinessException("No se puede agendar una cita en una fecha pasada", "PAST_DATE_ERROR");
        }

        // Obtener sucursal y tipo de servicio (Lanzan ResourceNotFound si no existen)
        Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "id", request.getIdSucursal()));

        TipoServicio tipoServicio = tipoServicioRepository.findById(request.getIdTipoServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Servicio", "id", request.getIdTipoServicio()));

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
        cita.setFechaCreacion(LocalDateTime.now());
        cita.setFechaActualizacion(LocalDateTime.now());

        return citaRepository.save(cita);
    }

    public List<Cita> obtenerCitasPorUsuario(Long idUsuario) {
        return citaRepository.findByIdUsuarioOrderByFechaCitaDesc(idUsuario);
    }

    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "id", id));
    }

    public Cita reprogramarCita(Long id, LocalDate nuevaFecha, java.time.LocalTime nuevaHora) {
        Cita cita = obtenerPorId(id);

        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede reprogramar la cita a una fecha pasada", "PAST_DATE_ERROR");
        }

        if (!cita.getEstadoCita().equals("Cancelada") && !cita.getEstadoCita().equals("Completada")) {
            cita.setFechaCita(nuevaFecha);
            cita.setHoraCita(nuevaHora);
            cita.setFechaActualizacion(LocalDateTime.now());
            return citaRepository.save(cita);
        } else {
            throw new BusinessException("No se puede reprogramar una cita que ya fue cancelada o completada", "INVALID_STATUS_TRANSITION");
        }
    }

    public Cita actualizarEstado(Long id, String nuevoEstado) {
        Cita cita = obtenerPorId(id);
        cita.setEstadoCita(nuevoEstado);
        cita.setFechaActualizacion(LocalDateTime.now());
        return citaRepository.save(cita);
    }

    public void cancelarCita(Long id) {
        Cita cita = obtenerPorId(id);

        if (cita.getEstadoCita().equals("Completada")) {
            throw new BusinessException("No se puede cancelar una cita que ya fue completada", "INVALID_STATUS_TRANSITION");
        }

        cita.setEstadoCita("Cancelada");
        cita.setFechaActualizacion(LocalDateTime.now());
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