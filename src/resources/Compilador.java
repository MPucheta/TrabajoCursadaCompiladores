package resources;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;

public class Compilador {

	
	//basicamente este es el entry point del programa. Por ahora es testeo pero despues 
	//va a crear una instancia de analizador sintactico que se va a ir encargando de pedir tokens y demas
	
	private static AnalizadorLexico AL=null;
	public static void imprimirTablaSimbolos() {
			System.out.println("***MOSTRANDO ESTADO DE TABLA DE SIMBOLOS***");
			System.out.println("Nombre --> TipoToken Lexema");
			for(String s:AL.tablaSimbolos.keySet() ) {
				List<Object> atributos=AL.tablaSimbolos.get(s);
				String aux="";
				for(Object o: atributos)
					aux= aux + o.toString() + " ";
				System.out.println(s + " --> " + aux);
				
			}
		
	}
	
	public static void main(String[] args) {
		ArchivoTexto fuente=null;
		try {
			fuente = new ArchivoTexto("CasosDePrueba\\TP1_9.txt");
		} catch (IOException e) {
			System.out.println("error al abrir el archivo");
			e.printStackTrace();
		}
		String programa="";
		Iterator<Character> it= fuente.getIterator();
		
		while(it.hasNext()) {
			
				programa+=it.next();
			
			
			
		}
		//ESTO ES MUY IMPORTANTE, windows introduce \r\n al momento del linebreak
		//lo que puede introducir problemas ya que uno programa para el salto de linea general \n
		programa=programa.replaceAll("\r\n" , "\n"); 
		
		System.out.println(programa);
		AL = new AnalizadorLexico(programa);
		
		int finToken=0;
		int numToken=1;
		while(AL.pos<programa.length()) { //quizas habria que buscar una forma de avisar cuando no hay mas fuente. Quizas un metodo en AL? canReadNextToken()
			
			finToken=AL.yylex();
			if(finToken!=-1)
				System.out.println("Token " + (numToken++) + " " + finToken);
			
		}
		
		imprimirTablaSimbolos();
		
	}

}
