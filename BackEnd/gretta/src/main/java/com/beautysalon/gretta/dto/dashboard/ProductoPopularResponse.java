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
public class ProductoPopularResponse {

    private Integer idProducto;
    private String nombreProducto;
    private Long cantidadVendida;
    private BigDecimal totalVentas;
    private Integer stockActual;
}
