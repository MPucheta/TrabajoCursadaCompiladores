package resources;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class GeneradorCodigo {
	
	//pensada para generar codigo ASM. Para el TP4
	
	static Hashtable<String, Boolean> tablaDeOcupacion=new Hashtable<>(); //es una tabla con el registro y si esta ocupado o no. Si esta libre entonces el valor del boolean es false
	
	private String new_line_windows="\r\n";
	
	
	public GeneradorCodigo() {
		
		String[] regs = {"EAX","EBX","ECX","EDX"};//er de extender para usar solo lo necesario. Es decir, si toy usando variables de 16 bits, no usar todo el reg de 32
		
		for( String r: regs) {
		
			this.setearOcupacionRegistro(r, false);
		}
		
		
	}
	
	
	
	public static void setearOcupacionRegistro(String r,Boolean ocupado) {
		tablaDeOcupacion.put(r,ocupado);
		
	}
	
	public static Hashtable<String, Boolean> getTablaOcupacion(){
		return tablaDeOcupacion;
	}
	
	public List<String> generarCodigoIntermedio(Arbol raiz){
		
		List<String> out= new ArrayList<>();
		
		
		return out;
		
	}
	
	public List<String> generarHeader(){
		String[] header= {".386",".model flat,stdcall",".stack 200h","option casemap: none"};
		List<String> out= new ArrayList<>();
		for(String h: header) {
			out.add(h+new_line_windows);
		}
		
		return out;
	}
	
	public List<String> generarIncludes(){
		List<String> out= new ArrayList<>();
		String[] includes= {// el \\ escapa el caracter \ para includes. Quizas haya que cambiarlo igual, pero la plataforma destino es windows... (MASM32)

					"include c:\\masm32\\include\\windows.inc",
					"include c:\\masm32\\include\\kernel32.inc",
					"include c:\\masm32\\include\\user32.inc",
					"include c:\\masm32\\include\\masm32.inc",
					"includelib c:\\masm32\\lib\\kernel32.lib",
					"includelib c:\\masm32\\lib\\user32.lib",
					"includelib c:\\masm32\\lib\\masm32.lib"
		};
		
		for(String i: includes) {
			out.add(i+new_line_windows);
			
		}
		return out;
		
	}
	
	public List<String> generarSeccionDatos(Hashtable<String, Atributos> tablaSimbolos){
		List<String> out= new ArrayList<>();
		List<String> outCtes= new ArrayList<>();
		List<String> outVariables= new ArrayList<>();
		//metodologia:
		//Basicamente aca van constantes y variables.
		//Las ctes tienen un atributo valor ya definido en la tablaSimbolos
		//Las variables no tienen valor pero si tienen tipo
		
		//Por lo que se va a hacer una pasada en la tabla de simbolos. Si una cte o variable es de tipo integer, se define con DW (16 bits-2bytes). Si es uslinteger con DD (4 bytes)
		//Si hay cadena_caracteres tambien se definen pero utilizando db.
		
		
		for(String clave: tablaSimbolos.keySet()) {
			
			Atributos atts = tablaSimbolos.get(clave);
			String tipoDatos="";
			String tipo=(String)atts.get("Tipo");
			
			boolean esCadena=false;
			if(tipo.equals("integer")) {
				tipoDatos="DW";	
			}
			if(tipo.equals("uslinteger")) {
				tipoDatos="DD";
			}
			if(tipo.equals("cadena_caracteres")) {
				esCadena=true;
				tipoDatos="DB"; //por alguna razon los string se guardan como un DB, debe ser un puntero de 8 bits...
			}
			String aux=clave+" "+tipoDatos +" "; //FALTA EL VALOR, EL TERMINAL NULL EN CASO DE CADENA O ? EN CASO DE VARIABLES
			if(esCadena) {
				aux=aux + "\""+clave+"\"" + ",0" +new_line_windows; // "clave",0
				outCtes.add(aux);
			}else {
				Integer valor = (Integer) atts.get("Valor");
				if(valor!=null) {
					//es cte 
					aux+=valor+new_line_windows;
					outCtes.add(aux);
				}else {
					//es una variable no inicializada. Por lo que es coherente ponerle el ambito
					aux=clave+"@"+atts.get("Ambito")+" "+tipoDatos +" "; //si se comenta esto lo demas sigue funcionando y en la seccion de datos solo se muestra el lexema
					aux+="?"+new_line_windows; //? se indica cuando la variable no tiene valor al momento de la compilacion. O sea, en variables que toman valor en tiempo de ejecucion
					outVariables.add(aux);
				}
			}
		}
		
		String data=new_line_windows+".data";
		out.add(data);
		out.add(new_line_windows+";ctes/datos inicializados"+new_line_windows);
		out.addAll(outCtes);
		out.add(new_line_windows+";variables no inicializadas"+new_line_windows);
		out.addAll(outVariables);

		return out;
	}
	
	
	
	public static void main(String[] args) {
		GeneradorCodigo g=new GeneradorCodigo();
		setearOcupacionRegistro("EAX", true);
		for(String s: tablaDeOcupacion.keySet()) {
			System.out.println("valor " + s + " ocupado " + tablaDeOcupacion.get(s));
			
		}
	}

}


