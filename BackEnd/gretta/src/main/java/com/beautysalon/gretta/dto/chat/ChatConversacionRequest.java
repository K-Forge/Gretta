package com.beautysalon.gretta.dto.chat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatConversacionRequest {

    @NotNull(message = "El ID del cliente es requerido")
    private Integer idCliente;
}
