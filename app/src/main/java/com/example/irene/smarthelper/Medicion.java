package com.example.irene.smarthelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Clase Java que representa cada una de las mediciones que realizará la MiBand 2.
 * Almacena información referente al identificador del usuario (usuarioId), la fecha y hora de la lectura (tiempo),
 * el valor del pulso cardíaco (pulso) y el valor del porcentaje de oxígeno en sangre (por defecto -1).
 */
public class Medicion {
    private String usuarioId;
    private String tiempo;
    private String pulso;
    private String oxigeno;

    /**
     * Constructor de la clase Medicion.
     * @param pulso valor del pulso cardíaco leído.
     * @param id identificador del usuario.
     */
    public Medicion(String pulso, String id) {
        this.usuarioId = id;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat  df = new SimpleDateFormat("yyyy'-'MM'-'dd HH:mm:ss");
        this.tiempo = df.format(calendar.getTime());
        this.pulso = pulso;
        this.oxigeno = "-1";
    }

    /**
     * Getter del atributo usuarioId.
     * @return valor del identificador del usuario.
     */
    public String getUsuarioId() {
        return usuarioId;
    }

    /**
     * Setter del atributo usuarioId.
     * @param usuarioId -> valor nuevo para el identificador del usuario.
     */
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Getter del atributo tiempo.
     * @return valor de la fecha y hora en que se leyó la medición.
     */
    public String getTiempo() {
        return tiempo;
    }

    /**
     * Setter del atributo tiempo.
     * @param tiempo -> nueva fecha y hora para la medición.
     */
    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Getter del atributo pulso.
     * @return valor del pulso cardíaco de la medición.
     */
    public String getPulso() {
        return pulso;
    }

    /**
     * Setter del atributo pulso.
     * @param pulso -> nuevo valor de la medición cardíaca.
     */
    public void setPulso(String pulso) {
        this.pulso = pulso;
    }

    /**
     * Getter del atributo oxígeno.
     * @return valor correspondiente al porcentaje de oxígeno en sangre.
     */
    public String getOxigeno() {
        return oxigeno;
    }

    /**
     * Setter del atributo oxígeno.
     * @param oxigeno -> nuevo valor del porcenaje de oxígeno en sangre.
     */
    public void setOxigeno(String oxigeno) {
        this.oxigeno = oxigeno;
    }
}
