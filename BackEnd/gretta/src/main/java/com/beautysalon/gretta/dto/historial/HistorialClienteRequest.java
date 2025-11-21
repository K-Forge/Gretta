package com.beautysalon.gretta.dto.historial;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClienteRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Integer idServicio;

    @NotNull(message = "La fecha del servicio es obligatoria")
    private LocalDateTime fechaServicio;
}
