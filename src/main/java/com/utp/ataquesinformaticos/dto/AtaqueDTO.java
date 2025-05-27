package com.utp.ataquesinformaticos.dto;

import com.utp.ataquesinformaticos.model.*;
import java.time.LocalDate;

public class AtaqueDTO {

    private Integer id;
    private String tipo;
    private String vector;
    private String sistemaAfectado;
    private Severidad severidad;
    private LocalDate fechaEvento;
    private Integer amenazaId;
    private AmenazaDTO amenaza;

    // Constructores, getters y setters
    public AtaqueDTO() {
    }

    public AtaqueDTO(String tipo, String vector, String sistemaAfectado, Severidad severidad,
            LocalDate fechaEvento, Integer amenazaId) {
        this.tipo = tipo;
        this.vector = vector;
        this.sistemaAfectado = sistemaAfectado;
        this.severidad = severidad;
        this.fechaEvento = fechaEvento;
        this.amenazaId = amenazaId;
    }

// Modificar el método fromEntity en AtaqueDTO para incluir la información de la amenaza completa
    public static AtaqueDTO fromEntity(Ataque ataque) {
        AtaqueDTO dto = new AtaqueDTO();
        dto.setId(ataque.getId());
        dto.setTipo(ataque.getTipo());
        dto.setVector(ataque.getVector());
        dto.setSistemaAfectado(ataque.getSistemaAfectado());
        dto.setSeveridad(ataque.getSeveridad());
        dto.setFechaEvento(ataque.getFechaEvento());

        if (ataque.getAmenaza() != null) {
            dto.setAmenazaId(ataque.getAmenaza().getId());
            dto.setAmenaza(AmenazaDTO.fromEntity(ataque.getAmenaza()));
        }

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

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getSistemaAfectado() {
        return sistemaAfectado;
    }

    public void setSistemaAfectado(String sistemaAfectado) {
        this.sistemaAfectado = sistemaAfectado;
    }

    public Severidad getSeveridad() {
        return severidad;
    }

    public void setSeveridad(Severidad severidad) {
        this.severidad = severidad;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Integer getAmenazaId() {
        return amenazaId;
    }

    public void setAmenazaId(Integer amenazaId) {
        this.amenazaId = amenazaId;
    }

    public AmenazaDTO getAmenaza() {
        return amenaza;
    }

    public void setAmenaza(AmenazaDTO amenaza) {
        this.amenaza = amenaza;
    }

}
