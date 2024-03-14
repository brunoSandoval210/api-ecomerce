package com.ecomerce.apirest.models.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ecomerce.apirest.models.persistence.PrendaTallas;

public interface IPrendaTallasDao extends JpaRepository<PrendaTallas, Integer>{

	@Query(value = "SELECT * FROM prenda_tallas "
			+ "WHERE status = 1 ", nativeQuery = true)
	List<PrendaTallas> findByAllStatus(Pageable pageable);
	
	@Query(value = "SELECT * FROM prenda_tallas WHERE status = 1 AND prenda_tallas_prenda_id = :id", nativeQuery = true)
	List<PrendaTallas> findAllTallasByIdPrenda(int id);

	
	@Modifying
	@Query(value = "UPDATE prenda_tallas "
			+ "SET "
			+ "status = 0 "
			+ "WHERE prenda_tallas_prenda_id = :id", nativeQuery = true)
	public void deleteStatusById(int id);
}
