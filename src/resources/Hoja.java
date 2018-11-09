package resources;

public class Hoja extends Arbol{

	public Hoja(String clave){
		super(clave);
	}

	
	public String imprimir(String offset, String prefijo) {
		return offset + prefijo + " Hoja " + this.getValor() + "\n";
	}


	@Override
	public Arbol generarCodigo(GeneradorCodigo gc) {
		return this;
	}
}
