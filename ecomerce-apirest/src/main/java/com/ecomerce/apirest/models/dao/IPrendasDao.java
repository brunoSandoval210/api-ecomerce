package com.ecomerce.apirest.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecomerce.apirest.models.persistence.Prenda;

public interface IPrendasDao extends JpaRepository<Prenda, Integer>{
	
	@Query(value = "SELECT * FROM prenda "
			+ "WHERE status = 1 ", nativeQuery = true)
	List<Prenda> findByAllByStatus(Pageable pageable);
	
	@Query(value = "SELECT * FROM prenda WHERE status = 1 AND prenda_id = :id", nativeQuery = true)
	Optional<Prenda> findAllTallasByIdPrenda(int id);
	
	@Modifying
	@Query(value = "UPDATE prenda "
			+ "SET "
			+ "status = 0 "
			+ "WHERE prenda_id = :id", nativeQuery = true)
	public void deleteStatusById(int id);
}
