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
@RequestMapping("/api/ataques")
public class AtaqueRestController {
    
    private final AtaqueService ataqueService;
    
    @Autowired
    public AtaqueRestController(AtaqueService ataqueService) {
        this.ataqueService = ataqueService;
    }
    
    @GetMapping
    public ResponseEntity<List<AtaqueDTO>> listarAtaques() {
        List<AtaqueDTO> ataques = ataqueService.listarTodosLosAtaques();
        return ResponseEntity.ok(ataques);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AtaqueDTO> obtenerAtaque(@PathVariable Integer id) {
        AtaqueDTO ataque = ataqueService.buscarPorId(id);
        return ResponseEntity.ok(ataque);
    }
    
    @PostMapping
    public ResponseEntity<AtaqueDTO> crearAtaque(@Valid @RequestBody AtaqueDTO ataqueDTO) {
        AtaqueDTO nuevoAtaque = ataqueService.guardarAtaque(ataqueDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoAtaque.getId())
                .toUri();
        return ResponseEntity.created(location).body(nuevoAtaque);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AtaqueDTO> actualizarAtaque(
            @PathVariable Integer id, 
            @Valid @RequestBody AtaqueDTO ataqueDTO) {
        AtaqueDTO ataqueActualizado = ataqueService.actualizarAtaque(id, ataqueDTO);
        return ResponseEntity.ok(ataqueActualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtaque(@PathVariable Integer id) {
        ataqueService.eliminarAtaque(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/amenaza/{amenazaId}")
    public ResponseEntity<List<AtaqueDTO>> listarPorAmenaza(@PathVariable Integer amenazaId) {
        List<AtaqueDTO> ataques = ataqueService.buscarPorAmenazaId(amenazaId);
        return ResponseEntity.ok(ataques);
    }
    
    @GetMapping("/severidad/{severidad}")
    public ResponseEntity<List<AtaqueDTO>> listarPorSeveridad(
            @PathVariable Severidad severidad) {
        List<AtaqueDTO> ataques = ataqueService.buscarPorSeveridad(severidad);
        return ResponseEntity.ok(ataques);
    }
}