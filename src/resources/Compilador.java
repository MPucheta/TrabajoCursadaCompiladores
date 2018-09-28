package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class Compilador {

	
	//basicamente este es el entry point del programa. Por ahora es testeo pero despues 
	//va a crear una instancia de analizador sintactico que se va a ir encargando de pedir tokens y demas
	
	
	private static Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
	private static AnalizadorLexico AL;
	
	
	
	
	public static void main(String[] args) {
		ArchivoTexto fuente=null;
		
		try {

			fuente = new ArchivoTexto("CasosDePruebaTP2\\test.txt");

		} catch (IOException e) {
			System.out.println("Error al abrir el archivo.");
			e.printStackTrace();
		}
		String programa= fuente.leerArchivo();

		//ESTO ES MUY IMPORTANTE, windows introduce \r\n al momento del linebreak
		//lo que puede introducir problemas ya que uno programa para el salto de linea general \n
		programa = programa.replaceAll("\r\n" , "\n"); 
		programa+="\n"; //esto es un arreglo medio trucho. 
		//La cuestion es que cuando alguien escribe un programa valido el ultimo token puede no ser detectado si no agrega un salto de linea
		//esto es basicamente porque el grafo espera algo distinto a una letra o '_' para terminar el token de palabra reservada
		//es decir, nunca se devuelve un token (o se devuelve 0) ya que el ultimo token valido se detecta como si estuviera
		// cortado por la mitad
		
		AL = new AnalizadorLexico(programa, tablaSimbolos);

		
		Parser parser= new Parser(AL, tablaSimbolos);
		parser.run();
		System.out.println("detectado \n" + parser.estructurasGramaticalesDetectadas);
		
		
		
		TestCompilador.imprimirTablaSimbolos(tablaSimbolos);
		try {
			ArchivoTexto.escribirEnDisco("tokensLeidos.txt",parser.getTokensLeidos());
			ArchivoTexto.escribirEnDisco("erroresEnParsing.txt",parser.getErroresDetallados());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		System.out.println("errores \n" + parser.getErroresDetallados());
		
		

		
	}

}
