package GUI;

public class GestionPedido {
    int id_pedido, id_cliente;
    String Estado;
    int Subtotal, Total;

    public GestionPedido(int id_pedido, int id_cliente, String estado, int subtotal, int total) {
        this.id_pedido = id_pedido;
        this.id_cliente = id_cliente;
        Estado = estado;
        Subtotal = subtotal;
        Total = total;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
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
}