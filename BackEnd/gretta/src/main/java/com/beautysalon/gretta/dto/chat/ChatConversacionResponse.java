package com.beautysalon.gretta.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatConversacionResponse {

    private Integer idConversacion;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDateTime fechaInicio;
    private Long totalMensajes;
}
