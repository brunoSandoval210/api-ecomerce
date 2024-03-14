package com.ecomerce.apirest.models.persistence;




import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private int id;

    @NotNull
    @Size(max = 50)
    @Column(name = "usuario_nombre")
    private String nombre;

    @NotNull
    @Size(max = 50)
    @Column(name = "usuario_apellido")
    private String apellido;

    @NotNull
    @Size(min = 9, max = 9)
    @Column(name = "usuario_telefono")
    private String telefono;

    @NotNull
    @Email(message = "ingrese un email valido")
    @Column(name = "usuario_email")
    private String email;

    @NotNull
    @Column(name = "usuario_password")
    private String password;

    @NotNull
    @Size(min=8,max =12)
    @Column(name = "usuario_documento")
    private String documento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_tipo_documento_id")
    private TipoDocumento tipoDocumento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_tipo_usuario_id")
    private TipoUsuario tipoUsuario;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Direccion> direcciones;
    
    /*
    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos;
    */
}