package com.beautysalon.gretta.entity;

import com.beautysalon.gretta.entity.converter.CanalComunicacionConverter;
import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.entity.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false, length = 40)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 40)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 255)
    private String correo;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipodocumento", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "numerodocumento", nullable = false, unique = true, length = 50)
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuario rol;

    @Convert(converter = CanalComunicacionConverter.class)
    @Column(name = "canalpreferido")
    private CanalComunicacion canalPreferido;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fechacreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "creadopor")
    private Integer creadoPor;

    @Column(name = "modificadopor")
    private Integer modificadoPor;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Estilista estilista;
}
