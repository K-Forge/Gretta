package com.beautysalon.gretta.dto.auth;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.entity.enums.TipoDocumento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 40, message = "El nombre no puede exceder 40 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 40, message = "El apellido no puede exceder 40 caracteres")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Size(max = 255, message = "El correo no puede exceder 255 caracteres")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El teléfono solo puede contener números y caracteres válidos")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 50, message = "El número de documento no puede exceder 50 caracteres")
    private String numeroDocumento;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;

    private CanalComunicacion canalPreferido;

    // Campos adicionales para estilista
    private String especialidad;
    private String disponibilidad;
}
