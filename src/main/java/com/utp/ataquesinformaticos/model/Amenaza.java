package com.utp.ataquesinformaticos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "amenazas")
public class Amenaza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivelRiesgo", length = 20)  // Especifica la longitud máxima
    private NivelRiesgo nivelRiesgo;

    private LocalDate fechaDeteccion;
    
    //Una Amenaza esta relacionada a MUCHOS ataques 
    @OneToMany(mappedBy = "amenaza")
    private List<Ataque> ataques;

    //Constructores 
    public Amenaza() {
    }
    

    public Amenaza(Integer id, String tipo, String descripcion, NivelRiesgo nivelRiesgo, LocalDate fechaDeteccion) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nivelRiesgo = nivelRiesgo;
        this.fechaDeteccion = fechaDeteccion;
    }
    
    

    public Amenaza(Integer id, String tipo, String descripcion, NivelRiesgo nivelRiesgo, LocalDate fechaDeteccion, List<Ataque> ataques) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nivelRiesgo = nivelRiesgo;
        this.fechaDeteccion = fechaDeteccion;
        this.ataques = ataques;
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

    public List<Ataque> getAtaques() {
        return ataques;
    }

    public void setAtaques(List<Ataque> ataques) {
        this.ataques = ataques;
    }
    
     // Método auxiliar para agregar un ataque a esta amenaza
    public void addAtaque(Ataque ataque) {
        ataques.add(ataque);
        ataque.setAmenaza(this);
    }

}
