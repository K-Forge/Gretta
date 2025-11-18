package com.beautysalon.gretta.dto.auth;

import com.beautysalon.gretta.entity.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tipo = "Bearer";
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private RolUsuario rol;
}
