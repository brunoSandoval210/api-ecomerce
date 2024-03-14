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
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pedido_usuario_id")
    private Usuario usuario;

    @Column(name = "pedido_total")
    private double total;
    
    /*public double getTotal() {
        for (DetallePedido detalle : detalles) {
            total += detalle.getImporte();
        }
        return total;
    }*/
    
    /*public void setTotal(double total) {
    	for (DetallePedido detalle : detalles) {
            total += detalle.getImporte();
        }
    	this.total=total;
    }*/
    /*public void calcularTotal() {
        double total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.getImporte();
        }
        this.total = total;
    }*/
    
    /*public void agregarLinea(Prenda pre, int can){
        DetallePedido lin=new DetallePedido();
        lin.setPrenda(pre);
        lin.setCantidad(can);
        detalles.add(lin);
    }
    
    public void quitarLinea(int id){
        for(int i=0; i<detalles.size();i++){
        	DetallePedido lin=(DetallePedido)detalles.get(i);
            if(lin.getPrenda().getId()==id){
            	detalles.remove(i);
            }
        }
    }
    
    public void actualizarStockPrendas() {
        for (DetallePedido detalle : detalles) {
            Prenda prenda = detalle.getPrenda();
            int cantidadVendida = detalle.getCantidad();
            prenda.setStock(prenda.getStock() - cantidadVendida);
        }
    }*/
    // Constructor, getters y setters
}