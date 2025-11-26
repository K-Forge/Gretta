package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.dashboard.*;
import com.beautysalon.gretta.dto.reporte.ReporteCitaResponse;
import com.beautysalon.gretta.dto.reporte.ReporteVentaResponse;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.service.DashboardService;
import com.beautysalon.gretta.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ReporteService reporteService;

    // ==================== DASHBOARD ====================

    @GetMapping("/estadisticas-generales")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<EstadisticasGeneralesResponse> obtenerEstadisticasGenerales() {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticasGenerales());
    }

    @GetMapping("/resumen-del-dia")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Map<String, Object>> obtenerResumenDelDia() {
        return ResponseEntity.ok(dashboardService.obtenerResumenDelDia());
    }

    @GetMapping("/servicios-populares")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ServicioPopularResponse>> obtenerServiciosPopulares(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(dashboardService.obtenerServiciosPopulares(limite));
    }

    @GetMapping("/productos-populares")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ProductoPopularResponse>> obtenerProductosPopulares(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(dashboardService.obtenerProductosPopulares(limite));
    }

    @GetMapping("/ingresos-por-mes")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<IngresosPorPeriodoResponse>> obtenerIngresosPorMes(
            @RequestParam(defaultValue = "12") int cantidadMeses) {
        return ResponseEntity.ok(dashboardService.obtenerIngresosPorMes(cantidadMeses));
    }

    @GetMapping("/clientes-frecuentes")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ClienteFrecuenteResponse>> obtenerClientesFrecuentes(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(dashboardService.obtenerClientesFrecuentes(limite));
    }

    @GetMapping("/estilistas-productivos")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<EstilistaProductivoResponse>> obtenerEstilistasMasProductivos(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(dashboardService.obtenerEstilistasMasProductivos(limite));
    }

    // ==================== REPORTES ====================

    @GetMapping("/reportes/ventas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ReporteVentaResponse>> generarReporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarReporteVentas(fechaInicio, fechaFin));
    }

    @GetMapping("/reportes/ventas/cliente/{idCliente}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ReporteVentaResponse>> generarReporteVentasPorCliente(
            @PathVariable Integer idCliente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarReporteVentasPorCliente(idCliente, fechaInicio, fechaFin));
    }

    @GetMapping("/reportes/ventas/resumen")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Map<String, Object>> generarResumenVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarResumenVentas(fechaInicio, fechaFin));
    }

    @GetMapping("/reportes/citas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ReporteCitaResponse>> generarReporteCitas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarReporteCitas(fechaInicio, fechaFin));
    }

    @GetMapping("/reportes/citas/estado/{estado}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ReporteCitaResponse>> generarReporteCitasPorEstado(
            @PathVariable EstadoCita estado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarReporteCitasPorEstado(fechaInicio, fechaFin, estado));
    }

    @GetMapping("/reportes/citas/estilista/{idEstilista}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ReporteCitaResponse>> generarReporteCitasPorEstilista(
            @PathVariable Integer idEstilista,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarReporteCitasPorEstilista(idEstilista, fechaInicio, fechaFin));
    }

    @GetMapping("/reportes/citas/resumen")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Map<String, Object>> generarResumenCitas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.generarResumenCitas(fechaInicio, fechaFin));
    }
}
