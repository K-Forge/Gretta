package com.beautysalon.gretta.dto.notificacion;

import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequest {

    @NotNull(message = "El ID del usuario es requerido")
    private Integer idUsuario;

    private Integer idCita;

    private Integer idPromocion;

    @NotNull(message = "El tipo de notificación es requerido")
    private TipoNotificacion tipo;

    @NotBlank(message = "El asunto es requerido")
    @Size(max = 100, message = "El asunto no puede exceder 100 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje es requerido")
    private String mensaje;
}
