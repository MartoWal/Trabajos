package Final;

import javax.swing.JOptionPane;

public class juego_quien_es_quien_ {

	public class QuienEsQuienJOption {
	    public static void main(String[] args) {
	        String[] personajes = {
	            "Ines", "Juan", "Maria", "Luca", 
	            "Luciana", "Martin", "Carla", "Fede"
	        };

	        String[] preguntas = {
	            "¿Es mujer?",           
	            "¿Tiene ojos azules?",  
	            "¿Usa anteojos?",       
	            "¿Tiene pelo rubio?",   
	            "¿Tiene barba?",        
	            "¿Lleva gorra?"         
	        };

	        boolean[][] datos = {
	            {true,  true,  false, true,  false, false}, // Ines
	            {false, false, true,  false, true,  false}, // Juan
	            {true,  false, true,  false, false, true }, // Maria
	            {false, true,  false, true,  true,  true }, // Luca
	            {true,  false, false, false, false, false}, // Luciana
	            {false, true,  true,  false, true,  false}, // Martin
	            {true,  true,  true,  true,  false, false}, // Carla
	            {false, false, false, false, false, true }  // Fede
	        };

	        int personajeElegido = (int)(Math.random() * personajes.length);
	        boolean[] posibles = new boolean[personajes.length];
	        for (int i = 0; i < posibles.length; i++) {
	            posibles[i] = true;
	        }

	        int intentos = 0;
	        boolean adivinado = false;

	        JOptionPane.showMessageDialog(null, "¡Bienvenido al juego ¿Quien es quien?!\n Tenes que adivinar que personaje eligio la computadora.");

	        while (intentos < 7 && !adivinado) {
	            // Mostrar menú de preguntas
	            String menu = "Elegi una pregunta:\n";
	            for (int i = 0; i < preguntas.length; i++) {
	                menu += (i + 1) + ". " + preguntas[i] + "\n";
	            }

	            String entrada = JOptionPane.showInputDialog(menu);
	            if (entrada == null) break; // cancelar

	            int opcion = -1;
	            try {
	                opcion = Integer.parseInt(entrada) - 1;
	            } catch (Exception e) {
	                JOptionPane.showMessageDialog(null, " Ingresa un numero valido.");
	                continue;
	            }

	            if (opcion < 0 || opcion >= preguntas.length) {
	                JOptionPane.showMessageDialog(null, " Opcion invalida. Elegi del 1 al " + preguntas.length);
	                continue;
	            }

	            boolean respuesta = datos[personajeElegido][opcion];
	            JOptionPane.showMessageDialog(null, " Respuesta: " + (respuesta ? "Si" : "No"));

	            // Filtrar personajes
	            for (int i = 0; i < personajes.length; i++) {
	                if (posibles[i] && datos[i][opcion] != respuesta) {
	                    posibles[i] = false;
	                }
	            }

	            // Mostrar personajes posibles
	            String posiblesTexto = " Personajes que aun pueden ser:\n";
	            boolean hayOpciones = false;
	            for (int i = 0; i < personajes.length; i++) {
	                if (posibles[i]) {
	                    posiblesTexto += "- " + personajes[i] + "\n";
	                    hayOpciones = true;
	                }
	            }
	            if (!hayOpciones) {
	                posiblesTexto += "X Ningun personaje coincide con las respuestas.";
	            }
	            JOptionPane.showMessageDialog(null, posiblesTexto);

	            intentos++;

	            // Preguntar si queres adivinar
	            String respuestaAdivinar = JOptionPane.showInputDialog("¿Queres intentar adivinar quien es? (s/n)");
	            if (respuestaAdivinar == null) break;
	            if (respuestaAdivinar.toLowerCase().startsWith("s")) {
	                String intentoNombre = JOptionPane.showInputDialog("¿Quien crees que es?");
	                if (intentoNombre == null) break;

	                if (intentoNombre.equalsIgnoreCase(personajes[personajeElegido])) {
	                    JOptionPane.showMessageDialog(null, " ¡Adivinaste! Era " + personajes[personajeElegido]);
	                    adivinado = true;
	                } else {
	                    JOptionPane.showMessageDialog(null, "X No es " + intentoNombre + ".");
	                    intentos++;
	                }
	            }
	        }

	        if (!adivinado) {
	            JOptionPane.showMessageDialog(null, " Te quedaste sin intentos. El personaje era: " + personajes[personajeElegido]);
	        }

	        // Preguntar si quiere volver a jugar
	        String jugarDeNuevo = JOptionPane.showInputDialog("¿Queres jugar otra vez? (s/n)");
	        if (jugarDeNuevo != null && jugarDeNuevo.toLowerCase().startsWith("s")) {
	            main(null);
	        } else {
	            JOptionPane.showMessageDialog(null, "¡Gracias por jugar!");
	        }

	}

}}
