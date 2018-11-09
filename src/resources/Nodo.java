package resources;

public class Nodo extends Arbol{
	Arbol hijoDerecho, hijoIzquierdo;
	
	public Nodo(String valor, Arbol hijoIzquierdo, Arbol hijoDerecho){
		super(valor);
		this.hijoDerecho = hijoDerecho;
		this.hijoIzquierdo = hijoIzquierdo;
	}

	@Override
	public String imprimir(String offset, String prefijo) {
		String salida = offset + prefijo+ " Nodo " + this.getValor() + "\n";
		salida += hijoIzquierdo.imprimir(offset + "-   " , "Hijo izquierdo:");
		salida += hijoDerecho.imprimir(offset + "-   ", "Hijo derecho:" );
		return salida;
	}
}
