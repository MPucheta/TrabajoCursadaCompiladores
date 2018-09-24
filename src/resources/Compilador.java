package resources;

import java.io.IOException;
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
			fuente = new ArchivoTexto("CasosDePrueba\\TP1_11.txt");
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
		
		int finToken=0;
		/*
		Parser parser= new Parser(AL);
		parser.run();
		System.out.println("detectado \n" + parser.estructurasGramaticalesDetectadas);*/
		
		while (!AL.finDePrograma()) {
			finToken=AL.yylex();
			if(finToken!=0)
				System.out.println(Token.tipoToken(finToken) + "  "+AL.nroLinea);
			
		}
		
		System.out.println("\nERRORES LEXICOS: \n");
		System.out.println(AL.erroresLexicos());
		//TestCompilador.imprimirTablaSimbolos(tablaSimbolos);
		
	}

}
