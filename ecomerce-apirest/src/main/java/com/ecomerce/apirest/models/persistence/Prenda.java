package com.ecomerce.apirest.models.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prenda")
public class Prenda {

	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prenda_id")
    private int id;

    @Column(name = "prenda_nombre")
    private String nombre;

    @Column(name = "prenda_descripcion")
    private String descripcion;

    @JsonIgnore
    @Column(name = "prenda_stock")
    private int stock;
    
    @Column(name = "prenda_precio")
    private double precio;

    /*@OneToMany(mappedBy = "prenda")
    private List<PrendaTallas> tallas;*/
    // Constructor, getters y setters
}