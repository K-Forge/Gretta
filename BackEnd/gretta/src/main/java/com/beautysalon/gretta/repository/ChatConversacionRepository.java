package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.ChatConversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversacionRepository extends JpaRepository<ChatConversacion, Integer> {

    @Query("SELECT c FROM ChatConversacion c WHERE c.cliente.idCliente = :idCliente ORDER BY c.fechaInicio DESC")
    List<ChatConversacion> findByClienteOrderByFechaDesc(@Param("idCliente") Integer idCliente);

    @Query("SELECT c FROM ChatConversacion c WHERE c.cliente.idCliente = :idCliente " +
           "AND c.fechaInicio BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fechaInicio DESC")
    List<ChatConversacion> findByClienteAndFechaBetween(
            @Param("idCliente") Integer idCliente,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT c FROM ChatConversacion c ORDER BY c.fechaInicio DESC")
    List<ChatConversacion> findAllOrderByFechaDesc();

    @Query("SELECT c FROM ChatConversacion c WHERE c.cliente.idCliente = :idCliente " +
           "ORDER BY c.fechaInicio DESC LIMIT 1")
    Optional<ChatConversacion> findUltimaConversacionByCliente(@Param("idCliente") Integer idCliente);

    @Query("SELECT COUNT(c) FROM ChatConversacion c WHERE c.cliente.idCliente = :idCliente")
    Long countByCliente(@Param("idCliente") Integer idCliente);
}
