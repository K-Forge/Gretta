package com.beautysalon.gretta.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversacionDetalleResponse {

    private Integer idConversacion;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDateTime fechaInicio;
    private Long totalMensajes;
    private List<ChatMensajeResponse> mensajes;
}
