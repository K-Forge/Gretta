package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.cita.CitaRequest;
import com.beautysalon.gretta.dto.cita.CitaResponse;
import com.beautysalon.gretta.entity.*;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final EstilistaRepository estilistaRepository;
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerTodas() {
        return citaRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerPorCliente(Integer idCliente) {
        return citaRepository.findByClienteOrderByFechaDesc(idCliente).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerPorEstilista(Integer idEstilista) {
        return citaRepository.findByEstilista_IdEstilista(idEstilista).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        return convertirAResponse(cita);
    }

    @Transactional
    public CitaResponse crear(CitaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getIdCliente()));

        Estilista estilista = estilistaRepository.findById(request.getIdEstilista())
                .orElseThrow(() -> new RuntimeException("Estilista no encontrado con ID: " + request.getIdEstilista()));

        Servicio servicio = servicioRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + request.getIdServicio()));

        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setEstilista(estilista);
        cita.setServicio(servicio);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraCita(LocalTime.parse(request.getHoraCita()));
        cita.setCanalReserva(request.getCanalReserva());
        cita.setEstado(request.getEstado() != null ? request.getEstado() : EstadoCita.PENDIENTE);
        cita.setObservaciones(request.getObservaciones());

        cita = citaRepository.save(cita);
        return convertirAResponse(cita);
    }

    @Transactional
    public CitaResponse actualizar(Integer id, CitaRequest request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getIdCliente()));

        Estilista estilista = estilistaRepository.findById(request.getIdEstilista())
                .orElseThrow(() -> new RuntimeException("Estilista no encontrado con ID: " + request.getIdEstilista()));

        Servicio servicio = servicioRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + request.getIdServicio()));

        cita.setCliente(cliente);
        cita.setEstilista(estilista);
        cita.setServicio(servicio);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraCita(LocalTime.parse(request.getHoraCita()));
        cita.setCanalReserva(request.getCanalReserva());
        cita.setEstado(request.getEstado() != null ? request.getEstado() : cita.getEstado());
        cita.setObservaciones(request.getObservaciones());

        cita = citaRepository.save(cita);
        return convertirAResponse(cita);
    }

    @Transactional
    public CitaResponse cambiarEstado(Integer id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        cita.setEstado(nuevoEstado);
        cita = citaRepository.save(cita);
        return convertirAResponse(cita);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }

    private CitaResponse convertirAResponse(Cita cita) {
        return CitaResponse.builder()
                .idCita(cita.getIdCita())
                .idCliente(cita.getCliente().getIdCliente())
                .nombreCliente(cita.getCliente().getUsuario().getNombre() + " " + 
                              cita.getCliente().getUsuario().getApellido())
                .idEstilista(cita.getEstilista().getIdEstilista())
                .nombreEstilista(cita.getEstilista().getUsuario().getNombre() + " " + 
                                cita.getEstilista().getUsuario().getApellido())
                .idServicio(cita.getServicio().getIdServicio())
                .nombreServicio(cita.getServicio().getNombre())
                .fechaCita(cita.getFechaCita())
                .horaCita(cita.getHoraCita().toString())
                .canalReserva(cita.getCanalReserva())
                .estado(cita.getEstado())
                .observaciones(cita.getObservaciones())
                .fechaCreacion(cita.getFechaCreacion())
                .fechaModificacion(cita.getFechaModificacion())
                .build();
    }
}
