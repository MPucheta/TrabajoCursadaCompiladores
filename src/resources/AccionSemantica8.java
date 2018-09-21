package resources;

public class AccionSemantica8 implements AccionSemantica {
	//devuelve el token para < >
	//es casi igual a AS7, cambia en que no agrega el caracter recien leido al buffer 
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.pos--;
		return new Token(AL.ASCIIToken(AL.buffer.toCharArray()[0]), null);
	}

}

