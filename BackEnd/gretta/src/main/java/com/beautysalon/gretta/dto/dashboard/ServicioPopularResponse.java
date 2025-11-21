package com.beautysalon.gretta.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioPopularResponse {

    private Integer idServicio;
    private String nombreServicio;
    private Long cantidadCitas;
    private String duracion;
}
