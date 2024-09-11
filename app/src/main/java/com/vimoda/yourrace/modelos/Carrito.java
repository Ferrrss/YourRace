package com.vimoda.yourrace.modelos;

public class Carrito {
    String codigo,color,fechaIngreso,marca,modelo,precio,talla;
    int contadorStock=0;
    public Carrito() {
    }

    public Carrito(String codigo, String color, String fechaIngreso, String marca, String modelo, String precio, String talla) {
        this.codigo = codigo;
        this.color = color;
        this.fechaIngreso = fechaIngreso;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.talla = talla;
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

    public int acumuladorStock(){
        if(contadorStock >= 0){
            contadorStock++;
            return contadorStock;
        }
        return contadorStock;
    }

    public int desacumularStock(){
        if(contadorStock > 0){
            contadorStock--;
        }
        return contadorStock;
    }

    public int getContadorStock() {
        return contadorStock;
    }
    public void setContadorStock(int contadorStock) {
        this.contadorStock = contadorStock;
    }

    //Metodo para los titulos de la tienda virtual
    public String getName(){
        String name = getMarca() + " "+ getModelo();
        return name;
    }
}