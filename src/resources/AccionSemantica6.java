package resources;

public class AccionSemantica6 implements AccionSemantica {

	
	//esta accion semantica se define como
	/* >devolver ultimo char leido a la entrada (AL.pos--)
	 * >verificar que existe en la tabla de palabras reservadas
	 * >si existe devolver un token de palabra reservada (clave a null)
	 * >si no existe informar error
	 */
	
	
	
	@Override
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.pos--;
		Token token = null;
		if (AL.existePalabraReservada(AL.buffer))
			token = new Token(AL.mapeoTipoTokens.get(AL.buffer), null);
		else
			AL.agregarError("Error: la palabra '" + AL.buffer + "' no se pudo identificar como palabra reservada. Línea: " + AL.nroLinea);
		return token;
	}

	
	
}
