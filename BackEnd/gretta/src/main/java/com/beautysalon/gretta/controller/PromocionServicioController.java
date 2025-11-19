package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.promocionservicio.AsignarServiciosRequest;
import com.beautysalon.gretta.dto.promocionservicio.PromocionServicioRequest;
import com.beautysalon.gretta.dto.promocionservicio.PromocionServicioResponse;
import com.beautysalon.gretta.service.PromocionServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promocion-servicio")
@RequiredArgsConstructor
public class PromocionServicioController {

    private final PromocionServicioService promocionServicioService;

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<PromocionServicioResponse> asignarServicio(
            @Valid @RequestBody PromocionServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(promocionServicioService.asignarServicioAPromocion(request));
    }

    @PostMapping("/multiple")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<PromocionServicioResponse>> asignarMultiplesServicios(
            @Valid @RequestBody AsignarServiciosRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(promocionServicioService.asignarMultiplesServicios(request));
    }

    @GetMapping("/promocion/{idPromocion}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<PromocionServicioResponse>> obtenerServiciosPorPromocion(
            @PathVariable Integer idPromocion) {
        return ResponseEntity.ok(promocionServicioService.obtenerServiciosPorPromocion(idPromocion));
    }

    @GetMapping("/servicio/{idServicio}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<PromocionServicioResponse>> obtenerPromocionesPorServicio(
            @PathVariable Integer idServicio) {
        return ResponseEntity.ok(promocionServicioService.obtenerPromocionesPorServicio(idServicio));
    }

    @GetMapping("/existe")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<Boolean> existeRelacion(
            @RequestParam Integer idPromocion,
            @RequestParam Integer idServicio) {
        return ResponseEntity.ok(promocionServicioService.existeRelacion(idPromocion, idServicio));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Integer id) {
        promocionServicioService.eliminarAsignacion(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/promocion/{idPromocion}/servicio/{idServicio}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminarAsignacionPorPromocionYServicio(
            @PathVariable Integer idPromocion,
            @PathVariable Integer idServicio) {
        promocionServicioService.eliminarAsignacionPorPromocionYServicio(idPromocion, idServicio);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/promocion/{idPromocion}/todas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminarTodasAsignacionesDePromocion(@PathVariable Integer idPromocion) {
        promocionServicioService.eliminarTodasAsignacionesDePromocion(idPromocion);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/servicio/{idServicio}/todas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminarTodasAsignacionesDeServicio(@PathVariable Integer idServicio) {
        promocionServicioService.eliminarTodasAsignacionesDeServicio(idServicio);
        return ResponseEntity.noContent().build();
    }
}
