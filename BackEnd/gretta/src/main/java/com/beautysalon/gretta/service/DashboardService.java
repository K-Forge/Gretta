package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.dashboard.*;
import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.Venta;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import com.beautysalon.gretta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final EstilistaRepository estilistaRepository;
    private final ServicioRepository servicioRepository;
    private final ProductoRepository productoRepository;
    private final CitaRepository citaRepository;
    private final VentaRepository ventaRepository;
    private final PromocionRepository promocionRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Transactional(readOnly = true)
    public EstadisticasGeneralesResponse obtenerEstadisticasGenerales() {
        LocalDateTime inicioMes = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime finMes = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);

        List<Venta> ventas = ventaRepository.findAll();
        BigDecimal ingresosTotales = ventas.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Venta> ventasMes = ventaRepository.findByFechaVentaBetween(inicioMes, finMes);
        BigDecimal ingresosDelMes = ventasMes.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return EstadisticasGeneralesResponse.builder()
                .totalClientes(clienteRepository.count())
                .totalEstilistas(estilistaRepository.count())
                .totalServicios(servicioRepository.count())
                .totalProductos(productoRepository.count())
                .totalCitas(citaRepository.count())
                .citasPendientes(citaRepository.countByEstado(EstadoCita.PENDIENTE))
                .citasConfirmadas(citaRepository.countByEstado(EstadoCita.CONFIRMADA))
                .citasCompletadas(citaRepository.countByEstado(EstadoCita.COMPLETADA))
                .totalVentas((long) ventas.size())
                .ingresosTotales(ingresosTotales)
                .ingresosDelMes(ingresosDelMes)
                .clientesActivos(clienteRepository.count())
                .promocionesVigentes(promocionRepository.countByActivoAndFechaInicioBeforeAndFechaFinAfter(
                        true, LocalDateTime.now(), LocalDateTime.now()))
                .build();
    }

    @Transactional(readOnly = true)
    public List<ServicioPopularResponse> obtenerServiciosPopulares(int limite) {
        List<Cita> citas = citaRepository.findAll();
        
        Map<Integer, Long> serviciosCounts = citas.stream()
                .collect(Collectors.groupingBy(
                        cita -> cita.getServicio().getIdServicio(),
                        Collectors.counting()
                ));

        return serviciosCounts.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(limite)
                .map(entry -> {
                    Cita cita = citas.stream()
                            .filter(c -> c.getServicio().getIdServicio().equals(entry.getKey()))
                            .findFirst()
                            .orElseThrow();
                    
                    return ServicioPopularResponse.builder()
                            .idServicio(entry.getKey())
                            .nombreServicio(cita.getServicio().getNombre())
                            .cantidadCitas(entry.getValue())
                            .duracion(cita.getServicio().getDuracion().toString())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoPopularResponse> obtenerProductosPopulares(int limite) {
        List<Object[]> productosVendidos = detalleVentaRepository.findProductosMasVendidos();
        
        return productosVendidos.stream()
                .limit(limite)
                .map(data -> ProductoPopularResponse.builder()
                        .idProducto((Integer) data[0])
                        .nombreProducto((String) data[1])
                        .cantidadVendida(((Number) data[2]).longValue())
                        .totalVentas((BigDecimal) data[3])
                        .stockActual((Integer) data[4])
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IngresosPorPeriodoResponse> obtenerIngresosPorMes(int cantidadMeses) {
        LocalDateTime fechaFin = LocalDateTime.now();
        LocalDateTime fechaInicio = fechaFin.minusMonths(cantidadMeses);
        
        List<Venta> ventas = ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
        
        Map<YearMonth, List<Venta>> ventasPorMes = ventas.stream()
                .collect(Collectors.groupingBy(v -> YearMonth.from(v.getFechaVenta())));

        return ventasPorMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<Venta> ventasMes = entry.getValue();
                    BigDecimal totalIngresos = ventasMes.stream()
                            .map(Venta::getTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal promedio = ventasMes.isEmpty() 
                            ? BigDecimal.ZERO 
                            : totalIngresos.divide(
                                    BigDecimal.valueOf(ventasMes.size()), 
                                    2, 
                                    RoundingMode.HALF_UP
                              );

                    return IngresosPorPeriodoResponse.builder()
                            .periodo(entry.getKey().toString())
                            .totalIngresos(totalIngresos)
                            .cantidadVentas((long) ventasMes.size())
                            .cantidadCitas(0L) // Se puede calcular si se necesita
                            .promedioVenta(promedio)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteFrecuenteResponse> obtenerClientesFrecuentes(int limite) {
        List<Cliente> clientes = clienteRepository.findAll();
        
        return clientes.stream()
                .map(cliente -> {
                    Long cantidadCitas = citaRepository.countByCliente_IdCliente(cliente.getIdCliente());
                    Long cantidadCompras = ventaRepository.countByCliente_IdCliente(cliente.getIdCliente());
                    
                    return ClienteFrecuenteResponse.builder()
                            .idCliente(cliente.getIdCliente())
                            .nombreCliente(cliente.getUsuario().getNombre() + " " + 
                                         cliente.getUsuario().getApellido())
                            .cantidadCitas(cantidadCitas)
                            .cantidadCompras(cantidadCompras)
                            .canalPreferido(cliente.getUsuario().getCanalPreferido() != null 
                                    ? cliente.getUsuario().getCanalPreferido().toString() 
                                    : "No especificado")
                            .build();
                })
                .sorted(Comparator.comparing(ClienteFrecuenteResponse::getCantidadCitas).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstilistaProductivoResponse> obtenerEstilistasMasProductivos(int limite) {
        List<Object[]> data = citaRepository.getEstilistasMasActivos();
        
        return data.stream()
                .limit(limite)
                .map(obj -> {
                    Integer idEstilista = (Integer) obj[0];
                    String nombre = (String) obj[1];
                    String apellido = (String) obj[2];
                    Long cantidadCitas = ((Number) obj[3]).longValue();
                    
                    return EstilistaProductivoResponse.builder()
                            .idEstilista(idEstilista)
                            .nombreEstilista(nombre + " " + apellido)
                            .especialidad("") // Se puede agregar si está disponible
                            .cantidadCitas(cantidadCitas)
                            .clientesAtendidos(0L) // Se puede calcular desde HistorialEstilista
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerResumenDelDia() {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<Cita> citasHoy = citaRepository.findByFechaCitaBetween(inicioDia, finDia);
        List<Venta> ventasHoy = ventaRepository.findByFechaVentaBetween(inicioDia, finDia);

        BigDecimal ingresosHoy = ventasHoy.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("citasDelDia", citasHoy.size());
        resumen.put("citasPendientes", citasHoy.stream().filter(c -> c.getEstado() == EstadoCita.PENDIENTE).count());
        resumen.put("citasConfirmadas", citasHoy.stream().filter(c -> c.getEstado() == EstadoCita.CONFIRMADA).count());
        resumen.put("citasCompletadas", citasHoy.stream().filter(c -> c.getEstado() == EstadoCita.COMPLETADA).count());
        resumen.put("ventasDelDia", ventasHoy.size());
        resumen.put("ingresosDelDia", ingresosHoy);

        return resumen;
    }
}
