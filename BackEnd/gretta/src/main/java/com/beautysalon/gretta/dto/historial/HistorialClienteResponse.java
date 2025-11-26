package com.beautysalon.gretta.dto.historial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClienteResponse {

    private Integer idHistorial;
    private Integer idCliente;
    private String nombreCliente;
    private Integer idServicio;
    private String nombreServicio;
    private String descripcionServicio;
    private LocalDateTime fechaServicio;
}
