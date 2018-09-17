package resources;

import java.util.Hashtable;
import java.util.List;

public class TestCompilador {
	Compilador compilador;
	
	public TestCompilador(Compilador c){
		this.compilador = c;
	}

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
}
