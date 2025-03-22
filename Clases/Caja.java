package Clases;
/**
 * Bueno, esta clase es para manejar la caja de la farmacia.
 * Aquí se guarda el dinero que entra y sale en este caso es ingresos e egresos, pero por ahora solo se maneja efectivo.
 * Más adelante, se podría agregar pagos con tarjeta, pero aún no está implementado.
 */
public class Caja {
    /** Este es el número que identifica la caja, como un ID único. */
    int idCaja;
    /** Cuánto dinero en efectivo hay en la caja en este momento. */
    double saldoActual;
    /** El tipo de dinero que maneja la caja. Por ahora, siempre es "Efectivo", pero después puede cambiar a otro concepto. */
    String concepto;

    /**
     * Constructor para crear una caja con un ID, un saldo inicial y un concepto.
     *
     * @param idCaja Es el número que identifica la caja.
     * @param saldoActual Es el dinero en efectivo que tiene la caja cuando se crea.
     * @param concepto Es el tipo de dinero que maneja la caja (por ahora solo "Efectivo").
     */
    public Caja(int idCaja, double saldoActual,String concepto) {
        this.idCaja = idCaja;
        this.concepto = concepto;
        this.saldoActual = saldoActual;
    }
    /**
     * Constructor vacío. Está ahí por si se necesita en algún momento.
     */

    public Caja() {}

    /**
     * Devuelve el concepto de la caja, es decir, pues el tipo de dinero que maneja.
     *
     * @return El concepto de la caja. Actualmente, siempre es "Efectivo", pero la idea esque se pueda meter como tal otros métodos de pago más adeltante.
     */
    public String getConcepto() {return concepto;}

    /**
     * Aquí se puede cambiar el concepto de la caja. Aunque por ahora será "Efectivo",
     * más adelante podría incluir pagos con tarjeta.
     *
     * @param concepto El tipo de dinero que maneja la caja (actualmente solo "Efectivo").
     */
    public void setConcepto(String concepto) {this.concepto = concepto;}
    /**
     * Devuelve el ID de la caja, que sirve cmo tal para poder identificarla.
     *
     * @return El ID de la caja.
     */
    public int getIdCaja() {
        return idCaja;
    }

    /**
     * Cambia el ID de la caja, pero, pues en teoría este número no debería cambiar.
     *
     * @param idCaja El nuevo ID de la caja.
     */
    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    /**
     * Devuelve cuánto dinero en efectivo hay en la caja en este momento.
     *
     * @return El saldo actual en efectivo.
     */
    public double getSaldoActual() {
        return saldoActual;
    }

    /**
     * Aquí se puede cambiar el saldo de la caja, ya sea cuando entra o sale dinero es decir (ingresos e egresos).
     *
     * @param saldoActual Nuevo monto de dinero en la caja.
     */
    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
}
