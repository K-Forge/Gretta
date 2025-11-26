package com.beautysalon.gretta.dto.historial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasEstilistaResponse {

    private Integer idEstilista;
    private String nombreEstilista;
    private String especialidad;
    private Long totalVisitas;
    private Long totalClientesAtendidos;
    private List<Map<String, Object>> clientesFrecuentes;
    private LocalDateTime ultimaVisita;
}
