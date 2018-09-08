package resources;

public class AccionSemantica3 implements AccionSemantica {

	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.buffer+=nuevoChar;
		return null;
	}

}
