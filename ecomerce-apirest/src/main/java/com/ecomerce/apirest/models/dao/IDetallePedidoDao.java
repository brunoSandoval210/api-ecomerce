package com.ecomerce.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecomerce.apirest.models.persistence.DetallePedido;

public interface IDetallePedidoDao extends JpaRepository<DetallePedido, Integer>{
	
	@Query(value = "Select * from detalle_pedido "
			+ "WHERE status = 1 "
			+ "AND detalle_pedido_pedido_id = :id", nativeQuery = true)
	List<DetallePedido> findbyAllStatusByPedidoId(int id);

}
