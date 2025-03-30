package Clases;
/**
 * Esta clase que representa un producto dentro del sistema.
 * Contiene información como nombre, descripción, tipo, precio, stock y stock minimo.
 */
public class Producto {
    int idProducto;
    String nombre;
    String descripcion;
    String tipo;
    double precio;
    int stock;
    int stockMinimo;

    /**
     * Constructor que inicializa un producto con todos sus atributos.
     *
     * @param idProducto  Identificador del producto.
     * @param nombre      Nombre del producto.
     * @param descripcion Descripción del producto.
     * @param tipo        Tipo o categoría del producto.
     * @param precio      Precio del producto.
     * @param stock       Cantidad en stock.
     * @param stockMinimo Cantidad mínima en stock antes de reponer.
     */
    public Producto(int idProducto, String nombre, String descripcion, String tipo, double precio, int stock, int stockMinimo) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public Producto() {
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return ID del producto.
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto.
     *
     * @param idProducto Nuevo ID del producto.
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return Nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre Nuevo nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return Descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     *
     * @param descripcion Nueva descripción del producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el tipo o categoría del producto.
     *
     * @return Tipo del producto.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo o categoría del producto.
     *
     * @param tipo Nuevo tipo del producto.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return Precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio Nuevo precio del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad disponible en stock.
     *
     * @return Cantidad en stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad disponible en stock.
     *
     * @param stock Nueva cantidad en stock.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la cantidad mínima en stock antes de requerir reposición.
     *
     * @return Cantidad mínima en stock.
     */
    public int getStockMinimo() {
        return stockMinimo;
    }

    /**
     * Establece la cantidad mínima en stock antes de requerir reposición.
     *
     * @param stockMinimo Nueva cantidad mínima en stock.
     */
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}