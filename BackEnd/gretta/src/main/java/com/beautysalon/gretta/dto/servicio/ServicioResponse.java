package com.beautysalon.gretta.dto.servicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponse {

    private Integer idServicio;
    private String nombre;
    private String descripcion;
    private String duracion;
    private BigDecimal precio;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
