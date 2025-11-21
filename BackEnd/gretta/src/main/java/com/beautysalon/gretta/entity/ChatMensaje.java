package com.beautysalon.gretta.entity;

import com.beautysalon.gretta.entity.enums.TipoMensaje;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatmensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmensaje")
    private Integer idMensaje;

    @ManyToOne
    @JoinColumn(name = "idconversacion", nullable = false)
    private ChatConversacion conversacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipomensaje", nullable = false)
    private TipoMensaje tipoMensaje;

    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fechamensaje", nullable = false)
    private LocalDateTime fechaMensaje;
}
