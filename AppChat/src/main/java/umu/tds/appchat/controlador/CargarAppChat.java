package umu.tds.appchat.controlador;

import java.awt.EventQueue;
import java.time.LocalDate;

import umu.tds.appchat.dominio.ContactoIndividual;
import umu.tds.appchat.dominio.RepositorioUsuarios;
import umu.tds.appchat.vista.VentanaLogin;

/**
 * Clase para lanzar la aplicacion.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class CargarAppChat {
	
	/**
	 * Método principal de la aplicación.
	 * 
	 * Inicia la aplicación en el hilo de despacho de eventos de AWT,
	 * encargándose de mostrar de forma segura la ventana de login {@link VentanaLogin}
	 * en el hilo principal de la interfaz gráfica.
	 * 
	 * @param args argumentos de línea de comandos (no se utilizan)
	 */
	public static void main(String[] args) {
		AppChat.getInstance().registrarUsuario("javi", "1", "1", LocalDate.now(), "src/main/resources/imagen.png", "hola", "javi@a.com");
		AppChat.getInstance().registrarUsuario("angel", "2", "2", LocalDate.now(), "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Sasso_lungo_da_passo_pordoi.jpg/330px-Sasso_lungo_da_passo_pordoi.jpg", "hola", "angel@a.com");
		AppChat.getInstance().registrarUsuario("pepe", "3", "3", LocalDate.now(), "src/main/resources/profile1.jpg", "hola", "pepe@a.com");
		AppChat.getInstance().registrarUsuario("diego", "4", "4", LocalDate.now(), "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Herbstlandschaft_%28am_Rebhang%29.jpg/330px-Herbstlandschaft_%28am_Rebhang%29.jpg", "hola", "diego@a.com");
		AppChat.getInstance().registrarUsuario("elena", "5", "5", LocalDate.now(), "https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Winterlandschaft_%28Schwarzwald%29.JPG/330px-Winterlandschaft_%28Schwarzwald%29.JPG", "hola", "elena@a.com");
		
		AppChat.getInstance().login("1", "1");
		
		ContactoIndividual c1 = AppChat.getInstance().agregarContactoIndividual("angel", "2");
		ContactoIndividual c2 = AppChat.getInstance().agregarContactoIndividual("pepe", "3");
		
		AppChat.getInstance().enviarMensajeContacto(c1, "Hola, ¿cómo estás?", -1);
		AppChat.getInstance().enviarMensajeContacto(c1, "", 2);
		
		AppChat.getInstance().enviarMensajeContacto(c2, "Cuando cantas?", -1);
		AppChat.getInstance().enviarMensajeContacto(c2, "", 6);
		
		AppChat.getInstance().login("2", "2");
		
		//ContactoIndividual c3 = AppChat.getInstance().agregarContactoIndividual("javi", "1");
		ContactoIndividual c3 = RepositorioUsuarios.INSTANCE.buscarUsuarioPorMovil("2").getContactoIndividual("1");
		ContactoIndividual c4 = AppChat.getInstance().agregarContactoIndividual("diego", "4");
		/*ContactoIndividual c5 = */AppChat.getInstance().agregarContactoIndividual("elena", "5");
		
		AppChat.getInstance().enviarMensajeContacto(c3, "Vienes este finde?", -1);
		AppChat.getInstance().enviarMensajeContacto(c3, "", 3);
		AppChat.getInstance().enviarMensajeContacto(c4, "Juegas esta semana?", -1);
		
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
