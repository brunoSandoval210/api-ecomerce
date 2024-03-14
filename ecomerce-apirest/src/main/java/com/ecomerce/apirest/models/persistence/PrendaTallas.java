package com.ecomerce.apirest.models.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "prenda_tallas")
public class PrendaTallas {

	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prenda_tallas_id")
    private int id;
	
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "prenda_tallas_prenda_id")
    private Prenda prenda;

    @ManyToOne
    @JoinColumn(name = "prenda_tallas_tallas_id")
    private Tallas tallas;

    @Column(name = "prenda_tallas_cantidad")
    private int cantidad;

    @Column(name="prenda_tallas_accion")
    private char accion;
    /*public char getTalla() {
    	return tallas.getDescripcion();
    }*/
    // Constructor, getters y setters
}