package com.beautysalon.gretta.dto.notificacion;

import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponse {

    private Integer idNotificacion;
    private Integer idUsuario;
    private String nombreUsuario;
    private Integer idCita;
    private Integer idPromocion;
    private TipoNotificacion tipo;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private EstadoNotificacion estado;
}
