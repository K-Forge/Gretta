package com.beautysalon.gretta.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteFrecuenteResponse {

    private Integer idCliente;
    private String nombreCliente;
    private Long cantidadCitas;
    private Long cantidadCompras;
    private String canalPreferido;
}
