package resources;




public class AccionSemantica4 implements AccionSemantica {
	//esta accion semantica se define como:
	/*>Devolver a la entrada lo ultimo leido
	 * >verificar si cte entra en rango definido para i
	 * 		si no, pisar con maximo valor e informar warning
	 * >verificar si existe en tabla simbolo
	 * 	Si: retornar <cte, puntTS>
	 *  No: dar de alta en TS y retornar <cte,punts>
	 * @see resources.AccionSemantica#ejecutar(resources.AnalizadorLexico, char)
	 */
	private int minValor=-32768;
	private int maxValor=32767;
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.pos--;
		String cte=AL.buffer; //claveTablaSimbolo
		cte=cte.split("_")[0]; //ya que lo leido se supone que es una cte de la forma, por ejemplo, 123_i
		
		int valorCte=new Integer(cte).intValue();
		Token devuelto=null;
		
		if(valorCte< minValor || valorCte> maxValor ) { //chequeo de rangos
			valorCte=maxValor; //tecnica de reemplazo
			System.out.println("Warning: Reemplazo de valor en linea: " + AL.nroLinea);
		}
		
		if(!AL.tablaSimbolos.containsKey(AL.buffer)) {
			
			AL.AltaEnTablaSimbolos(AL.buffer, "integer","CTE",valorCte);
			
		}
			
		devuelto= new Token(AL.mapeoTipoTokens.get("CTE"),AL.buffer); 
		//si no existia, se creo antes, si existia devuelvo el TIPO DE TOKEN y su clave de acceso a la tabla de simbolos
		//Se incrementa para que se pueda hacer otra entrada a la TablaSimbolos con otra clave
		
		return devuelto;
		
		
		
		
		
	}


}
