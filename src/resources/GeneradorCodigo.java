package resources;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class GeneradorCodigo {
	
	//pensada para generar codigo ASM. Para el TP4
	
	static Hashtable<String, Boolean> tablaDeOcupacion=new Hashtable<>(); //es una tabla con el registro y si esta ocupado o no. Si esta libre entonces el valor del boolean es false
	List<String> codigo= new ArrayList<>();
	private String new_line_windows="\n";
	private Hashtable<String,Atributos> tablaSimbolos;
	int current_label=1;
	Stack<String> pilaEtiquetas=new Stack<>();
	
	int posibleElse=-1; //int adicional por si hay que agregar una label extra por el then/else en vez de then solamente
	
	// el label del fallo de condicion se comparte entre un fallo normal y un else. 
	//Si no hay else no tengo que poner el JMP incondicional.
	
	
	public GeneradorCodigo(Hashtable<String,Atributos> tablaSimbolos) {
		
		this.tablaSimbolos=tablaSimbolos;
		String[] regs = {"EAX","EBX","ECX","EDX"};//er de extender para usar solo lo necesario. Es decir, si toy usando variables de 16 bits, no usar todo el reg de 32
		
		for( String r: regs) {
		
			this.setearOcupacionRegistro(r, false);
		}
		
		
	}
	
	
	
	public String quitarSufijo(String cteConSufijo) {
		//NOTESE SI EL STRING NO ES UNA CTE DE LA FORMA 1_tipo igual se retorna el primer elemento.
		//Por ejemplo quitarSufijo(var) devuelve var. POR LO TANTO CUIDADO SI DECIDO USAR _VAR
		return cteConSufijo.split("_")[0]; //ej, 4_i me quedo con "4"
		
		
	}
	
	public static void setearOcupacionRegistro(String r,Boolean ocupado) {
		tablaDeOcupacion.put(r,ocupado);
		
	}
	
	public static Hashtable<String, Boolean> getTablaOcupacion(){
		return tablaDeOcupacion;
	}
	
	public void addNewLabel() {
		//esto es re choto de hacer pero no vi otra forma.
		//Es un metodo invocable, en especial por Arbol, para agregar una label antes de generar mas codigo
		
		
		String label="Label"+(current_label++);
		pilaEtiquetas.add(label);
		codigo.add(label+":" +new_line_windows); //aca toy agregando label posta, que ya va en codigo como para hacer un salto
	}
	
	
	private boolean esRegistro(String posibleReg) {
		//hay una situacion en la que la variable tenga de identificador el nombre de un registro....
		//Es medio rebuscado pero repensar
		for(String s:tablaDeOcupacion.keySet()) {
			if(s.equals(posibleReg)) {
				return true;
			}
		}
		return false;
	}
	private boolean esVariable(String posibleVariable) {
		//Esto es para discernir si algo es una variable o no. Util para optimizar el uso de registros
		String declarada=(String) tablaSimbolos.get(posibleVariable).get("Declarada");
		return declarada!=null; //si es variable o funcion tiene Declarada. Por lo que declarada no es null
	}
	
	
	public static String getRegistroLibre(boolean prioritario) {
		//el boolean prioritario sirve para reservar los EAX y EDX para multiplicaciones y divisiones
	
		
		
		for(String s: tablaDeOcupacion.keySet()) {
			
			if(tablaDeOcupacion.get(s)==false) { //si esta libre la clave retorna false el metodo get
				if(prioritario) {
					if(s.equals("EAX")||s.equals("EDX"))
						return s;	
				}else {
					if(s.equals("EBX")||s.equals("ECX"))
						return s;	
				}
				
			}
		}
		
		//por alguna razon, mas que nada debugging, no hay reg libre...
		System.out.println("ACA SALIO ALGO TREMENDAMENTE MAL, NO HAY REG LIBRES");
		return null;
	}
	
	private String plantillaOperacionesExpresiones(String valorIzq,String valorDer,String opASM,boolean esConmutativa) 
		{
			String registroOcupado="";
			String generado=opASM+" ";
			
			if(esRegistro(valorIzq)){ //opero entre registro y reg/variable. 
				//izq es registro
				generado+=valorIzq+",";
				if(esVariable(valorDer))
					generado+="_"+valorDer+new_line_windows; //para quehaga matching con las variables en .data
				else
					generado+=quitarSufijo(valorDer)+new_line_windows;
				if(esRegistro(valorDer)) {
				    setearOcupacionRegistro(valorDer, false); //libero registro
				}
				registroOcupado=valorIzq;
			}else {
				//izq es una variable o inm, si la operacion es conmutativa puedo reusar el reg, si no debo usar otro reg.
				if(esConmutativa&&esRegistro(valorDer)) { 
					generado+=valorDer+",";
					if(esVariable(valorIzq))
						generado+="_"+valorIzq+new_line_windows; //para quehaga matching con las variables en .data
					else
						generado+=quitarSufijo(valorIzq)+new_line_windows;
				}else {
					//operacion es entre dos variables o variable/inmediato, debo mover a registros...
					String regLibre="";
					if(opASM.equals("MUL")||opASM.equals("DIV"))
						regLibre=getRegistroLibre(true);
					else
						regLibre=getRegistroLibre(false);
					if(regLibre!=null) {
						setearOcupacionRegistro(regLibre, true);
						registroOcupado=regLibre;
						generado="MOV "+regLibre+","+"_"+(valorIzq)+new_line_windows; //se pisa el anterior generado ya que genero otra linea previa
						if(esVariable(valorDer))
							generado+=opASM+" "+regLibre+","+"_"+valorDer+new_line_windows;
						else
							generado+=opASM+" "+regLibre+","+quitarSufijo(valorDer)+new_line_windows;
						
						
					}
					
				}
				
				
			}
			codigo.add(generado);
			return registroOcupado;
	}
	
	private String plantillaOperacionesComparacion(String valorIzq, String valorDer) {
		String out="";
		String regLibre="_"+valorIzq; //si es una variable ya la modifico, si luego encuentro que se necesita un registro entonces se pisa
		String esVariable=(String) tablaSimbolos.get(valorIzq).get("Declarada"); //si es una cte entonces no tiene el campo declarada y no se puede comparar
		if(!esRegistro(valorIzq)&&esVariable==null) { //el reg de destino de comparacion no puede ser un valor inmediato
			//Si pongo || en la condicion anterior incluso cuando es variable la mueve a reg. O puedo sacar todo lo de esVariable si asi se desea
			regLibre=getRegistroLibre(false);
			if(regLibre!=null) {
				
				out+="MOV "+ regLibre + ","+quitarSufijo(valorIzq)+new_line_windows; //le saco el sufijo porque es un inmediato
				
			}
		}
		
		if(esVariable(valorDer))
			out+="CMP "+regLibre+", _"+valorDer + new_line_windows;
		else
			//no es una variable, por lo que es un inmediato o un registro al cual quitarSufijo no le afecta
			out+="CMP "+regLibre+","+quitarSufijo(valorDer) + new_line_windows; //regLibre es el nombre de la variable/reg anterior o el nuevo registro que se debe poner el valor Inmediato
		return out;
		
	}
	public Arbol generarCodigoArbol(String operacion, Arbol izquierdo, Arbol derecho){
		//se va a chequear que el izquierdo sea un registro para poder optimizar el codigo y no correr riesgo de usar registro de mas
		//luego se va a chequear que el derecho sea o no un registro asi lo libero
		//En el caso de que una operacion sea conmutativa, se puede optimizar aun mas.
		
		
		String registroOcupado = "";
		String valorIzq=izquierdo.getValor();
	
		String valorDer=derecho.getValor();
		
		switch(operacion){
			case("+"): {
				//Operacion conmutativa, por lo que puedo manipular el reg izq o derecho como destino
				//OPASM  es ADD
				registroOcupado=generarSuma(valorIzq,valorDer);
				break;
			}
			case("-"):  {
				registroOcupado=generarResta(valorIzq, valorDer);
				break;
			}
			
			case("*"): {
				registroOcupado=generarMultiplicacion(valorIzq, valorDer);
				break;
			}
			case("/"):{
				registroOcupado=generarDivision(valorIzq, valorDer);
				break;
			}
			case(":="): {
				registroOcupado=generarAsignacion(valorIzq,valorDer);
				break;
			}
			case("="): {
				//las comparaciones son un poco especiales. EN asm deben estar siempre CMP {reg/mem}, {reg/mem/inm}
				//Por otra parte se deben generar los JUMP con la labels.
				//Pero la labels no las se hasta generar efectivamente el codigo mas adelante. Lo que se hace entonces es generar los saltos
				//poner la labels en una pila para mas tarde.
				//La cuestion es que pueden haber infinitos saltos en un programa, por lo que las labels no pueden ser siempre label1 y label2.
				//Lo que se hara es crear label+current_Label. Siendo current_label un contador por cada label creada. Por lo tanto puedo tener labels infinitos.
				registroOcupado=generarIgual(valorIzq,valorDer);
				
				break;
			}
			case("<"): {
				registroOcupado=generarMenor(valorIzq, valorDer);
				break;
			}
			case("<="): {
				registroOcupado=generarMenorIgual(valorIzq, valorDer);
				break;
			}
			case(">"): {
				registroOcupado=generarMayor(valorIzq, valorDer);
				break;
			}
			case(">="): {
				registroOcupado=generarMayorIgual(valorIzq, valorDer);
				break;
			}
			case("!="): {
				registroOcupado=generarDistinto(valorIzq, valorDer);
				break;
			}
			case("if"): break;
			case("cuerpo"):break;
			case("while"): {
				registroOcupado=generarWhile();
				break;
			}
			case("lista_sentencias"): break;
		}
		return new Hoja(registroOcupado);
	}
	
	public Arbol generarCodigoArbol(String operacion, Arbol hijo){
		//Nodos rengos que tienen unicos hijos
		
		String valorHijo=hijo.getValor();
		String registroOcupado = "";
		switch(operacion.toLowerCase()){
			case("invocacion"): break;
			case("impresion"): {
				String aux= valorHijo.split("'")[1];//por ejemplo 'test' pasa a ser test
				String generado="INVOKE printf, ADDR " + "_"+aux + new_line_windows; // el printf en minuscula porque es una funcion externa
				codigo.add(generado);
				break;
			}
			case("cuerpo"): break;
			case("then"): {
				registroOcupado=generarThen();
				break;
			}
			case("else"): {
				registroOcupado=generarElse();
				break;
			}
			case("condicion"): break;
			case("casting"): break;
		}
		return new Hoja(registroOcupado);
	}
	
	
	
	

	
	private String generarSuma(String valorIzq,String valorDer){
		String registroOcupado = plantillaOperacionesExpresiones(valorIzq, valorDer, "ADD", true);
		
		return registroOcupado;
	}
	private String generarResta(String valorIzq,String valorDer){
		String registroOcupado = plantillaOperacionesExpresiones(valorIzq, valorDer, "SUB", false);
		return registroOcupado;
	}
	private String generarMultiplicacion(String valorIzq,String valorDer){
		String registroOcupado = plantillaOperacionesExpresiones(valorIzq, valorDer, "MUL", true);
		return registroOcupado;
	}
	private String generarDivision(String valorIzq,String valorDer){
		String registroOcupado = plantillaOperacionesExpresiones(valorIzq, valorDer, "DIV", false);
		return registroOcupado;
	}
	private String generarAsignacion(String valorIzq,String valorDer){
		String registroOcupado = "";
		//podria modificar la plantilla de operaciones pero la va a complicar demasiado, ademas se sabe que el lado izq es una variable
		//y solo hay que verificar si es entre dos variables o var y registro.
		String generado="";
		if(esRegistro(valorDer)) {
			//opero entre variable y reg
			generado="MOV "+ "_"+valorIzq+","+valorDer+new_line_windows; //Por ej, MOV _var,ECX
			setearOcupacionRegistro(valorDer, false);
		}else {
			//operacion entre variable y variable o variable y constante
			if(!esVariable(valorDer)) { //ASM SOPORTA MOV VAR,INMEDIATO
				generado="MOV "+"_"+valorIzq+","+quitarSufijo(valorDer)+new_line_windows;
				
			}else {
				//Toy operando entre dos variables. ASM no soport MOV var,var2 ; Debo moverla entre registros
				String regLibre=getRegistroLibre(false);
				if(regLibre!=null) {
					setearOcupacionRegistro(regLibre, true);
					generado="MOV "+regLibre+", _"+valorDer+new_line_windows;
					generado+="MOV "+"_"+valorIzq+","+regLibre+new_line_windows; 
					setearOcupacionRegistro(regLibre, false);
				}
			}
			//hay una optimizacion posible entre los dos cuerpos del if, pero complicaria el seguimiento del codigo.
			//basicamente despues del primer generado es hacer valorDer=regLibre
			//y sacar la ocupacion de los registros ya que nunca lo termino ocupando... pero si no lo pongo hay que reanalizarlo para entenderlo
		}
		
		codigo.add(generado);
		return registroOcupado;
	}
	
	private String generarIgual(String valorIzq,String valorDer){
		String registroOcupado = "";
		String generado=plantillaOperacionesComparacion(valorIzq, valorDer);
		
		
		String etiqueta="Label"+ (current_label++);
		pilaEtiquetas.push(etiqueta); 
		generado+= "JNE " + etiqueta+new_line_windows; //se agrega el salto a not equal, si es equal sigue la ejecucion normalmente.
		//el lado derecho debe ser o un inmediato o un reg o una variable. Como fue definida la gramatica es una expresion y es un inmediato(o hay problema) o es un registro (por usar varias operaciones)
		
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarMenor(String valorIzq,String valorDer){
		String registroOcupado = "";
		String generado=plantillaOperacionesComparacion(valorIzq, valorDer);
		
		
		String etiqueta="Label"+ (current_label++);
		pilaEtiquetas.push(etiqueta); 
		generado+= "JGE " + etiqueta+new_line_windows; //si es menor sigo ejecutando un then
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarMenorIgual(String valorIzq,String valorDer){
		String registroOcupado = "";
		String generado=plantillaOperacionesComparacion(valorIzq, valorDer);
		
		
		String etiqueta="Label"+ (current_label++);
		pilaEtiquetas.push(etiqueta); 
		generado+= "JG " + etiqueta+new_line_windows; //si es menor sigo ejecutando un then
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarMayor(String valorIzq,String valorDer){
		String registroOcupado = "";
		String generado=plantillaOperacionesComparacion(valorIzq, valorDer);
		
		
		String etiqueta="Label"+ (current_label++);
		pilaEtiquetas.push(etiqueta); 
		generado+= "JLE " + etiqueta+new_line_windows; //si es menor sigo ejecutando un then
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarMayorIgual(String valorIzq,String valorDer){
		String registroOcupado = "";
		String generado=plantillaOperacionesComparacion(valorIzq, valorDer);
		
		
		String etiqueta="Label"+ (current_label++);
		pilaEtiquetas.push(etiqueta); 
		generado+= "JL " + etiqueta+new_line_windows; //si es menor sigo ejecutando un then
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarDistinto(String valorIzq,String valorDer){
		String registroOcupado = "";
		String generado=plantillaOperacionesComparacion(valorIzq, valorDer);
		
		
		String etiqueta="Label"+ (current_label++);
		pilaEtiquetas.push(etiqueta); 
		generado+= "JE " + etiqueta+new_line_windows; //si es menor sigo ejecutando un then
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarIf(){
		String registroOcupado = "";
		return registroOcupado;
	}
	private String generarCuerpo(){ /*Dos versiones de este?*/
		String registroOcupado = "";
	
		return registroOcupado;
	}
	private String generarWhile(){
		String registroOcupado = "";
		//debido a como se recorre el arbol al momento de agregar la label del while queda abajo del todo. Y la label agregada por cond queda sobre ella.
		//Basicamente lo que se hace es invertir el orden, aunque hay que revisar que esto no se rompa con otras estructuras dentro. (Aunque se probo con un if dentro y andaba bien).
		String pop1=pilaEtiquetas.pop();
		String pop2=pilaEtiquetas.pop();
		String labelDeVueltaYFin="JMP "+pop2+new_line_windows;
		
		labelDeVueltaYFin+=pop1+":"+new_line_windows; //el pop1 es una etiqueta que va en codigo, mientras que pop2 es un salto a nombre de label
		codigo.add(labelDeVueltaYFin);
		
		return registroOcupado;
	}
	private String generarListaSentencias(){
		String registroOcupado = "";
		return registroOcupado;
	}
	private String generarImpresion(){
		String registroOcupado = "";
		return registroOcupado;
	}
	private String generarInvocacion(){
		String registroOcupado = "";
		return registroOcupado;
	}
	private String generarThen(){
		String registroOcupado = "";
		String generado="";
	
		
		posibleElse=codigo.size(); //me guardo un posible int para reinsertar codigo en caso de que haya else
		generado=pilaEtiquetas.pop()+":"+new_line_windows;
		codigo.add(generado);
		return registroOcupado;
	}
	private String generarElse(){
		String registroOcupado = "";
		String generado="";
		if(posibleElse!=-1) {
			
			String loQueDebiaInsertarEnThen="JMP "+ "Label"+(current_label)+new_line_windows; //el label de aca es el 2, pero corresponde a codigo anterior que debi generar en then
			codigo.add(posibleElse, loQueDebiaInsertarEnThen); //inserto directamente 
			generado="Label"+(current_label++)+":"+new_line_windows; //dejo preparado el label 3 por si hay mas de estras estructuras
			codigo.add(generado);
			posibleElse=-1;
		}
		return registroOcupado;
	}
	private String generarCondicion(){
		String registroOcupado = "";
		return registroOcupado;
	}
	private String generarCasting(){
		String registroOcupado = "";
		return registroOcupado;
	}
	

	public List<String> generarPreStartCode(Arbol raiz){
		
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
					"includelib c:\\masm32\\lib\\masm32.lib",
					"include \\masm32\\include\\masm32rt.inc",
					"dll_dllcrt0 PROTO C",
					"printf PROTO C:VARARG"
		};
		
		for(String i: includes) {
			out.add(i+new_line_windows);
			
		}
		return out;
		
	}
	
	public List<String> generarSeccionDatos(){
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
			Object valor=null;
			boolean esCadena=false;
			if(tipo.equals("integer")) {
				//tipoDatos="DW";	//descomentar esto si hay que implementar optimizacion de registros, para integer usar AX en vez de EAX....
				tipoDatos="DD"; //esto implica el uso de los reg de 32 bits
				valor = (Integer) atts.get("Valor");
			}
			if(tipo.equals("uslinteger")) {
				tipoDatos="DD";
				valor = (Long) atts.get("Valor"); //esto es por si se rompe por pasar el valor a long en AL, antes habia un double....
			}
			if(tipo.equals("cadena_caracteres")) {
				esCadena=true;
				tipoDatos="DB"; //por alguna razon los string se guardan como un DB, debe ser un puntero de 8 bits...
			}
			//String aux="_"+quitarSufijo(clave)+" "+tipoDatos +" "; //FALTA EL VALOR, EL TERMINAL NULL EN CASO DE CADENA O ? EN CASO DE VARIABLES
			String aux="_"+clave+" "+tipoDatos +" ";
			//le podria quitar el sufijo a la clave. Pero como no parece que esto se vaya a usar en si, si no para mostrar la tabla de simbolos. Puede que sea irrelevante.
			//En realidad nunca voy a usar algo como _1.... Si puedo generar el codigo directamente como inmediato
			if(esCadena) {
				aux=aux + "\""+clave+"\"" + ",0" +new_line_windows; // "clave",0
				outCtes.add(aux);
			}else {
				
				if(valor!=null) {
					//es cte 
					aux+=valor+new_line_windows;
					outCtes.add(aux);
				}else {
					//es una variable no inicializada. Por lo que es coherente ponerle el ambito
					//aux=clave+"@"+atts.get("Ambito")+" "+tipoDatos +" "; //si se comenta esto lo demas sigue funcionando y en la seccion de datos solo se muestra el lexema
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
	
	public List<String> generarCodigo(Arbol arbol){
		codigo= new ArrayList<>(); //se limpia por las dudas, ademas puedo geerar codigo con varios arboles.
		codigo.add(new_line_windows+".code"+new_line_windows);
		//ESPACIO EN BLANCO PARA GENERAR EL PRECODE: FUNCIONES Y DEMAS DE CLOSURE
		codigo.add(new_line_windows+"start:"+new_line_windows);
		arbol.generarCodigo(this);
		
		return codigo; //se esperar que arbol.generarCodigo modifique el codigo que es una lista global
	}
	public List<String> generarFin(){
		
		List<String> out = new ArrayList<>();
		String[] endArch= {
		
		"inkey \"Press any key to exit...\"", //para que no se cierre la consola de una
		"invoke ExitProcess, 0",
		"end start"
		};
		for(String s:endArch) {
			out.add(s+new_line_windows);
		}
		return out;
	}
	public static void main(String[] args) {
		GeneradorCodigo g=new GeneradorCodigo(new Hashtable<>());
		setearOcupacionRegistro("EAX", true);
		for(String s: tablaDeOcupacion.keySet()) {
			System.out.println("valor " + s + " ocupado " + tablaDeOcupacion.get(s));
			
		}
	}
}


