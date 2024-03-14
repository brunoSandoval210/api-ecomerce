package com.ecomerce.apirest.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.apirest.DTOS.ActualizarPrendaDTO;
import com.ecomerce.apirest.DTOS.IngresarStockDTO;
import com.ecomerce.apirest.DTOS.PrendaDTO;
import com.ecomerce.apirest.DTOS.RegistrarPrendaDTO;
import com.ecomerce.apirest.models.persistence.Prenda;
import com.ecomerce.apirest.models.persistence.PrendaTallas;
import com.ecomerce.apirest.models.persistence.Tallas;
import com.ecomerce.apirest.models.service.IPrendaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

//@CrossOrigin(origins= {"http://localhost:5173"})
@RestController
@RequestMapping("/API")
public class PrendaController {

	@Autowired
	private IPrendaService prendaService;

	@GetMapping("Prendas")
	public ResponseEntity<?> index(@RequestParam(defaultValue = "0") Integer page) {
		List<PrendaDTO> prendasDTO = new ArrayList<PrendaDTO>();
		Pageable pageable = PageRequest.of(page, 10);
		List<Prenda> prendas = prendaService.findByAllByStatus(pageable);

		if (prendas.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "No hay prendas en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		for (Prenda prenda : prendas) {
			PrendaDTO prendaDTO = new PrendaDTO();
			List<PrendaTallas> tallas = prendaService.findByAllByStatusTallas(prenda.getId());
			prendaDTO.setId(prenda.getId());
			prendaDTO.setNombre(prenda.getNombre());
			prendaDTO.setDescripcion(prenda.getDescripcion());
			prendaDTO.setStock(prenda.getStock());
			prendaDTO.setPrecio(prenda.getPrecio());

			List<PrendaDTO.TallaDTO> tallasDTO = new ArrayList<>();

			for (PrendaTallas talla : tallas) {
				PrendaDTO.TallaDTO tallaDTO = new PrendaDTO.TallaDTO();
				tallaDTO.setTalla(talla.getTallas().getDescripcion());
				tallaDTO.setCantidad(talla.getCantidad());
				tallasDTO.add(tallaDTO);
			}

			prendaDTO.setTallas(tallasDTO);
			prendasDTO.add(prendaDTO);
		}

		return new ResponseEntity<List<PrendaDTO>>(prendasDTO, HttpStatus.OK);
	}

	@GetMapping("Prenda")
	public ResponseEntity<?> show(@RequestParam int id) {
		Prenda prenda = null;
		Map<String, Object> response = new HashMap<>();
		try {
			prenda = prendaService.findPrendaByStatusById(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (prenda == null) {
			response.put("mensaje", "La prenda con ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		PrendaDTO prendaDto = new PrendaDTO();
		prendaDto.setNombre(prenda.getNombre());
		prendaDto.setDescripcion(prenda.getDescripcion());
		prendaDto.setPrecio(prenda.getPrecio());
		prendaDto.setStock(prenda.getStock());
		List<PrendaTallas> tallas = prendaService.findByAllByStatusTallas(prenda.getId());
		List<PrendaDTO.TallaDTO> tallasDTO = new ArrayList<>();

		for (PrendaTallas talla : tallas) {
			PrendaDTO.TallaDTO tallaDTO = new PrendaDTO.TallaDTO();
			tallaDTO.setTalla(talla.getTallas().getDescripcion());
			tallaDTO.setCantidad(talla.getCantidad());
			tallasDTO.add(tallaDTO);
		}

		prendaDto.setTallas(tallasDTO);

		return new ResponseEntity<PrendaDTO>(prendaDto, HttpStatus.OK);
	}

	@PostMapping("Prenda")
	public ResponseEntity<?> create(@Valid @RequestBody RegistrarPrendaDTO prendaRequest, BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			Prenda nuevaPrenda = new Prenda();
			nuevaPrenda.setNombre(prendaRequest.getPrenda().getNombre());
			nuevaPrenda.setDescripcion(prendaRequest.getPrenda().getDescripcion());
			nuevaPrenda.setPrecio(prendaRequest.getPrenda().getPrecio());
			nuevaPrenda.setStock(prendaRequest.Stock());

			List<PrendaTallas> tallas = new ArrayList<>();

			List<RegistrarPrendaDTO.TallaDto> tallasDto = prendaRequest.getTallas();

			for (RegistrarPrendaDTO.TallaDto tallaDTO : tallasDto) {
				PrendaTallas prendaTallas = new PrendaTallas();
				prendaTallas.setCantidad(tallaDTO.getCantidad());
				Tallas talla = new Tallas();
				talla.setId(tallaDTO.getTalla());
				prendaTallas.setTallas(talla);
				prendaTallas.setAccion(tallaDTO.getAccion());

				tallas.add(prendaTallas);
			}

			Prenda prendaCreada = prendaService.save(nuevaPrenda, tallas);

			response.put("mensaje", "La prenda se creó con éxito");
			response.put("Prenda id", prendaCreada.getId());
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			if (e.getCause() instanceof ConstraintViolationException) {
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("Prenda")
	public ResponseEntity<?> update(@Valid @RequestBody ActualizarPrendaDTO prenda, BindingResult result,
			@RequestParam int id) {

		Prenda prendaActualizar = new Prenda();

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Prenda prendaActualizado = new Prenda();
		try {
			prendaActualizar.setId(id);
			prendaActualizar.setNombre(prenda.getNombre());
			prendaActualizar.setDescripcion(prenda.getDescripcion());
			prendaActualizar.setPrecio(prenda.getPrecio());
			prendaActualizado=prendaService.update(prendaActualizar);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la prenda en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Verifica si la prenda se actualizó correctamente antes de usarlo
		if (prendaActualizado != null) {
			response.put("mensaje", "La prenda se actualizó con éxito");
			response.put("usuario", prendaActualizado.getId());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		} else {
			response.put("mensaje", "No se encontró la prenda a actualizar");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("Prenda")
	public ResponseEntity<String> delete(@RequestParam int id){
		Prenda prenda=prendaService.findPrendaByStatusById(id);
		try {
			if(prenda==null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La prenda con el Id "+ id +" no existe");
			}
			prendaService.deleteStatusById(id);
			return ResponseEntity.ok("La prenda se elimino correctamente");
		}
		catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al eliminar la prenda con ID "+id);
		}
	}
	
	@PostMapping("Prenda/Stock")
	public ResponseEntity<?> enterStock(@Valid @RequestBody List<IngresarStockDTO> stock, BindingResult result,
			@RequestParam int id) {

		//Prenda prendaActualizar = new Prenda();

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Prenda prendaActualizado = new Prenda();
		try {
			prendaActualizado=prendaService.enterStock(stock,id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el stock de la prenda en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Verifica si la prenda se actualizó correctamente antes de usarlo
		if (prendaActualizado != null) {
			response.put("mensaje", "La prenda se actualizó con éxito");
			response.put("usuario", prendaActualizado.getId());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		} else {
			response.put("mensaje", "No se encontró la prenda a actualizar");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
	}
	
}
