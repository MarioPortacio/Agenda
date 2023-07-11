package com.example.agenda.model;

import java.io.Serializable;

public class Tarea implements Serializable {

    private String actividad;
    private String dia;
    private String mes;
    private String anio;
    private String hora;
    private String min;
    private String desc;
    private String prio;
    private String tid;


    public Tarea() { //constructor

    }


    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }


    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }


    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }



    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    @Override
    public String toString() { //vista de lo que se vera en pantalla al listar las tareas
        return
                "Actividad: " + actividad +
                ", Fecha: " + anio+"/"+ mes+ "/"+dia +
                ", Hora: " +hora+":"+min + ", Prioridad: " +prio;
    }
    //"actividad='" + actividad + '\'' +

}
