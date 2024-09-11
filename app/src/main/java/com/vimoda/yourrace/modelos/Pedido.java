package com.vimoda.yourrace.modelos;

public class Pedido {
    private String id;
    private String fecha;
    private String hora;
    private double precioTotal;
    private String estado;

    // Constructor vacío requerido por Firestore
    public Pedido() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Constructor con parámetros
    public Pedido(String fecha, String hora, double precioTotal, String estado) {
        this.fecha = fecha;
        this.hora = hora;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }

    // Getters y Setters
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
