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

    /**
     * Constructor que inicializa una venta con todos sus atributos.
     *
     * @param idVenta Identificador único de la venta.
     * @param idProducto Identificador del producto.
     * @param idCliente Identificador del cliente que realizó la compra.
     * @param precioUnitario Precio unitario del producto.
     * @param iva Impuesto aplicado a la venta.
     * @param precioTotal Precio total de la venta.
     * @param fecha Fecha en la que se realizó la venta.
     */
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

    /**
     * Obtiene el identificador de la venta.
     *
     * @return ID de la venta.
     */
    public int getIdVenta() {
        return idVenta;
    }

    /**
     * Establece el identificador de la venta.
     *
     * @param idVenta Nuevo ID de la venta.
     */
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * Obtiene el identificador del producto vendido.
     *
     * @return ID del producto.
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto vendido.
     *
     * @param idProducto Nuevo ID del producto.
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el identificador del cliente que realizó la compra.
     *
     * @return ID del cliente.
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Establece el identificador del cliente que realizó la compra.
     *
     * @param idCliente Nuevo ID del cliente.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Obtiene el precio unitario del producto vendido.
     *
     * @return Precio unitario del producto.
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del producto vendido.
     *
     * @param precioUnitario Nuevo precio unitario del producto.
     */
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene el impuesto aplicado a la venta.
     *
     * @return IVA aplicado a la venta.
     */
    public double getIva() {
        return iva;
    }

    /**
     * Establece el impuesto aplicado a la venta.
     *
     * @param iva Nuevo valor del IVA.
     */
    public void setIva(double iva) {
        this.iva = iva;
    }

    /**
     * Obtiene el precio total de la venta.
     *
     * @return Precio total de la venta.
     */
    public double getPrecioTotal() {
        return precioTotal;
    }

    /**
     * Establece el precio total de la venta.
     *
     * @param precioTotal Nuevo precio total de la venta.
     */
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    /**
     * Obtiene la fecha en la que se realizó la venta.
     *
     * @return Fecha de la venta.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha en la que se realizó la venta.
     *
     * @param fecha Nueva fecha de la venta.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

