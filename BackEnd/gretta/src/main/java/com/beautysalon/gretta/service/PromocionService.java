package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.promocion.PromocionRequest;
import com.beautysalon.gretta.dto.promocion.PromocionResponse;
import com.beautysalon.gretta.entity.Promocion;
import com.beautysalon.gretta.repository.PromocionRepository;
import com.beautysalon.gretta.service.validation.PromocionValidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromocionService {

    private final PromocionRepository promocionRepository;
    private final PromocionValidacionService validacionService;

    @Transactional(readOnly = true)
    public List<PromocionResponse> obtenerTodas() {
        return promocionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PromocionResponse> obtenerActivas() {
        return promocionRepository.findByActivoTrue().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PromocionResponse> obtenerVigentes() {
        return promocionRepository.findPromocionesVigentes(LocalDateTime.now()).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PromocionResponse obtenerPorId(Integer id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));
        return convertirAResponse(promocion);
    }

    @Transactional
    public PromocionResponse crear(PromocionRequest request) {
        Promocion promocion = new Promocion();
        promocion.setTitulo(request.getTitulo());
        promocion.setDescripcion(request.getDescripcion());
        promocion.setDescuento(request.getDescuento());
        promocion.setFechaInicio(request.getFechaInicio());
        promocion.setFechaFin(request.getFechaFin());
        promocion.setActivo(request.getActivo());

        // Validar todos los aspectos de la promoción
        validacionService.validarPromocion(promocion);

        promocion = promocionRepository.save(promocion);
        return convertirAResponse(promocion);
    }

    @Transactional
    public PromocionResponse actualizar(Integer id, PromocionRequest request) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        promocion.setTitulo(request.getTitulo());
        promocion.setDescripcion(request.getDescripcion());
        promocion.setDescuento(request.getDescuento());
        promocion.setFechaInicio(request.getFechaInicio());
        promocion.setFechaFin(request.getFechaFin());
        promocion.setActivo(request.getActivo());

        // Validar todos los aspectos de la promoción actualizada
        validacionService.validarPromocion(promocion);

        promocion = promocionRepository.save(promocion);
        return convertirAResponse(promocion);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!promocionRepository.existsById(id)) {
            throw new RuntimeException("Promoción no encontrada con ID: " + id);
        }
        promocionRepository.deleteById(id);
    }

    @Transactional
    public PromocionResponse desactivar(Integer id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));
        promocion.setActivo(false);
        promocion = promocionRepository.save(promocion);
        return convertirAResponse(promocion);
    }

    private PromocionResponse convertirAResponse(Promocion promocion) {
        return PromocionResponse.builder()
                .idPromocion(promocion.getIdPromocion())
                .titulo(promocion.getTitulo())
                .descripcion(promocion.getDescripcion())
                .descuento(promocion.getDescuento())
                .fechaInicio(promocion.getFechaInicio())
                .fechaFin(promocion.getFechaFin())
                .activo(promocion.getActivo())
                .fechaCreacion(promocion.getFechaCreacion())
                .build();
    }
}
