package resources;

import java.util.ArrayList;
import java.util.List;

public class AccionSemantica5 implements AccionSemantica{
	//esta accion semantica se define como:
		/*>Devolver a la entrada lo ultimo leido
		 * >verificar si cte entra en rango definido para ui (enteros sin signo)
		 * 		si no, pisar con maximo valor e informar warning
		 * >verificar si existe en tabla simbolo
		 * 	Si: retornar <cte, puntTS>
		 *  No: dar de alta en TS y retornar <cte,punts>
		 * @see resources.AccionSemantica#ejecutar(resources.AnalizadorLexico, char)
		 */
		private double minValor = 0;
		private double maxValor = 4294967295.0;
		public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
			AL.pos--;
			String cte=AL.buffer; //claveTablaSimbolo
			Double cteClaveTS= new Double(cte);
			double valorCte = cteClaveTS.doubleValue();
			Token devuelto=null;
			
			if (valorCte< minValor || valorCte> maxValor ) { //chequeo de rangos
				valorCte=maxValor; //tecnica de reemplazo
				System.out.println("Warning: Reemplazo de valor en linea: " + AL.nroLinea);
			}
			
			if(!AL.tablaSimbolos.containsKey(cteClaveTS)) { 
				
				
				
				List<Object> nuevosAtributos= new ArrayList<Object>();
				nuevosAtributos.add(cte); //nombre
				nuevosAtributos.add("usinteger"); //tipo
				nuevosAtributos.add("CONSTANTE"); //tipoToken
				nuevosAtributos.add(valorCte); //valor
				AL.tablaSimbolos.put(cteClaveTS, nuevosAtributos);
				
			}
				
			devuelto= new Token(AL.mapeoTipoTokens.get("CONSTANTE"),cteClaveTS); 
			//si no existia, se creo antes, si existia devuelvo el TIPO DE TOKEN y su clave de acceso a la tabla de simbolos
			//Se incrementa para que se pueda hacer otra entrada a la TablaSimbolos con otra clave
			
			return devuelto;
			
			
			
			
			
		}
}
