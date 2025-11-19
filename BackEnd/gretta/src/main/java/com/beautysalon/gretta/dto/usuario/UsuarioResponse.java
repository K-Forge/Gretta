package com.beautysalon.gretta.dto.usuario;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.entity.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private RolUsuario rol;
    private CanalComunicacion canalPreferido;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
