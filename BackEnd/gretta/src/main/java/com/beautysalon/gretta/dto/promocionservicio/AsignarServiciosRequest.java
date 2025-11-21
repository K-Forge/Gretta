package com.beautysalon.gretta.dto.promocionservicio;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarServiciosRequest {

    @NotNull(message = "El ID de la promoción es requerido")
    private Integer idPromocion;

    @NotEmpty(message = "Debe proporcionar al menos un servicio")
    private List<Integer> idsServicios;
}
