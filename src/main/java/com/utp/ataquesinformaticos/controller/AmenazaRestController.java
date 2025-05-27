package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.dto.*;
import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.service.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/amenazas")
public class AmenazaRestController {
    
    private final AmenazaService amenazaService;
    
    @Autowired
    public AmenazaRestController(AmenazaService amenazaService) {
        this.amenazaService = amenazaService;
    }
    
    @GetMapping
    public ResponseEntity<List<AmenazaDTO>> listarAmenazas() {
        List<AmenazaDTO> amenazas = amenazaService.listarTodasLasAmenazas();
        return ResponseEntity.ok(amenazas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AmenazaDTO> obtenerAmenaza(@PathVariable Integer id) {
        AmenazaDTO amenaza = amenazaService.buscarPorId(id);
        return ResponseEntity.ok(amenaza);
    }
    
    @PostMapping
    public ResponseEntity<AmenazaDTO> crearAmenaza(@Valid @RequestBody AmenazaDTO amenazaDTO) {
        AmenazaDTO nuevaAmenaza = amenazaService.guardarAmenaza(amenazaDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaAmenaza.getId())
                .toUri();
        return ResponseEntity.created(location).body(nuevaAmenaza);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AmenazaDTO> actualizarAmenaza(
            @PathVariable Integer id, 
            @Valid @RequestBody AmenazaDTO amenazaDTO) {
        AmenazaDTO amenazaActualizada = amenazaService.actualizarAmenaza(id, amenazaDTO);
        return ResponseEntity.ok(amenazaActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAmenaza(@PathVariable Integer id) {
        amenazaService.eliminarAmenaza(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/nivel/{nivelRiesgo}")
    public ResponseEntity<List<AmenazaDTO>> listarPorNivelRiesgo(
            @PathVariable NivelRiesgo nivelRiesgo) {
        List<AmenazaDTO> amenazas = amenazaService.buscarPorNivelRiesgo(nivelRiesgo);
        return ResponseEntity.ok(amenazas);
    }
}
