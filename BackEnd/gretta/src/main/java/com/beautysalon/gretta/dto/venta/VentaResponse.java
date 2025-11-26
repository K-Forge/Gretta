package com.beautysalon.gretta.dto.venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponse {

    private Integer idVenta;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private List<DetalleVentaResponse> detalles;
}
