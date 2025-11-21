package com.beautysalon.gretta.dto.chat;

import com.beautysalon.gretta.entity.enums.TipoMensaje;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMensajeRequest {

    @NotNull(message = "El ID de la conversación es requerido")
    private Integer idConversacion;

    @NotNull(message = "El tipo de mensaje es requerido")
    private TipoMensaje tipoMensaje;

    @NotBlank(message = "El contenido del mensaje es requerido")
    private String contenido;
}
