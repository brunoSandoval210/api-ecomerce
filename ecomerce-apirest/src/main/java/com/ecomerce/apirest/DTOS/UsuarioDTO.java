package com.ecomerce.apirest.DTOS;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

	private int id;
	private String nombre;
	private String apellido;
    private String telefono;
    private String email;
    private String password;
    private String documento;
    private String tipoDocumento;
    private String tipoUsuario;
    private List<DireccionDTO> direcciones;
    private List<PedidoDTO> pedidos;
    
    
    @Getter
    @Setter
    public static class DireccionDTO {
        private String departamento;
        private String provincia;
        private String distrito;
        private String descripcion;
    }
    
    
    @Getter
    @Setter
    public static class PedidoDTO {
    	private int id;
        private List<DetallePedidoDTO> detalles;
        private double total;
    }
    
    
    @Getter
    @Setter
    public static class DetallePedidoDTO {
        private String prenda;
        private int cantidad;
        private double importe;
    }
    
}
