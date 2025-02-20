package umu.tds.appchat.dominio;

import java.time.LocalDateTime;

public class Mensaje {
    private String texto;
    private int emoticono;
    private int tipo;
    private LocalDateTime fechaHoraEnvio;

    public Mensaje(String texto, int tipo) {
        this.texto = texto;
        this.tipo = tipo;
        this.fechaHoraEnvio = LocalDateTime.now();
    }
    
    public Mensaje(int emoticono, int tipo) {
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

	public int getTipo() {
		return tipo;
	}

	public LocalDateTime getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }
}

