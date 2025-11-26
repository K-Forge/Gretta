package com.beautysalon.gretta.dto.cita;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {

    private Integer idCita;
    private Integer idCliente;
    private String nombreCliente;
    private Integer idEstilista;
    private String nombreEstilista;
    private Integer idServicio;
    private String nombreServicio;
    private LocalDateTime fechaCita;
    private String horaCita;
    private CanalComunicacion canalReserva;
    private EstadoCita estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
