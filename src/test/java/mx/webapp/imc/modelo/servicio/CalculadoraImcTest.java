package mx.webapp.imc.modelo.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Pruebas de CalculadoraImc: fórmula, redondeo y validación del peso (RF_0007, RF_0008).
 */
class CalculadoraImcTest {

    @Test
    void calculaImcConDosDecimales() throws ValidacionException {
        assertEquals(24.98, new CalculadoraImc(76.50, 1.75).calcularImc(), 0.0001);
        assertEquals(26.78, new CalculadoraImc(82.00, 1.75).calcularImc(), 0.0001);
        assertEquals(22.10, new CalculadoraImc(58.00, 1.62).calcularImc(), 0.0001);
    }

    @Test
    void redondeaElPesoADosDecimales() throws ValidacionException {
        CalculadoraImc calculadora = new CalculadoraImc(76.499, 1.75);
        calculadora.calcularImc();
        assertEquals(76.50, calculadora.getPeso(), 0.0001);
    }

    @Test
    void rechazaPesoCero() {
        ValidacionException ex = assertThrows(ValidacionException.class,
                () -> new CalculadoraImc(0, 1.75).calcularImc());
        assertEquals("peso", ex.getCampo());
        assertEquals("El peso debe ser mayor a 0 kg.", ex.getMessage());
    }

    @Test
    void rechazaPesoNegativo() {
        ValidacionException ex = assertThrows(ValidacionException.class,
                () -> new CalculadoraImc(-70, 1.75).calcularImc());
        assertEquals("El peso debe ser mayor a 0 kg.", ex.getMessage());
    }

    @Test
    void rechazaPesoFueraDeRango() {
        assertThrows(ValidacionException.class, () -> new CalculadoraImc(0.5, 1.75).calcularImc());
        assertThrows(ValidacionException.class, () -> new CalculadoraImc(600, 1.75).calcularImc());
        assertThrows(ValidacionException.class, () -> new CalculadoraImc(Double.NaN, 1.75).calcularImc());
    }
}
