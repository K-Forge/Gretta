package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Notificacion;
import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    
    List<Notificacion> findByUsuario_IdUsuario(Integer idUsuario);
    
    List<Notificacion> findByEstado(EstadoNotificacion estado);
    
    // Queries para tareas programadas
    List<Notificacion> findByEstadoAndFechaEnvioBefore(EstadoNotificacion estado, LocalDateTime fecha);
    
    long countByEstadoAndFechaEnvioBetween(EstadoNotificacion estado, LocalDateTime inicio, LocalDateTime fin);
    
    // Métodos adicionales para gestión de notificaciones
    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaEnvioDesc(Integer idUsuario);
    
    List<Notificacion> findByUsuario_IdUsuarioAndEstado(Integer idUsuario, EstadoNotificacion estado);
    
    List<Notificacion> findByTipo(com.beautysalon.gretta.entity.enums.TipoNotificacion tipo);
    
    List<Notificacion> findByCita_IdCita(Integer idCita);
    
    List<Notificacion> findByPromocion_IdPromocion(Integer idPromocion);
    
    Long countByUsuario_IdUsuario(Integer idUsuario);
    
    Long countByUsuario_IdUsuarioAndEstado(Integer idUsuario, EstadoNotificacion estado);
}
