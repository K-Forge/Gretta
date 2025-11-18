package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.servicio.ServicioRequest;
import com.beautysalon.gretta.dto.servicio.ServicioResponse;
import com.beautysalon.gretta.entity.Servicio;
import com.beautysalon.gretta.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<ServicioResponse> obtenerTodos() {
        return servicioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicioResponse> obtenerActivos() {
        return servicioRepository.findByActivoTrue().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
        return convertirAResponse(servicio);
    }

    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        Servicio servicio = new Servicio();
        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setDuracion(LocalTime.parse(request.getDuracion()));
        servicio.setPrecio(request.getPrecio());
        servicio.setActivo(request.getActivo());

        servicio = servicioRepository.save(servicio);
        return convertirAResponse(servicio);
    }

    @Transactional
    public ServicioResponse actualizar(Integer id, ServicioRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setDuracion(LocalTime.parse(request.getDuracion()));
        servicio.setPrecio(request.getPrecio());
        servicio.setActivo(request.getActivo());

        servicio = servicioRepository.save(servicio);
        return convertirAResponse(servicio);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado con ID: " + id);
        }
        servicioRepository.deleteById(id);
    }

    @Transactional
    public ServicioResponse desactivar(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
        servicio.setActivo(false);
        servicio = servicioRepository.save(servicio);
        return convertirAResponse(servicio);
    }

    private ServicioResponse convertirAResponse(Servicio servicio) {
        return ServicioResponse.builder()
                .idServicio(servicio.getIdServicio())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .duracion(servicio.getDuracion().toString())
                .precio(servicio.getPrecio())
                .activo(servicio.getActivo())
                .fechaCreacion(servicio.getFechaCreacion())
                .build();
    }
}
