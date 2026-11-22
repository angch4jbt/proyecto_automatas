
package co.edu.poli.chomsky.controlador;

import java.util.List;
import co.edu.poli.chomsky.modelo.*;

public class SimuladorControlador {

    private Nivel3 nivel3 = new Nivel3();
    private Nivel2 nivel2 = new Nivel2();
    private Nivel1 nivel1 = new Nivel1();
    private Nivel0 nivel0 = new Nivel0();

    public String ejecutarNivel3(String cadena) {
        return nivel3.simular(cadena);
    }

    public List<String> ejecutarPasosNivel2(String cadena) {
        return nivel2.generarPasos(cadena);
    }

    public String ejecutarNivel2(String cadena) {
        return nivel2.simular(cadena);
    }

    public String ejecutarNivel1(String cadena) {
        return nivel1.simular(cadena);
    }

    public String ejecutarNivel0(String cadena) {
        return nivel0.simular(cadena);
    }
}