package resources;

public class NodoRengo extends Arbol{
	Arbol hijo;
	
	public NodoRengo(String valor, Arbol hijo){
		super(valor);
		this.hijo = hijo;
	}
	
	public String imprimir(String offset, String prefijo) {
		String salida = offset + prefijo + " Nodo " + this.getValor() + "\n";
		salida += hijo.imprimir(offset + "-   " , "Hijo");
		return salida;
	}
}
