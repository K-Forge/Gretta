package com.beautysalon.gretta.dto.usuario;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.entity.enums.TipoDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class ActualizarUsuarioRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 40, message = "El apellido debe tener entre 2 y 40 caracteres")
    private String apellido;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo debe ser válido")
    private String correo;

    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El teléfono debe contener solo números y caracteres válidos")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    private TipoDocumento tipoDocumento;

    private String numeroDocumento;

    private RolUsuario rol;

    private CanalComunicacion canalPreferido;

    private Boolean activo;
}
