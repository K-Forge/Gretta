package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.venta.VentaRequest;
import com.beautysalon.gretta.dto.venta.VentaResponse;
import com.beautysalon.gretta.service.VentaService;
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
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<VentaResponse>> obtenerTodas() {
        return ResponseEntity.ok(ventaService.obtenerTodas());
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<VentaResponse>> obtenerPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(ventaService.obtenerPorCliente(idCliente));
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<VentaResponse>> obtenerPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(ventaService.obtenerPorPeriodo(fechaInicio, fechaFin));
    }

    @GetMapping("/total-periodo")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> obtenerTotalVentasPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            Double total = ventaService.obtenerTotalVentasPeriodo(fechaInicio, fechaFin);
            Map<String, Object> response = new HashMap<>();
            response.put("fechaInicio", fechaInicio);
            response.put("fechaFin", fechaFin);
            response.put("total", total);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al calcular total de ventas");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/productos-mas-vendidos")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> obtenerProductosMasVendidos() {
        try {
            List<Object[]> productos = ventaService.obtenerProductosMasVendidos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener productos más vendidos");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.obtenerPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener venta");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> crear(@Valid @RequestBody VentaRequest request) {
        try {
            VentaResponse response = ventaService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear venta");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody VentaRequest request) {
        try {
            VentaResponse response = ventaService.actualizar(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar venta");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}/anular")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<?> anular(@PathVariable Integer id) {
        try {
            ventaService.anular(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Venta anulada exitosamente. Stock devuelto.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al anular venta");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
