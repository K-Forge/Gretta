package com.beautysalon.gretta.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstilistaProductivoResponse {

    private Integer idEstilista;
    private String nombreEstilista;
    private String especialidad;
    private Long cantidadCitas;
    private Long clientesAtendidos;
}
