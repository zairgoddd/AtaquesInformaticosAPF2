package com.utp.ataquesinformaticos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ataques")
public class Ataque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tipo;
    private String vector;
    private String sistemaAfectado;

    @Column(name = "severidad", length = 20)  // Especifica la longitud máxima
    @Enumerated(EnumType.STRING)
    private Severidad severidad;

    private LocalDate fechaEvento;
   
    //MUCHOS ATAQUES están relacionados a UNA sola amenaza
    @ManyToOne
    @JoinColumn(name = "amenaza_id")
    private Amenaza amenaza;

    //Constructores 
    public Ataque() {
    }

    public Ataque(Integer id, String tipo, String vector, String sistemaAfectado, Severidad severidad, LocalDate fechaEvento, Amenaza amenaza) {
        this.id = id;
        this.tipo = tipo;
        this.vector = vector;
        this.sistemaAfectado = sistemaAfectado;
        this.severidad = severidad;
        this.fechaEvento = fechaEvento;
        this.amenaza = amenaza;
    }

    //Getters and Setters 
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

    public Amenaza getAmenaza() {
        return amenaza;
    }

    public void setAmenaza(Amenaza amenaza) {
        this.amenaza = amenaza;
    }

}
