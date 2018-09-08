package resources;



public class Token {
	public int numeroAsignado; //por ejemplo, para el literal =, el numero 17
	public Object claveTablaSimbolo; //esto es por si en algunos casos se debe retornar cosas como <cte,punteroTablaSimbolo>

	//como se implementa la TS como un hashtable, no necesito un puntero si no la clave de acceso
	public Token(int numAsignado, Object clave) {
		numeroAsignado=numAsignado;
		claveTablaSimbolo=clave;
			
	}
	
}
