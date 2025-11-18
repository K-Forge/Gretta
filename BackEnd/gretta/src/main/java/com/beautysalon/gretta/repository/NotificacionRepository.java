package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Notificacion;
import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    
    List<Notificacion> findByUsuario_IdUsuario(Integer idUsuario);
    
    List<Notificacion> findByEstado(EstadoNotificacion estado);
}
