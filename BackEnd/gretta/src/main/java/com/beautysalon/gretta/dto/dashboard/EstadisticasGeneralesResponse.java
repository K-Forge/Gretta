package com.beautysalon.gretta.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasGeneralesResponse {

    private Long totalClientes;
    private Long totalEstilistas;
    private Long totalServicios;
    private Long totalProductos;
    private Long totalCitas;
    private Long citasPendientes;
    private Long citasConfirmadas;
    private Long citasCompletadas;
    private Long totalVentas;
    private BigDecimal ingresosTotales;
    private BigDecimal ingresosDelMes;
    private Long clientesActivos;
    private Long promocionesVigentes;
}
