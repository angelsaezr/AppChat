package umu.tds.appchat.dominio;

import java.time.LocalDateTime;

/**
 * Clase Mensaje. Cada contacto tiene su propia lista de mensajes.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
/**
 * Representa un mensaje dentro de la aplicación de mensajería.
 * Contiene texto, un emoticono, el tipo de mensaje (enviado o recibido), 
 * la fecha y hora de envío, y un código identificador.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class Mensaje {

    /**
     * Texto del mensaje.
     */
    private String texto;

    /**
     * Código del emoticono asociado al mensaje.
     */
    private int emoticono;

    /**
     * Tipo del mensaje: enviado o recibido.
     */
    private TipoMensaje tipo;

    /**
     * Fecha y hora en la que fue enviado el mensaje.
     */
    private LocalDateTime fechaHoraEnvio;

    /**
     * Código identificador del mensaje.
     */
    private int codigo;

    /**
     * Crea un mensaje con el texto, emoticono y tipo especificados.
     * La fecha y hora de envío se establece al momento de creación.
     *
     * @param texto contenido del mensaje
     * @param emoticono código del emoticono
     * @param tipo tipo del mensaje (enviado o recibido)
     */
    public Mensaje(String texto, int emoticono, TipoMensaje tipo) {
        this.texto = texto;
        this.emoticono = emoticono;
        this.tipo = tipo;
        this.fechaHoraEnvio = LocalDateTime.now();
    }

    /**
     * Obtiene el texto del mensaje.
     *
     * @return texto del mensaje
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Obtiene el código del emoticono.
     *
     * @return código del emoticono
     */
    public int getEmoticono() {
        return emoticono;
    }

    /**
     * Obtiene el tipo del mensaje.
     *
     * @return tipo del mensaje
     */
    public TipoMensaje getTipo() {
        return tipo;
    }

    /**
     * Devuelve la fecha y hora de envío del mensaje.
     *
     * @return fecha y hora de envío
     */
    public LocalDateTime getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }

    /**
     * Obtiene el código identificador del mensaje.
     *
     * @return código del mensaje
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código identificador del mensaje.
     *
     * @param codigo nuevo código
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}