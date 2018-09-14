package resources;

public class AccionSemantica9 implements AccionSemantica{
	//esta accion semántica se encarga de sumar 1 al numero de lineas cuando se lee un salto de linea
	
	@Override
	public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {
		AL.nroLinea++;
		return null;
	}

}
