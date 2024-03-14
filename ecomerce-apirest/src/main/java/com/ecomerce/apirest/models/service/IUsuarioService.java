package com.ecomerce.apirest.models.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.ecomerce.apirest.DTOS.RegistrarUsuarioDTO;
import com.ecomerce.apirest.DTOS.UsuarioDTO;
import com.ecomerce.apirest.models.persistence.Usuario;


public interface IUsuarioService {
	
	public List<UsuarioDTO> findAllUsuarioByStatus(Pageable pageable);	
	public UsuarioDTO findUsuarioByStatusById(int id);
	public Usuario save(RegistrarUsuarioDTO usuarioDTO);
	public Usuario update(Usuario Usuario);
	public void deleteStatusById(int id);

}
