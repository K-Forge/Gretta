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
    @Builder.Default
    private String tipo = "Bearer";
    private com.beautysalon.gretta.entity.Usuario usuario;
    private Integer idCliente;
    private Integer idEstilista;
}
