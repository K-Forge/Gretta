package com.beautysalon.gretta.dto.promocionservicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromocionServicioResponse {

    private Integer idPromocionServicio;
    private Integer idPromocion;
    private String tituloPromocion;
    private Integer idServicio;
    private String nombreServicio;
}
