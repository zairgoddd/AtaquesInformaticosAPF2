package com.utp.ataquesinformaticos.dto;

import com.utp.ataquesinformaticos.model.*;
import java.time.LocalDate;

public class AmenazaDTO {
    private Integer id;
    private String tipo;
    private String descripcion;
    private NivelRiesgo nivelRiesgo;
    private LocalDate fechaDeteccion;
    
    // Constructores, getters y setters
    public AmenazaDTO() {
    }
    
    public AmenazaDTO(String tipo, String descripcion, NivelRiesgo nivelRiesgo, LocalDate fechaDeteccion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nivelRiesgo = nivelRiesgo;
        this.fechaDeteccion = fechaDeteccion;
    }
    
    // Para mapear desde la entidad
    public static AmenazaDTO fromEntity(Amenaza amenaza) {
        AmenazaDTO dto = new AmenazaDTO();
        dto.setId(amenaza.getId());
        dto.setTipo(amenaza.getTipo());
        dto.setDescripcion(amenaza.getDescripcion());
        dto.setNivelRiesgo(amenaza.getNivelRiesgo());
        dto.setFechaDeteccion(amenaza.getFechaDeteccion());
        return dto;
    }
    
    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NivelRiesgo getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(NivelRiesgo nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public LocalDate getFechaDeteccion() {
        return fechaDeteccion;
    }

    public void setFechaDeteccion(LocalDate fechaDeteccion) {
        this.fechaDeteccion = fechaDeteccion;
    }
}