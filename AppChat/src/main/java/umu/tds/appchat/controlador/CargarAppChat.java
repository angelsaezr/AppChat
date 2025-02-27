package umu.tds.appchat.controlador;

import java.awt.EventQueue;
import java.time.LocalDate;

import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.RepositorioUsuarios;
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
		appChat.registrarUsuario("Javi", "11", "aa", LocalDate.of(1960, 10, 03),"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/1200px-Default_pfp.svg.png", "Hola, soy jesus", "email");
		appChat.registrarUsuario("Lamine Yamal", "22", "bb", LocalDate.of(1995, 12, 28), "https://upload.wikimedia.org/wikipedia/commons/8/8d/Lamine_Yamal%2C_S%C3%A1nchez_se_reuni%C3%B3_con_los_futbolistas_de_la_selecci%C3%B3n_espa%C3%B1ola_tras_ganar_la_Eurocopa_2024_%283%29_%28cropped%29.jpg", "hola, soy lamine", "email");
		appChat.registrarUsuario("Leo Messi", "33", "cc", LocalDate.of(2000, 5, 15), "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Lionel-Messi-Argentina-2022-FIFA-World-Cup_%28cropped%29.jpg/220px-Lionel-Messi-Argentina-2022-FIFA-World-Cup_%28cropped%29.jpg", "hola, soy messi", "email");
		//appChat.registrarUsuario("dd", "44", "dd", LocalDate.of(1970, 5, 11), "/usuarios/foto-diego.png", "hola, soy diego", "email");
		//appChat.registrarUsuario("ee", "55", "ee", LocalDate.of(1990, 3, 28), "/usuarios/annetaylor.jpg", "hola, soy anne", "email");
		
		appChat.login("11", "aa");
		
		ContactoIndividual c2 = appChat.agregarContacto("Lamine", "22");
		//ContactoIndividual c3 = appChat.agregarContacto("Messi", "33");
		
		appChat.enviarMensajeContacto(c2, "Hola, ¿cómo estás?", -1);
		appChat.enviarMensajeContacto(c2, "", 2);
		appChat.enviarMensajeContacto(c2, "Bien?", 2);
		
		//appChat.enviarMensajeContacto(c3, "Cuando juegas?", -1);
		//appChat.enviarMensajeContacto(c3, "", 6);
		
		appChat.login("22", "bb");
		
		ContactoIndividual c1 =appChat.agregarContacto("javi", "11");
		c1 = RepositorioUsuarios.INSTANCE.buscarUsuarioPorMovil("22").getContactoIndividual("11");
		//ContactoIndividual c4 = appChat.agregarContacto("diego", "44");
		//ContactoIndividual c5 = appChat.agregarContacto("anne", "55");
		
		appChat.enviarMensajeContacto(c1, "Vienes este finde?", -1);
		appChat.enviarMensajeContacto(c1, "", 3);
	    //appChat.enviarMensajeContacto(c4, "Juegas esta semana?", -1, TipoMensaje.ENVIADO);
		
		appChat.login("33", "cc");
		
		ContactoIndividual c4 =appChat.agregarContacto("javi", "11");
		appChat.enviarMensajeContacto(c4, "Que miras bobo", -1);
		appChat.enviarMensajeContacto(c4, "", 3);
	    
		appChat.login("11", "aa");
		appChat.enviarMensajeContacto(c2, "Sí, voy", 2);
		appChat.enviarMensajeContacto(c2, "", 5);
		appChat.enviarMensajeContacto(c2, "Adiós", 8);
		
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
