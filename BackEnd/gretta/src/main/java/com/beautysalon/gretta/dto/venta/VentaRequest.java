package com.beautysalon.gretta.dto.venta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<DetalleVentaDTO> detalles;
}
