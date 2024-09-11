package com.vimoda.yourrace.modelos;

public class DetalleProducto {
    private static final String TAG = "DetalleProducto";
    private String marca;
    private String modelo;
    private int cantidad;
    private String talla;
    private double precio;
    private double subtotal;

    public DetalleProducto() {
        // Constructor vacío requerido por Firestore
    }

    public DetalleProducto(String marca, String modelo, int cantidad, String talla, double precio, double subtotal) {
        this.marca = marca;
        this.modelo = modelo;
        this.cantidad = cantidad;
        this.talla = talla;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTalla() {
        return talla;
    }
    public void setTalla(String talla) {
        this.talla = talla;
    }

    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }


    @Override
    public String toString() {
        return "DetalleProducto{" +
                "marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", cantidad=" + cantidad +
                ", talla='" + talla + '\'' +
                ", precio=" + precio +
                ", subtotal=" + subtotal +
                '}';
    }
}
