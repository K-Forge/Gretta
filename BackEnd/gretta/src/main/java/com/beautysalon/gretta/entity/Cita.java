package com.beautysalon.gretta.entity;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcita")
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idestilista", nullable = false)
    private Estilista estilista;

    @ManyToOne
    @JoinColumn(name = "idservicio", nullable = false)
    private Servicio servicio;

    @Column(name = "fechacita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "horacita", nullable = false)
    private LocalTime horaCita;

    @Enumerated(EnumType.STRING)
    @Column(name = "canalreserva", nullable = false, columnDefinition = "canal_comunicacion")
    private CanalComunicacion canalReserva;

    @CreationTimestamp
    @Column(name = "fechacreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_cita")
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}
