package com.example.irene.smarthelper;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Medicion {
    private String usuarioId;
    private String tiempo;
    private String pulso;
    private String oxigeno;

    public Medicion(String pulso, String id) {
        this.usuarioId = id;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat  df = new SimpleDateFormat("yyyy'-'MM'-'dd HH:mm:ss");
        this.tiempo = df.format(calendar.getTime());
        this.pulso = pulso;
        this.oxigeno = "-1";
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

    public String getPulso() {
        return pulso;
    }

    public void setPulso(String pulso) {
        this.pulso = pulso;
    }

    public String getOxigeno() {
        return oxigeno;
    }

    public void setOxigeno(String oxigeno) {
        this.oxigeno = oxigeno;
    }
}
