package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.historial.EstadisticasEstilistaResponse;
import com.beautysalon.gretta.dto.historial.HistorialEstilistaRequest;
import com.beautysalon.gretta.dto.historial.HistorialEstilistaResponse;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.Estilista;
import com.beautysalon.gretta.entity.HistorialEstilista;
import com.beautysalon.gretta.repository.ClienteRepository;
import com.beautysalon.gretta.repository.EstilistaRepository;
import com.beautysalon.gretta.repository.HistorialEstilistaRepository;
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
public class HistorialEstilistaService {

    private final HistorialEstilistaRepository historialRepository;
    private final EstilistaRepository estilistaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<HistorialEstilistaResponse> obtenerPorEstilista(Integer idEstilista) {
        return historialRepository.findByEstilistaOrderByFechaDesc(idEstilista).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialEstilistaResponse> obtenerPorEstilistaYPeriodo(
            Integer idEstilista, 
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        return historialRepository.findByEstilistaAndFechaBetween(idEstilista, fechaInicio, fechaFin).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialEstilistaResponse> obtenerPorCliente(Integer idCliente) {
        return historialRepository.findByCliente(idCliente).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistorialEstilistaResponse> obtenerPorEstilistaYCliente(Integer idEstilista, Integer idCliente) {
        return historialRepository.findByEstilistaAndCliente(idEstilista, idCliente).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistorialEstilistaResponse obtenerPorId(Integer id) {
        HistorialEstilista historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con ID: " + id));
        return convertirAResponse(historial);
    }

    @Transactional(readOnly = true)
    public EstadisticasEstilistaResponse obtenerEstadisticasEstilista(Integer idEstilista) {
        Estilista estilista = estilistaRepository.findById(idEstilista)
                .orElseThrow(() -> new RuntimeException("Estilista no encontrado con ID: " + idEstilista));

        Long totalVisitas = historialRepository.countByEstilista(idEstilista);
        Long totalClientesAtendidos = historialRepository.countClientesAtendidos(idEstilista);
        List<Object[]> clientesFrecuentesData = historialRepository.getClientesFrecuentesPorEstilista(idEstilista);
        
        List<Map<String, Object>> clientesFrecuentes = clientesFrecuentesData.stream()
                .map(data -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", data[0]);
                    map.put("apellido", data[1]);
                    map.put("visitas", data[2]);
                    return map;
                })
                .collect(Collectors.toList());

        List<HistorialEstilista> historiales = historialRepository.findByEstilistaOrderByFechaDesc(idEstilista);
        LocalDateTime ultimaVisita = historiales.isEmpty() ? null : historiales.get(0).getFechaVisita();

        return EstadisticasEstilistaResponse.builder()
                .idEstilista(idEstilista)
                .nombreEstilista(estilista.getUsuario().getNombre() + " " + estilista.getUsuario().getApellido())
                .especialidad(estilista.getEspecialidad())
                .totalVisitas(totalVisitas)
                .totalClientesAtendidos(totalClientesAtendidos)
                .clientesFrecuentes(clientesFrecuentes)
                .ultimaVisita(ultimaVisita)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerEstilistasMasActivos() {
        List<Object[]> data = historialRepository.getEstilistasMasActivos();
        return data.stream()
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", obj[0]);
                    map.put("apellido", obj[1]);
                    map.put("trabajos", obj[2]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public HistorialEstilistaResponse registrar(HistorialEstilistaRequest request) {
        Estilista estilista = estilistaRepository.findById(request.getIdEstilista())
                .orElseThrow(() -> new RuntimeException("Estilista no encontrado con ID: " + request.getIdEstilista()));

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getIdCliente()));

        HistorialEstilista historial = new HistorialEstilista();
        historial.setEstilista(estilista);
        historial.setCliente(cliente);
        historial.setFechaVisita(request.getFechaVisita());

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

    private HistorialEstilistaResponse convertirAResponse(HistorialEstilista historial) {
        return HistorialEstilistaResponse.builder()
                .idHistorial(historial.getIdHistorial())
                .idEstilista(historial.getEstilista().getIdEstilista())
                .nombreEstilista(historial.getEstilista().getUsuario().getNombre() + " " + 
                                historial.getEstilista().getUsuario().getApellido())
                .especialidadEstilista(historial.getEstilista().getEspecialidad())
                .idCliente(historial.getCliente().getIdCliente())
                .nombreCliente(historial.getCliente().getUsuario().getNombre() + " " + 
                              historial.getCliente().getUsuario().getApellido())
                .fechaVisita(historial.getFechaVisita())
                .build();
    }
}
