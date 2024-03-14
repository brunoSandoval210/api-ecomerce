package com.ecomerce.apirest.models.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecomerce.apirest.DTOS.RegistrarUsuarioDTO;
import com.ecomerce.apirest.DTOS.UsuarioDTO;
import com.ecomerce.apirest.models.dao.IDetallePedidoDao;
import com.ecomerce.apirest.models.dao.IDireccionDao;
import com.ecomerce.apirest.models.dao.IPedidoDao;
import com.ecomerce.apirest.models.dao.IUsuarioDao;
import com.ecomerce.apirest.models.persistence.DetallePedido;
import com.ecomerce.apirest.models.persistence.Direccion;
import com.ecomerce.apirest.models.persistence.Pedido;
import com.ecomerce.apirest.models.persistence.TipoDocumento;
import com.ecomerce.apirest.models.persistence.TipoUsuario;
import com.ecomerce.apirest.models.persistence.Usuario;

@Service
public class UsuarioServiceImp implements IUsuarioService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IDireccionDao direccionDao;
	
	@Autowired
	private IPedidoDao pedidoDao;
	
	@Autowired
	private IDetallePedidoDao detalleDao;
	

	@Override
	@Transactional(readOnly = true)
	public List<UsuarioDTO> findAllUsuarioByStatus(Pageable pageable){
		List<UsuarioDTO> usuariosDTO=new ArrayList<UsuarioDTO>();
		List<Usuario> usuarios=usuarioDao.findAllUsuarioByStatus(pageable);
		for(Usuario usuario : usuarios ) {
			UsuarioDTO usuarioDTO = convertirUsuarioToDTO(usuario);
		    usuariosDTO.add(usuarioDTO);
		}
			
		return usuariosDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO findUsuarioByStatusById(int id) {
		Usuario usuario=usuarioDao.findUsuarioByStatusById(id).orElse(null);
		UsuarioDTO usuarioDTO=convertirUsuarioToDTO(usuario);		
		return usuarioDTO;
	}
	
	
	@Override
	@Transactional
	public Usuario save(RegistrarUsuarioDTO usuarioDTO) {
		Usuario usuario=convertirUsuarioDTOToUsuario(usuarioDTO, null);	
		List<Direccion> direcciones=usuario.getDirecciones();
		if (direcciones != null) {
			for(Direccion direccion:direcciones) {
				direccionDao.save(direccion);
			}
		}
		return usuarioDao.save(usuario);
	}
	
	@Override
	@Transactional
	public void deleteStatusById(int id) {
		usuarioDao.deleteStatusById(id);
	}
	
	@Override
	@Transactional
	public Usuario update(Usuario usuario) {
	Usuario traerUsuario=usuarioDao.findUsuarioByStatusById(usuario.getId()).orElse(null);
	if(traerUsuario!=null) {
		// Actualizar los datos del usuario
		traerUsuario.setNombre(usuario.getNombre());
		traerUsuario.setApellido(usuario.getApellido());
		traerUsuario.setTelefono(usuario.getTelefono());
		traerUsuario.setEmail(usuario.getEmail());
		traerUsuario.setPassword(usuario.getPassword());
		traerUsuario.setDocumento(usuario.getDocumento());
		traerUsuario.setTipoDocumento(usuario.getTipoDocumento());
		traerUsuario.setTipoUsuario(usuario.getTipoUsuario());	
		
		// Obtener las direcciones nuevas proporcionadas por el usuario
		List<Direccion> direccionesNuevas = usuario.getDirecciones();

		// Obtener las direcciones existentes del usuario
		List<Direccion> listaDireccionesUsuario = direccionDao.findbyAllStatusByUsuarioId(traerUsuario.getId());

		// Eliminar las direcciones existentes que no estén en las nuevas
		for (Direccion direccionExistente : listaDireccionesUsuario) {
		    if (!direccionesNuevas.contains(direccionExistente)) {
		        // Eliminar la dirección existente que no está en las nuevas
		        direccionDao.deleteByIdStatus(direccionExistente.getId());
		    }
		}

		// Actualizar o agregar las nuevas direcciones
		for (Direccion direccionNueva : direccionesNuevas) {
		    if (listaDireccionesUsuario.contains(direccionNueva)) {
		        // La dirección existe, actualizarla
		        Direccion direccionExistente = listaDireccionesUsuario.get(listaDireccionesUsuario.indexOf(direccionNueva));
		        // Actualizar los datos de la dirección existente con los datos de la dirección nueva
		        direccionExistente.setDepartamento(direccionNueva.getDepartamento());
		        direccionExistente.setProvincia(direccionNueva.getProvincia());
		        direccionExistente.setDistrito(direccionNueva.getDistrito());
		        direccionExistente.setDescripcion(direccionNueva.getDescripcion());
		        // Continuar actualizando otros campos de dirección según sea necesario
		        direccionDao.save(direccionExistente);
		    } else {
		        // La dirección no existe, agregarla
		        direccionNueva.setUsuario(traerUsuario);
		        direccionDao.save(direccionNueva);
		    }
		}
		return usuarioDao.save(traerUsuario);
	}
	
	return null;	
		
	}
	
	public UsuarioDTO convertirUsuarioToDTO(Usuario usuario) {
	    UsuarioDTO usuarioDTO = new UsuarioDTO();
	    usuarioDTO.setId(usuario.getId());
	    usuarioDTO.setNombre(usuario.getNombre());
	    usuarioDTO.setApellido(usuario.getApellido());
	    usuarioDTO.setTelefono(usuario.getTelefono());
	    usuarioDTO.setEmail(usuario.getEmail());
	    usuarioDTO.setPassword(usuario.getPassword());
	    usuarioDTO.setDocumento(usuario.getDocumento());
	    usuarioDTO.setTipoDocumento(usuario.getTipoDocumento().getNombre());
	    usuarioDTO.setTipoUsuario(usuario.getTipoUsuario().getNombre());

	    // Obtener direcciones del usuario
	    List<Direccion> direcciones = direccionDao.findbyAllStatusByUsuarioId(usuario.getId());
	    List<UsuarioDTO.DireccionDTO> direccionesDTO = new ArrayList<>();
	    for (Direccion direccion : direcciones) {
	        UsuarioDTO.DireccionDTO direccionDTO = new UsuarioDTO.DireccionDTO();
	        direccionDTO.setDepartamento(direccion.getDepartamento());
	        direccionDTO.setProvincia(direccion.getProvincia());
	        direccionDTO.setDistrito(direccion.getDistrito());
	        direccionDTO.setDescripcion(direccion.getDescripcion());
	        direccionesDTO.add(direccionDTO);
	    }
	    usuarioDTO.setDirecciones(direccionesDTO);

	    // Obtener pedidos del usuario
	    List<Pedido> pedidos = pedidoDao.findbyAllStatusByUsuarioId(usuario.getId());
	    List<UsuarioDTO.PedidoDTO> pedidosDTO = new ArrayList<>();
	    for (Pedido pedido : pedidos) {
	        UsuarioDTO.PedidoDTO pedidoDTO = new UsuarioDTO.PedidoDTO();
	        pedidoDTO.setId(pedido.getId());
	        List<UsuarioDTO.DetallePedidoDTO> detallesDTO = new ArrayList<>();
	        List<DetallePedido> detalles = detalleDao.findbyAllStatusByPedidoId(pedido.getId());
	        for (DetallePedido detalle : detalles) {
	            UsuarioDTO.DetallePedidoDTO detalleDTO = new UsuarioDTO.DetallePedidoDTO();
	            detalleDTO.setPrenda(detalle.getPrenda().getNombre());
	            detalleDTO.setCantidad(detalle.getCantidad());
	            detalleDTO.setImporte(detalle.getImporte());
	            detallesDTO.add(detalleDTO);
	        }
	        pedidoDTO.setDetalles(detallesDTO);
	        pedidoDTO.setTotal(pedido.getTotal());
	        pedidosDTO.add(pedidoDTO);
	    }
	    usuarioDTO.setPedidos(pedidosDTO);

	    return usuarioDTO;
	}
	
	public Usuario convertirUsuarioDTOToUsuario(RegistrarUsuarioDTO usuarioDTO,Usuario usuarioEncontrado) {
		
		Usuario usuario = usuarioEncontrado != null ? usuarioEncontrado : new Usuario();
		Integer id = usuarioEncontrado != null ? usuarioEncontrado.getId() : null;
		if(id==null) {
		
		}else {
			usuario.setId(usuarioDTO.getId());
		}
		
	    usuario.setNombre(usuarioDTO.getNombre());
	    usuario.setApellido(usuarioDTO.getApellido());
	    usuario.setTelefono(usuarioDTO.getTelefono());
	    usuario.setEmail(usuarioDTO.getEmail());
	    usuario.setPassword(usuarioDTO.getPassword());
	    usuario.setDocumento(usuarioDTO.getDocumento());

	    // Establecer el TipoDocumento y TipoUsuario
	    TipoDocumento tipoDocumento = new TipoDocumento();
	    tipoDocumento.setId(usuarioDTO.getTipoDocumento());
	    usuario.setTipoDocumento(tipoDocumento);

	    TipoUsuario tipoUsuario = new TipoUsuario();
	    tipoUsuario.setId(usuarioDTO.getTipoUsuario());
	    usuario.setTipoUsuario(tipoUsuario);

	    // Obtener las direcciones del DTO del usuario
	    List<RegistrarUsuarioDTO.DireccionDTO> direccionesDTO = usuarioDTO.getDirecciones();

	    // Verificar si hay direcciones para evitar errores
	    if (direccionesDTO != null && !direccionesDTO.isEmpty()) {
	        List<Direccion> direcciones = new ArrayList<>();

	        // Iterar sobre las direcciones del DTO y convertirlas a entidades de Direccion
	        for (RegistrarUsuarioDTO.DireccionDTO direccionDTO : direccionesDTO) {
	            Direccion direccion = new Direccion();
	            direccion.setDepartamento(direccionDTO.getDepartamento());
	            direccion.setProvincia(direccionDTO.getProvincia());
	            direccion.setDistrito(direccionDTO.getDistrito());
	            direccion.setDescripcion(direccionDTO.getDescripcion());
	            direccion.setUsuario(usuario);
	            direcciones.add(direccion);
	        }

	         //Establecer las direcciones en el usuario
	        usuario.setDirecciones(direcciones);
	    }

	    return usuario;
	}

}
