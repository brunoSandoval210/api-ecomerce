package com.ecomerce.apirest.DTOS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrendaDTO {
	@JsonIgnore
	private int id;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;
    private List<TallaDTO> tallas;
    
    public List<TallaDTO> getTallas(){
    	Map<String, Integer> cantidadesPorTalla = new HashMap<>();
    	// Iterar sobre las tallas y actualizar las cantidades en el mapa
        for (TallaDTO tallaDTO : tallas) {
            String talla = tallaDTO.getTalla();
            int cantidad = tallaDTO.getCantidad();

            // Si la talla ya está en el mapa, sumar la cantidad
            if (cantidadesPorTalla.containsKey(talla)) {
                cantidad += cantidadesPorTalla.get(talla);
            }

            // Actualizar la cantidad en el mapa
            cantidadesPorTalla.put(talla, cantidad);
        }
        
        // Limpiar la lista de tallas antes de agregar las tallas consolidadas
        tallas.clear();
     // Iterar sobre el mapa y agregar las tallas consolidadas a la lista
        for (Map.Entry<String, Integer> entry : cantidadesPorTalla.entrySet()) {
            TallaDTO tallaDTO = new TallaDTO();
            tallaDTO.setTalla(entry.getKey());
            tallaDTO.setCantidad(entry.getValue());
            tallas.add(tallaDTO);
        }

        // Devolver la lista de tallas consolidadas
    	return tallas;
    }
    
    
    @Getter
    @Setter
    public static class TallaDTO {
        private String talla;
        private int cantidad;
    }
    

}
