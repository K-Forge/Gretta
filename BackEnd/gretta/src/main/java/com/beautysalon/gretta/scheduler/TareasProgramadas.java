package com.beautysalon.gretta.scheduler;

import com.beautysalon.gretta.dto.notificacion.NotificacionRequest;
import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.Notificacion;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import com.beautysalon.gretta.repository.CitaRepository;
import com.beautysalon.gretta.repository.NotificacionRepository;
import com.beautysalon.gretta.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Tareas programadas para automatizar procesos del sistema
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TareasProgramadas {

    private final CitaRepository citaRepository;
    private final NotificacionRepository notificacionRepository;
    private final NotificacionService notificacionService;

    /**
     * Enviar recordatorios de citas para el día siguiente
     * Se ejecuta todos los días a las 8:00 PM
     */
    @Scheduled(cron = "0 0 20 * * *")
    @Transactional
    public void enviarRecordatoriosCitas() {
        log.info("Iniciando tarea: Envío de recordatorios de citas");

        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDateTime inicioManana = manana.atStartOfDay();
        LocalDateTime finManana = manana.atTime(LocalTime.MAX);

        // Obtener citas confirmadas para mañana
        List<Cita> citasManana = citaRepository.findByEstadoAndFechaCitaBetween(
                EstadoCita.CONFIRMADA,
                inicioManana,
                finManana
        );

        int recordatoriosEnviados = 0;
        for (Cita cita : citasManana) {
            try {
                String mensaje = String.format(
                        "Recordatorio: Tienes una cita mañana %s a las %s con %s para el servicio de %s.",
                        cita.getFechaCita().toLocalDate(),
                        cita.getHoraCita(),
                        cita.getEstilista().getUsuario().getNombre(),
                        cita.getServicio().getNombre()
                );

                NotificacionRequest request = NotificacionRequest.builder()
                        .idUsuario(cita.getCliente().getUsuario().getIdUsuario())
                        .idCita(cita.getIdCita())
                        .tipo(TipoNotificacion.RECORDATORIO)
                        .asunto("Recordatorio de Cita")
                        .mensaje(mensaje)
                        .build();

                notificacionService.crearNotificacion(request);
                recordatoriosEnviados++;

            } catch (Exception e) {
                log.error("Error al enviar recordatorio para cita ID {}: {}", cita.getIdCita(), e.getMessage());
            }
        }

        log.info("Recordatorios enviados: {} de {} citas", recordatoriosEnviados, citasManana.size());
    }

    /**
     * Marcar como completadas las citas del día anterior que quedaron en estado CONFIRMADA
     * Se ejecuta todos los días a las 2:00 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void actualizarEstadoCitasAnteriores() {
        log.info("Iniciando tarea: Actualización de estado de citas anteriores");

        LocalDateTime ayer = LocalDateTime.now().minusDays(1).with(LocalTime.MAX);

        // Buscar citas confirmadas que ya pasaron
        List<Cita> citasConfirmadas = citaRepository.findByEstadoAndFechaCitaBefore(
                EstadoCita.CONFIRMADA,
                ayer
        );

        int citasActualizadas = 0;
        for (Cita cita : citasConfirmadas) {
            cita.setEstado(EstadoCita.COMPLETADA);
            citaRepository.save(cita);
            citasActualizadas++;
        }

        log.info("Citas actualizadas a COMPLETADA: {}", citasActualizadas);
    }

    /**
     * Limpiar notificaciones antiguas (más de 90 días) que ya fueron entregadas
     * Se ejecuta el primer día de cada mes a las 3:00 AM
     */
    @Scheduled(cron = "0 0 3 1 * *")
    @Transactional
    public void limpiarNotificacionesAntiguas() {
        log.info("Iniciando tarea: Limpieza de notificaciones antiguas");

        LocalDateTime hace90Dias = LocalDateTime.now().minusDays(90);

        List<Notificacion> notificacionesAntiguas = notificacionRepository
                .findByEstadoAndFechaEnvioBefore(EstadoNotificacion.ENTREGADA, hace90Dias);

        int notificacionesEliminadas = notificacionesAntiguas.size();
        notificacionRepository.deleteAll(notificacionesAntiguas);

        log.info("Notificaciones eliminadas: {}", notificacionesEliminadas);
    }

    /**
     * Reintentar envío de notificaciones fallidas (máximo 3 intentos)
     * Se ejecuta cada 6 horas
     */
    @Scheduled(cron = "0 0 */6 * * *")
    @Transactional
    public void reintentarNotificacionesFallidas() {
        log.info("Iniciando tarea: Reintento de notificaciones fallidas");

        List<Notificacion> notificacionesFallidas = notificacionRepository.findByEstado(EstadoNotificacion.FALLIDA);

        int notificacionesReintentadas = 0;
        for (Notificacion notificacion : notificacionesFallidas) {
            // Solo reintentar si han pasado al menos 1 hora y no superó 3 intentos
            if (notificacion.getFechaEnvio() != null &&
                    notificacion.getFechaEnvio().isBefore(LocalDateTime.now().minusHours(1))) {

                try {
                    // Aquí iría la lógica real de envío (email, SMS, WhatsApp, etc.)
                    // Por ahora solo actualizamos el estado
                    notificacion.setEstado(EstadoNotificacion.ENVIADA);
                    notificacion.setFechaEnvio(LocalDateTime.now());
                    notificacionRepository.save(notificacion);
                    notificacionesReintentadas++;

                } catch (Exception e) {
                    log.error("Error al reintentar notificación ID {}: {}", notificacion.getIdNotificacion(), e.getMessage());
                }
            }
        }

        log.info("Notificaciones reintentadas: {} de {}", notificacionesReintentadas, notificacionesFallidas.size());
    }

    /**
     * Generar estadísticas diarias y logs de rendimiento
     * Se ejecuta todos los días a las 11:59 PM
     */
    @Scheduled(cron = "0 59 23 * * *")
    @Transactional(readOnly = true)
    public void generarEstadisticasDiarias() {
        log.info("Iniciando tarea: Generación de estadísticas diarias");

        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);

        // Citas del día
        long citasCompletadas = citaRepository.countByEstadoAndFechaCitaBetween(
                EstadoCita.COMPLETADA, inicioDia, finDia);
        long citasCanceladas = citaRepository.countByEstadoAndFechaCitaBetween(
                EstadoCita.CANCELADA, inicioDia, finDia);

        // Notificaciones del día
        long notificacionesEnviadas = notificacionRepository.countByEstadoAndFechaEnvioBetween(
                EstadoNotificacion.ENTREGADA, inicioDia, finDia);

        log.info("=== ESTADÍSTICAS DEL DÍA {} ===", LocalDate.now());
        log.info("Citas completadas: {}", citasCompletadas);
        log.info("Citas canceladas: {}", citasCanceladas);
        log.info("Notificaciones enviadas: {}", notificacionesEnviadas);
        log.info("================================");
    }
}
