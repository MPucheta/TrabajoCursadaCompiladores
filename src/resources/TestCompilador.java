package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TestCompilador {
	Compilador compilador;
	
	public TestCompilador(Compilador c){
		this.compilador = c;
	}

	private static List<String> casosPruebaTP1;
	private static List<String> casosPruebaTP2;
	private static List<String> casosPruebaTP2conErrores;
	
	public static void imprimirTablaSimbolos(Hashtable<String, List<Object>> TS) {
		
		System.out.println("\n***TABLA DE SIMBOLOS***\n");
		
		for (String s: TS.keySet()) {
			List<Object> atributos = TS.get(s);
			String atts="";
			for(Object o: atributos)
				atts = atts + o.toString() + " ";
			System.out.println(s + " --> " + atts);
			
		}
	}
	private static void initCasosPrueba() {
		casosPruebaTP1=new ArrayList<>();
		casosPruebaTP2=new ArrayList<>();
		casosPruebaTP2conErrores=new ArrayList<>();
		int[] casosCorrespondientes = {1,2,4,5,6,7,8,9,10,11};
		for(int i=0;i<casosCorrespondientes.length;i++)  { //casos de prueba lexico. Son 11
			casosPruebaTP1.add("CasosDePruebaTP1\\TP1_"+casosCorrespondientes[i]+".txt");
			
		}
		int[] casosCorrespondientes2 = {1,2,3,4,5,6,7,8,9,12,13,15,16,18};
		for(int i=0;i<casosCorrespondientes2.length;i++) {
			casosPruebaTP2.add("CasosDePruebaTP2\\TP2_"+casosCorrespondientes2[i]+".txt");
			casosPruebaTP2conErrores.add("CasosDePruebaTP2\\TP2_"+casosCorrespondientes2[i]+"_errores.txt");
		}
		
	}
	
	public static void main (String[] args) {
		ArchivoTexto fuente=null;
		
		initCasosPrueba();
		List<String> salidaTP1=new ArrayList<>();
		List<String> salidaTP2=new ArrayList<>();
		List<String> salidaTP2errores=new ArrayList<>();
		
		salidaTP1.add("EJECUCION DEL ANALIZADOR LEXICO SOBRE LOS CASOS DE PRUEBA  ");
		
		for(int i=0;i<casosPruebaTP1.size();i++) { //probando casosPruebTP1
			try {

				fuente = new ArchivoTexto(casosPruebaTP1.get(i));

			} catch (IOException e) {
				System.out.println("Error al abrir el archivo.");
				e.printStackTrace();
			}
			salidaTP1.add("\n\n\n*******************************************************\n \n \n");
			salidaTP1.add(" SALIDA PARA " + casosPruebaTP1.get(i) + "\n \n \n");
			Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
			AnalizadorLexico AL;
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
			
			
			int finToken=-1;
			String s;
			String tipo;
			while(finToken!=0) {
				Token token=AL.getToken();
				tipo=Token.tipoToken(finToken);
				finToken=token.tipoDeToken;
				if(finToken==0)
					tipo = "END OF FILE (EOF) ";
				
				s="Linea: " + AL.nroLinea + ". Detecado token " + finToken + " identificado como " + tipo + "\n";
				salidaTP1.add(s);
				
			}
			
			
		}
		try {
			ArchivoTexto.escribirEnDisco("SalidaTP1.txt", salidaTP1);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		//*****************************CASOSPRUEBATP2**********************************//
		
		salidaTP2.add("EJECUCION DEL ANALIZADOR SINTACTICO SOBRE LOS CASOS DE PRUEBA  ");
		
		
		for(int i=0;i<casosPruebaTP2.size();i++) { //probando casosPruebTP2
			try {

				fuente = new ArchivoTexto(casosPruebaTP2.get(i));

			} catch (IOException e) {
				System.out.println("Error al abrir el archivo.");
				e.printStackTrace();
			}
			salidaTP2.add("\n\n\n*******************************************************\n \n \n");
			salidaTP2.add(" SALIDA PARA " + casosPruebaTP2.get(i) + "\n \n \n");
			
			
			
			Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
			AnalizadorLexico AL;
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
			
			salidaTP2.add("Estructuras Gramaticales Detectadas \n\n\n");
			salidaTP2.addAll(parser.getEstructurasGramaticalesDetectadas());
			salidaTP2.add("\n\n\nErrores de parsing generales \n\n\n");
			salidaTP2.addAll(parser.getErroresGenerales());
			salidaTP2.add("\n\n\nErrores de parsing identificados \n\n\n");
			salidaTP2.addAll(parser.getErroresDetallados());
			
		}
		
		try {
			ArchivoTexto.escribirEnDisco("SalidaTP2.txt", salidaTP2);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		//*****************************CASOSPRUEBATP2 con errores**********************************//
		
salidaTP2.add("EJECUCION DEL ANALIZADOR SINTACTICO SOBRE LOS CASOS DE PRUEBA CON ERRORES ");
		
		
		for(int i=0;i<casosPruebaTP2conErrores.size();i++) { //probando casosPruebTP2
			try {

				fuente = new ArchivoTexto(casosPruebaTP2conErrores.get(i));

			} catch (IOException e) {
				System.out.println("Error al abrir el archivo.");
				e.printStackTrace();
			}
			salidaTP2errores.add("\n\n\n*******************************************************\n \n \n");
			salidaTP2errores.add(" SALIDA PARA " + casosPruebaTP2conErrores.get(i) + "\n \n \n");
			
			
			
			Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
			AnalizadorLexico AL;
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
			
			salidaTP2errores.add("Estructuras Gramaticales Detectadas \n\n\n");
			salidaTP2errores.addAll(parser.getEstructurasGramaticalesDetectadas());
			salidaTP2errores.add("\n\n\nErrores de parsing generales \n\n\n");
			salidaTP2errores.addAll(parser.getErroresGenerales());
			salidaTP2errores.add("\n\n\nErrores de parsing identificados \n\n\n");
			salidaTP2errores.addAll(parser.getErroresDetallados());
			
		}
		
		try {
			ArchivoTexto.escribirEnDisco("SalidaTP2CasosConErrores.txt", salidaTP2errores);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}
}
