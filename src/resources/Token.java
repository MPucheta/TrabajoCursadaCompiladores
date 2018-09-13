package resources;



public class Token {
	public int tipoDeToken; //por ejemplo, para el literal =, el numero 17
	public String claveTablaSimbolo; //esto es por si en algunos casos se debe retornar cosas como <cte,punteroTablaSimbolo>

	//como se implementa la TS como un hashtable, no necesito un puntero si no la clave de acceso
	public Token(int numAsignado, String clave) {
		tipoDeToken=numAsignado;
		claveTablaSimbolo=clave;
			
	}
	
}
