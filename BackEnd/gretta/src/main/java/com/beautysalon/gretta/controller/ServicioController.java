package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.servicio.ServicioRequest;
import com.beautysalon.gretta.dto.servicio.ServicioResponse;
import com.beautysalon.gretta.service.ServicioService;
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
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> obtenerTodos() {
        return ResponseEntity.ok(servicioService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ServicioResponse>> obtenerActivos() {
        return ResponseEntity.ok(servicioService.obtenerActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(servicioService.obtenerPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener servicio");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> crear(@Valid @RequestBody ServicioRequest request) {
        try {
            ServicioResponse response = servicioService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear servicio");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody ServicioRequest request) {
        try {
            ServicioResponse response = servicioService.actualizar(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar servicio");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> desactivar(@PathVariable Integer id) {
        try {
            ServicioResponse response = servicioService.desactivar(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al desactivar servicio");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            servicioService.eliminar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Servicio eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar servicio");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
