package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.historial.EstadisticasClienteResponse;
import com.beautysalon.gretta.dto.historial.HistorialClienteRequest;
import com.beautysalon.gretta.dto.historial.HistorialClienteResponse;
import com.beautysalon.gretta.service.HistorialClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial-cliente")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HistorialClienteController {

    private final HistorialClienteService historialService;

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<HistorialClienteResponse>> obtenerPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(historialService.obtenerPorCliente(idCliente));
    }

    @GetMapping("/cliente/{idCliente}/periodo")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<HistorialClienteResponse>> obtenerPorClienteYPeriodo(
            @PathVariable Integer idCliente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(historialService.obtenerPorClienteYPeriodo(idCliente, fechaInicio, fechaFin));
    }

    @GetMapping("/servicio/{idServicio}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<HistorialClienteResponse>> obtenerPorServicio(@PathVariable Integer idServicio) {
        return ResponseEntity.ok(historialService.obtenerPorServicio(idServicio));
    }

    @GetMapping("/estadisticas/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> obtenerEstadisticasCliente(@PathVariable Integer idCliente) {
        try {
            EstadisticasClienteResponse estadisticas = historialService.obtenerEstadisticasCliente(idCliente);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener estadísticas");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/servicios-mas-frecuentes")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> obtenerServiciosMasFrecuentes() {
        try {
            List<Map<String, Object>> servicios = historialService.obtenerServiciosMasFrecuentes();
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener servicios más frecuentes");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(historialService.obtenerPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener historial");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> registrar(@Valid @RequestBody HistorialClienteRequest request) {
        try {
            HistorialClienteResponse response = historialService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al registrar historial");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            historialService.eliminar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Historial eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar historial");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
