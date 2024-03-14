package com.ecomerce.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecomerce.apirest.models.persistence.Pedido;

public interface IPedidoDao extends JpaRepository<Pedido, Integer>{

	@Query(value = "Select * from pedido "
			+ "WHERE status = 1 "
			+ "AND pedido_usuario_id = :id", nativeQuery = true)
	List<Pedido> findbyAllStatusByUsuarioId(int id);

}