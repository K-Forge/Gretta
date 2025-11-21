package com.beautysalon.gretta.dto.promocionservicio;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromocionServicioRequest {

    @NotNull(message = "El ID de la promoción es requerido")
    private Integer idPromocion;

    @NotNull(message = "El ID del servicio es requerido")
    private Integer idServicio;
}
