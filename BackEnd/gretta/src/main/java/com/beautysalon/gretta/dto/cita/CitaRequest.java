package com.beautysalon.gretta.dto.cita;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El ID del estilista es obligatorio")
    private Integer idEstilista;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Integer idServicio;

    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDateTime fechaCita;

    @NotBlank(message = "La hora de la cita es obligatoria")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", message = "Formato de hora inválido (HH:MM:SS)")
    private String horaCita;

    @NotNull(message = "El canal de reserva es obligatorio")
    private CanalComunicacion canalReserva;

    private EstadoCita estado;

    private String observaciones;
}
