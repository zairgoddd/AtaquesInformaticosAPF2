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
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AmenazaService {

    private final AmenazaRepository amenazaRepository;
    
    @Autowired
    public AmenazaService(AmenazaRepository amenazaRepository) {
        this.amenazaRepository = amenazaRepository;
    }

    public List<AmenazaDTO> listarTodasLasAmenazas() {
        return amenazaRepository.findAll().stream()
                .map(AmenazaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AmenazaDTO buscarPorId(Integer id) {
        Amenaza amenaza = amenazaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenaza no encontrada con id: " + id));
        return AmenazaDTO.fromEntity(amenaza);
    }

    // Método para buscar amenaza por ID
    public Amenaza findById(Integer amenazaId) {
        return amenazaRepository.findById(amenazaId)
                .orElseThrow(() -> new EntityNotFoundException("Amenaza con ID " + amenazaId + " no encontrada"));
    }

    public AmenazaDTO guardarAmenaza(AmenazaDTO amenazaDTO) {
        Amenaza amenaza = new Amenaza();
        amenaza.setTipo(amenazaDTO.getTipo());
        amenaza.setDescripcion(amenazaDTO.getDescripcion());
        amenaza.setNivelRiesgo(amenazaDTO.getNivelRiesgo());
        amenaza.setFechaDeteccion(amenazaDTO.getFechaDeteccion() != null
                ? amenazaDTO.getFechaDeteccion() : LocalDate.now());

        Amenaza savedAmenaza = amenazaRepository.save(amenaza);
        return AmenazaDTO.fromEntity(savedAmenaza);
    }

    public AmenazaDTO actualizarAmenaza(Integer id, AmenazaDTO amenazaDTO) {
        Amenaza amenaza = amenazaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenaza no encontrada con id: " + id));

        amenaza.setTipo(amenazaDTO.getTipo());
        amenaza.setDescripcion(amenazaDTO.getDescripcion());
        amenaza.setNivelRiesgo(amenazaDTO.getNivelRiesgo());
        if (amenazaDTO.getFechaDeteccion() != null) {
            amenaza.setFechaDeteccion(amenazaDTO.getFechaDeteccion());
        }

        Amenaza updatedAmenaza = amenazaRepository.save(amenaza);
        return AmenazaDTO.fromEntity(updatedAmenaza);
    }

    public void eliminarAmenaza(Integer id) {
        if (!amenazaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenaza no encontrada con id: " + id);
        }
        amenazaRepository.deleteById(id);
    }

    public List<AmenazaDTO> buscarPorNivelRiesgo(NivelRiesgo nivelRiesgo) {
        return amenazaRepository.findByNivelRiesgo(nivelRiesgo).stream()
                .map(AmenazaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Amenaza> findAll() {
        return amenazaRepository.findAll();
    }

    public long countAllAmenazas() {
        return amenazaRepository.count();
    }

    // Método para obtener las amenazas recientes (por ejemplo, las ultimas 4)
    public List<Amenaza> findRecentAmenazas() {
        Pageable pageable = PageRequest.of(0, 4, Sort.by("fechaDeteccion").descending());
        return amenazaRepository.findAll(pageable).getContent();
    }

    //
    public int contarAmenazasPorNivelRiesgo(NivelRiesgo nivelRiesgo) {
        return amenazaRepository.findByNivelRiesgo(nivelRiesgo).size();
    }
    
    // Contar amenazas por nivel de riesgo
    public int contarAmenazasPorNivel(NivelRiesgo nivelRiesgo) {
        return amenazaRepository.countByNivelRiesgo(nivelRiesgo);
    }
    
    // Encontrar amenazas por nivel de riesgo
    public List<Amenaza> findAmenazasByNivelRiesgo(NivelRiesgo nivelRiesgo) {
        return amenazaRepository.findByNivelRiesgo(nivelRiesgo);
    }
    
    // Contar amenazas por tipo
    public Map<String, Integer> contarAmenazasPorTipo() {
        List<Object[]> results = amenazaRepository.countByTipo();
        Map<String, Integer> map = new HashMap<>();
        for (Object[] result : results) {
            map.put((String) result[0], ((Long) result[1]).intValue());
        }
        return map;
    }
    
    // Obtener todas las amenazas
    public List<Amenaza> findAllAmenazas() {
        return amenazaRepository.findAll();
    }
}
