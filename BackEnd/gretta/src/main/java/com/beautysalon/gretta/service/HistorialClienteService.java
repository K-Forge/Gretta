package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.historial.EstadisticasClienteResponse;
import com.beautysalon.gretta.dto.historial.HistorialClienteRequest;
import com.beautysalon.gretta.dto.historial.HistorialClienteResponse;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.HistorialCliente;
import com.beautysalon.gretta.entity.Servicio;
import com.beautysalon.gretta.repository.ClienteRepository;
import com.beautysalon.gretta.repository.HistorialClienteRepository;
import com.beautysalon.gretta.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialClienteService {

    private final HistorialClienteRepository historialRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<HistorialClienteResponse> obtenerPorCliente(Integer idCliente) {
        return historialRepository.findByClienteOrderByFechaDesc(idCliente).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialClienteResponse> obtenerPorClienteYPeriodo(
            Integer idCliente, 
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        return historialRepository.findByClienteAndFechaBetween(idCliente, fechaInicio, fechaFin).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialClienteResponse> obtenerPorServicio(Integer idServicio) {
        return historialRepository.findByServicio(idServicio).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistorialClienteResponse obtenerPorId(Integer id) {
        HistorialCliente historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con ID: " + id));
        return convertirAResponse(historial);
    }

    @Transactional(readOnly = true)
    public EstadisticasClienteResponse obtenerEstadisticasCliente(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        Long totalServicios = historialRepository.countByCliente(idCliente);
        List<Object[]> serviciosFrecuentesData = historialRepository.getServiciosMasFrecuentesPorCliente(idCliente);
        
        List<Map<String, Object>> serviciosFrecuentes = serviciosFrecuentesData.stream()
                .map(data -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("servicio", data[0]);
                    map.put("cantidad", data[1]);
                    return map;
                })
                .collect(Collectors.toList());

        List<HistorialCliente> historiales = historialRepository.findByClienteOrderByFechaDesc(idCliente);
        LocalDateTime ultimaVisita = historiales.isEmpty() ? null : historiales.get(0).getFechaServicio();

        return EstadisticasClienteResponse.builder()
                .idCliente(idCliente)
                .nombreCliente(cliente.getUsuario().getNombre() + " " + cliente.getUsuario().getApellido())
                .totalServicios(totalServicios)
                .serviciosFrecuentes(serviciosFrecuentes)
                .ultimaVisita(ultimaVisita)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerServiciosMasFrecuentes() {
        List<Object[]> data = historialRepository.getServiciosMasFrecuentes();
        return data.stream()
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("servicio", obj[0]);
                    map.put("cantidad", obj[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public HistorialClienteResponse registrar(HistorialClienteRequest request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getIdCliente()));

        Servicio servicio = servicioRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + request.getIdServicio()));

        HistorialCliente historial = new HistorialCliente();
        historial.setCliente(cliente);
        historial.setServicio(servicio);
        historial.setFechaServicio(request.getFechaServicio());

        historial = historialRepository.save(historial);
        return convertirAResponse(historial);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!historialRepository.existsById(id)) {
            throw new RuntimeException("Historial no encontrado con ID: " + id);
        }
        historialRepository.deleteById(id);
    }

    private HistorialClienteResponse convertirAResponse(HistorialCliente historial) {
        return HistorialClienteResponse.builder()
                .idHistorial(historial.getIdHistorial())
                .idCliente(historial.getCliente().getIdCliente())
                .nombreCliente(historial.getCliente().getUsuario().getNombre() + " " + 
                              historial.getCliente().getUsuario().getApellido())
                .idServicio(historial.getServicio().getIdServicio())
                .nombreServicio(historial.getServicio().getNombre())
                .descripcionServicio(historial.getServicio().getDescripcion())
                .fechaServicio(historial.getFechaServicio())
                .build();
    }
}
