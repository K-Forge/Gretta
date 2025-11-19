package com.beautysalon.gretta.dto.chat;

import com.beautysalon.gretta.entity.enums.TipoMensaje;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMensajeResponse {

    private Integer idMensaje;
    private Integer idConversacion;
    private TipoMensaje tipoMensaje;
    private String contenido;
    private LocalDateTime fechaMensaje;
}
