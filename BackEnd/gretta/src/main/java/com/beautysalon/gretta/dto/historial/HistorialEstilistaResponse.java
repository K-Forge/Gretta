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
public class HistorialEstilistaResponse {

    private Integer idHistorial;
    private Integer idEstilista;
    private String nombreEstilista;
    private String especialidadEstilista;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDateTime fechaVisita;
}
