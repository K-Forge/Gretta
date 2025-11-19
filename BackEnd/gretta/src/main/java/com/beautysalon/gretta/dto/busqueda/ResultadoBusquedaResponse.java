package com.beautysalon.gretta.dto.busqueda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoBusquedaResponse<T> {

    private List<T> resultados;
    private Long totalResultados;
    private Integer paginaActual;
    private Integer totalPaginas;
    private Integer tamanoPagina;
}
