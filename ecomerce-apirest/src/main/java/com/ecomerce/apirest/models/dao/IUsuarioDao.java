package com.ecomerce.apirest.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import com.ecomerce.apirest.models.persistence.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Integer>{
	
	@Query(value = "SELECT * FROM usuario WHERE status = 1", nativeQuery = true)
	List<Usuario> findAllUsuarioByStatus(Pageable pageable);
	
	@Query(value = "SELECT * FROM usuario WHERE status = 1 AND usuario_id = :id", nativeQuery = true)
	Optional<Usuario> findUsuarioByStatusById(int id);
	
	@Modifying
	@Query(value = "UPDATE usuario "
			+ "SET "
			+ "status = 0 "
			+ "WHERE usuario_id = :id", nativeQuery = true)
	void deleteStatusById(int id);
}
