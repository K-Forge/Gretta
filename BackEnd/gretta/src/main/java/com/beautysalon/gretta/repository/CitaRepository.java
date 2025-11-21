package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    
    List<Cita> findByCliente_IdCliente(Integer idCliente);
    
    List<Cita> findByEstilista_IdEstilista(Integer idEstilista);
    
    List<Cita> findByEstado(EstadoCita estado);
    
    @Query("SELECT c FROM Cita c WHERE c.estilista.idEstilista = :idEstilista AND c.fechaCita = :fecha")
    List<Cita> findByEstilistaAndFecha(Integer idEstilista, LocalDateTime fecha);
    
    @Query("SELECT c FROM Cita c WHERE c.cliente.idCliente = :idCliente ORDER BY c.fechaCita DESC")
    List<Cita> findByClienteOrderByFechaDesc(Integer idCliente);
    
    // Queries para tareas programadas
    List<Cita> findByEstadoAndFechaCitaBetween(EstadoCita estado, LocalDateTime inicio, LocalDateTime fin);
    
    List<Cita> findByEstadoAndFechaCitaBefore(EstadoCita estado, LocalDateTime fecha);
    
    long countByEstadoAndFechaCitaBetween(EstadoCita estado, LocalDateTime inicio, LocalDateTime fin);
    
    // Métodos adicionales para reportes y dashboard
    List<Cita> findByFechaCitaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<Cita> findByFechaCitaBetweenAndEstado(LocalDateTime inicio, LocalDateTime fin, EstadoCita estado);
    
    List<Cita> findByEstilista_IdEstilistaAndFechaCitaBetween(Integer idEstilista, LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT e.idEstilista, e.usuario.nombre, e.usuario.apellido, COUNT(c) as total " +
           "FROM Cita c JOIN c.estilista e " +
           "WHERE c.estado = 'COMPLETADA' " +
           "GROUP BY e.idEstilista, e.usuario.nombre, e.usuario.apellido " +
           "ORDER BY total DESC")
    List<Object[]> getEstilistasMasActivos();
    
    Long countByEstado(EstadoCita estado);
    
    Long countByCliente_IdCliente(Integer idCliente);
    
    List<Cita> findByCliente_IdClienteAndFechaCitaBetween(Integer idCliente, LocalDateTime inicio, LocalDateTime fin);
}
