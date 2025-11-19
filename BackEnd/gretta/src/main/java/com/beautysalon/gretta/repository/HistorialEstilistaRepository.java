package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.HistorialEstilista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialEstilistaRepository extends JpaRepository<HistorialEstilista, Integer> {
    
    List<HistorialEstilista> findByEstilista_IdEstilista(Integer idEstilista);
    
    @Query("SELECT h FROM HistorialEstilista h WHERE h.estilista.idEstilista = :idEstilista ORDER BY h.fechaVisita DESC")
    List<HistorialEstilista> findByEstilistaOrderByFechaDesc(Integer idEstilista);
    
    @Query("SELECT h FROM HistorialEstilista h WHERE h.cliente.idCliente = :idCliente")
    List<HistorialEstilista> findByCliente(Integer idCliente);
    
    @Query("SELECT h FROM HistorialEstilista h WHERE h.estilista.idEstilista = :idEstilista " +
           "AND h.fechaVisita BETWEEN :fechaInicio AND :fechaFin ORDER BY h.fechaVisita DESC")
    List<HistorialEstilista> findByEstilistaAndFechaBetween(Integer idEstilista, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(h) FROM HistorialEstilista h WHERE h.estilista.idEstilista = :idEstilista")
    Long countByEstilista(Integer idEstilista);
    
    @Query("SELECT COUNT(DISTINCT h.cliente.idCliente) FROM HistorialEstilista h WHERE h.estilista.idEstilista = :idEstilista")
    Long countClientesAtendidos(Integer idEstilista);
    
    @Query("SELECT h.cliente.usuario.nombre, h.cliente.usuario.apellido, COUNT(h) as visitas " +
           "FROM HistorialEstilista h " +
           "WHERE h.estilista.idEstilista = :idEstilista " +
           "GROUP BY h.cliente.idCliente, h.cliente.usuario.nombre, h.cliente.usuario.apellido " +
           "ORDER BY visitas DESC")
    List<Object[]> getClientesFrecuentesPorEstilista(Integer idEstilista);
    
    @Query("SELECT h.estilista.usuario.nombre, h.estilista.usuario.apellido, COUNT(h) as trabajos " +
           "FROM HistorialEstilista h " +
           "GROUP BY h.estilista.idEstilista, h.estilista.usuario.nombre, h.estilista.usuario.apellido " +
           "ORDER BY trabajos DESC")
    List<Object[]> getEstilistasMasActivos();
    
    @Query("SELECT h FROM HistorialEstilista h " +
           "WHERE h.estilista.idEstilista = :idEstilista AND h.cliente.idCliente = :idCliente " +
           "ORDER BY h.fechaVisita DESC")
    List<HistorialEstilista> findByEstilistaAndCliente(Integer idEstilista, Integer idCliente);
}
