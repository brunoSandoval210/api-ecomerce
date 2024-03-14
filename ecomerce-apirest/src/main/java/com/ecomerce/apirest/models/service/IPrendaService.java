package com.ecomerce.apirest.models.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.ecomerce.apirest.DTOS.IngresarStockDTO;
import com.ecomerce.apirest.models.persistence.Prenda;
import com.ecomerce.apirest.models.persistence.PrendaTallas;

public interface IPrendaService {

	public List<Prenda> findByAllByStatus(Pageable pageable);
	List<PrendaTallas> findByAllByStatusTallas(int id);
	public Prenda findPrendaByStatusById(int id);
	public Prenda save(Prenda prenda,List<PrendaTallas> tallas);
	public Prenda update(Prenda prenda);
	public void deleteStatusById(int id);
	public Prenda enterStock(List<IngresarStockDTO> nuevoStock,int idPrendas);

}
