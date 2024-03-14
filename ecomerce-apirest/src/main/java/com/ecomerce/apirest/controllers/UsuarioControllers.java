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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.apirest.DTOS.ActualizarUsuarioDTO;
import com.ecomerce.apirest.DTOS.RegistrarUsuarioDTO;
import com.ecomerce.apirest.DTOS.UsuarioDTO;
import com.ecomerce.apirest.models.persistence.Direccion;
import com.ecomerce.apirest.models.persistence.TipoDocumento;
import com.ecomerce.apirest.models.persistence.TipoUsuario;
import com.ecomerce.apirest.models.persistence.Usuario;
import com.ecomerce.apirest.models.service.IUsuarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

//@CrossOrigin(origins= {"http://localhost:5173"})
@RestController
@RequestMapping("/API")
public class UsuarioControllers {

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("Usuarios")
	public ResponseEntity<?> index(@RequestParam(defaultValue = "0") Integer page) {
		Pageable pageable = PageRequest.of(page, 10);
		List<UsuarioDTO> usuarios = usuarioService.findAllUsuarioByStatus(pageable);

		if (usuarios.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "No hay usuarios en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<UsuarioDTO>>(usuarios, HttpStatus.OK);
	}

	@GetMapping("Usuario")
	public ResponseEntity<?> show(@RequestParam int id) {
		UsuarioDTO usuario = null;
		Map<String, Object> response = new HashMap<>();
		try {
			usuario = usuarioService.findUsuarioByStatusById(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (usuario == null) {
			response.put("mensaje", "El usuario ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UsuarioDTO>(usuario, HttpStatus.OK);
	}

	@PostMapping("Usuario")
	public ResponseEntity<?> create(@Valid @RequestBody RegistrarUsuarioDTO usuario, BindingResult result) {
		Usuario nuevoUsuario = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			nuevoUsuario = usuarioService.save(usuario);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			if (e.getCause() instanceof ConstraintViolationException) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
			}

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El usuario se creó con éxito");
		response.put("Usuario id", nuevoUsuario.getId());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("Usuario")
	public ResponseEntity<?> update(@Valid @RequestBody ActualizarUsuarioDTO usuario, BindingResult result,
			@RequestParam int id) {

		Usuario usuarioActualizar = new Usuario();

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Usuario usuarioActualizado = new Usuario();
		List<Direccion>direccionesActualizada=new ArrayList<Direccion>();
		try {
			usuarioActualizar.setId(id);
			usuarioActualizar.setNombre(usuario.getNombre());
			usuarioActualizar.setApellido(usuario.getApellido());
			usuarioActualizar.setTelefono(usuario.getTelefono());
			usuarioActualizar.setEmail(usuario.getEmail());
			usuarioActualizar.setPassword(usuario.getPassword());
			usuarioActualizar.setDocumento(usuario.getDocumento());
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setId(usuario.getTipoDocumento());
			usuarioActualizar.setTipoDocumento(tipoDocumento);
			TipoUsuario tipoUsuario = new TipoUsuario();
			tipoUsuario.setId(usuario.getTipoUsuario());
			usuarioActualizar.setTipoUsuario(tipoUsuario);
			
			
			List<ActualizarUsuarioDTO.DireccionDTO>listaDireccion=usuario.getDirecciones();
			for(ActualizarUsuarioDTO.DireccionDTO direccion:listaDireccion) {
				Direccion direccionn=new Direccion();
				direccionn.setDepartamento(direccion.getDepartamento());
				direccionn.setProvincia(direccion.getProvincia());
				direccionn.setDistrito(direccion.getDistrito());
				direccionn.setDescripcion(direccion.getDescripcion());
				direccionesActualizada.add(direccionn);
			}
			usuarioActualizar.setDirecciones(direccionesActualizada);
			usuarioActualizado = usuarioService.update(usuarioActualizar);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el usuario en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Verifica si el usuario se actualizó correctamente antes de usarlo
		if (usuarioActualizado != null) {
			response.put("mensaje", "El usuario se actualizó con éxito");
			response.put("usuario", usuarioActualizado.getId());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		} else {
			response.put("mensaje", "No se encontró el usuario a actualizar");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@DeleteMapping("Usuario")
	public ResponseEntity<String> delete(@RequestParam int id) {
		UsuarioDTO usuario = usuarioService.findUsuarioByStatusById(id);
		try {
			if (usuario == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no existe.");
			}
			usuarioService.deleteStatusById(id);
			return ResponseEntity.ok("El usuario se ha eliminado correctamente.");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al eliminar el usuario con ID " + id);
		}
	}

}
