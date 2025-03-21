package Clases;

import java.util.Date;

public class Venta {
    private int idVenta;
    private int idProducto;
    private int idCliente;
    private double precioUnitario;
    private double iva;
    private double precioTotal;
    private Date fecha;

    public Venta(int idVenta, int idProducto, int idCliente, double precioUnitario, double iva, double precioTotal, Date fecha) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.idCliente = idCliente;
        this.precioUnitario = precioUnitario;
        this.iva = iva;
        this.precioTotal = precioTotal;
        this.fecha = fecha;
    }

    public Venta() {}

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

