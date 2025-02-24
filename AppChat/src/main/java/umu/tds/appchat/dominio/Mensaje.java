package umu.tds.appchat.dominio;

import java.time.LocalDateTime;

/**
 * Clase Mensaje. Cada contacto tiene su propia lista de mensajes.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Mensaje {
    private String texto;
    private int emoticono;
    private TipoMensaje tipo;
    private LocalDateTime fechaHoraEnvio;

    public Mensaje(String texto, int emoticono, TipoMensaje tipo) {
        this.texto = texto;
        this.emoticono = emoticono;
        this.tipo = tipo;
        this.fechaHoraEnvio = LocalDateTime.now();
    }

    public String getTexto() {
        return texto;
    }

    public int getEmoticono() {
		return emoticono;
	}

	public TipoMensaje getTipo() {
		return tipo;
	}

	public LocalDateTime getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }
}

