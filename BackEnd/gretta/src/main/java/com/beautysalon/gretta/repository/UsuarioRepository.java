package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);
    
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);
    
    boolean existsByCorreo(String correo);
    
    boolean existsByNumeroDocumento(String numeroDocumento);
}
