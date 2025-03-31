package GUI;

public class GestionVentas {

    int id_venta,	id_pedido, id_producto, cantidad;
    String tipo;
    int PrecioU;
    String Fecha;

    public GestionVentas(int id_venta, int id_pedido, int id_producto, int cantidad, String tipo, int precioU, String fecha) {
        this.id_venta = id_venta;
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.tipo = tipo;
        PrecioU = precioU;
        Fecha = fecha;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPrecioU() {
        return PrecioU;
    }

    public void setPrecioU(int precioU) {
        PrecioU = precioU;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}
