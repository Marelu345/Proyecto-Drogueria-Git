package Clases;

public class Pedido {
    int id_pedido, id_venta, Subtotal, Total;
    String 	Estado, TipoU;


    public Pedido(int id_pedido, int id_venta, int subtotal, int total, String estado, String tipoU) {
        this.id_pedido = id_pedido;
        this.id_venta = id_venta;
        Subtotal = subtotal;
        Total = total;
        Estado = estado;
        TipoU = tipoU;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(int subtotal) {
        Subtotal = subtotal;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getTipoU() {
        return TipoU;
    }

    public void setTipoU(String tipoU) {
        TipoU = tipoU;
    }
}
