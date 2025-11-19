package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.notificacion.EnviarNotificacionMasivaRequest;
import com.beautysalon.gretta.dto.notificacion.NotificacionRequest;
import com.beautysalon.gretta.dto.notificacion.NotificacionResponse;
import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.Notificacion;
import com.beautysalon.gretta.entity.Promocion;
import com.beautysalon.gretta.entity.Usuario;
import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import com.beautysalon.gretta.repository.CitaRepository;
import com.beautysalon.gretta.repository.NotificacionRepository;
import com.beautysalon.gretta.repository.PromocionRepository;
import com.beautysalon.gretta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;
    private final PromocionRepository promocionRepository;

    @Transactional
    public NotificacionResponse crearNotificacion(NotificacionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getIdUsuario()));

        Cita cita = null;
        if (request.getIdCita() != null) {
            cita = citaRepository.findById(request.getIdCita())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + request.getIdCita()));
        }

        Promocion promocion = null;
        if (request.getIdPromocion() != null) {
            promocion = promocionRepository.findById(request.getIdPromocion())
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + request.getIdPromocion()));
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setCita(cita);
        notificacion.setPromocion(promocion);
        notificacion.setTipo(request.getTipo());
        notificacion.setAsunto(request.getAsunto());
        notificacion.setMensaje(request.getMensaje());
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setEstado(EstadoNotificacion.PENDIENTE);

        notificacion = notificacionRepository.save(notificacion);
        return convertirAResponse(notificacion);
    }

    @Transactional
    public List<NotificacionResponse> enviarNotificacionMasiva(EnviarNotificacionMasivaRequest request) {
        List<Notificacion> notificaciones = new ArrayList<>();

        for (Integer idUsuario : request.getIdsUsuarios()) {
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

            Notificacion notificacion = new Notificacion();
            notificacion.setUsuario(usuario);
            notificacion.setTipo(request.getTipo());
            notificacion.setAsunto(request.getAsunto());
            notificacion.setMensaje(request.getMensaje());
            notificacion.setFechaEnvio(LocalDateTime.now());
            notificacion.setEstado(EstadoNotificacion.PENDIENTE);

            notificaciones.add(notificacion);
        }

        List<Notificacion> guardadas = notificacionRepository.saveAll(notificaciones);
        return guardadas.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificacionResponse enviarNotificacionRecordatorioCita(Integer idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + idCita));

        String asunto = "Recordatorio de Cita - Gretta";
        String mensaje = String.format(
                "Hola %s, te recordamos que tienes una cita programada para el %s a las %s. " +
                "Servicio: %s. ¡Te esperamos!",
                cita.getCliente().getUsuario().getNombre(),
                cita.getFechaCita().toLocalDate(),
                cita.getHoraCita(),
                cita.getServicio().getNombre()
        );

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(cita.getCliente().getUsuario());
        notificacion.setCita(cita);
        notificacion.setTipo(TipoNotificacion.RECORDATORIO);
        notificacion.setAsunto(asunto);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setEstado(EstadoNotificacion.ENVIADA);

        notificacion = notificacionRepository.save(notificacion);
        return convertirAResponse(notificacion);
    }

    @Transactional
    public List<NotificacionResponse> notificarPromocion(Integer idPromocion) {
        Promocion promocion = promocionRepository.findById(idPromocion)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + idPromocion));

        List<Usuario> usuarios = usuarioRepository.findByActivo(true);
        List<Notificacion> notificaciones = new ArrayList<>();

        String asunto = "¡Nueva Promoción! - " + promocion.getTitulo();
        String mensaje = String.format(
                "¡Tenemos una nueva promoción para ti! %s. Descuento: %.0f%%. " +
                "Válida desde %s hasta %s. ¡No te la pierdas!",
                promocion.getDescripcion() != null ? promocion.getDescripcion() : "",
                promocion.getDescuento(),
                promocion.getFechaInicio().toLocalDate(),
                promocion.getFechaFin().toLocalDate()
        );

        for (Usuario usuario : usuarios) {
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuario(usuario);
            notificacion.setPromocion(promocion);
            notificacion.setTipo(TipoNotificacion.PROMOCION);
            notificacion.setAsunto(asunto);
            notificacion.setMensaje(mensaje);
            notificacion.setFechaEnvio(LocalDateTime.now());
            notificacion.setEstado(EstadoNotificacion.ENVIADA);
            notificaciones.add(notificacion);
        }

        List<Notificacion> guardadas = notificacionRepository.saveAll(notificaciones);
        return guardadas.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPorUsuario(Integer idUsuario) {
        return notificacionRepository.findByUsuarioOrderByFechaDesc(idUsuario).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPorUsuarioYEstado(Integer idUsuario, EstadoNotificacion estado) {
        return notificacionRepository.findByUsuarioAndEstado(idUsuario, estado).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPorTipo(TipoNotificacion tipo) {
        return notificacionRepository.findByTipo(tipo).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPendientes() {
        return notificacionRepository.findByEstado(EstadoNotificacion.PENDIENTE).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPorCita(Integer idCita) {
        return notificacionRepository.findByCita(idCita).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPorPromocion(Integer idPromocion) {
        return notificacionRepository.findByPromocion(idPromocion).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificacionResponse obtenerPorId(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
        return convertirAResponse(notificacion);
    }

    @Transactional
    public NotificacionResponse marcarComoEnviada(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        notificacion.setEstado(EstadoNotificacion.ENVIADA);
        notificacion = notificacionRepository.save(notificacion);
        return convertirAResponse(notificacion);
    }

    @Transactional
    public NotificacionResponse marcarComoEntregada(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        notificacion.setEstado(EstadoNotificacion.ENTREGADA);
        notificacion = notificacionRepository.save(notificacion);
        return convertirAResponse(notificacion);
    }

    @Transactional
    public NotificacionResponse marcarComoFallida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        notificacion.setEstado(EstadoNotificacion.FALLIDA);
        notificacion = notificacionRepository.save(notificacion);
        return convertirAResponse(notificacion);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada con ID: " + id);
        }
        notificacionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long contarPorUsuario(Integer idUsuario) {
        return notificacionRepository.countByUsuario(idUsuario);
    }

    @Transactional(readOnly = true)
    public Long contarPorUsuarioYEstado(Integer idUsuario, EstadoNotificacion estado) {
        return notificacionRepository.countByUsuarioAndEstado(idUsuario, estado);
    }

    private NotificacionResponse convertirAResponse(Notificacion notificacion) {
        return NotificacionResponse.builder()
                .idNotificacion(notificacion.getIdNotificacion())
                .idUsuario(notificacion.getUsuario().getIdUsuario())
                .nombreUsuario(notificacion.getUsuario().getNombre() + " " + 
                              notificacion.getUsuario().getApellido())
                .idCita(notificacion.getCita() != null ? notificacion.getCita().getIdCita() : null)
                .idPromocion(notificacion.getPromocion() != null ? notificacion.getPromocion().getIdPromocion() : null)
                .tipo(notificacion.getTipo())
                .asunto(notificacion.getAsunto())
                .mensaje(notificacion.getMensaje())
                .fechaEnvio(notificacion.getFechaEnvio())
                .estado(notificacion.getEstado())
                .build();
    }
}
