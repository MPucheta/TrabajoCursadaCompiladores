package resources;


public class AccionSemantica2 implements AccionSemantica{
	//Esta accion semantica se encarga de guardar en la TS las cadenas de caracteres
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		String cadena = AL.buffer;
		String claveTS = "'" + AL.buffer + "'"; 
		
		if(!AL.TS.containsKey(claveTS)) {
			Atributos atts = new Atributos();
			atts.set("Token", "CADENA_CARACTERES");
			atts.set("Lexema", cadena);
			AL.altaEnTablaSimbolos(AL.buffer, atts);
		}
		
		
		return new Token(Token.CADENA_CARACTERES, claveTS, AL.nroLinea);
		
	}
	
}
