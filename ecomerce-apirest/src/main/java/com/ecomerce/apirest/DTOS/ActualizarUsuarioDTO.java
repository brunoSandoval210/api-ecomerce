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
public class ActualizarUsuarioDTO {
	@JsonIgnore
	private int id;
	private String nombre;
	private String apellido;
    private String telefono;
    private String email;
    private String password;
    private String documento;
    private int tipoDocumento;
    private int tipoUsuario;
    private List<DireccionDTO> direcciones;
    
    
    @Getter
    @Setter
    public static class DireccionDTO {
    	@JsonIgnore
    	private int id;
        private String departamento;
        private String provincia;
        private String distrito;
        private String descripcion;
    }
    
}
