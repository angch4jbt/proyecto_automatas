
package co.edu.poli.chomsky.modelo;

public class Nivel1 {

    public String simular(String cadena) {

        StringBuilder recorrido = new StringBuilder();

        if (cadena == null || !cadena.matches("a+b+c+")) {
            return "ERROR: La cadena debe contener a, b y c en ese orden.";
        }

        char[] cinta = cadena.toCharArray();
        int n = cinta.length;

        recorrido.append("NIVEL 1 - CINTA ACOTADA\n");
        recorrido.append("Lenguaje: a^n b^n c^n\n");
        recorrido.append("Cinta inicial: ").append(cadena).append("\n\n");

        while (true) {

            int posicionA = -1;

            for (int i = 0; i < n; i++) {
                if (cinta[i] == 'a') {
                    posicionA = i;
                    break;
                }
            }

            if (posicionA == -1) {
                break;
            }

            cinta[posicionA] = 'X';
            recorrido.append("Se marca a: ")
                    .append(new String(cinta)).append("\n");

            int posicionB = -1;

            for (int i = posicionA + 1; i < n; i++) {
                if (cinta[i] == 'b') {
                    posicionB = i;
                    break;
                }
            }

            if (posicionB == -1) {
                return recorrido + "\nNo se encontro b.\nRESULTADO: CADENA RECHAZADA";
            }

            cinta[posicionB] = 'Y';
            recorrido.append("Se marca b: ")
                    .append(new String(cinta)).append("\n");

            int posicionC = -1;

            for (int i = posicionB + 1; i < n; i++) {
                if (cinta[i] == 'c') {
                    posicionC = i;
                    break;
                }
            }

            if (posicionC == -1) {
                return recorrido + "\nNo se encontro c.\nRESULTADO: CADENA RECHAZADA";
            }

            cinta[posicionC] = 'Z';
            recorrido.append("Se marca c: ")
                    .append(new String(cinta)).append("\n\n");
        }

        for (char simbolo : cinta) {
            if (simbolo != 'X' && simbolo != 'Y' && simbolo != 'Z') {
                return recorrido + "\nQuedaron simbolos sin marcar.\nRESULTADO: CADENA RECHAZADA";
            }
        }

        recorrido.append("Cinta final: ").append(new String(cinta));
        recorrido.append("\nRESULTADO: CADENA ACEPTADA");

        return recorrido.toString();
    }
}
