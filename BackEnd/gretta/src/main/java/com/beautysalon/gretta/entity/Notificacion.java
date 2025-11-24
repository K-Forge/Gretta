package com.beautysalon.gretta.entity;

import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnotificacion")
    private Integer idNotificacion;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idcita")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "idpromocion")
    private Promocion promocion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo", nullable = false)
    private TipoNotificacion tipo;

    @Column(name = "asunto", nullable = false, length = 100)
    private String asunto;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @CreationTimestamp
    @Column(name = "fechaenvio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado", nullable = false)
    private EstadoNotificacion estado = EstadoNotificacion.PENDIENTE;
}
