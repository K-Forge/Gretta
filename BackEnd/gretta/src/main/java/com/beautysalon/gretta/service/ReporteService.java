package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.reporte.ReporteCitaResponse;
import com.beautysalon.gretta.dto.reporte.ReporteVentaResponse;
import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.DetalleVenta;
import com.beautysalon.gretta.entity.Venta;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.repository.CitaRepository;
import com.beautysalon.gretta.repository.DetalleVentaRepository;
import com.beautysalon.gretta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CitaRepository citaRepository;

    @Transactional(readOnly = true)
    public List<ReporteVentaResponse> generarReporteVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Venta> ventas = ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);

        return ventas.stream()
                .map(venta -> {
                    List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());
                    
                    List<ReporteVentaResponse.DetalleVentaReporte> detallesReporte = detalles.stream()
                            .map(detalle -> ReporteVentaResponse.DetalleVentaReporte.builder()
                                    .nombreProducto(detalle.getProducto().getNombre())
                                    .cantidad(detalle.getCantidad())
                                    .precioUnitario(detalle.getPrecioUnitario())
                                    .subtotal(detalle.getSubtotal())
                                    .build())
                            .collect(Collectors.toList());

                    return ReporteVentaResponse.builder()
                            .idVenta(venta.getIdVenta())
                            .fechaVenta(venta.getFechaVenta())
                            .nombreCliente(venta.getCliente().getUsuario().getNombre() + " " + 
                                         venta.getCliente().getUsuario().getApellido())
                            .total(venta.getTotal())
                            .detalles(detallesReporte)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReporteCitaResponse> generarReporteCitas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Cita> citas = citaRepository.findByFechaCitaBetween(fechaInicio, fechaFin);

        return citas.stream()
                .map(cita -> ReporteCitaResponse.builder()
                        .idCita(cita.getIdCita())
                        .fecha(cita.getFechaCita().toLocalDate())
                        .hora(cita.getHoraCita())
                        .nombreCliente(cita.getCliente().getUsuario().getNombre() + " " + 
                                     cita.getCliente().getUsuario().getApellido())
                        .nombreEstilista(cita.getEstilista().getUsuario().getNombre() + " " + 
                                       cita.getEstilista().getUsuario().getApellido())
                        .nombreServicio(cita.getServicio().getNombre())
                        .estado(cita.getEstado())
                        .canalReserva(cita.getCanalReserva().toString())
                        .observaciones(cita.getObservaciones())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReporteCitaResponse> generarReporteCitasPorEstado(
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin, 
            EstadoCita estado) {
        
        List<Cita> citas = citaRepository.findByFechaCitaBetweenAndEstado(fechaInicio, fechaFin, estado);

        return citas.stream()
                .map(cita -> ReporteCitaResponse.builder()
                        .idCita(cita.getIdCita())
                        .fecha(cita.getFechaCita().toLocalDate())
                        .hora(cita.getHoraCita())
                        .nombreCliente(cita.getCliente().getUsuario().getNombre() + " " + 
                                     cita.getCliente().getUsuario().getApellido())
                        .nombreEstilista(cita.getEstilista().getUsuario().getNombre() + " " + 
                                       cita.getEstilista().getUsuario().getApellido())
                        .nombreServicio(cita.getServicio().getNombre())
                        .estado(cita.getEstado())
                        .canalReserva(cita.getCanalReserva().toString())
                        .observaciones(cita.getObservaciones())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReporteCitaResponse> generarReporteCitasPorEstilista(
            Integer idEstilista,
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        
        List<Cita> citas = citaRepository.findByEstilista_IdEstilistaAndFechaCitaBetween(idEstilista, fechaInicio, fechaFin);

        return citas.stream()
                .map(cita -> ReporteCitaResponse.builder()
                        .idCita(cita.getIdCita())
                        .fecha(cita.getFechaCita().toLocalDate())
                        .hora(cita.getHoraCita())
                        .nombreCliente(cita.getCliente().getUsuario().getNombre() + " " + 
                                     cita.getCliente().getUsuario().getApellido())
                        .nombreEstilista(cita.getEstilista().getUsuario().getNombre() + " " + 
                                       cita.getEstilista().getUsuario().getApellido())
                        .nombreServicio(cita.getServicio().getNombre())
                        .estado(cita.getEstado())
                        .canalReserva(cita.getCanalReserva().toString())
                        .observaciones(cita.getObservaciones())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReporteVentaResponse> generarReporteVentasPorCliente(
            Integer idCliente,
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        
        List<Venta> ventas = ventaRepository.findByCliente_IdClienteAndFechaVentaBetween(idCliente, fechaInicio, fechaFin);

        return ventas.stream()
                .map(venta -> {
                    List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());
                    
                    List<ReporteVentaResponse.DetalleVentaReporte> detallesReporte = detalles.stream()
                            .map(detalle -> ReporteVentaResponse.DetalleVentaReporte.builder()
                                    .nombreProducto(detalle.getProducto().getNombre())
                                    .cantidad(detalle.getCantidad())
                                    .precioUnitario(detalle.getPrecioUnitario())
                                    .subtotal(detalle.getSubtotal())
                                    .build())
                            .collect(Collectors.toList());

                    return ReporteVentaResponse.builder()
                            .idVenta(venta.getIdVenta())
                            .fechaVenta(venta.getFechaVenta())
                            .nombreCliente(venta.getCliente().getUsuario().getNombre() + " " + 
                                         venta.getCliente().getUsuario().getApellido())
                            .total(venta.getTotal())
                            .detalles(detallesReporte)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> generarResumenVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Venta> ventas = ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
        
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalVentas", ventas.size());
        Double ingresosTotalesDouble = ventaRepository.calcularTotalVentasPorPeriodo(fechaInicio, fechaFin);
        java.math.BigDecimal ingresosTotales = java.math.BigDecimal.valueOf(ingresosTotalesDouble != null ? ingresosTotalesDouble : 0.0);
        resumen.put("ingresosTotales", ingresosTotales);
        resumen.put("promedioVenta", ventas.isEmpty() ? java.math.BigDecimal.ZERO : 
                ingresosTotales.divide(java.math.BigDecimal.valueOf(ventas.size()), 2, java.math.RoundingMode.HALF_UP));
        
        return resumen;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> generarResumenCitas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Cita> citas = citaRepository.findByFechaCitaBetween(fechaInicio, fechaFin);
        
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalCitas", citas.size());
        resumen.put("pendientes", citas.stream().filter(c -> c.getEstado() == EstadoCita.PENDIENTE).count());
        resumen.put("confirmadas", citas.stream().filter(c -> c.getEstado() == EstadoCita.CONFIRMADA).count());
        resumen.put("completadas", citas.stream().filter(c -> c.getEstado() == EstadoCita.COMPLETADA).count());
        resumen.put("canceladas", citas.stream().filter(c -> c.getEstado() == EstadoCita.CANCELADA).count());
        
        return resumen;
    }
}
