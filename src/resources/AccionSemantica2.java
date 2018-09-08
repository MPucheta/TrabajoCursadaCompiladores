package resources;


public class AccionSemantica2 implements AccionSemantica{
	//Esta accion semantica se encarga de reservar espacio en el buffer y
	//agregar el char en cuestion. 
	//En esta implementacion el buffer es un string por lo que reservar espacio se hace
	//mediante un new (ya que se toma como dinamico)
	
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.inicializarBuffer();
		AL.buffer+=nuevoChar;
		return null;
		
	}
	
}
