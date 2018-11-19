package resources;


public class AccionSemantica2 implements AccionSemantica{
	//Esta accion semantica se encarga de guardar en la TS las cadenas de caracteres
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		if (AL.buffer.isEmpty())
			AL.agregarError("Error: no se pueden definir cadenas de caracteres vacias vacias. Linea: " + AL.nroLinea);
		else{
			String cadena = AL.buffer;
			String claveTS = "'" + AL.buffer + "'"; 
			
			if(!AL.TS.containsKey(claveTS)) {
				Atributos atts = new Atributos();
				atts.set("Token", "CADENA_CARACTERES");
				atts.set("Lexema", cadena);
				atts.set("Tipo", "cadena_caracteres");
				AL.altaEnTablaSimbolos(AL.buffer, atts);
			}
			
			
			return new Token(Token.CADENA_CARACTERES, claveTS, AL.nroLinea);
		}
		return new Token(Token.YYERRCODE,null, AL.nroLinea);
	}
	
}
