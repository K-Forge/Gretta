package com.beautysalon.gretta.dto.reporte;

import com.beautysalon.gretta.entity.enums.EstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteCitaResponse {

    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String nombreCliente;
    private String nombreEstilista;
    private String nombreServicio;
    private EstadoCita estado;
    private String canalReserva;
    private String observaciones;
}
