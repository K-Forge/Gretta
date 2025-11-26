package com.beautysalon.gretta.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
}
