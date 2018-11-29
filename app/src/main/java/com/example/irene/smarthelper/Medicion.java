package com.example.irene.smarthelper;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Medicion {
    private String usuarioId;
    private String tiempo;
    private int pulso;
    private int oxigeno;

    public Medicion(String pulso, String id) {
        this.usuarioId = id;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat  df = new SimpleDateFormat("dd MM yyyy'---'HH:mm:ss");
        this.tiempo = df.format(calendar.getTime());
        this.pulso = Integer.parseInt(pulso);
        this.oxigeno = -1;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public int getPulso() {
        return pulso;
    }

    public void setPulso(int pulso) {
        this.pulso = pulso;
    }

    public int getOxigeno() {
        return oxigeno;
    }

    public void setOxigeno(int oxigeno) {
        this.oxigeno = oxigeno;
    }
}
