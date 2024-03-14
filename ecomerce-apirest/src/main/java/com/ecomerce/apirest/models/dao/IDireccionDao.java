package com.ecomerce.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecomerce.apirest.models.persistence.Direccion;

public interface IDireccionDao extends JpaRepository<Direccion, Integer>{

	@Query(value = "Select * from direccion "
			+ "WHERE status = 1 "
			+ "AND direccion_usuario_id = :id",nativeQuery = true)
	List<Direccion>findbyAllStatusByUsuarioId(int id);
	
	@Modifying
	@Query(value = "UPDATE direccion "
			+ "SET "
			+ "status = 0 "
			+ "WHERE direccion_id = :id", nativeQuery = true)
	public void deleteByIdStatus(int id);
	
}
