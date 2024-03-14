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
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue
    (strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_pedido_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "detalle_pedido_prenda_id")
    private Prenda prenda;

    @Column(name = "detalle_pedido_cantidad")
    private int cantidad;

    @Column(name = "detalle_pedido_importe")
    private double importe;

    @ManyToOne
    @JoinColumn(name = "detalle_pedido_pedido_id")
    private Pedido pedido;

    
    /*public Object[] getPrendas() {
    	Object[] lista=new Object[2];
    	Prenda p=prenda;
    	lista[0]="Nombre prenda: "+p.getNombre();
    	lista[1]="Precio prenda: "+p.getPrecio();
    	return lista;
    }*/
    
    /*public void calcularImporte() {
        importe = cantidad * prenda.getPrecio();
    }*/
    /*
    public void setImporte(double importe) {
    	importe=cantidad*prenda.getPrecio();
        this.importe=importe;
    }*/
    // Constructor, getters y setters
}