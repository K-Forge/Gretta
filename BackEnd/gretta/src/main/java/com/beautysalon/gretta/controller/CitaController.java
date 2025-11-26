package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.cita.CitaRequest;
import com.beautysalon.gretta.dto.cita.CitaResponse;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<CitaResponse>> obtenerTodas() {
        return ResponseEntity.ok(citaService.obtenerTodas());
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<CitaResponse>> obtenerPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(citaService.obtenerPorCliente(idCliente));
    }

    @GetMapping("/estilista/{idEstilista}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<CitaResponse>> obtenerPorEstilista(@PathVariable Integer idEstilista) {
        return ResponseEntity.ok(citaService.obtenerPorEstilista(idEstilista));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<CitaResponse>> obtenerPorEstado(@PathVariable EstadoCita estado) {
        return ResponseEntity.ok(citaService.obtenerPorEstado(estado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(citaService.obtenerPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener cita");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> crear(@Valid @RequestBody CitaRequest request) {
        try {
            CitaResponse response = citaService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear cita");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody CitaRequest request) {
        try {
            CitaResponse response = citaService.actualizar(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar cita");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam EstadoCita estado) {
        try {
            CitaResponse response = citaService.cambiarEstado(id, estado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al cambiar estado de cita");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            citaService.eliminar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cita eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar cita");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
