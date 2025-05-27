package com.utp.ataquesinformaticos.service;

import com.utp.ataquesinformaticos.dto.AmenazaDTO;
import com.utp.ataquesinformaticos.exception.ResourceNotFoundException;
import com.utp.ataquesinformaticos.model.Amenaza;
import com.utp.ataquesinformaticos.model.NivelRiesgo;
import com.utp.ataquesinformaticos.repository.AmenazaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AmenazaServiceTest {

    @Mock
    private AmenazaRepository amenazaRepository;

    @InjectMocks
    private AmenazaService amenazaService;

    private Amenaza amenaza1;
    private Amenaza amenaza2;
    private AmenazaDTO amenazaDTO;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        amenaza1 = new Amenaza();
        amenaza1.setId(1);
        amenaza1.setTipo("Malware");
        amenaza1.setDescripcion("Software malicioso");
        amenaza1.setNivelRiesgo(NivelRiesgo.ALTO);
        amenaza1.setFechaDeteccion(LocalDate.now());

        amenaza2 = new Amenaza();
        amenaza2.setId(2);
        amenaza2.setTipo("Phishing");
        amenaza2.setDescripcion("Ataque de ingeniería social");
        amenaza2.setNivelRiesgo(NivelRiesgo.MEDIO);
        amenaza2.setFechaDeteccion(LocalDate.now().minusDays(5));

        amenazaDTO = new AmenazaDTO();
        amenazaDTO.setTipo("Ransomware");
        amenazaDTO.setDescripcion("Secuestro de datos");
        amenazaDTO.setNivelRiesgo(NivelRiesgo.CRITICO);
        amenazaDTO.setFechaDeteccion(LocalDate.now());
    }

    @Test
    @DisplayName("Test listar todas las amenazas")
    void listarTodasLasAmenazasTest() {
        // Given
        List<Amenaza> amenazas = Arrays.asList(amenaza1, amenaza2);
        when(amenazaRepository.findAll()).thenReturn(amenazas);

        // When
        List<AmenazaDTO> result = amenazaService.listarTodasLasAmenazas();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Malware", result.get(0).getTipo());
        assertEquals("Phishing", result.get(1).getTipo());
        verify(amenazaRepository).findAll();
    }

    @Test
    @DisplayName("Test buscar amenaza por ID existente")
    void buscarPorIdExistenteTest() {
        // Given
        Integer id = 1;
        when(amenazaRepository.findById(id)).thenReturn(Optional.of(amenaza1));

        // When
        AmenazaDTO result = amenazaService.buscarPorId(id);

        // Then
        assertNotNull(result);
        assertEquals("Malware", result.getTipo());
        assertEquals(NivelRiesgo.ALTO, result.getNivelRiesgo());
        verify(amenazaRepository).findById(id);
    }

    @Test
    @DisplayName("Test buscar amenaza por ID inexistente")
    void buscarPorIdInexistenteTest() {
        // Given
        Integer id = 999;
        when(amenazaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            amenazaService.buscarPorId(id);
        });
        verify(amenazaRepository).findById(id);
    }

    @Test
    @DisplayName("Test guardar nueva amenaza")
    void guardarAmenazaTest() {
        // Given
        Amenaza amenazaGuardada = new Amenaza();
        amenazaGuardada.setId(3);
        amenazaGuardada.setTipo(amenazaDTO.getTipo());
        amenazaGuardada.setDescripcion(amenazaDTO.getDescripcion());
        amenazaGuardada.setNivelRiesgo(amenazaDTO.getNivelRiesgo());
        amenazaGuardada.setFechaDeteccion(amenazaDTO.getFechaDeteccion());

        when(amenazaRepository.save(any(Amenaza.class))).thenReturn(amenazaGuardada);

        // When
        AmenazaDTO result = amenazaService.guardarAmenaza(amenazaDTO);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("Ransomware", result.getTipo());
        assertEquals(NivelRiesgo.CRITICO, result.getNivelRiesgo());
        verify(amenazaRepository).save(any(Amenaza.class));
    }

    @Test
    @DisplayName("Test guardar amenaza con fecha nula")
    void guardarAmenazaConFechaNulaTest() {
        // Given
        amenazaDTO.setFechaDeteccion(null);
        
        Amenaza amenazaGuardada = new Amenaza();
        amenazaGuardada.setId(3);
        amenazaGuardada.setTipo(amenazaDTO.getTipo());
        amenazaGuardada.setDescripcion(amenazaDTO.getDescripcion());
        amenazaGuardada.setNivelRiesgo(amenazaDTO.getNivelRiesgo());
        amenazaGuardada.setFechaDeteccion(LocalDate.now());

        when(amenazaRepository.save(any(Amenaza.class))).thenReturn(amenazaGuardada);

        // When
        AmenazaDTO result = amenazaService.guardarAmenaza(amenazaDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.getFechaDeteccion());
        assertEquals(LocalDate.now(), result.getFechaDeteccion());
        verify(amenazaRepository).save(any(Amenaza.class));
    }

    @Test
    @DisplayName("Test actualizar amenaza existente")
    void actualizarAmenazaExistenteTest() {
        // Given
        Integer id = 1;
        when(amenazaRepository.findById(id)).thenReturn(Optional.of(amenaza1));
        
        amenazaDTO.setId(id);
        
        Amenaza amenazaActualizada = new Amenaza();
        amenazaActualizada.setId(id);
        amenazaActualizada.setTipo(amenazaDTO.getTipo());
        amenazaActualizada.setDescripcion(amenazaDTO.getDescripcion());
        amenazaActualizada.setNivelRiesgo(amenazaDTO.getNivelRiesgo());
        amenazaActualizada.setFechaDeteccion(amenazaDTO.getFechaDeteccion());

        when(amenazaRepository.save(any(Amenaza.class))).thenReturn(amenazaActualizada);

        // When
        AmenazaDTO result = amenazaService.actualizarAmenaza(id, amenazaDTO);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Ransomware", result.getTipo());
        assertEquals(NivelRiesgo.CRITICO, result.getNivelRiesgo());
        verify(amenazaRepository).findById(id);
        verify(amenazaRepository).save(any(Amenaza.class));
    }

    @Test
    @DisplayName("Test actualizar amenaza inexistente")
    void actualizarAmenazaInexistenteTest() {
        // Given
        Integer id = 999;
        when(amenazaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            amenazaService.actualizarAmenaza(id, amenazaDTO);
        });
        verify(amenazaRepository).findById(id);
        verify(amenazaRepository, never()).save(any(Amenaza.class));
    }

    @Test
    @DisplayName("Test eliminar amenaza existente")
    void eliminarAmenazaExistenteTest() {
        // Given
        Integer id = 1;
        when(amenazaRepository.existsById(id)).thenReturn(true);
        doNothing().when(amenazaRepository).deleteById(id);

        // When
        amenazaService.eliminarAmenaza(id);

        // Then
        verify(amenazaRepository).existsById(id);
        verify(amenazaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Test eliminar amenaza inexistente")
    void eliminarAmenazaInexistenteTest() {
        // Given
        Integer id = 999;
        when(amenazaRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            amenazaService.eliminarAmenaza(id);
        });
        verify(amenazaRepository).existsById(id);
        verify(amenazaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Test buscar amenazas por nivel de riesgo")
    void buscarPorNivelRiesgoTest() {
        // Given
        NivelRiesgo nivelRiesgo = NivelRiesgo.ALTO;
        List<Amenaza> amenazas = Arrays.asList(amenaza1);
        when(amenazaRepository.findByNivelRiesgo(nivelRiesgo)).thenReturn(amenazas);

        // When
        List<AmenazaDTO> result = amenazaService.buscarPorNivelRiesgo(nivelRiesgo);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Malware", result.get(0).getTipo());
        assertEquals(NivelRiesgo.ALTO, result.get(0).getNivelRiesgo());
        verify(amenazaRepository).findByNivelRiesgo(nivelRiesgo);
    }

    @Test
    @DisplayName("Test contar amenazas por nivel de riesgo")
    void contarAmenazasPorNivelRiesgoTest() {
        // Given
        NivelRiesgo nivelRiesgo = NivelRiesgo.ALTO;
        List<Amenaza> amenazas = Arrays.asList(amenaza1);
        when(amenazaRepository.findByNivelRiesgo(nivelRiesgo)).thenReturn(amenazas);

        // When
        int result = amenazaService.contarAmenazasPorNivelRiesgo(nivelRiesgo);

        // Then
        assertEquals(1, result);
        verify(amenazaRepository).findByNivelRiesgo(nivelRiesgo);
    }

    @Test
    @DisplayName("Test encontrar amenazas recientes")
    void findRecentAmenazasTest() {
        // Given
        List<Amenaza> amenazasRecientes = Arrays.asList(amenaza1, amenaza2);
        Page<Amenaza> page = new PageImpl<>(amenazasRecientes);
        when(amenazaRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        List<Amenaza> result = amenazaService.findRecentAmenazas();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(amenazaRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test encontrar amenaza por ID con método específico")
    void findByIdTest() {
        // Given
        Integer id = 1;
        when(amenazaRepository.findById(id)).thenReturn(Optional.of(amenaza1));

        // When
        Amenaza result = amenazaService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals("Malware", result.getTipo());
        assertEquals(NivelRiesgo.ALTO, result.getNivelRiesgo());
        verify(amenazaRepository).findById(id);
    }

    @Test
    @DisplayName("Test error al buscar amenaza por ID con método específico")
    void findByIdNotFoundTest() {
        // Given
        Integer id = 999;
        when(amenazaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            amenazaService.findById(id);
        });
        verify(amenazaRepository).findById(id);
    }
}