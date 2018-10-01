package resources;


public class AccionSemantica2 implements AccionSemantica{
	//Esta accion semantica se encarga de guardar en la TS las cadenas de caracteres
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		String cadena = AL.buffer;
		String claveTS = "'" + AL.buffer + "'"; 
		
		if(!AL.TS.containsKey(claveTS)) 
			AL.altaEnTablaSimbolos(claveTS,"CADENA_CARACTERES",cadena);
		
		
		return new Token(Token.CADENA_CARACTERES, claveTS, AL.nroLinea);
		
	}
	
}
