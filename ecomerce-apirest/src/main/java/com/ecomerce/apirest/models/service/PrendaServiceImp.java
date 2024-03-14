package com.ecomerce.apirest.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecomerce.apirest.DTOS.IngresarStockDTO;
import com.ecomerce.apirest.models.dao.IPrendaTallasDao;
import com.ecomerce.apirest.models.dao.IPrendasDao;
import com.ecomerce.apirest.models.persistence.Prenda;
import com.ecomerce.apirest.models.persistence.PrendaTallas;
import com.ecomerce.apirest.models.persistence.Tallas;

@Service
public class PrendaServiceImp implements IPrendaService{

	@Autowired
	IPrendasDao prendaDao;
	
	@Autowired
	IPrendaTallasDao prendaTallasDao;

	@Override
	@Transactional(readOnly = true)
	public List<Prenda> findByAllByStatus(Pageable pageable) {
		return (List<Prenda>)prendaDao.findByAllByStatus(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<PrendaTallas> findByAllByStatusTallas(int id) {
		List<PrendaTallas> tallas = prendaTallasDao.findAllTallasByIdPrenda(id);
		if (tallas.isEmpty()) {
		    return null;
		} else {
		}
		return tallas;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Prenda findPrendaByStatusById(int id) {
		return prendaDao.findAllTallasByIdPrenda(id).orElse(null);
	}

	@Override
	@Transactional
	public Prenda save(Prenda prenda,List<PrendaTallas> tallas) {
		Prenda savedPrenda=prendaDao.save(prenda);
		for (PrendaTallas talla : tallas) {
            talla.setPrenda(savedPrenda);
            prendaTallasDao.save(talla);
        }
		return savedPrenda;
	}
	
	@Override
	@Transactional
	public void deleteStatusById(int id) {
		prendaDao.deleteStatusById(id);
		prendaTallasDao.deleteStatusById(id);
	}

	@Override
	public Prenda update(Prenda prenda) {
		Prenda traerPrenda=prendaDao.findAllTallasByIdPrenda(prenda.getId()).orElse(null);
		if(traerPrenda!=null) {
			traerPrenda.setNombre(prenda.getNombre());
			traerPrenda.setDescripcion(prenda.getDescripcion());
			traerPrenda.setPrecio(prenda.getPrecio());
		}
		return prendaDao.save(traerPrenda);
	}

	@Override
	public Prenda enterStock(List<IngresarStockDTO> nuevoStockDTO,int idPrendas) {
		//List<PrendaTallas>nuevoStocks=new ArrayList<>();
		int nuevoStockIngresando=0;
		for(IngresarStockDTO stockDTO:nuevoStockDTO) {
			PrendaTallas stock=new PrendaTallas();
			Prenda idPrenda=new Prenda();
			idPrenda.setId(idPrendas);
			Tallas idTalla=new Tallas();
			idTalla.setId(stockDTO.getIdTallas());
			stock.setPrenda(idPrenda);
			stock.setTallas(idTalla);
			stock.setCantidad(stockDTO.getCantidad());
			stock.setAccion(stockDTO.getAccion());
			prendaTallasDao.save(stock);
			nuevoStockIngresando=nuevoStockIngresando+stock.getCantidad();
			//nuevoStocks.add(stock);
		}
		Prenda traerPrenda=prendaDao.findAllTallasByIdPrenda(idPrendas).orElse(null);
		int stock=traerPrenda.getStock();
		int nuevoStock=stock+nuevoStockIngresando;
		traerPrenda.setStock(nuevoStock);
		prendaDao.save(traerPrenda);
		
		return traerPrenda;
	}
	
}
