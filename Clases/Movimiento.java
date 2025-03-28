package Clases;

import java.sql.Timestamp;
import java.util.Date;
/**
 * Pues esta clase representa un movimiento de dinero dentro del sistema,
 * ya sea que entre o salga dinero. Como tal, cada movimiento
 * tiene su tipo, su categoría y la cantidad de plata que se movió.
 */
public class Movimiento {
    int id;
    String tipo;
    String categoria;
    double monto;
    Date fecha;
    /**
     * Este es el constructor que se usa para crear un movimiento con
     * todos los datos necesarios.
     *
     * @param id        Pues este es el número único del movimiento.
     * @param tipo      Aquí va si es un ingreso o un egreso.
     * @param categoria La categoría del movimiento, tipo "Ingreso" o "Egreso".
     * @param monto     Como tal, la cantidad de dinero que se movió.
     * @param fecha     La fecha en que se hizo el movimiento.
     */

    /**
     * Clase Movimiento que representa un registro financiero dentro del sistema.
     * Esta clase permite manejar información sobre ingresos y egresos de la farmacia.
     *
     * @author Estudiante SENA
     * @version 1.0
     */
    public Movimiento(int id, String tipo, String categoria, double monto, Date fecha) {
        this.id = id;
        this.tipo = tipo;
        this.categoria = categoria;
        this.monto = monto;
        this.fecha = fecha;
    }

    /**
     * Constructor vacío de la clase Movimiento.
     * Se usa para crear objetos sin inicializar atributos de inmediato.
     */

    public Movimiento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Método para obtener la fecha del movimiento.
     *
     * @return fecha del movimiento.
     */
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}