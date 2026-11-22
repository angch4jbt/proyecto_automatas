
package co.edu.poli.chomsky.modelo;

import java.util.*;

public class Nivel3 {

    public List<String> generarPasos(String cadena) {

        List<String> pasos = new ArrayList<>();

        if (cadena == null || !cadena.matches("[ab]*")) {
            pasos.add("ERROR: La cadena solo puede contener a y b.");
            return pasos;
        }

        String estado = "q0";

        pasos.add("INICIO DE LA SIMULACIÓN");
        pasos.add("Estado inicial: q0");
        pasos.add("Cadena de entrada: " + cadena);

        if (cadena.isEmpty()) {
            pasos.add("No hay símbolos para procesar.");
        }

        for (int i = 0; i < cadena.length(); i++) {

            char simbolo = cadena.charAt(i);

            String estadoAnterior = estado;

            if (simbolo == 'a') {
                estado = "q1";
            } else {
                estado = "q0";
            }

            pasos.add(
                "Posición: " + (i + 1) +
                " | Símbolo leído: " + simbolo +
                " | Transición: " + estadoAnterior +
                " → " + estado +
                " | Estado actual: " + estado
            );
        }

        pasos.add("Estado final: " + estado);

        if (estado.equals("q1")) {
            pasos.add("RESULTADO: CADENA ACEPTADA");
        } else {
            pasos.add("RESULTADO: CADENA RECHAZADA");
        }

        return pasos;
    }

    public String simular(String cadena) {

        List<String> pasos = generarPasos(cadena);

        return String.join("\n", pasos);
    }
}
