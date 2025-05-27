package com.utp.ataquesinformaticos.service;

import com.utp.ataquesinformaticos.dto.*;
import com.utp.ataquesinformaticos.exception.ResourceNotFoundException;
import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AtaqueService {

    private final AtaqueRepository ataqueRepository;
    private final AmenazaRepository amenazaRepository;

    @Autowired
    public AtaqueService(AtaqueRepository ataqueRepository, AmenazaRepository amenazaRepository) {
        this.ataqueRepository = ataqueRepository;
        this.amenazaRepository = amenazaRepository;
    }

    public List<AtaqueDTO> listarTodosLosAtaques() {
        return ataqueRepository.findAll().stream()
                .map(AtaqueDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    // Método para buscar ataque por ID
    public Ataque findById(Integer ataqueId) {
        return ataqueRepository.findById(ataqueId)
                .orElseThrow(() -> new EntityNotFoundException("Ataque con ID " + ataqueId + " no encontrado"));
    }

    public AtaqueDTO buscarPorId(Integer id) {
        Ataque ataque = ataqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ataque no encontrado con id: " + id));
        return AtaqueDTO.fromEntity(ataque);
    }

    public AtaqueDTO guardarAtaque(AtaqueDTO ataqueDTO) {
        // Validar que exista la amenaza asociada
        Amenaza amenaza = null;
        if (ataqueDTO.getAmenazaId() != null) {
            amenaza = amenazaRepository.findById(ataqueDTO.getAmenazaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Amenaza no encontrada con id: " + ataqueDTO.getAmenazaId()));
        }

        Ataque ataque = new Ataque();
        ataque.setTipo(ataqueDTO.getTipo());
        ataque.setVector(ataqueDTO.getVector());
        ataque.setSistemaAfectado(ataqueDTO.getSistemaAfectado());
        ataque.setSeveridad(ataqueDTO.getSeveridad());
        ataque.setFechaEvento(ataqueDTO.getFechaEvento() != null
                ? ataqueDTO.getFechaEvento() : LocalDate.now());
        ataque.setAmenaza(amenaza);

        Ataque savedAtaque = ataqueRepository.save(ataque);
        return AtaqueDTO.fromEntity(savedAtaque);
    }

    public AtaqueDTO actualizarAtaque(Integer id, AtaqueDTO ataqueDTO) {
        Ataque ataque = ataqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ataque no encontrado con id: " + id));

        // Actualizar amenaza asociada si cambió
        if (ataqueDTO.getAmenazaId() != null) {
            Amenaza amenaza = amenazaRepository.findById(ataqueDTO.getAmenazaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Amenaza no encontrada con id: " + ataqueDTO.getAmenazaId()));
            ataque.setAmenaza(amenaza);
        }

        ataque.setTipo(ataqueDTO.getTipo());
        ataque.setVector(ataqueDTO.getVector());
        ataque.setSistemaAfectado(ataqueDTO.getSistemaAfectado());
        ataque.setSeveridad(ataqueDTO.getSeveridad());
        if (ataqueDTO.getFechaEvento() != null) {
            ataque.setFechaEvento(ataqueDTO.getFechaEvento());
        }

        Ataque updatedAtaque = ataqueRepository.save(ataque);
        return AtaqueDTO.fromEntity(updatedAtaque);
    }

    public void eliminarAtaque(Integer id) {
        if (!ataqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ataque no encontrado con id: " + id);
        }
        ataqueRepository.deleteById(id);
    }

    public List<AtaqueDTO> buscarPorAmenazaId(Integer amenazaId) {
        return ataqueRepository.findByAmenazaId(amenazaId).stream()
                .map(AtaqueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AtaqueDTO> buscarPorSeveridad(Severidad severidad) {
        return ataqueRepository.findBySeveridad(severidad).stream()
                .map(AtaqueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Ataque> findAll() {
        return ataqueRepository.findAll();
    }

    public long countAllAtaques() {
        return ataqueRepository.count();
    }

    // Método para obtener los pedidos recientes (por ejemplo, los últimos 5)
    public List<Ataque> findRecentAtaques() {
        Pageable pageable = PageRequest.of(0, 4, Sort.by("fechaEvento").descending());
        return ataqueRepository.findAll(pageable).getContent();
    }
    
    //Cuenta los ataques por severidad
     public int contarAtaquesPorSeveridad(Severidad severidad) {
        return ataqueRepository.findBySeveridad(severidad).size();
    }
     
      // Encontrar ataques por severidad
    public List<Ataque> findAtaquesBySeveridad(Severidad severidad) {
        return ataqueRepository.findBySeveridad(severidad);
    }
    
    // Contar ataques por tipo
    public Map<String, Integer> contarAtaquesPorTipo() {
        List<Object[]> results = ataqueRepository.countByTipo();
        Map<String, Integer> map = new HashMap<>();
        for (Object[] result : results) {
            map.put((String) result[0], ((Long) result[1]).intValue());
        }
        return map;
    }
    
    // Contar ataques por mes
    public Map<String, Integer> contarAtaquesPorMes() {
        List<Object[]> results = ataqueRepository.countByMonth();
        Map<String, Integer> map = new HashMap<>();
        for (Object[] result : results) {
            map.put((String) result[0], ((Long) result[1]).intValue());
        }
        return map;
    }
    
    // Encontrar ataques por amenaza
    public List<Ataque> findAtaquesByAmenaza(Amenaza amenaza) {
        return ataqueRepository.findByAmenaza(amenaza);
    }
    
    // Obtener todos los ataques
    public List<Ataque> findAllAtaques() {
        return ataqueRepository.findAll();
    }

}
