package resources;

public class Nodo extends Arbol{
	Arbol hijoDerecho, hijoIzquierdo;
	
	public Nodo(String valor, Arbol hijoDerecho, Arbol hijoIzquierdo){
		super(valor);
		this.hijoDerecho = hijoDerecho;
		this.hijoIzquierdo = hijoIzquierdo;
	}

	@Override
	public String imprimir(String offset) {
		String salida = offset + "Nodo '" + this.getValor() + "'\n";
		salida += hijoIzquierdo.imprimir(offset + "    " );
		salida += hijoDerecho.imprimir(offset + "    " );
		return salida;
	}
}
