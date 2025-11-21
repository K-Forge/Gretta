package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Estilista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstilistaRepository extends JpaRepository<Estilista, Integer> {
    
    Optional<Estilista> findByUsuario_IdUsuario(Integer idUsuario);
    
    Optional<Estilista> findByUsuario(com.beautysalon.gretta.entity.Usuario usuario);
}
