package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.ChatMensaje;
import com.beautysalon.gretta.entity.enums.TipoMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMensajeRepository extends JpaRepository<ChatMensaje, Integer> {

    @Query("SELECT m FROM ChatMensaje m WHERE m.conversacion.idConversacion = :idConversacion " +
           "ORDER BY m.fechaMensaje ASC")
    List<ChatMensaje> findByConversacionOrderByFecha(@Param("idConversacion") Integer idConversacion);

    @Query("SELECT m FROM ChatMensaje m WHERE m.conversacion.idConversacion = :idConversacion " +
           "AND m.tipoMensaje = :tipoMensaje ORDER BY m.fechaMensaje ASC")
    List<ChatMensaje> findByConversacionAndTipo(
            @Param("idConversacion") Integer idConversacion,
            @Param("tipoMensaje") TipoMensaje tipoMensaje);

    @Query("SELECT COUNT(m) FROM ChatMensaje m WHERE m.conversacion.idConversacion = :idConversacion")
    Long countByConversacion(@Param("idConversacion") Integer idConversacion);

    @Query("SELECT COUNT(m) FROM ChatMensaje m WHERE m.conversacion.idConversacion = :idConversacion " +
           "AND m.tipoMensaje = :tipoMensaje")
    Long countByConversacionAndTipo(
            @Param("idConversacion") Integer idConversacion,
            @Param("tipoMensaje") TipoMensaje tipoMensaje);
}
