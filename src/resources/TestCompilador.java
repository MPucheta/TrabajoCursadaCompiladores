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
	
	public static String stringTablaSimbolos(Hashtable<String, List<Object>> TS){
		String salida = "";
		for (String s: TS.keySet()) {
			List<Object> atributos = TS.get(s);
			String atts="";
			for(Object o: atributos)
				atts = atts + o.toString() + " | ";
			salida += s + " --> " + atts + "\n";
			
		}
		return salida;
	}
	
	private static void initCasosPrueba() {
		casosPruebaTP1=new ArrayList<>();
		casosPruebaTP2=new ArrayList<>();
		casosPruebaTP2conErrores=new ArrayList<>();
		int[] casosCorrespondientes = {1,2,4,5,6,7,8,9,10,11};
		for(int i=0;i<casosCorrespondientes.length;i++)  { //casos de prueba lexico. Son 11
			casosPruebaTP1.add("CasosDePrueba\\TP1_"+casosCorrespondientes[i]+".txt");
			
		}
		int[] casosCorrespondientes2 = {1,2,3,4,5,6,7,8,9,12,13,15,16,18};
		for(int i=0;i<casosCorrespondientes2.length;i++) {
			casosPruebaTP2.add("CasosDePruebaTP2\\TP2_"+casosCorrespondientes2[i]+".txt");
			casosPruebaTP2conErrores.add("CasosDePruebaTP2\\TP2_"+casosCorrespondientes2[i]+"_errores.txt");
		}
		
	}
	
	public static void main (String[] args) {
		ArchivoTexto fuente=null;
		if (args.length> 0){
			try {
				fuente = new ArchivoTexto(args[0]);
			} catch (IOException e) {
				System.out.println("Error al abrir el archivo " + args[0]);
				e.printStackTrace();
			}
			List<String> salidaArchivo=new ArrayList<>();
			salidaArchivo.add("**************************************************************************************************************\n");
			salidaArchivo.add(" SALIDA PARA " + args[0] + "\n \n \n");
			
			Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
			AnalizadorLexico AL;
			String programa= fuente.leerArchivo();

			//windows introduce \r\n al momento del linebreak
			//lo que puede introducir problemas ya que uno programa para el salto de linea general \n
			programa = programa.replaceAll("\r\n" , "\n"); 
			programa+=" ";
			
			AL = new AnalizadorLexico(programa, tablaSimbolos);
			
			Arbol raizArbol=null;
			Parser parser= new Parser(AL, tablaSimbolos, raizArbol);
			parser.run();
			
			salidaArchivo.add("*****\nTokens detectados por el Analizador Lexico: \n\n");
			salidaArchivo.addAll(parser.getTokensLeidos());
			salidaArchivo.add("\n*****\nErrores Lexicos: \n\n");
			salidaArchivo.add(AL.erroresLexicos());
			salidaArchivo.add("\n*****\nEstructuras Gramaticales Detectadas por el Analizador Sintactico: \n\n");
			salidaArchivo.addAll(parser.getEstructurasGramaticalesDetectadas());
			salidaArchivo.add("\n\n*****\nErrores de parsing generales: \n\n");
			salidaArchivo.addAll(parser.getErroresGenerales());
			salidaArchivo.add("\n\n*****\nErrores de parsing identificados: \n\n");
			salidaArchivo.addAll(parser.getErroresDetallados());
			salidaArchivo.add("\n\n*****\nTabla de simbolos: \n\n");
			salidaArchivo.add(stringTablaSimbolos(tablaSimbolos));
		
		try {
			ArchivoTexto.escribirEnDisco("salida_" + args[0], salidaArchivo);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		}
		else{
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
				salidaTP1.add("\n\n\n**************************************************************************************************************\n");
				salidaTP1.add(" SALIDA PARA " + casosPruebaTP1.get(i) + "\n \n \n");
				salidaTP1.add("*****\nTokens detectados por el Analizador Lexico:\n\n");
				Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
				AnalizadorLexico AL;
				String programa= fuente.leerArchivo();
	
				//windows introduce \r\n al momento del linebreak
				//lo que puede introducir problemas ya que uno programa para el salto de linea general \n
				programa = programa.replaceAll("\r\n" , "\n"); 
				programa+=" "; //esto es un arreglo medio trucho. 
				//La cuestion es que cuando alguien escribe un programa valido el ultimo token puede no ser detectado si no agrega un salto de linea
				//esto es basicamente porque el grafo espera algo distinto a una letra o '_' para terminar el token de palabra reservada
				//es decir, nunca se devuelve un token (o se devuelve 0) ya que el ultimo token valido se detecta como si estuviera
				// cortado por la mitad
				
				AL = new AnalizadorLexico(programa, tablaSimbolos);
				
				
				int nroToken=-1;
				String s;
				String tipo;
				while(nroToken!=0) {
					Token token = AL.getToken();
					nroToken = token.tipoDeToken;
					tipo = Token.tipoToken(nroToken);
					
					
					s="Linea: " + AL.nroLinea + ". Detectado token '" + nroToken + "' identificado como " + tipo + "\n";
					salidaTP1.add(s);
					
				}
				
				salidaTP1.add("\n\n*****\nErrores Lexicos:\n\n");
				salidaTP1.add(AL.erroresLexicos());
				
				salidaTP1.add("\n\n*****\nTabla de simbolos:\n\n");
				salidaTP1.add(stringTablaSimbolos(tablaSimbolos));
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
				salidaTP2.add("\n\n\n**************************************************************************************************************\n");
				salidaTP2.add(" SALIDA PARA " + casosPruebaTP2.get(i) + "\n \n \n");
				
				
				
				Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
				AnalizadorLexico AL;
				String programa= fuente.leerArchivo();
	
				
				programa = programa.replaceAll("\r\n" , "\n"); 
				programa+=" ";
				
				AL = new AnalizadorLexico(programa, tablaSimbolos);
				
				Arbol raizArbol=null;
				Parser parser= new Parser(AL, tablaSimbolos,raizArbol);
				parser.run();
				
				salidaTP2.add("*****\nTokens detectados por el Analizador Lexico: \n\n");
				salidaTP2.addAll(parser.getTokensLeidos());
				salidaTP2.add("\n*****\nErrores Lexicos: \n\n");
				salidaTP2.add(AL.erroresLexicos());
				salidaTP2.add("\n*****\nEstructuras Gramaticales Detectadas por el Analizador Sintactico: \n\n");
				salidaTP2.addAll(parser.getEstructurasGramaticalesDetectadas());
				salidaTP2.add("\n\n*****\nErrores de parsing generales: \n\n");
				salidaTP2.addAll(parser.getErroresGenerales());
				salidaTP2.add("\n\n*****\nErrores de parsing identificados: \n\n");
				salidaTP2.addAll(parser.getErroresDetallados());
				salidaTP2.add("\n\n*****\nTabla de simbolos: \n\n");
				salidaTP2.add(stringTablaSimbolos(tablaSimbolos));
			}
			
			try {
				ArchivoTexto.escribirEnDisco("SalidaTP2.txt", salidaTP2);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			
			//*****************************CASOSPRUEBATP2 con errores**********************************//
			
			salidaTP2errores.add("EJECUCION DEL ANALIZADOR SINTACTICO SOBRE LOS CASOS DE PRUEBA CON ERRORES ");
			
			
			for(int i=0;i<casosPruebaTP2conErrores.size();i++) { //probando casosPruebTP2
				try {
	
					fuente = new ArchivoTexto(casosPruebaTP2conErrores.get(i));
	
				} catch (IOException e) {
					System.out.println("Error al abrir el archivo.");
					e.printStackTrace();
				}
				salidaTP2errores.add("\n\n\n**************************************************************************************************************\n");
				salidaTP2errores.add(" SALIDA PARA " + casosPruebaTP2conErrores.get(i) + "\n \n \n");
				
				
				
				Hashtable<String , List<Object>> tablaSimbolos = new Hashtable<>(); 
				AnalizadorLexico AL;
				String programa= fuente.leerArchivo();
	
		
				programa = programa.replaceAll("\r\n" , "\n"); 
				programa+=" "; 
				
				AL = new AnalizadorLexico(programa, tablaSimbolos);
				
				Arbol raizArbol=null;
				Parser parser= new Parser(AL, tablaSimbolos, raizArbol);
				parser.run();
				
				salidaTP2errores.add("*****\nTokens detectados por el Analizador Lexico: \n\n");
				salidaTP2errores.addAll(parser.getTokensLeidos());
				salidaTP2errores.add("\n*****\nErrores Lexicos: \n\n");
				salidaTP2errores.add(AL.erroresLexicos());
				salidaTP2errores.add("\n*****\nEstructuras Gramaticales Detectadas por el Analizador Sintactico: \n\n");
				salidaTP2errores.addAll(parser.getEstructurasGramaticalesDetectadas());
				salidaTP2errores.add("\n\n*****\nErrores de parsing generales: \n\n");
				salidaTP2errores.addAll(parser.getErroresGenerales());
				salidaTP2errores.add("\n\n*****\nErrores de parsing identificados: \n\n");
				salidaTP2errores.addAll(parser.getErroresDetallados());
				salidaTP2errores.add("\n\n*****\nTabla de simbolos: \n\n");
				salidaTP2errores.add(stringTablaSimbolos(tablaSimbolos));
				
			}
			
			try {
				ArchivoTexto.escribirEnDisco("SalidaTP2CasosConErrores.txt", salidaTP2errores);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
	}
}
