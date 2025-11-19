package com.beautysalon.gretta.service.validation;

import com.beautysalon.gretta.entity.Promocion;
import com.beautysalon.gretta.repository.PromocionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionValidacionService {

    private final PromocionRepository promocionRepository;

    public void validarNuevaPromocion(String titulo, BigDecimal descuento, 
                                       LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        
        // Validar descuento en rango válido (0-100)
        if (descuento.compareTo(BigDecimal.ZERO) < 0 || 
            descuento.compareTo(new BigDecimal("100")) > 0) {
            throw new RuntimeException("El descuento debe estar entre 0% y 100%");
        }

        // Validar que fecha fin sea posterior a fecha inicio
        if (fechaFin.isBefore(fechaInicio) || fechaFin.isEqual(fechaInicio)) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        // Validar que las fechas no sean en el pasado
        if (fechaInicio.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha de inicio no puede ser en el pasado");
        }

        // Validar duración mínima (al menos 1 día)
        if (fechaFin.isBefore(fechaInicio.plusDays(1))) {
            throw new RuntimeException("La promoción debe durar al menos 1 día");
        }

        // Validar duración máxima (no más de 6 meses)
        if (fechaFin.isAfter(fechaInicio.plusMonths(6))) {
            throw new RuntimeException("La promoción no puede durar más de 6 meses");
        }

        // Validar que no existan promociones similares activas con solapamiento de fechas
        List<Promocion> promocionesActivas = promocionRepository.findByActivo(true);
        
        for (Promocion promo : promocionesActivas) {
            boolean solapamiento = !(fechaFin.isBefore(promo.getFechaInicio()) || 
                                    fechaInicio.isAfter(promo.getFechaFin()));
            
            if (solapamiento && promo.getTitulo().equalsIgnoreCase(titulo)) {
                throw new RuntimeException(String.format(
                        "Ya existe una promoción activa con el mismo título en el período del %s al %s",
                        promo.getFechaInicio().toLocalDate(),
                        promo.getFechaFin().toLocalDate()
                ));
            }
        }
    }

    public void validarActualizacionPromocion(Integer idPromocion, LocalDateTime nuevaFechaFin) {
        Promocion promocion = promocionRepository.findById(idPromocion)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));

        // No se puede modificar una promoción expirada
        if (promocion.getFechaFin().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede modificar una promoción que ya expiró");
        }

        // Si se actualiza la fecha de fin
        if (nuevaFechaFin != null) {
            if (nuevaFechaFin.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("La nueva fecha de fin no puede ser en el pasado");
            }

            if (nuevaFechaFin.isBefore(promocion.getFechaInicio())) {
                throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
            }
        }
    }

    public void validarDesactivacion(Integer idPromocion) {
        Promocion promocion = promocionRepository.findById(idPromocion)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));

        if (!promocion.getActivo()) {
            throw new RuntimeException("La promoción ya está desactivada");
        }

        // Verificar si hay servicios o citas asociadas actualmente
        // Esto requeriría consultar la tabla PromocionServicios
        // Por ahora solo validamos que existe
    }

    public void validarCantidadPromocionesActivas() {
        List<Promocion> activas = promocionRepository.findByActivoAndFechaInicioBeforeAndFechaFinAfter(
                true, 
                LocalDateTime.now(), 
                LocalDateTime.now()
        );

        if (activas.size() >= 20) {
            throw new RuntimeException("Se ha alcanzado el límite de promociones activas simultáneas (20)");
        }
    }

    public void validarDescuentoExcesivo(BigDecimal descuento) {
        BigDecimal DESCUENTO_MAXIMO_ALERTA = new BigDecimal("70");
        
        if (descuento.compareTo(DESCUENTO_MAXIMO_ALERTA) > 0) {
            // Esto es solo una advertencia, no impide la creación
            System.out.println("ADVERTENCIA: Descuento superior al 70%. Requiere aprobación especial.");
        }
    }
}
