package resources;

import java.util.ArrayList;
import java.util.List;

public class AccionSemantica1 implements AccionSemantica {

	//definicion de accion semantica1
	/* >devolver ultimo caracter leido a la entrada
	 * >verificar que la longitud es menor a 25 caracteres
	 * 		>Si no cumple informar error (retornando token de error)
	 * >Buscar  en tabla de simbolos (buscar el String identificador, que debe ser el buffer completo
	 * 		>Si existe devolver <ID,PunTS>
	 * 		>Si no existe dar de alta y devolver <ID,PunTS>
	 * 
	 */
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		Token devuelto = null;
		String buffer=AL.buffer.substring(1); //para eliminar el '_'
		AL.pos--; //volver un caracter de lectura hacia atras
		
		if(buffer.length()>25) {
			System.out.println("Error: un identificador solo puede tener hasta 25 caracteres de longitud. Línea: " + AL.nroLinea); 
			devuelto=new Token(-1,null); //token de error, ver de redefinir cualca
			
			
		}else {
			
			if(!AL.tablaSimbolos.containsKey(buffer)) {
				//revisar esto, los atributos de la TS parecian bien pero cuando es identificador como que pierde el sentido
				//Y no tanto porque el ID luego esta bindeado a una valor (r-value). PREGUNTAR EN CLASE
				List<Object> nuevosAtributos= new ArrayList<Object>();
				nuevosAtributos.add(buffer); //nombre
				nuevosAtributos.add("ID"); //tipo
				nuevosAtributos.add("ID"); //tipoToken
				nuevosAtributos.add(buffer); //valor
				AL.tablaSimbolos.put(buffer, nuevosAtributos);
				
				
			}
			devuelto= new Token(AL.mapeoTipoTokens.get("ID"),buffer);
			
			
		}
		return devuelto;
	}

}
