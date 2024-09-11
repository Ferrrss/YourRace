package com.vimoda.yourrace.modelos;

public class Zapatos {
    String codigo,color,fechaIngreso,marca,modelo,precio,talla,stock;
    private int cantidad;
    private double subtotal;

    public Zapatos(){}
    public Zapatos(String codigo,String color, String fechaIngreso, String marca, String modelo,
                   String precio, String talla,String stock,int cantidad) {
        this.codigo = codigo;
        this.color = color;
        this.fechaIngreso = fechaIngreso;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.talla = talla;
        this.stock = stock;
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTalla() {
        return talla;
    }
    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getStock() { return stock; }
    public void setStock(String stock) { this.stock = stock; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    //Metodo para los titulos de la tienda virtual
    public String getName(){
        String name = getMarca() + " "+ getModelo();
        return name;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

}
