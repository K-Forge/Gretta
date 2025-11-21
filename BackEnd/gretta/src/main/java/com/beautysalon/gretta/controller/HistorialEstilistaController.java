package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.historial.EstadisticasEstilistaResponse;
import com.beautysalon.gretta.dto.historial.HistorialEstilistaRequest;
import com.beautysalon.gretta.dto.historial.HistorialEstilistaResponse;
import com.beautysalon.gretta.service.HistorialEstilistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial-estilista")
@RequiredArgsConstructor
public class HistorialEstilistaController {

    private final HistorialEstilistaService historialService;

    @GetMapping("/estilista/{idEstilista}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<HistorialEstilistaResponse>> obtenerPorEstilista(
            @PathVariable Integer idEstilista) {
        return ResponseEntity.ok(historialService.obtenerPorEstilista(idEstilista));
    }

    @GetMapping("/estilista/{idEstilista}/periodo")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<HistorialEstilistaResponse>> obtenerPorEstilistaYPeriodo(
            @PathVariable Integer idEstilista,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(historialService.obtenerPorEstilistaYPeriodo(idEstilista, fechaInicio, fechaFin));
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<HistorialEstilistaResponse>> obtenerPorCliente(
            @PathVariable Integer idCliente) {
        return ResponseEntity.ok(historialService.obtenerPorCliente(idCliente));
    }

    @GetMapping("/estilista/{idEstilista}/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<HistorialEstilistaResponse>> obtenerPorEstilistaYCliente(
            @PathVariable Integer idEstilista,
            @PathVariable Integer idCliente) {
        return ResponseEntity.ok(historialService.obtenerPorEstilistaYCliente(idEstilista, idCliente));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<HistorialEstilistaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(historialService.obtenerPorId(id));
    }

    @GetMapping("/estadisticas/{idEstilista}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<EstadisticasEstilistaResponse> obtenerEstadisticas(
            @PathVariable Integer idEstilista) {
        return ResponseEntity.ok(historialService.obtenerEstadisticasEstilista(idEstilista));
    }

    @GetMapping("/estilistas-mas-activos")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<Map<String, Object>>> obtenerEstilistasMasActivos() {
        return ResponseEntity.ok(historialService.obtenerEstilistasMasActivos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<HistorialEstilistaResponse> registrar(
            @Valid @RequestBody HistorialEstilistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(historialService.registrar(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        historialService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
