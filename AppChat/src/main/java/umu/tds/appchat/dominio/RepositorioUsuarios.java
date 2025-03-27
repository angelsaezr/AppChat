package umu.tds.appchat.dominio;

import java.util.HashMap;
import java.util.Map;

import umu.tds.appchat.persistencia.DAOException;
import umu.tds.appchat.persistencia.FactoriaDAO;

/**
 * Clase RepositorioUsuarios.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class RepositorioUsuarios {
	public static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();
	private static RepositorioUsuarios unicaInstancia = null;
    private Map<String, Usuario> usuarios; // Mapa con clave = móvil, valor = usuario
    private Map<Integer, Usuario> usuariosCod; // Codigos usuarios

    private RepositorioUsuarios() {
        this.usuarios = new HashMap<>();
        this.usuariosCod = new HashMap<>();
        
        try {
			FactoriaDAO.getUnicaInstancia().getUsuarioDAO().recuperarTodosLosUsuarios().stream()
		    	.forEach(u -> {
		    		usuarios.put(u.getMovil(), u);
		    		usuariosCod.put(u.getCodigo(), u);
		    });
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
    }
    
    public static RepositorioUsuarios getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new RepositorioUsuarios();
		return unicaInstancia;
	}

    // Método para agregar un usuario
    public boolean addUsuario(Usuario usuario) {
        if (usuario == null || usuarios.containsKey(usuario.getMovil())) {
            return false; // No se permite añadir usuarios duplicados por número de móvil
        }
        usuarios.put(usuario.getMovil(), usuario);
        usuariosCod.put(usuario.getCodigo(), usuario);
        return true;
    }

    // Método para eliminar un usuario
    public boolean removeUsuario(Usuario usuario) {
        if (usuario.getMovil() == null || !usuarios.containsKey(usuario.getMovil())) {
            return false;
        }
        usuarios.remove(usuario.getMovil());
        usuariosCod.remove(usuario.getCodigo());
        return true;
    }

    // Método para buscar un usuario por móvil
    public Usuario buscarUsuarioPorMovil(String movil) {
        return usuarios.entrySet().stream()
                .filter(entry -> entry.getKey().equals(movil))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    // Método para verificar si un usuario está registrado
    public boolean existeUsuario(String movil) {
        return usuarios.keySet().stream().anyMatch(key -> key.equals(movil));
    }
}
