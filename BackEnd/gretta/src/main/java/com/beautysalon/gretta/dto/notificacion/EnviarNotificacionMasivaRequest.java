package com.beautysalon.gretta.dto.notificacion;

import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnviarNotificacionMasivaRequest {

    @NotEmpty(message = "Debe proporcionar al menos un usuario")
    private List<Integer> idsUsuarios;

    @NotNull(message = "El tipo de notificación es requerido")
    private TipoNotificacion tipo;

    @NotBlank(message = "El asunto es requerido")
    private String asunto;

    @NotBlank(message = "El mensaje es requerido")
    private String mensaje;
}
