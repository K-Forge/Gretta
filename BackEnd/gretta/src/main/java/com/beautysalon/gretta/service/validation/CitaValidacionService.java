package com.beautysalon.gretta.service.validation;

import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.Estilista;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.repository.CitaRepository;
import com.beautysalon.gretta.repository.ClienteRepository;
import com.beautysalon.gretta.repository.EstilistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaValidacionService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final EstilistaRepository estilistaRepository;

    // Horario laboral: Lunes a Sábado, 8:00 AM - 8:00 PM
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(20, 0);

    public void validarNuevaCita(Integer idCliente, Integer idEstilista, 
                                  LocalDateTime fechaCita, LocalTime horaCita, Integer idCita) {
        
        // Validar cliente existe y está activo
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        if (!cliente.getUsuario().getActivo()) {
            throw new RuntimeException("El cliente no está activo");
        }

        // Validar estilista existe
        Estilista estilista = estilistaRepository.findById(idEstilista)
                .orElseThrow(() -> new RuntimeException("Estilista no encontrado"));

        if (!estilista.getUsuario().getActivo()) {
            throw new RuntimeException("El estilista no está activo");
        }

        // Validar día laboral (no domingo)
        if (fechaCita.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RuntimeException("No se pueden agendar citas los domingos");
        }

        // Validar horario laboral
        if (horaCita.isBefore(HORA_INICIO) || horaCita.isAfter(HORA_FIN)) {
            throw new RuntimeException("La cita debe estar entre las 8:00 AM y 8:00 PM");
        }

        // Validar que la fecha no sea en el pasado
        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaCita.toLocalDate(), horaCita);
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden agendar citas en el pasado");
        }

        // Validar que el estilista no tenga otra cita en el mismo horario
        List<Cita> citasEstilista = citaRepository.findByEstilista_IdEstilistaAndFechaCitaBetween(
                idEstilista, 
                fechaCita.minusHours(2), 
                fechaCita.plusHours(2)
        );

        for (Cita cita : citasEstilista) {
            if (idCita != null && cita.getIdCita().equals(idCita)) {
                continue;
            }
            if (cita.getEstado() != EstadoCita.CANCELADA && 
                cita.getHoraCita().equals(horaCita)) {
                throw new RuntimeException("El estilista ya tiene una cita agendada en este horario");
            }
        }

        // Validar que el cliente no tenga múltiples citas pendientes el mismo día
        List<Cita> citasCliente = citaRepository.findByCliente_IdClienteAndFechaCitaBetween(
                idCliente,
                fechaCita.toLocalDate().atStartOfDay(),
                fechaCita.toLocalDate().atTime(23, 59, 59)
        );

        long citasPendientes = citasCliente.stream()
                .filter(c -> idCita == null || !c.getIdCita().equals(idCita))
                .filter(c -> c.getEstado() == EstadoCita.PENDIENTE || 
                           c.getEstado() == EstadoCita.CONFIRMADA)
                .count();

        if (citasPendientes >= 2) {
            throw new RuntimeException("El cliente ya tiene 2 citas programadas para este día");
        }
    }

    public void validarCambioEstado(Integer idCita, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        EstadoCita estadoActual = cita.getEstado();

        // Validar transiciones de estado permitidas
        switch (estadoActual) {
            case PENDIENTE:
                if (nuevoEstado != EstadoCita.CONFIRMADA && 
                    nuevoEstado != EstadoCita.CANCELADA) {
                    throw new RuntimeException("Una cita PENDIENTE solo puede pasar a CONFIRMADA o CANCELADA");
                }
                break;
            case CONFIRMADA:
                if (nuevoEstado != EstadoCita.COMPLETADA && 
                    nuevoEstado != EstadoCita.CANCELADA) {
                    throw new RuntimeException("Una cita CONFIRMADA solo puede pasar a COMPLETADA o CANCELADA");
                }
                break;
            case COMPLETADA:
                throw new RuntimeException("Una cita COMPLETADA no puede cambiar de estado");
            case CANCELADA:
                throw new RuntimeException("Una cita CANCELADA no puede cambiar de estado");
        }

        // No se puede completar una cita futura
        if (nuevoEstado == EstadoCita.COMPLETADA && 
            cita.getFechaCita().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("No se puede completar una cita que aún no ha ocurrido");
        }
    }

    public void validarActualizacionCita(Integer idCita, LocalDateTime nuevaFecha, LocalTime nuevaHora) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // No se puede modificar una cita completada o cancelada
        if (cita.getEstado() == EstadoCita.COMPLETADA || 
            cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("No se puede modificar una cita " + cita.getEstado());
        }

        // Validar con menos de 2 horas de anticipación
        LocalDateTime ahora = LocalDateTime.now();
        if (cita.getFechaCita().minusHours(2).isBefore(ahora)) {
            throw new RuntimeException("No se puede modificar una cita con menos de 2 horas de anticipación");
        }

        // Validar nueva fecha/hora si se proporcionan
        if (nuevaFecha != null && nuevaHora != null) {
            validarNuevaCita(
                cita.getCliente().getIdCliente(),
                cita.getEstilista().getIdEstilista(),
                nuevaFecha,
                nuevaHora,
                cita.getIdCita()
            );
        }
    }

    public void validarCancelacion(Integer idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new RuntimeException("No se puede cancelar una cita completada");
        }

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("La cita ya está cancelada");
        }

        // Advertencia si se cancela con menos de 24 horas de anticipación
        LocalDateTime ahora = LocalDateTime.now();
        if (cita.getFechaCita().minusHours(24).isBefore(ahora)) {
            // Podría generar una penalización o registro
            System.out.println("ADVERTENCIA: Cancelación con menos de 24 horas de anticipación");
        }
    }

    // Método sobrecargado para validar con objeto Cita completo
    public void validarNuevaCita(Cita cita) {
        if (cita == null) {
            throw new RuntimeException("La cita no puede ser null");
        }
        validarNuevaCita(
            cita.getCliente().getIdCliente(),
            cita.getEstilista().getIdEstilista(),
            cita.getFechaCita(),
            cita.getHoraCita(),
            cita.getIdCita()
        );
    }

    public void validarModificacionCita(Cita cita) {
        if (cita == null) {
            throw new RuntimeException("La cita no puede ser null");
        }
        
        if (cita.getIdCita() == null) {
            throw new RuntimeException("El ID de la cita es requerido para modificación");
        }

        // Verificar que la cita existe
        Cita citaExistente = citaRepository.findById(cita.getIdCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // No se puede modificar una cita completada o cancelada
        if (citaExistente.getEstado() == EstadoCita.COMPLETADA) {
            throw new RuntimeException("No se puede modificar una cita completada");
        }
        
        if (citaExistente.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("No se puede modificar una cita cancelada");
        }
    }

    public void validarTransicionEstado(EstadoCita estadoActual, EstadoCita nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) {
            throw new RuntimeException("Los estados no pueden ser null");
        }

        // Validar transiciones permitidas
        switch (estadoActual) {
            case PENDIENTE:
                if (nuevoEstado != EstadoCita.CONFIRMADA && nuevoEstado != EstadoCita.CANCELADA) {
                    throw new RuntimeException("Desde PENDIENTE solo se puede pasar a CONFIRMADA o CANCELADA");
                }
                break;
            case CONFIRMADA:
                if (nuevoEstado != EstadoCita.COMPLETADA && nuevoEstado != EstadoCita.CANCELADA) {
                    throw new RuntimeException("Desde CONFIRMADA solo se puede pasar a COMPLETADA o CANCELADA");
                }
                break;
            case COMPLETADA:
                throw new RuntimeException("No se puede cambiar el estado de una cita COMPLETADA");
            case CANCELADA:
                throw new RuntimeException("No se puede cambiar el estado de una cita CANCELADA");
            default:
                throw new RuntimeException("Estado no reconocido: " + estadoActual);
        }
    }
}
