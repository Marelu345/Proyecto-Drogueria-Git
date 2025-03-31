package Clases;

/**
 * Clase que representa el detalle de un pedido.
 * Contiene la información de cada producto dentro de un pedido,
 * incluyendo cantidad, subtotal y referencias a pedido y producto.
 */
public class DetallePedido {
    int id_detalleV, id_pedido,	Cantidad, id_producto;
    float Subtotal;

    /**
     * Constructor para inicializar un detalle de pedido con todos sus atributos.
     *
     * @param id_detalleV Identificador único del detalle del pedido.
     * @param id_pedido Identificador del pedido al que pertenece.
     * @param cantidad Cantidad del producto.
     * @param id_producto Identificador del producto.
     * @param subtotal Subtotal del detalle del pedido.
     */
    public DetallePedido(int id_detalleV, int id_pedido, int cantidad, int id_producto, float subtotal) {
        this.id_detalleV = id_detalleV;
        this.id_pedido = id_pedido;
        this.Cantidad = cantidad;
        this.id_producto = id_producto;
        this.Subtotal = subtotal;
    }

    /**
     * Obtiene el identificador del detalle del pedido.
     *
     * @return ID del detalle del pedido.
     */
    public int getId_detalleV() {
        return id_detalleV;
    }

    /**
     * Establece el identificador del detalle del pedido.
     *
     * @param id_detalleV Nuevo ID del detalle del pedido.
     */
    public void setId_detalleV(int id_detalleV) {
        this.id_detalleV = id_detalleV;
    }

    /**
     * Obtiene el identificador del pedido asociado.
     *
     * @return ID del pedido.
     */
    public int getId_pedido() {
        return id_pedido;
    }

    /**
     * Establece el identificador del pedido asociado.
     *
     * @param id_pedido Nuevo ID del pedido.
     */
    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    /**
     * Obtiene la cantidad de productos en este detalle de pedido.
     *
     * @return Cantidad de productos.
     */
    public int getCantidad() {
        return Cantidad;
    }

    /**
     * Establece la cantidad de productos en este detalle de pedido.
     *
     * @param cantidad Nueva cantidad de productos.
     */
    public void setCantidad(int cantidad) {
        this.Cantidad = cantidad;
    }

    /**
     * Obtiene el identificador del producto asociado a este detalle.
     *
     * @return ID del producto.
     */
    public int getId_producto() {
        return id_producto;
    }

    /**
     * Establece el identificador del producto asociado a este detalle.
     *
     * @param id_producto Nuevo ID del producto.
     */
    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    /**
     * Obtiene el subtotal de este detalle de pedido.
     *
     * @return Subtotal del detalle.
     */
    public float getSubtotal() {
        return Subtotal;
    }

    /**
     * Establece el subtotal de este detalle de pedido.
     *
     * @param subtotal Nuevo subtotal del detalle.
     */
    public void setSubtotal(float subtotal) {
        this.Subtotal = subtotal;
    }
}
