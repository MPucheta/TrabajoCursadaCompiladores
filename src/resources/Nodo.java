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

	@Override
	public Arbol generarCodigo(GeneradorCodigo gc) {
		this.hijoIzquierdo = hijoIzquierdo.generarCodigo(gc);
		this.hijoDerecho = hijoDerecho.generarCodigo(gc);
		
		return gc.generarCodigoArbol(this.valor, this.hijoIzquierdo, this.hijoDerecho);
	}
}
