package com.ecomerce.apirest.DTOS;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPrendaDTO {
	private PrendaDto prenda;
	private List<TallaDto> tallas;
	
	public int Stock() {
		int stock = 0;
		if (tallas != null) {
			for (TallaDto talla : tallas) {
				stock += talla.getCantidad();
			}
		}
		return stock;
	}

	@Getter
	@Setter
	public static class PrendaDto {
		@JsonIgnore
		private int id;
		private String nombre;
		private String descripcion;
		private double precio;

	}

	@Getter
	@Setter
	public static class TallaDto {
		private int talla;
		private int cantidad;
		private char accion='1';
	}
}
