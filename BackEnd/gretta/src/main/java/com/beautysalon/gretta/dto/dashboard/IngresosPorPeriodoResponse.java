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
public class IngresosPorPeriodoResponse {

    private String periodo;
    private BigDecimal totalIngresos;
    private Long cantidadVentas;
    private Long cantidadCitas;
    private BigDecimal promedioVenta;
}
