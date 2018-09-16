package resources;

public class AccionSemantica7 implements AccionSemantica {
	//devuelve el token para + - * / ( ) { } ; , = := != >= <=
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.buffer += nuevoChar;
		int equivalente=0;
		if(AL.buffer.length()==1)
			equivalente= AL.ASCIIToken(AL.buffer.charAt(0));
		else
			equivalente=AL.mapeoTipoTokens.get(AL.buffer);
		
		return new Token(equivalente, null);
	}

}

