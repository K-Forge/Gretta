package com.beautysalon.gretta.service;

import com.beautysalon.gretta.entity.Estilista;
import com.beautysalon.gretta.repository.EstilistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstilistaService {

    private final EstilistaRepository estilistaRepository;

    @Transactional(readOnly = true)
    public List<Estilista> obtenerTodos() {
        return estilistaRepository.findAll();
    }
}
