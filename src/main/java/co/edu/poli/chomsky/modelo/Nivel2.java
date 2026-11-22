
package co.edu.poli.chomsky.modelo;

import java.util.*;

public class Nivel2 {

    public List<String> generarPasos(String cadena) {
        List<String> pasos = new ArrayList<>();
        Stack<Character> pila = new Stack<>();

        if (cadena == null || !cadena.matches("a*b*")) {
            pasos.add("ERROR: Primero a y después b.");
            return pasos;
        }

        pasos.add("PILA INICIAL: VACÍA");

        for (char c : cadena.toCharArray()) {

            if (c == 'a') {
                pila.push('A');
                pasos.add("Lee a | Apila A | " + pila);
            } else {
                if (pila.isEmpty()) {
                    pasos.add("No hay A para retirar.");
                    pasos.add("CADENA RECHAZADA");
                    return pasos;
                }

                pila.pop();
                pasos.add("Lee b | Retira A | " + pila);
            }
        }

        pasos.add(pila.isEmpty()
                ? "CADENA ACEPTADA"
                : "CADENA RECHAZADA");

        return pasos;
    }

    public String simular(String cadena) {
        return String.join("\n", generarPasos(cadena));
    }
}
