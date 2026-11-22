
package co.edu.poli.chomsky.modelo;

public class Nivel0 {

    public String simular(String cadena) {

        if (cadena == null || !cadena.matches("1+\\+1+")) {
            return "ERROR: Escribe una suma unaria, por ejemplo 11+1.";
        }

        StringBuilder recorrido = new StringBuilder();

        char[] cinta = (cadena + "_").toCharArray();

        int posicionMas = cadena.indexOf('+');

        recorrido.append("NIVEL 0 - MAQUINA DE TURING\n");
        recorrido.append("Operacion: suma unaria\n\n");

        mostrar(recorrido, cinta, 0, "q0", "Inicio de la maquina.");

        // q0: busca el signo +
        for (int i = 0; i < posicionMas; i++) {
            mostrar(recorrido, cinta, i, "q0",
                    "Lee un 1 y avanza a la derecha.");
        }

        mostrar(recorrido, cinta, posicionMas, "q0",
                "Encuentra el signo +.");


        cinta[posicionMas] = '_';

        mostrar(recorrido, cinta, posicionMas, "q1",
                "Escribe blanco en lugar del signo +.");

        
        for (int i = posicionMas + 1; i < cadena.length(); i++) {

            mostrar(recorrido, cinta, i, "q1",
                    "Lee un 1 del segundo numero.");

            cinta[i] = '_';

            mostrar(recorrido, cinta, i, "q2",
                    "Borra el 1 para desplazarlo.");

            cinta[i - 1] = '1';

            mostrar(recorrido, cinta, i - 1, "q2",
                    "Escribe el 1 en la posicion anterior.");

            mostrar(recorrido, cinta, i, "q3",
                    "Avanza para leer el siguiente simbolo.");
        }

        mostrar(recorrido, cinta, cadena.length(), "qf",
                "No hay mas unos. La maquina termina.");

        String resultado = new String(cinta).replace("_", "");

        recorrido.append("\nRESULTADO: ");
        recorrido.append(resultado);
        recorrido.append("\nSUMA COMPLETADA");

        return recorrido.toString();
    }

    private void mostrar(
            StringBuilder recorrido,
            char[] cinta,
            int cabeza,
            String estado,
            String mensaje) {

        recorrido.append("Estado: ").append(estado);
        recorrido.append(" | Cabezal: ").append(cabeza);
        recorrido.append("\nCinta: ").append(new String(cinta));
        recorrido.append("\n").append(mensaje).append("\n\n");
    }
}
