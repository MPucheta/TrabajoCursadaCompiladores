package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Compilador {

	
	//basicamente este es el entry point del programa. Por ahora es testeo pero despues 
	//va a crear una instancia de analizador sintactico que se va a ir encargando de pedir tokens y demas
	
	
	private static Hashtable<String , Atributos> tablaSimbolos = new Hashtable<>(); 
	private static AnalizadorLexico AL;
	
	
	
	
	public static void main(String[] args) {
		
		if (args.length != 1){
			System.out.println("Uso del programa: compilador.jar <NOMBRE_ARCHIVO_ENTRADA>");
			System.exit(0);
		}
		
		ArchivoTexto fuente=null;
		Arbol raizArbol=null;
		try {


			
			fuente = new ArchivoTexto(args[0]); //o TP4_Custom.txt

		} catch (IOException e) {
			System.out.println("Error al abrir el archivo.");
			e.printStackTrace();
		}
		String programa= fuente.leerArchivo();

		//ESTO ES MUY IMPORTANTE, windows introduce \r\n al momento del linebreak
		//lo que puede introducir problemas ya que uno programa para el salto de linea general \n
		programa = programa.replaceAll("\r\n" , "\n"); 
		programa+=" "; //esto es un arreglo medio trucho. 
		//La cuestion es que cuando alguien escribe un programa valido el ultimo token puede no ser detectado si no agrega un salto de linea
		//esto es basicamente porque el grafo espera algo distinto a una letra o '_' para terminar el token de palabra reservada
		//es decir, nunca se devuelve un token (o se devuelve 0) ya que el ultimo token valido se detecta como si estuviera
		// cortado por la mitad
		
		AL = new AnalizadorLexico(programa, tablaSimbolos);

		List<String> reporte = new ArrayList<>();
		Parser parser= new Parser(AL, tablaSimbolos,raizArbol);
		parser.run();
		//System.out.println("detectado \n" + parser.estructurasGramaticalesDetectadas);
		
		//reporte += TestCompilador.stringTablaSimbolos(tablaSimbolos);
		reporte.add("\n\n******************** ERRORES ********************\n\n");
		reporte.add(parser.AL.erroresLexicos());
		reporte.addAll(parser.getErroresGenerales());
		reporte.addAll(parser.getErroresDetallados());
		reporte.addAll(parser.getErroresChequeoSemantico());
		
		boolean errores = false;
		
		
		//TestCompilador.imprimirTablaSimbolos(tablaSimbolos);
		
		if (!parser.getErroresDetallados().isEmpty() || !parser.getErroresChequeoSemantico().isEmpty()){
			
			errores = true;
		}
		
		for (String s: reporte)
			System.out.print(s);
		
		System.out.println("*************************************************\n\n");
		/*
		try {
			ArchivoTexto.escribirEnDisco("tokensLeidos.txt",parser.getTokensLeidos());
			ArchivoTexto.escribirEnDisco("erroresEnParsing.txt",parser.getErroresDetallados());
		} catch (IOException e) {
			
			e.printStackTrace();
		}*/
		
		
		
	//	System.out.println("errores \n" + parser.getErroresDetallados());
		if (!errores){
		Hashtable<String, Arbol> arboles = parser.getArbolesSintacticos();
		
		
		reporte.add("\n\n*********************** ARBOLES SINTACTICOS *********************** \n\n");
		for (String ambito: arboles.keySet()){
			System.out.println("Arbol: " + ambito );
			System.out.println(arboles.get(ambito).imprimir("", ""));
			reporte.add("Arbol: " + ambito+ "\n\n");
			reporte.add(arboles.get(ambito).imprimir("", "") + "\n\n");
		}
		
		
		
		GeneradorCodigo g= new GeneradorCodigo(tablaSimbolos);
		
		List<String> datos;
		List<String> header =g.generarHeader();
		List<String> includes = g.generarIncludes();
		
		List<String> asm=header;
		
		
		List<String> codigo=new ArrayList<>();
		List<String> preCodigo=new ArrayList<>();
		preCodigo.add("\n"+".code"+"\n");
		Hashtable<String, Arbol> claveArboles=parser.getArbolesSintacticos();
		for(String clave: claveArboles.keySet() ){
			if(clave.equals("main")) {
				codigo.addAll(g.generarCodigo(claveArboles.get(clave)));		
			}else {
				preCodigo.addAll(g.generarPreStartCode(claveArboles.get(clave), clave));
			}
			
			
			
		}
		for(String s:preCodigo) {
			System.out.print(s);
		}
		for(String s:codigo) {
			System.out.print(s);
		}
		
		List<String> fin=g.generarFin();
		
		datos=g.generarSeccionDatos(); //se vuelven a generar los datos con la TS actualizada
		
		asm.addAll(includes);
		asm.addAll(datos);
		asm.addAll(preCodigo);
		asm.addAll(codigo);
		asm.addAll(fin);
		try {
			ArchivoTexto.escribirEnDisco("salida_assembler.asm", asm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\n\nCompilacion exitosa. El codigo assembler de salida se encuentra en el archivo 'salida_assembler.asm'.");
		
		
		}
		
		
		reporte.add("\n\n****************** TABLA DE SIMBOLOS ******************\n\n");
		reporte.add(TestCompilador.stringTablaSimbolos(tablaSimbolos));
		if (!errores)
			reporte.add("\n\n\nCompilacion exitosa. El codigo assembler de salida se encuentra en el archivo 'salida_assembler.asm'.");
		else{
			reporte.add("\n\nHubo errores durante la compilacion. Codigo no compilado\n\n");
			System.out.println("\n\n\n\nHubo errores durante la compilacion. Codigo no compilado");
		}
		try {
			ArchivoTexto.escribirEnDisco("reportes.txt", reporte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\nTabla de simbolos, Arboles sintacticos, reportes de errores se encuentran en el archivo 'reportes.txt'.");
		
		
		
	}

}





