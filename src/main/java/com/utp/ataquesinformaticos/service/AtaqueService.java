package com.utp.ataquesinformaticos.service;

import com.utp.ataquesinformaticos.dto.AtaqueDTO;
import com.utp.ataquesinformaticos.exception.ResourceNotFoundException;
import com.utp.ataquesinformaticos.model.Amenaza;
import com.utp.ataquesinformaticos.model.Ataque;
import com.utp.ataquesinformaticos.model.Severidad;
import com.utp.ataquesinformaticos.repository.AmenazaRepository;
import com.utp.ataquesinformaticos.repository.AtaqueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtaqueService {

    private final AtaqueRepository ataqueRepository;
    private final AmenazaRepository amenazaRepository;

    @Autowired
    public AtaqueService(AtaqueRepository ataqueRepository, AmenazaRepository amenazaRepository) {
        this.ataqueRepository = ataqueRepository;
        this.amenazaRepository = amenazaRepository;
    }

    @Transactional(readOnly = true)
    public List<AtaqueDTO> listarTodosLosAtaques() {
        return ataqueRepository.findAll().stream()
                .map(AtaqueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Ataque findById(Integer ataqueId) {
        return ataqueRepository.findById(ataqueId)
                .orElseThrow(() -> new EntityNotFoundException("Ataque con ID " + ataqueId + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public AtaqueDTO buscarPorId(Integer id) {
        Ataque ataque = ataqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ataque no encontrado con id: " + id));
        return AtaqueDTO.fromEntity(ataque);
    }

    @Transactional
    public AtaqueDTO guardarAtaque(AtaqueDTO ataqueDTO) {
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

    @Transactional
    public AtaqueDTO actualizarAtaque(Integer id, AtaqueDTO ataqueDTO) {
        Ataque ataque = ataqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ataque no encontrado con id: " + id));

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

    @Transactional
    public void eliminarAtaque(Integer id) {
        if (!ataqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ataque no encontrado con id: " + id);
        }
        ataqueRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AtaqueDTO> buscarPorAmenazaId(Integer amenazaId) {
        return ataqueRepository.findByAmenazaId(amenazaId).stream()
                .map(AtaqueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AtaqueDTO> buscarPorSeveridad(Severidad severidad) {
        return ataqueRepository.findBySeveridad(severidad).stream()
                .map(AtaqueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Ataque> findAll() {
        return ataqueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long countAllAtaques() {
        return ataqueRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Ataque> findRecentAtaques() {
        Pageable pageable = PageRequest.of(0, 4, Sort.by("fechaEvento").descending());
        return ataqueRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public int contarAtaquesPorSeveridad(Severidad severidad) {
        return ataqueRepository.findBySeveridad(severidad).size();
    }
}
