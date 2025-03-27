package Clases;

public class DetallePedido {
    int id_detalleV, id_pedido,	Cantidad, id_producto;
    float Subtotal;

    public DetallePedido(int id_detalleV, int id_pedido, int cantidad, int id_producto, float subtotal) {
        this.id_detalleV = id_detalleV;
        this.id_pedido = id_pedido;
        Cantidad = cantidad;
        this.id_producto = id_producto;
        Subtotal = subtotal;
    }

    public int getId_detalleV() {
        return id_detalleV;
    }

    public void setId_detalleV(int id_detalleV) {
        this.id_detalleV = id_detalleV;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public float getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(float subtotal) {
        Subtotal = subtotal;
    }
}
