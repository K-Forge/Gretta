package com.beautysalon.gretta.dto.usuario;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPerfilRequest {

    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    private String nombre;

    @Size(min = 2, max = 40, message = "El apellido debe tener entre 2 y 40 caracteres")
    private String apellido;

    @Email(message = "El correo debe ser válido")
    private String correo;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El teléfono debe contener solo números y caracteres válidos")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    private CanalComunicacion canalPreferido;
}
