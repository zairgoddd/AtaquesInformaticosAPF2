package com.utp.ataquesinformaticos.service;

import com.utp.ataquesinformaticos.dto.AmenazaDTO;
import com.utp.ataquesinformaticos.exception.ResourceNotFoundException;
import com.utp.ataquesinformaticos.model.Amenaza;
import com.utp.ataquesinformaticos.model.NivelRiesgo;
import com.utp.ataquesinformaticos.repository.AmenazaRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AmenazaService {

    private final AmenazaRepository amenazaRepository;

    @Autowired
    public AmenazaService(AmenazaRepository amenazaRepository) {
        this.amenazaRepository = amenazaRepository;
    }

    @Transactional(readOnly = true)
    public List<AmenazaDTO> listarTodasLasAmenazas() {
        return amenazaRepository.findAll().stream()
                .map(AmenazaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AmenazaDTO buscarPorId(Integer id) {
        Amenaza amenaza = amenazaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenaza no encontrada con id: " + id));
        return AmenazaDTO.fromEntity(amenaza);
    }

    @Transactional(readOnly = true)
    public Amenaza findById(Integer amenazaId) {
        return amenazaRepository.findById(amenazaId)
                .orElseThrow(() -> new EntityNotFoundException("Amenaza con ID " + amenazaId + " no encontrada"));
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public void eliminarAmenaza(Integer id) {
        if (!amenazaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenaza no encontrada con id: " + id);
        }
        amenazaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AmenazaDTO> buscarPorNivelRiesgo(NivelRiesgo nivelRiesgo) {
        return amenazaRepository.buscarPorNivel(nivelRiesgo).stream()
                .map(AmenazaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Amenaza> findAll() {
        return amenazaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long countAllAmenazas() {
        return amenazaRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Amenaza> findRecentAmenazas() {
        Pageable pageable = PageRequest.of(0, 4, Sort.by("fechaDeteccion").descending());
        return amenazaRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public int contarAmenazasPorNivelRiesgo(NivelRiesgo nivelRiesgo) {
        return amenazaRepository.buscarPorNivel(nivelRiesgo).size();
    }
}
