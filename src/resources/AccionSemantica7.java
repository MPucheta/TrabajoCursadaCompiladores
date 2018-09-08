package resources;

public class AccionSemantica7 implements AccionSemantica {
	//devuelve el token para + - * / ( ) { } ; , = := != >= <=
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.buffer += nuevoChar;
		return new Token(AL.mapeoTipoTokens.get(AL.buffer), null);
	}

}

