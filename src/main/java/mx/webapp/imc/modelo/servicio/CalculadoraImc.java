package mx.webapp.imc.modelo.servicio;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calcula el índice de masa corporal: IMC = peso (kg) / estatura² (m).
 */
public class CalculadoraImc {

    public static final double PESO_MINIMO = 1.00;
    public static final double PESO_MAXIMO = 500.00;

    private double peso;
    private double estatura;

    public CalculadoraImc(double peso, double estatura) {
        this.peso = peso;
        this.estatura = estatura;
    }

    /**
     * Redondea el peso a dos decimales, valida los datos y regresa el IMC
     * redondeado a dos decimales.
     *
     * @throws ValidacionException si el peso es 0, negativo o está fuera de rango
     */
    public double calcularImc() throws ValidacionException {
        if (Double.isNaN(peso) || Double.isInfinite(peso)) {
            throw new ValidacionException("peso", "Captura un peso válido, por ejemplo 76.50.");
        }
        peso = redondear(peso);
        if (peso <= 0) {
            throw new ValidacionException("peso", "El peso debe ser mayor a 0 kg.");
        }
        if (peso < PESO_MINIMO || peso > PESO_MAXIMO) {
            throw new ValidacionException("peso", "El peso debe estar entre 1 y 500 kg.");
        }
        if (estatura < UsuarioService.ESTATURA_MINIMA || estatura > UsuarioService.ESTATURA_MAXIMA) {
            throw new ValidacionException("estatura", "La estatura debe estar entre 1.00 y 2.50 m.");
        }
        return redondear(peso / (estatura * estatura));
    }

    private double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }
}
