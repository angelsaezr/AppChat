package umu.tds.appchat.controlador;

import java.awt.EventQueue;
import java.time.LocalDate;

import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.RepositorioUsuarios;
import umu.tds.appchat.dominio.TipoMensaje;
import umu.tds.appchat.vista.VentanaLogin;

public class CargarAppChat {
	
	/**
	 * Clase para lanzar la aplicacion.
	 * 
	 * @author Ángel
	 * @author Francisco Javier
	 */
	public static void main(String[] args) {
		AppChat appChat = AppChat.INSTANCE;
		appChat.registrarUsuario("aa", "11", "aa", LocalDate.of(1960, 10, 03),"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/1200px-Default_pfp.svg.png", "Hola, soy jesus", "email");
		appChat.registrarUsuario("bb", "22", "bb", LocalDate.of(1995, 12, 28), "", "hola, soy elena", "email");
		appChat.registrarUsuario("cc", "33", "cc", LocalDate.of(2000, 5, 15), "/usuarios/rosalia.jpg", "hola, soy rosalia", "email");
		appChat.registrarUsuario("dd", "44", "dd", LocalDate.of(1970, 5, 11), "/usuarios/foto-diego.png", "hola, soy diego", "email");
		appChat.registrarUsuario("ee", "55", "ee", LocalDate.of(1990, 3, 28), "/usuarios/annetaylor.jpg", "hola, soy anne", "email");
		
		appChat.login("11", "aa");
		
		ContactoIndividual c2 = appChat.agregarContacto("elena", "22");
		ContactoIndividual c3 = appChat.agregarContacto("rosalia", "33");
		
		appChat.enviarMensajeContacto(c2, "Hola, ¿cómo estás?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 2, TipoMensaje.ENVIADO);
		
		appChat.enviarMensajeContacto(c3, "Cuando cantas?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 6, TipoMensaje.ENVIADO);
		
		appChat.login("22", "bb");
		
		//ContactoIndividual c1 =appChat.agregarContacto("jesus", "11");
		ContactoIndividual c1 = RepositorioUsuarios.INSTANCE.buscarUsuarioPorMovil("22").getContactoIndividual("11");
		ContactoIndividual c4 = appChat.agregarContacto("diego", "44");
		//ContactoIndividual c5 = appChat.agregarContacto("anne", "55");
		
		appChat.enviarMensajeContacto(c1, "Vienes este finde?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c1, "", 3, TipoMensaje.ENVIADO);
	    appChat.enviarMensajeContacto(c4, "Juegas esta semana?", -1, TipoMensaje.ENVIADO);	
	    
	    System.out.println("Fin de la carga de datos");
	    
	    EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaLogin frame = new VentanaLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
	}

}
