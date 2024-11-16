package com.example.intento8;

import java.io.Serializable;
import java.util.ArrayList;

public class Voucher implements Serializable {
    private String idVoucher;
    private ArrayList<CarritoItem> productos;
    private double total;
    private String fechaHora;

    public Voucher(String idVoucher, ArrayList<CarritoItem> productos, double total, String fechaHora) {
        this.idVoucher = idVoucher;
        this.productos = productos;
        this.total = total;
        this.fechaHora = fechaHora;
    }

    public String getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    public ArrayList<CarritoItem> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<CarritoItem> productos) {
        this.productos = productos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
