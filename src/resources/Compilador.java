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
			fuente = new ArchivoTexto("CasosDePrueba\\TP1_1.txt");
		} catch (IOException e) {
			System.out.println("Error al abrir el archivo.");
			e.printStackTrace();
		}
		String programa= fuente.leerArchivo();

		//ESTO ES MUY IMPORTANTE, windows introduce \r\n al momento del linebreak
		//lo que puede introducir problemas ya que uno programa para el salto de linea general \n
		programa = programa.replaceAll("\r\n" , "\n"); 
		
		//System.out.println(programa);
		AL = new AnalizadorLexico(programa, tablaSimbolos);
		
		int finToken=0;
		
		Parser parser= new Parser(AL);
		parser.run();
		
		/*
		while (!AL.finDePrograma()) {
			finToken=AL.yylex();
			if(finToken!=-1)
				System.out.println(Token.tipoToken(finToken));
		}
		*/
		TestCompilador.imprimirTablaSimbolos(tablaSimbolos);
		
	}

}
