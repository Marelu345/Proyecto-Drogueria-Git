package Clases;

public class Caja {
    int idCaja;
    double saldoActual;

    public Caja(int idCaja, double saldoActual) {
        this.idCaja = idCaja;
        this.saldoActual = saldoActual;
    }

    public Caja() {}

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
}
