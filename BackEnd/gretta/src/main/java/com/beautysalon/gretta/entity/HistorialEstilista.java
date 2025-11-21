package com.beautysalon.gretta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historialestilista")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstilista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhistorial")
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "idestilista", nullable = false)
    private Estilista estilista;

    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;

    @Column(name = "fechavisita", nullable = false)
    private LocalDateTime fechaVisita;
}
