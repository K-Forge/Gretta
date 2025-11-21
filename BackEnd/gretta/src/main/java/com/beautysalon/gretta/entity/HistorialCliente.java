package com.beautysalon.gretta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historialcliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhistorial")
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idservicio", nullable = false)
    private Servicio servicio;

    @Column(name = "fechaservicio", nullable = false)
    private LocalDateTime fechaServicio;
}
