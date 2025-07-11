package umu.tds.appchat.dominio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import umu.tds.appchat.persistencia.DAOException;
import umu.tds.appchat.persistencia.FactoriaDAO;

/**
 * Repositorio que gestiona la colección de usuarios en la aplicación.
 * Implementa el patrón Singleton para asegurar una única instancia.
 * Carga usuarios desde la base de datos mediante DAO en su construcción.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class RepositorioUsuarios {

    /**
     * Instancia pública accesible del repositorio (Singleton).
     */
    public static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

    /**
     * Instancia única utilizada por el método {@code getUnicaInstancia()}.
     */
    private static RepositorioUsuarios unicaInstancia = null;

    /**
     * Mapa de usuarios indexado por número de móvil.
     */
    private Map<String, Usuario> usuarios; // Mapa con clave = móvil, valor = usuario

    /**
     * Constructor privado que inicializa el repositorio y carga los usuarios desde la base de datos.
     */
    private RepositorioUsuarios() {
        this.usuarios = new HashMap<>();

        try {
            FactoriaDAO.getUnicaInstancia().getUsuarioDAO().recuperarTodosLosUsuarios().stream()
                .forEach(u -> {
                    usuarios.put(u.getMovil(), u);
                });
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la única instancia del repositorio (patrón Singleton).
     *
     * @return instancia única de RepositorioUsuarios
     */
    public static RepositorioUsuarios getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new RepositorioUsuarios();
        return unicaInstancia;
    }

    /**
     * Devuelve una lista con todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    public LinkedList<Usuario> getUsuarios() {
        return new LinkedList<Usuario>(usuarios.values());
    }

    /**
     * Agrega un nuevo usuario al repositorio.
     *
     * @param nombre nombre del usuario
     * @param movil número de móvil del usuario
     * @param contraseña contraseña del usuario
     * @param fechaNacimiento fecha de nacimiento del usuario
     * @param imagen ruta de la imagen de perfil
     * @param saludo saludo personalizado del usuario
     * @param email correo electrónico del usuario
     * @return nuevoUsuario si no existe un usuario con ese móvil, null si ya existe un usuario con ese móvil
     */
    public Usuario addUsuario(String nombre, String movil, String contraseña, LocalDate fechaNacimiento, String imagen, String saludo, String email) {
    	if (this.buscarUsuarioPorMovil(movil) != null) {
            return null; // Ya existe un usuario con ese número
        }
        Usuario nuevoUsuario = new Usuario(nombre, movil, contraseña, imagen, saludo, email, fechaNacimiento);
        usuarios.put(movil, nuevoUsuario);
        return nuevoUsuario;
    }

    /**
     * Elimina un usuario del repositorio.
     *
     * @param usuario el usuario a eliminar
     * @return true si fue eliminado correctamente, false si no existía
     */
    public boolean removeUsuario(Usuario usuario) {
        if (usuario.getMovil() == null || !usuarios.containsKey(usuario.getMovil())) {
            return false;
        }
        usuarios.remove(usuario.getMovil());
        return true;
    }

    /**
     * Busca un usuario en el repositorio por número de móvil.
     *
     * @param movil número de móvil del usuario
     * @return el usuario encontrado, o null si no existe
     */
    public Usuario buscarUsuarioPorMovil(String movil) {
        return usuarios.get(movil);
    }

    /**
     * Verifica si un usuario está registrado en el sistema.
     *
     * @param movil número de móvil del usuario
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existeUsuario(String movil) {
        return usuarios.containsKey(movil);
    }
}