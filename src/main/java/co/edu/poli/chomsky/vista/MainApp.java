package co.edu.poli.chomsky.vista;
import java.util.*;
import co.edu.poli.chomsky.controlador.SimuladorControlador;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {
    private SimuladorControlador controlador = new SimuladorControlador();
    private Circle q0, q1;
    private QuadCurve flechaA, flechaB, bucleQ0, bucleQ1;
    private VBox pilaVisual = new VBox(4);
    private HBox simbolos = new HBox(6);
    private VBox panelRecorrido = new VBox(8);
    private VBox vistaCinta = new VBox(5);
    private Label textoEstado = new Label("Estado actual: q0");
    private Label progreso = new Label("Símbolos procesados: 0");
    private Label resultadoVisual = new Label("Esperando simulación...");
    private Label cabezal = new Label("Cabezal: esperando");
    private TextArea salida = new TextArea();
    private String nivel = "3";
    private String cadena = "";
    private int posicion = 0;
    private int cabezaActual = 0;
    private List<String> pasos = new ArrayList<>();
    private List<Label> etiquetas = new ArrayList<>();
    private List<String> cintas = new ArrayList<>();
    private List<Integer> cabezas = new ArrayList<>();
    private List<String> mensajesCinta = new ArrayList<>();
    private List<String> estadosCinta = new ArrayList<>();
    private boolean aceptaCinta = false;
    private String resultadoCinta = "";
    private Timeline animacion = new Timeline();

    private void estilo(Label l) {
        l.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
    }

    private String estiloBoton(String color) {
        return "-fx-background-color:" + color +
               ";-fx-text-fill:white;-fx-font-weight:bold;" +
               "-fx-background-radius:7;-fx-padding:8;";
    }

    @Override
    public void start(Stage ventana) {
        Label encabezado = new Label("TEORÍA DE LA COMPUTACIÓN");
        encabezado.setStyle("-fx-text-fill:#a78bfa;-fx-font-weight:bold;");
        Label titulo = new Label("Jerarquía de Chomsky");
        titulo.setStyle("-fx-text-fill:white;-fx-font-size:22px;-fx-font-weight:bold;");
        Label descripcion = new Label(
            "Explora y simula los cuatro niveles del lenguaje formal."
        );
        descripcion.setStyle("-fx-text-fill:#aab4c5;");
        VBox cabecera = new VBox(4, encabezado, titulo, descripcion);

        Button n3 = tarjeta("NIVEL 3", "Lenguajes regulares", "Autómata finito", "#60a5fa");
        Button n2 = tarjeta("NIVEL 2", "Libres de contexto", "Autómata de pila", "#c4b5fd");
        Button n1 = tarjeta("NIVEL 1", "Sensibles al contexto", "Cinta acotada", "#6ee7b7");
        Button n0 = tarjeta("NIVEL 0", "Recursivamente enumerables", "Máquina de Turing", "#fcd34d");

        GridPane tarjetas = new GridPane();
        tarjetas.setHgap(9);
        tarjetas.setVgap(9);
        tarjetas.add(n3, 0, 0);
        tarjetas.add(n2, 1, 0);
        tarjetas.add(n1, 0, 1);
        tarjetas.add(n0, 1, 1);

        Label mensaje = new Label("Selecciona uno de los cuatro niveles.");
        estilo(mensaje);
        TextField entrada = new TextField();
        entrada.setPromptText("Escribe aquí tu cadena...");
        entrada.setStyle("-fx-background-color:#263449;-fx-text-fill:white;");

        Button simular = new Button("▶ SIMULAR CADENA");
        simular.setMaxWidth(Double.MAX_VALUE);
        simular.setStyle(estiloBoton("#7c3aed"));

        Label tituloRecorrido = new Label("RECORRIDO DE LA SIMULACIÓN");
        estilo(tituloRecorrido);
        Pane diagrama = crearDiagrama();

        pilaVisual.setAlignment(Pos.BOTTOM_CENTER);
        pilaVisual.setMinHeight(130);
        pilaVisual.setStyle("-fx-border-color:#64748b;-fx-border-width:0 0 3 0;");
        Label tituloPila = new Label("PILA (LIFO)");
        estilo(tituloPila);
        VBox vistaPila = new VBox(8, tituloPila, pilaVisual);
        vistaPila.setAlignment(Pos.CENTER);
        vistaPila.setPrefHeight(175);

        Label tituloCinta = new Label("CINTA Y CABEZAL");
        estilo(tituloCinta);
        estilo(cabezal);
        vistaCinta.getChildren().addAll(tituloCinta, cabezal);
        vistaCinta.setAlignment(Pos.CENTER);
        vistaCinta.setPadding(new Insets(10));

        estilo(textoEstado);
        estilo(progreso);
        estilo(resultadoVisual);

        Button iniciar = new Button("▶ Iniciar");
        Button siguiente = new Button("Paso siguiente");
        Button reiniciar = new Button("↻ Reiniciar");
        iniciar.setStyle(estiloBoton("#2563eb"));
        siguiente.setStyle(estiloBoton("#7c3aed"));
        reiniciar.setStyle(estiloBoton("#475569"));

        HBox controles = new HBox(7, iniciar, siguiente, reiniciar);
        controles.setAlignment(Pos.CENTER);
        simbolos.setAlignment(Pos.CENTER);
        simbolos.setPadding(new Insets(8));

        panelRecorrido.getChildren().addAll(
            tituloRecorrido, diagrama, vistaPila, vistaCinta,
            textoEstado, simbolos, progreso,
            resultadoVisual, controles
        );
        panelRecorrido.setAlignment(Pos.CENTER);
        panelRecorrido.setPadding(new Insets(10));
        panelRecorrido.setStyle(
            "-fx-background-color:#202b40;-fx-background-radius:9;"
        );
        panelRecorrido.setVisible(false);
        panelRecorrido.setManaged(false);

        salida.setEditable(false);
        salida.setWrapText(true);
        salida.setPrefHeight(90);
        salida.setStyle(
            "-fx-control-inner-background:#111c2e;" +
            "-fx-text-fill:#dbeafe;"
        );

        VBox simulacion = new VBox(
            8, mensaje, new Label("CADENA DE ENTRADA"),
            entrada, simular, panelRecorrido,
            new Label("RESULTADO DE LA SIMULACIÓN"), salida
        );
        simulacion.setPadding(new Insets(10));
        simulacion.setStyle(
            "-fx-background-color:#182438;-fx-background-radius:10;"
        );

        n3.setOnAction(e -> seleccionar("3", mensaje, entrada, diagrama, vistaPila));
        n2.setOnAction(e -> seleccionar("2", mensaje, entrada, diagrama, vistaPila));
        n1.setOnAction(e -> seleccionar("1", mensaje, entrada, diagrama, vistaPila));
        n0.setOnAction(e -> seleccionar("0", mensaje, entrada, diagrama, vistaPila));
        simular.setOnAction(e -> preparar(entrada.getText().trim()));

        iniciar.setOnAction(e -> {
            if (posicion < totalPasos()) {
                animacion.stop();
                animacion = new Timeline(
                    new KeyFrame(Duration.millis(900), ev -> mostrarPaso())
                );
                animacion.setCycleCount(Animation.INDEFINITE);
                animacion.play();
            }
        });

        siguiente.setOnAction(e -> {
            animacion.stop();
            mostrarPaso();
        });

        reiniciar.setOnAction(e -> {
            animacion.stop();
            preparar(cadena);
        });

        VBox panel = new VBox(10, cabecera, tarjetas, simulacion);
        panel.setPadding(new Insets(14));
        panel.setStyle("-fx-background-color:#101827;");
        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background:#101827;");
        ventana.setScene(new Scene(scroll, 600, 580));
        ventana.setTitle("Simulador - Jerarquía de Chomsky");
        ventana.setMinWidth(580);
        ventana.setMinHeight(560);
        ventana.show();
    }

    private void seleccionar(String n, Label mensaje, TextField entrada,
                             Pane diagrama, VBox vistaPila) {
        animacion.stop();
        nivel = n;
        String[] mensajes = {
            "Nivel 0 | Máquina de Turing: suma unaria.",
            "Nivel 1 | Lenguaje a^n b^n c^n.",
            "Nivel 2 | Lenguaje a^n b^n.",
            "Nivel 3 | Cadenas que terminan en a."
        };
        String[] ejemplos = {"11+1", "aabbcc", "aabb", "aba"};
        int i = Integer.parseInt(n);
        mensaje.setText(mensajes[i]);
        entrada.setPromptText("Ejemplo: " + ejemplos[i]);
        diagrama.setVisible(n.equals("3"));
        diagrama.setManaged(n.equals("3"));
        vistaPila.setVisible(n.equals("2"));
        vistaPila.setManaged(n.equals("2"));
        vistaCinta.setVisible(n.equals("1") || n.equals("0"));
        vistaCinta.setManaged(n.equals("1") || n.equals("0"));
        simbolos.setVisible(n.equals("3") || n.equals("2"));
        simbolos.setManaged(n.equals("3") || n.equals("2"));
        panelRecorrido.setVisible(false);
        panelRecorrido.setManaged(false);
        salida.clear();
    }

    private void preparar(String texto) {
        animacion.stop();
        cadena = texto;
        posicion = 0;
        cabezaActual = 0;
        pasos.clear();
        etiquetas.clear();
        cintas.clear();
        cabezas.clear();
        mensajesCinta.clear();
        estadosCinta.clear();
        simbolos.getChildren().clear();
        pilaVisual.getChildren().clear();
        vistaCinta.getChildren().removeIf(n -> n instanceof HBox);
        salida.clear();

        if (cadena.isEmpty()) {
            salida.setText("Escribe una cadena.");
            return;
        }

        if (nivel.equals("3")) {
            if (!cadena.matches("[ab]+")) {
                salida.setText("ERROR: Usa solamente a y b.");
                return;
            }
            pasos.addAll(
                controlador.ejecutarNivel3(cadena).lines().toList()
            );
        } else if (nivel.equals("2")) {
            if (!cadena.matches("a*b*")) {
                salida.setText("ERROR: Primero a y después b.");
                return;
            }
            pasos = controlador.ejecutarPasosNivel2(cadena);
        } else if (nivel.equals("1")) {
            if (!cadena.matches("a+b+c+")) {
                salida.setText("ERROR: Escribe primero a, después b y finalmente c.");
                return;
            }
            prepararNivel1();
        } else {
            if (!cadena.matches("1+\\+1+")) {
                salida.setText("ERROR: Escribe una suma unaria, por ejemplo 11+1.");
                return;
            }
            prepararNivel0();
        }

        for (char c : cadena.toCharArray()) {
            Label l = new Label(String.valueOf(c));
            l.setMinSize(32, 32);
            l.setAlignment(Pos.CENTER);
            l.setStyle(estiloSimbolo(false));
            etiquetas.add(l);
            simbolos.getChildren().add(l);
        }

        panelRecorrido.setVisible(true);
        panelRecorrido.setManaged(true);
        textoEstado.setText(
            nivel.equals("3")
                ? "Estado actual: q0"
                : nivel.equals("0")
                    ? "Estado actual: q0"
                    : "Pila inicial: vacía"
        );
        progreso.setText("Símbolos procesados: 0");
        resultadoVisual.setText("Simulación preparada.");
        actualizarEstados("q0");

        if (nivel.equals("1") || nivel.equals("0")) {
            mostrarCinta(cintas.get(0), cabezas.get(0));
            cabezal.setText("Cabezal: listo para iniciar");
        }
    }

    // NIVEL 1: CINTA ACOTADA
    private void prepararNivel1() {
        char[] cinta = ("⊢" + cadena + "⊣").toCharArray();
        cabezaActual = 1;
        aceptaCinta = true;
        agregarPaso(cinta, cabezaActual, "Cinta inicial con delimitadores.");

        while (true) {
            int a = buscarDesde(cinta, 'a', 1);
            if (a == -1) {
                break;
            }
            moverCabezal(cinta, a);
            cinta[a] = 'X';
            agregarPaso(cinta, a, "Se marca a como X.");
            int b = buscarDesde(cinta, 'b', a + 1);
            if (b == -1) {
                aceptaCinta = false;
                break;
            }
            moverCabezal(cinta, b);
            cinta[b] = 'Y';
            agregarPaso(cinta, b, "Se marca b como Y.");
            int c = buscarDesde(cinta, 'c', b + 1);
            if (c == -1) {
                aceptaCinta = false;
                break;
            }
            moverCabezal(cinta, c);
            cinta[c] = 'Z';
            agregarPaso(cinta, c, "Se marca c como Z.");
            moverCabezal(cinta, 1);
            agregarPaso(cinta, 1, "Regresa al inicio para repetir.");
        }

        for (char simbolo : cinta) {
            if (simbolo != 'X' && simbolo != 'Y' &&
                simbolo != 'Z' && simbolo != '⊢' &&
                simbolo != '⊣') {
                aceptaCinta = false;
            }
        }

        resultadoCinta = aceptaCinta
            ? "CADENA ACEPTADA"
            : "CADENA RECHAZADA";
        agregarPaso(cinta, cabezaActual, "Verificación final.");
    }

    // NIVEL 0: MÁQUINA DE TURING
    private void prepararNivel0() {
        char[] cinta = (cadena + "_").toCharArray();
        int mas = cadena.indexOf('+');
        cabezaActual = 0;

        // q0: buscar el signo +
        for (int i = 0; i <= mas; i++) {
            moverCabezal(cinta, i, "q0");
            agregarPaso(
                cinta, i,
                "q0: Lee el símbolo " + cinta[i] + ".",
                "q0"
            );
        }

        // q1: borrar el signo +
        cinta[mas] = '_';
        agregarPaso(
            cinta, mas,
            "q1: Borra el signo + y escribe un blanco.",
            "q1"
        );

        // q1, q2 y q3: desplazar los unos
        for (int i = mas + 1; i < cadena.length(); i++) {
            moverCabezal(cinta, i, "q1");
            agregarPaso(
                cinta, i,
                "q1: Lee un 1 del segundo número.",
                "q1"
            );
            cinta[i] = '_';
            agregarPaso(
                cinta, i,
                "q2: Borra el 1 de su posición.",
                "q2"
            );
            cinta[i - 1] = '1';
            agregarPaso(
                cinta, i - 1,
                "q2: Escribe el 1 en la casilla anterior.",
                "q2"
            );
            agregarPaso(
                cinta, i - 1,
                "q3: Continúa con el siguiente símbolo.",
                "q3"
            );
        }

        // qf: terminar
        cabezaActual = cadena.length();
        agregarPaso(
            cinta, cabezaActual,
            "qf: La suma ha terminado.",
            "qf"
        );
        aceptaCinta = true;
        String resultado = new String(cinta).replace("_", "");
        resultadoCinta = "SUMA COMPLETADA: " + resultado;
    }

    // MOVIMIENTO DEL CABEZAL
    private void moverCabezal(char[] cinta, int destino) {
        moverCabezal(cinta, destino, "Cinta acotada");
    }

    private void moverCabezal(
            char[] cinta, int destino, String estado) {
        int direccion = destino >= cabezaActual ? 1 : -1;
        while (cabezaActual != destino) {
            cabezaActual += direccion;
            agregarPaso(
                cinta,
                cabezaActual,
                "El cabezal se mueve.",
                estado
            );
        }
    }

    // GUARDAR PASOS DE LA CINTA
    private void agregarPaso(char[] cinta, int cabeza, String mensaje) {
        agregarPaso(cinta, cabeza, mensaje, "Cinta acotada");
    }

    private void agregarPaso(
            char[] cinta, int cabeza,
            String mensaje, String estado) {
        cintas.add(new String(cinta));
        cabezas.add(cabeza);
        mensajesCinta.add(mensaje);
        estadosCinta.add(estado);
    }

    private int buscarDesde(char[] cinta, char simbolo, int inicio) {
        for (int i = inicio; i < cinta.length - 1; i++) {
            if (cinta[i] == simbolo) {
                return i;
            }
        }
        return -1;
    }

    private int totalPasos() {
        if (nivel.equals("1") || nivel.equals("0")) {
            return cintas.size();
        }
        return cadena.length();
    }

    // MOSTRAR LOS PASOS
    private void mostrarPaso() {
        if (nivel.equals("1") || nivel.equals("0")) {
            if (posicion >= cintas.size()) {
                animacion.stop();
                return;
            }

            mostrarCinta(cintas.get(posicion), cabezas.get(posicion));
            cabezal.setText(
                "Cabezal en posición: " + cabezas.get(posicion)
            );

            if (nivel.equals("0")) {
                textoEstado.setText(
                    "Estado actual: " + estadosCinta.get(posicion)
                );
            } else {
                textoEstado.setText("Estado actual: Cinta acotada");
            }

            resultadoVisual.setText(mensajesCinta.get(posicion));
            progreso.setText(
                "Pasos ejecutados: " + (posicion + 1) +
                " de " + cintas.size()
            );
            posicion++;

            if (posicion == cintas.size()) {
                animacion.stop();
                salida.setText(resultadoCinta);
                resultadoVisual.setText("Simulación terminada.");
            }
            return;
        }

        if (posicion >= cadena.length()) {
            animacion.stop();
            return;
        }

        char c = cadena.charAt(posicion);
        etiquetas.forEach(
            l -> l.setStyle(estiloSimbolo(false))
        );
        etiquetas.get(posicion).setStyle(estiloSimbolo(true));

        if (nivel.equals("2")) {
            if (c == 'a') {
                Label bloque = new Label("A");
                bloque.setMinSize(42, 28);
                bloque.setAlignment(Pos.CENTER);
                bloque.setStyle(
                    "-fx-background-color:#7c3aed;" +
                    "-fx-text-fill:white;-fx-font-weight:bold;" +
                    "-fx-background-radius:5;"
                );
                pilaVisual.getChildren().add(0, bloque);
                resultadoVisual.setText("Se apila A.");
            } else if (!pilaVisual.getChildren().isEmpty()) {
                pilaVisual.getChildren().remove(0);
                resultadoVisual.setText("Se desapila A.");
            } else {
                resultadoVisual.setText("No hay A para retirar.");
                salida.setText("CADENA RECHAZADA");
                animacion.stop();
                return;
            }

            textoEstado.setText(
                "Pila: " + pilaVisual.getChildren().size() + " elementos"
            );
        } else {
            String anterior = textoEstado.getText().endsWith("q1")
                ? "q1" : "q0";
            String nuevo = c == 'a' ? "q1" : "q0";
            actualizarEstados(nuevo);
            textoEstado.setText("Estado actual: " + nuevo);
            flechaA.setStroke(Color.web("#60a5fa"));
            flechaB.setStroke(Color.web("#c4b5fd"));
            bucleQ0.setStroke(Color.LIGHTGRAY);
            bucleQ1.setStroke(Color.LIGHTGRAY);

            if (anterior.equals("q0") && c == 'a') {
                flechaA.setStroke(Color.YELLOW);
            } else if (anterior.equals("q1") && c == 'b') {
                flechaB.setStroke(Color.YELLOW);
            } else if (anterior.equals("q0")) {
                bucleQ0.setStroke(Color.YELLOW);
            } else {
                bucleQ1.setStroke(Color.YELLOW);
            }

            resultadoVisual.setText(
                "Lee " + c + ": " + anterior + " → " + nuevo
            );
        }

        posicion++;
        progreso.setText(
            "Símbolos procesados: " + posicion +
            " de " + cadena.length()
        );

        if (posicion == cadena.length()) {
            animacion.stop();

            if (nivel.equals("2")) {
                boolean acepta = pilaVisual.getChildren().isEmpty();
                salida.setText(
                    acepta ? "CADENA ACEPTADA" : "CADENA RECHAZADA"
                );
                resultadoVisual.setText(
                    acepta ? "La pila terminó vacía."
                           : "Quedaron símbolos en la pila."
                );
            } else {
                boolean acepta = textoEstado.getText().endsWith("q1");
                salida.setText(
                    acepta ? "CADENA ACEPTADA" : "CADENA RECHAZADA"
                );
                resultadoVisual.setText(
                    acepta ? "Estado final q1." : "Estado final q0."
                );
            }
        }
    }
    // DIBUJAR CINTA
    private void mostrarCinta(String cinta, int cabeza) {
        vistaCinta.getChildren().removeIf(n -> n instanceof HBox);
        HBox casillas = new HBox(4);
        casillas.setAlignment(Pos.CENTER);

        for (int i = 0; i < cinta.length(); i++) {
            Label celda = new Label(String.valueOf(cinta.charAt(i)));
            celda.setMinSize(30, 34);
            celda.setAlignment(Pos.CENTER);

            if (i == cabeza) {
                celda.setStyle(
                    "-fx-background-color:#7c3aed;" +
                    "-fx-text-fill:white;-fx-font-size:16px;" +
                    "-fx-font-weight:bold;-fx-background-radius:5;" +
                    "-fx-border-color:#facc15;-fx-border-width:2;"
                );
            } else {
                celda.setStyle(estiloSimbolo(false));
            }
            casillas.getChildren().add(celda);
        }
        vistaCinta.getChildren().add(casillas);
    }

    private void actualizarEstados(String estado) {
        q0.setFill(
            estado.equals("q0")
                ? Color.web("#7c3aed")
                : Color.web("#263449")
        );
        q1.setFill(
            estado.equals("q1")
                ? Color.web("#7c3aed")
                : Color.web("#263449")
        );
    }

    // DIAGRAMA DEL NIVEL 3
    private Pane crearDiagrama() {
        Pane p = new Pane();
        p.setPrefSize(340, 175);
        p.setMinSize(340, 175);
        p.setMaxSize(340, 175);
        Line inicial = new Line(5, 90, 52, 90);
        inicial.setStroke(Color.LIGHTGRAY);
        q0 = new Circle(82, 90, 27, Color.web("#263449"));
        q0.setStroke(Color.web("#60a5fa"));
        q0.setStrokeWidth(2);
        q1 = new Circle(258, 90, 29, Color.web("#263449"));
        q1.setStroke(Color.web("#6ee7b7"));
        q1.setStrokeWidth(2);
        Circle doble = new Circle(258, 90, 24);
        doble.setFill(Color.TRANSPARENT);
        doble.setStroke(Color.web("#6ee7b7"));
        flechaA = new QuadCurve(109, 78, 170, 24, 230, 78);
        flechaB = new QuadCurve(230, 105, 170, 157, 109, 105);
        bucleQ0 = new QuadCurve(60, 66, 82, 15, 104, 66);
        bucleQ1 = new QuadCurve(236, 64, 258, 13, 280, 64);

        for (QuadCurve f : List.of(
                flechaA, flechaB, bucleQ0, bucleQ1)) {
            f.setFill(Color.TRANSPARENT);
            f.setStroke(Color.LIGHTGRAY);
            f.setStrokeWidth(2);
        }

        javafx.scene.text.Text t0 =
            new javafx.scene.text.Text(70, 95, "q0");
        javafx.scene.text.Text t1 =
            new javafx.scene.text.Text(246, 95, "q1");
        javafx.scene.text.Text ta =
            new javafx.scene.text.Text(166, 35, "a");
        javafx.scene.text.Text tb =
            new javafx.scene.text.Text(166, 153, "b");

        for (var t : List.of(t0, t1, ta, tb)) {
            t.setFill(Color.WHITE);
        }

        p.getChildren().addAll(
            inicial, flechaA, flechaB, bucleQ0, bucleQ1,
            q0, q1, doble, t0, t1, ta, tb
        );
        return p;
    }

    // TARJETAS
    private Button tarjeta(
            String nivel, String nombre,
            String modelo, String color) {
        Label numero = new Label(nivel);
        numero.setStyle(
            "-fx-text-fill:" + color +
            ";-fx-font-size:17px;-fx-font-weight:bold;"
        );
        Label descripcion = new Label(nombre);
        descripcion.setStyle(
            "-fx-text-fill:white;-fx-font-weight:bold;"
        );
        Label detalle = new Label(modelo);
        detalle.setStyle("-fx-text-fill:#aab4c5;");
        VBox contenido = new VBox(2, numero, descripcion, detalle);
        contenido.setPadding(new Insets(7));
        Button b = new Button();
        b.setGraphic(contenido);
        b.setPrefSize(250, 65);
        b.setStyle(
            "-fx-background-color:#202b40;" +
            "-fx-background-radius:10;" +
            "-fx-border-color:#34435b;" +
            "-fx-border-radius:10;"
        );
        return b;
    }

    private String estiloSimbolo(boolean activo) {
        return activo
            ? "-fx-background-color:#7c3aed;-fx-text-fill:white;" +
              "-fx-font-size:17px;-fx-font-weight:bold;-fx-background-radius:7;"
            : "-fx-background-color:#263449;-fx-text-fill:#dbeafe;" +
              "-fx-font-size:17px;-fx-background-radius:7;";
    }

    public static void main(String[] args) {
        launch(args);
    }
}