package resources;

public class Hoja extends Arbol{

	public Hoja(String clave){
		super(clave);
	}

	
	public String imprimir(String offset) {
		return offset + "Hoja '" + this.getValor() + "'\n";
	}
}
