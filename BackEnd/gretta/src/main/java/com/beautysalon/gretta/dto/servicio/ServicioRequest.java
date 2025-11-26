package com.beautysalon.gretta.dto.servicio;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioRequest {

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La duración es obligatoria")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", message = "Formato de duración inválido (HH:MM:SS)")
    private String duracion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    private Boolean activo = true;
}
