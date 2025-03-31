package Clases;
/**
 * Esta clase es la principal para gestionar y realizar Pedidos.
 * Aqui se guardan los datos necesarios para realizar un pedido
 */
public class Pedido {
    int id_pedido, id_venta;
    double Subtotal, Total;
    String 	Estado, TipoU;

    /**
     * Constructor.
     *
     * @param id_pedido El número de identificación del pedido.
     * @param id_venta  El número de identificación de la venta y llave foranea.
     * @param subtotal  Valor parcial cuando se añade un producto.
     * @param total   Valor total del Pedido.
     * @param estado  El momento en el que se encuentra el pedido.
     * @param tipoU  Unidad, Blister, Caja
     */

    public Pedido(int id_pedido, int id_venta, double subtotal, double total, String estado, String tipoU) {
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
    /**
     * Asigna un nuevo ID al pedido.
     * @param id_pedido Identificador del Pedido.
     */
    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }
    /**
     * Obtiene el id del pedido.
     * @return Id del pedido.
     */
    public int getId_venta() {
        return id_venta;
    }
    /**
     * Llama al id venta.
     * @param id_venta Identificador de la venta.
     */
    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }
    /**
     * Obtiene el id de la venta para retornarla.
     * @return Id de la venta.
     */
    public double getSubtotal() {
        return Subtotal;
    }
    /**
     * Establece el subtotal del pedido.
     * @param subtotal Nuevo total del pedido.
     */
    public void setSubtotal(double subtotal) {
        Subtotal = subtotal;
    }
    /**
     * Obtiene la suma de los productos agregados.
     * @return Se a lmacena en venta.
     */
    public double getTotal() {
        return Total;
    }
    /**
     * Establece el total del pedido.
     * @param total Nuevo total del pedido.
     */
    public void setTotal(double total) {
        Total = total;
    }
    /**
     * Obtiene el los valores de pago total.
     * @return Se almacena en venta.
     */

    public String getEstado() {
        return Estado;
    }
    /**
     * Establece el estado del pedido, en preparación, enviado o entregado.
     * @param estado Estado en que se encuentra el pedido.
     */
    public void setEstado(String estado) {
        Estado = estado;
    }
    /**
     * Obtiene en que momento o estado se encuentra el pedido.
     * @return Se muestra en pantalla.
     */
    public String getTipoU() {
        return TipoU;
    }

    public void setTipoU(String tipoU) {
        TipoU = tipoU;
    }
}

//https://github.com/Marelu345/Proyecto-Drogueria-Git.git
