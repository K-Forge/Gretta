package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.promocion.PromocionRequest;
import com.beautysalon.gretta.dto.promocion.PromocionResponse;
import com.beautysalon.gretta.service.PromocionService;
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
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PromocionController {

    private final PromocionService promocionService;

    @GetMapping
    public ResponseEntity<List<PromocionResponse>> obtenerTodas() {
        return ResponseEntity.ok(promocionService.obtenerTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<PromocionResponse>> obtenerActivas() {
        return ResponseEntity.ok(promocionService.obtenerActivas());
    }

    @GetMapping("/vigentes")
    public ResponseEntity<List<PromocionResponse>> obtenerVigentes() {
        return ResponseEntity.ok(promocionService.obtenerVigentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(promocionService.obtenerPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener promoción");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> crear(@Valid @RequestBody PromocionRequest request) {
        try {
            PromocionResponse response = promocionService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear promoción");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody PromocionRequest request) {
        try {
            PromocionResponse response = promocionService.actualizar(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar promoción");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> desactivar(@PathVariable Integer id) {
        try {
            PromocionResponse response = promocionService.desactivar(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al desactivar promoción");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            promocionService.eliminar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Promoción eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar promoción");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
