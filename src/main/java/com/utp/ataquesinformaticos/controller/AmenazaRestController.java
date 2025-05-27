package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.dto.*;
import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.service.*;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/amenazas")
@CrossOrigin(origins = "*") // Agregar CORS
public class AmenazaRestController {
    
    private final AmenazaService amenazaService;
    
    @Autowired
    public AmenazaRestController(AmenazaService amenazaService) {
        this.amenazaService = amenazaService;
    }
    
    @GetMapping
    public ResponseEntity<List<AmenazaDTO>> listarAmenazas() {
        try {
            List<AmenazaDTO> amenazas = amenazaService.listarTodasLasAmenazas();
            System.out.println("✅ Listando amenazas: " + amenazas.size() + " encontradas");
            return ResponseEntity.ok(amenazas);
        } catch (Exception e) {
            System.err.println("❌ Error al listar amenazas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AmenazaDTO> obtenerAmenaza(@PathVariable Integer id) {
        try {
            AmenazaDTO amenaza = amenazaService.buscarPorId(id);
            if (amenaza == null) {
                System.out.println("⚠️ Amenaza no encontrada con ID: " + id);
                return ResponseEntity.notFound().build();
            }
            System.out.println("✅ Amenaza encontrada: " + amenaza);
            return ResponseEntity.ok(amenaza);
        } catch (Exception e) {
            System.err.println("❌ Error al buscar amenaza por ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crearAmenaza(@RequestBody AmenazaDTO amenazaDTO) {
        try {
            System.out.println("📝 Intentando crear amenaza: " + amenazaDTO);
            
            // Validaciones básicas
            if (amenazaDTO.getTipo() == null || amenazaDTO.getTipo().trim().isEmpty()) {
                System.err.println("❌ Error: Tipo de amenaza es requerido");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El tipo de amenaza es requerido"));
            }
            
            if (amenazaDTO.getDescripcion() == null || amenazaDTO.getDescripcion().trim().isEmpty()) {
                System.err.println("❌ Error: Descripción es requerida");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La descripción es requerida"));
            }
            
            // Asegurar que el ID sea null para creación
            amenazaDTO.setId(null);
            
            AmenazaDTO nuevaAmenaza = amenazaService.guardarAmenaza(amenazaDTO);
            System.out.println("✅ Amenaza creada exitosamente: " + nuevaAmenaza);
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(nuevaAmenaza.getId())
                    .toUri();
            
            return ResponseEntity.created(location).body(nuevaAmenaza);
            
        } catch (Exception e) {
            System.err.println("❌ Error al crear amenaza: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAmenaza(
            @PathVariable Integer id, 
            @RequestBody AmenazaDTO amenazaDTO) {
        try {
            System.out.println("🔄 Intentando actualizar amenaza ID: " + id + " con datos: " + amenazaDTO);
            
            // Validaciones básicas
            if (amenazaDTO.getTipo() == null || amenazaDTO.getTipo().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El tipo de amenaza es requerido"));
            }
            
            if (amenazaDTO.getDescripcion() == null || amenazaDTO.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La descripción es requerida"));
            }
            
            // Verificar que la amenaza existe
            AmenazaDTO amenazaExistente = amenazaService.buscarPorId(id);
            if (amenazaExistente == null) {
                System.out.println("⚠️ Amenaza no encontrada para actualizar con ID: " + id);
                return ResponseEntity.notFound().build();
            }
            
            // Asegurar que el ID del DTO coincida con el path parameter
            amenazaDTO.setId(id);
            
            AmenazaDTO amenazaActualizada = amenazaService.actualizarAmenaza(id, amenazaDTO);
            System.out.println("✅ Amenaza actualizada exitosamente: " + amenazaActualizada);
            
            return ResponseEntity.ok(amenazaActualizada);
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar amenaza: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAmenaza(@PathVariable Integer id) {
        try {
            System.out.println("🗑️ Intentando eliminar amenaza ID: " + id);
            
            // Verificar que la amenaza existe antes de eliminar
            AmenazaDTO amenazaExistente = amenazaService.buscarPorId(id);
            if (amenazaExistente == null) {
                System.out.println("⚠️ Amenaza no encontrada para eliminar con ID: " + id);
                return ResponseEntity.notFound().build();
            }
            
            amenazaService.eliminarAmenaza(id);
            System.out.println("✅ Amenaza eliminada exitosamente ID: " + id);
            
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar amenaza: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    @GetMapping("/nivel/{nivelRiesgo}")
    public ResponseEntity<List<AmenazaDTO>> listarPorNivelRiesgo(
            @PathVariable String nivelRiesgo) { // Cambié a String para mayor flexibilidad
        try {
            System.out.println("🔍 Buscando amenazas por nivel: " + nivelRiesgo);
            
            // Convertir string a enum si es necesario
            NivelRiesgo nivel;
            try {
                nivel = NivelRiesgo.valueOf(nivelRiesgo.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Nivel de riesgo inválido: " + nivelRiesgo);
                return ResponseEntity.badRequest().build();
            }
            
            List<AmenazaDTO> amenazas = amenazaService.buscarPorNivelRiesgo(nivel);
            System.out.println("✅ Encontradas " + amenazas.size() + " amenazas de nivel " + nivelRiesgo);
            
            return ResponseEntity.ok(amenazas);
            
        } catch (Exception e) {
            System.err.println("❌ Error al buscar por nivel de riesgo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Endpoint adicional para debug - puedes eliminarlo después
    @GetMapping("/debug/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Controller funcionando correctamente");
        response.put("timestamp", new Date());
        response.put("service", amenazaService != null ? "Service inyectado" : "Service NULL");
        
        System.out.println("🔧 Test endpoint ejecutado: " + response);
        return ResponseEntity.ok(response);
    }
}
