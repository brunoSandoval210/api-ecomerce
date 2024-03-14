package com.ecomerce.apirest.models.persistence;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direccion_id")
    private int id;

    @Column(name = "direccion_departamento")
    private String departamento;

    @Column(name = "direccion_provincia")
    private String provincia;

    @Column(name = "direccion_distrito")
    private String distrito;

    @Column(name = "direccion_descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "direccion_usuario_id")
    private Usuario usuario;

    // Constructor, getters y setters
}