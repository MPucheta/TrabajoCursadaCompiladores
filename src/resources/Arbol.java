package resources;

public abstract class Arbol {
	String valor;
	public abstract String imprimir(String offset, String prefijo);
	public abstract Arbol generarCodigo(GeneradorCodigo gc);
	
	public Arbol(String valor){
		this.valor = valor;
	}
	public String getValor(){
		return valor;
	};
	
	
}
