package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.promocionservicio.AsignarServiciosRequest;
import com.beautysalon.gretta.dto.promocionservicio.PromocionServicioRequest;
import com.beautysalon.gretta.dto.promocionservicio.PromocionServicioResponse;
import com.beautysalon.gretta.entity.Promocion;
import com.beautysalon.gretta.entity.PromocionServicio;
import com.beautysalon.gretta.entity.Servicio;
import com.beautysalon.gretta.repository.PromocionRepository;
import com.beautysalon.gretta.repository.PromocionServicioRepository;
import com.beautysalon.gretta.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromocionServicioService {

    private final PromocionServicioRepository promocionServicioRepository;
    private final PromocionRepository promocionRepository;
    private final ServicioRepository servicioRepository;

    @Transactional
    public PromocionServicioResponse asignarServicioAPromocion(PromocionServicioRequest request) {
        Promocion promocion = promocionRepository.findById(request.getIdPromocion())
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + request.getIdPromocion()));

        Servicio servicio = servicioRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + request.getIdServicio()));

        // Verificar si ya existe la relación
        if (promocionServicioRepository.existsByPromocionAndServicio(request.getIdPromocion(), request.getIdServicio())) {
            throw new RuntimeException("El servicio ya está asignado a esta promoción");
        }

        PromocionServicio promocionServicio = new PromocionServicio();
        promocionServicio.setPromocion(promocion);
        promocionServicio.setServicio(servicio);

        promocionServicio = promocionServicioRepository.save(promocionServicio);
        return convertirAResponse(promocionServicio);
    }

    @Transactional
    public List<PromocionServicioResponse> asignarMultiplesServicios(AsignarServiciosRequest request) {
        Promocion promocion = promocionRepository.findById(request.getIdPromocion())
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + request.getIdPromocion()));

        List<PromocionServicio> nuevasAsignaciones = new ArrayList<>();

        for (Integer idServicio : request.getIdsServicios()) {
            Servicio servicio = servicioRepository.findById(idServicio)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + idServicio));

            // Solo crear si no existe
            if (!promocionServicioRepository.existsByPromocionAndServicio(request.getIdPromocion(), idServicio)) {
                PromocionServicio promocionServicio = new PromocionServicio();
                promocionServicio.setPromocion(promocion);
                promocionServicio.setServicio(servicio);
                nuevasAsignaciones.add(promocionServicio);
            }
        }

        List<PromocionServicio> guardadas = promocionServicioRepository.saveAll(nuevasAsignaciones);
        return guardadas.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PromocionServicioResponse> obtenerServiciosPorPromocion(Integer idPromocion) {
        return promocionServicioRepository.findByPromocion(idPromocion).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PromocionServicioResponse> obtenerPromocionesPorServicio(Integer idServicio) {
        return promocionServicioRepository.findByServicio(idServicio).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Servicio> obtenerServiciosDePromocion(Integer idPromocion) {
        List<PromocionServicio> relaciones = promocionServicioRepository.findByPromocion(idPromocion);
        return relaciones.stream()
                .map(PromocionServicio::getServicio)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Promocion> obtenerPromocionesDeServicio(Integer idServicio) {
        List<PromocionServicio> relaciones = promocionServicioRepository.findByServicio(idServicio);
        return relaciones.stream()
                .map(PromocionServicio::getPromocion)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existeRelacion(Integer idPromocion, Integer idServicio) {
        return promocionServicioRepository.existsByPromocionAndServicio(idPromocion, idServicio);
    }

    @Transactional
    public void eliminarAsignacion(Integer id) {
        if (!promocionServicioRepository.existsById(id)) {
            throw new RuntimeException("Asignación no encontrada con ID: " + id);
        }
        promocionServicioRepository.deleteById(id);
    }

    @Transactional
    public void eliminarAsignacionPorPromocionYServicio(Integer idPromocion, Integer idServicio) {
        PromocionServicio promocionServicio = promocionServicioRepository
                .findByPromocionAndServicio(idPromocion, idServicio)
                .orElseThrow(() -> new RuntimeException("No existe asignación entre la promoción y el servicio"));
        
        promocionServicioRepository.delete(promocionServicio);
    }

    @Transactional
    public void eliminarTodasAsignacionesDePromocion(Integer idPromocion) {
        List<PromocionServicio> asignaciones = promocionServicioRepository.findByPromocion(idPromocion);
        promocionServicioRepository.deleteAll(asignaciones);
    }

    @Transactional
    public void eliminarTodasAsignacionesDeServicio(Integer idServicio) {
        List<PromocionServicio> asignaciones = promocionServicioRepository.findByServicio(idServicio);
        promocionServicioRepository.deleteAll(asignaciones);
    }

    private PromocionServicioResponse convertirAResponse(PromocionServicio promocionServicio) {
        return PromocionServicioResponse.builder()
                .idPromocionServicio(promocionServicio.getIdPromocionServicio())
                .idPromocion(promocionServicio.getPromocion().getIdPromocion())
                .tituloPromocion(promocionServicio.getPromocion().getTitulo())
                .idServicio(promocionServicio.getServicio().getIdServicio())
                .nombreServicio(promocionServicio.getServicio().getNombre())
                .build();
    }
}
