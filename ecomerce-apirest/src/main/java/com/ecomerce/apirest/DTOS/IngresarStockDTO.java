package com.ecomerce.apirest.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngresarStockDTO {
	@JsonIgnore
	private int id;
	//private int idPrenda;
	private int idTallas;
	private int cantidad;
	@JsonIgnore
	private char accion='1';
}
