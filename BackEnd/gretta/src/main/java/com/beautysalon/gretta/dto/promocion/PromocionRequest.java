package com.beautysalon.gretta.dto.promocion;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede exceder 100 caracteres")
    private String titulo;

    private String descripcion;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El descuento no puede ser mayor a 100")
    private BigDecimal descuento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    private Boolean activo = true;
}
