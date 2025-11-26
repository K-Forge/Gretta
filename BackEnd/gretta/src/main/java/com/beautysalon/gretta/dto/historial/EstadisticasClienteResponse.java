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
public class EstadisticasClienteResponse {

    private Integer idCliente;
    private String nombreCliente;
    private Long totalServicios;
    private List<Map<String, Object>> serviciosFrecuentes;
    private LocalDateTime ultimaVisita;
}
