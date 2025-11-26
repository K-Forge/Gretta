package com.beautysalon.gretta.dto.historial;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstilistaRequest {

    @NotNull(message = "El ID del estilista es obligatorio")
    private Integer idEstilista;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "La fecha de visita es obligatoria")
    private LocalDateTime fechaVisita;
}
