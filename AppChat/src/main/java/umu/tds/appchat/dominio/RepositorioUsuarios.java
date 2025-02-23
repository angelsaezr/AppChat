package umu.tds.appchat.dominio;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase RepositorioUsuarios.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class RepositorioUsuarios {
	public static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();
    private Map<String, Usuario> usuarios; // Mapa con clave = móvil, valor = usuario

    private RepositorioUsuarios() {
        this.usuarios = new HashMap<>();
    }

    // Método para agregar un usuario
    public boolean addUsuario(Usuario usuario) {
        if (usuario == null || usuarios.containsKey(usuario.getMovil())) {
            return false; // No se permite añadir usuarios duplicados por número de móvil
        }
        usuarios.put(usuario.getMovil(), usuario);
        return true;
    }

    // Método para eliminar un usuario
    public boolean removeUsuario(String movil) {
        if (movil == null || !usuarios.containsKey(movil)) {
            return false;
        }
        usuarios.remove(movil);
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
