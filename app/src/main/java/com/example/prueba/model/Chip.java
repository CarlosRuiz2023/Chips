package com.example.prueba.model;

import java.io.Serializable;

public class Chip implements Serializable {
    private String imei;
    private String numero;
    private String baneo;
    private String compania;
    private String detalle;

    public Chip() {
    }

    public Chip(String imei, String numero, String baneo, String compania, String detalle) {
        this.imei = imei;
        this.numero = numero;
        this.baneo = baneo;
        this.compania = compania;
        this.detalle = detalle;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBaneo() {
        return baneo;
    }

    public void setBaneo(String baneo) {
        this.baneo = baneo;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Imei:" + imei + '\n' +
                "Numero:" + numero +'\n' +
                "Baneo:" + baneo + '\n' +
                "Compania:" + compania + '\n' +
                "Detalle:" + detalle + '\n';
    }
}

