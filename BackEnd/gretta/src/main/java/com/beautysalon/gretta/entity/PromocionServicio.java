package com.beautysalon.gretta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promocionservicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpromocionservicio")
    private Integer idPromocionServicio;

    @ManyToOne
    @JoinColumn(name = "idpromocion", nullable = false)
    private Promocion promocion;

    @ManyToOne
    @JoinColumn(name = "idservicio", nullable = false)
    private Servicio servicio;
}
