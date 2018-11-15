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

	String sufijoVariablesYFunciones="@"; //se puede usar el "_" para la forma tradicional....
	String textSeparator="@";
	// el label del fallo de condicion se comparte entre un fallo normal y un else.
	//Si no hay else no tengo que poner el JMP incondicional.
	static String[] registers ={"EBX","ECX","EDX","EAX"};

	public GeneradorCodigo(Hashtable<String,Atributos> tablaSimbolos) {

		this.tablaSimbolos=tablaSimbolos;
		//er de extender para usar solo lo necesario. Es decir, si toy usando variables de 16 bits, no usar todo el reg de 32

		for( String r: registers) {

			this.setearOcupacionRegistro(r, false);
		}


	}



	public String quitarSufijo(String cteConSufijo) {
		//NOTESE SI EL STRING NO ES UNA CTE DE LA FORMA 1_tipo igual se retorna el primer elemento.
		//Por ejemplo quitarSufijo(var) devuelve var. POR LO TANTO CUIDADO SI DECIDO USAR _VAR
		return cteConSufijo.split("_")[0]; //ej, 4_i me quedo con "4"


	}

	public static void setearOcupacionRegistro(String r,Boolean ocupado) {
		
		if(r.length()==2)//AX,BX,CX,DX
			r="E"+r;
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
		String declarada=null;
		Atributos var=tablaSimbolos.get(posibleVariable);
		if(var!=null)
			declarada=(String) var.get("Declarada");
		return declarada!=null; //si es variable o funcion tiene Declarada. Por lo que declarada no es null
	}


	private String getModo(String clave) {
		//sea variable o una cte tiene un tipo en la tabla de simbolos
		//Si es un registro se sabe que si empieza con E es un registro extended de 32 bits
		String modo= "";
		if(esRegistro(clave)) {
			if(clave.substring(0, 1).equals("E")) {
				modo="16";
			}else {
				modo="32";
			}
		}
		String tipo=(String)tablaSimbolos.get(clave).get("Tipo");
		
		if(tipo.equals("integer"))
			modo="16";
		else 
			if( tipo.equals("uslinteger")){
			modo="32";
		}
		return modo;
	}
	public static String getRegistroLibre(boolean prioritario,String modo) {
		//el boolean prioritario sirve para reservar los EAX y EDX para multiplicaciones y divisiones

		
		String out=null;
		
		for(String s: registers) {

			if(tablaDeOcupacion.get(s)==false) { //si esta libre la clave retorna false el metodo get
				if(prioritario) {
					if(s.equals("EDX")||s.equals("EAX")) {
						if(modo.equals("16"))
							out=s.substring(1); //se le saca el E de extended
						else
							out=s;
					}
				}else {
					if(s.equals("EBX")||s.equals("ECX")) {
						if(modo.equals("16"))
							out=s.substring(1); //se le saca el E de extended
						else
							out=s;
					}
				}

			}
		}
		if(out==null) { //ya no se puede escatimar registros
			for(String s: registers) {
				if(tablaDeOcupacion.get(s)==false)
					out=s;
			}
			
		}
		//por alguna razon, mas que nada debugging, no hay reg libre...
		if(out==null)
			System.out.println("ACA SALIO ALGO TREMENDAMENTE MAL, NO HAY REG LIBRES");
		return out;
	}

	private String plantillaOperacionesExpresiones(String valorIzq,String valorDer,String opASM,boolean esConmutativa)
		{
			String registroOcupado="";
			String generado=opASM+" ";
			
			if(esRegistro(valorIzq)){ //opero entre registro y reg/variable/inm.
				//izq es registro
				generado+=valorIzq+",";
				if(esVariable(valorDer))
					generado+=sufijoVariablesYFunciones+valorDer+new_line_windows; //para quehaga matching con las variables en .data
				else {
					if(esRegistro(valorDer)) {
					    setearOcupacionRegistro(valorDer, false); //libero registro
					}else {
						if(opASM.equals("MUL")||opASM.equals("DIV")) {//el lado derecho no puede ser un inmediato en MUL y DIV
							String regAux=getRegistroLibre(true, getModo(valorDer));
							if(regAux!=null) {
								setearOcupacionRegistro(regAux, true);
								generado="MOV " +regAux+","+quitarSufijo(valorDer)+new_line_windows; //debo pisar lo anterior, ya que debo mover registros
								generado+=opASM+" "+valorIzq+","+regAux+new_line_windows;
								setearOcupacionRegistro(regAux, false);
							}
						}else //es un inmediato valido ya que ADD y SUB lo permiten
							generado+=quitarSufijo(valorDer)+new_line_windows;
					}
				}
				registroOcupado=valorIzq;
			}else {
				//izq es una variable o inm, si la operacion es conmutativa puedo reusar el reg, si no debo usar otro reg.
				if(esConmutativa&&esRegistro(valorDer)) {
					generado+=valorDer+",";
					if(esVariable(valorIzq))
						generado+=sufijoVariablesYFunciones+valorIzq+new_line_windows; //para quehaga matching con las variables en .data
					else
						generado+=quitarSufijo(valorIzq)+new_line_windows;
				}else {
					//operacion es entre dos variables o variable/inmediato, debo mover a registros...
					//si es asi las variables (el valorIzq por ejemplo tienen la forma de var1 que puedo buscar en la TS)
					//Si lo puedo buscar en la TS, puedo saber el tipo y decidir que registro (16 o 32 bits) necesito
					
					String modo=getModo(valorIzq);
					String regLibre="";
					
					if(opASM.equals("MUL")||opASM.equals("DIV"))
						regLibre=getRegistroLibre(true,modo);
					else
						regLibre=getRegistroLibre(false,modo);
					
					if(regLibre!=null) {
						setearOcupacionRegistro(regLibre, true);
						registroOcupado=regLibre;
						generado="MOV "+regLibre+","+sufijoVariablesYFunciones+(valorIzq)+new_line_windows; //se pisa el anterior generado ya que genero otra linea previa
						if(esVariable(valorDer))
							generado+=opASM+" "+regLibre+","+sufijoVariablesYFunciones+valorDer+new_line_windows;
						else {
							if(opASM.equals("MUL")||opASM.equals("DIV")) {//el lado derecho no puede ser un inmediato en MUL y DIV
								String regAux=getRegistroLibre(true, getModo(valorDer));
								if(regAux!=null) {
									setearOcupacionRegistro(regAux, true);
									generado="MOV " +regAux+","+quitarSufijo(valorDer)+new_line_windows; //debo pisar lo anterior, ya que debo mover registros
									generado+=opASM+" "+regLibre+","+regAux+new_line_windows; //hago
									setearOcupacionRegistro(regAux, false);
								}
							}else //es un inmediato valido para ADD y SUB
								{
								generado+=opASM+" "+regLibre+","+quitarSufijo(valorDer)+new_line_windows;
						
							}
							
						}
							


					}

				}


			}
			codigo.add(generado);
			return registroOcupado;
	}

	private String plantillaOperacionesComparacion(String valorIzq, String valorDer) {
		String out="";
		String regLibre=sufijoVariablesYFunciones+valorIzq; //si es una variable ya la modifico, si luego encuentro que se necesita un registro entonces se pisa
		String esVariable=(String) tablaSimbolos.get(valorIzq).get("Declarada"); //si es una cte entonces no tiene el campo declarada y no se puede comparar
		if(!esRegistro(valorIzq)&&esVariable==null) { //el reg de destino de comparacion no puede ser un valor inmediato
			//Si pongo || en la condicion anterior incluso cuando es variable la mueve a reg. O puedo sacar todo lo de esVariable si asi se desea
			
			regLibre=getRegistroLibre(false,getModo(valorIzq));
			if(regLibre!=null) {

				out+="MOV "+ regLibre + ","+quitarSufijo(valorIzq)+new_line_windows; //le saco el sufijo porque es un inmediato

			}
		}

		if(esVariable(valorDer))
			out+="CMP "+regLibre+","+ sufijoVariablesYFunciones+valorDer + new_line_windows;
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
			case("invocacion"): registroOcupado = generarInvocacion(hijo.getValor()); break;
			case("impresion"): {
				String aux= valorHijo.split("'")[1];//por ejemplo 'test' pasa a ser test
				String generado="INVOKE printf, ADDR " + sufijoVariablesYFunciones+aux.replace(" ", textSeparator) + new_line_windows; // el printf en minuscula porque es una funcion externa
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
			case("casting"): registroOcupado = generarCasting(hijo.getValor()); break;
		}
		return new Hoja(registroOcupado);
	}






	private String generarSuma(String valorIzq,String valorDer){
		String registroOcupado = "";
		String opASM="ADD";
		String generado=opASM+" ";
		if(esRegistro(valorIzq)){ //opero entre registro y reg/variable/inm.
				//izq es registro
				generado+=valorIzq+",";
				if(esVariable(valorDer))
					generado+=sufijoVariablesYFunciones+valorDer+new_line_windows; //para quehaga matching con las variables en .data
				else
					generado+=quitarSufijo(valorDer)+new_line_windows;
				
				
				registroOcupado=valorIzq;
			}else {
				//izq es una variable o inm, si la operacion es conmutativa puedo reusar el reg, si no debo usar otro reg.
				if(esRegistro(valorDer)) {
					generado+=valorDer+",";
					if(esVariable(valorIzq))
						generado+=sufijoVariablesYFunciones+valorIzq+new_line_windows; //para quehaga matching con las variables en .data
					else
						generado+=quitarSufijo(valorIzq)+new_line_windows;
				}else {
					//operacion es entre dos variables o variable/inmediato, debo mover a registros...
					//si es asi las variables (el valorIzq por ejemplo tienen la forma de var1 que puedo buscar en la TS)
					//Si lo puedo buscar en la TS, puedo saber el tipo y decidir que registro (16 o 32 bits) necesito
					
					String modo=getModo(valorIzq);
					String regLibre="";
					
					regLibre=getRegistroLibre(false,modo);
					
					if(regLibre!=null) {
						setearOcupacionRegistro(regLibre, true);
						registroOcupado=regLibre;
						if(esVariable(valorIzq))
							generado="MOV "+regLibre+","+sufijoVariablesYFunciones+(valorIzq)+new_line_windows; //se pisa el anterior generado ya que genero otra linea previa
						else
							generado="MOV "+regLibre+","+quitarSufijo(valorIzq)+new_line_windows;
						
						if(esVariable(valorDer))
							generado+=opASM+" "+regLibre+","+sufijoVariablesYFunciones+valorDer+new_line_windows;
						else
							generado+=opASM+" "+regLibre+","+quitarSufijo(valorDer)+new_line_windows; //funciona en reg e inmediatos
					
							


					}

				}


			}
			codigo.add(generado);
			if(esRegistro(valorDer)) { //en ambos casos el lado derecho se libera si es reg
			    setearOcupacionRegistro(valorDer, false); //libero registro
			}
	
		return registroOcupado;
	}
	private String generarResta(String valorIzq,String valorDer){
		String registroOcupado = "";
		String opASM="SUB";
		String generado=opASM+" ";
		if(esRegistro(valorIzq)){ //opero entre registro y reg/variable/inm.
				//izq es registro
				generado+=valorIzq+",";
				if(esVariable(valorDer))
					generado+=sufijoVariablesYFunciones+valorDer+new_line_windows; //para quehaga matching con las variables en .data
				else
					generado+=quitarSufijo(valorDer)+new_line_windows;
				
				
				registroOcupado=valorIzq;
		}else {
				    //como SUB no es conmutativa debo generar un registro para el lado izquierdo. Si el lado derecho es un registro queda SUB R1,R2
				 
					//operacion es entre dos variables o variable/inmediato, debo mover a registros...
					//si es asi las variables (el valorIzq por ejemplo tienen la forma de var1 que puedo buscar en la TS)
					//Si lo puedo buscar en la TS, puedo saber el tipo y decidir que registro (16 o 32 bits) necesito
					
					String modo=getModo(valorIzq);
					String regLibre="";
					
					regLibre=getRegistroLibre(false,modo);
					
					if(regLibre!=null) {
						setearOcupacionRegistro(regLibre, true);
						registroOcupado=regLibre;
						if(esVariable(valorIzq))
							generado="MOV "+regLibre+","+sufijoVariablesYFunciones+(valorIzq)+new_line_windows; //se pisa el anterior generado ya que genero otra linea previa
						else
							generado="MOV "+regLibre+","+quitarSufijo(valorIzq)+new_line_windows;
						if(esVariable(valorDer))
							generado+=opASM+" "+regLibre+","+sufijoVariablesYFunciones+valorDer+new_line_windows;
						else
							generado+=opASM+" "+regLibre+","+quitarSufijo(valorDer)+new_line_windows; //funciona en reg e inmediatos
			
					}

				


			}
			codigo.add(generado);
			
			if(esRegistro(valorDer)) { //en ambos casos el lado derecho se libera si es reg
			    setearOcupacionRegistro(valorDer, false); //libero registro
			}
		return registroOcupado;
	}
	
	
	private String generarMultiplicacion(String valorIzq,String valorDer){
		String registroOcupado = "";
		String opASM="MUL";
		String generado="";
		String regLibre=getRegistroLibre(false, getModo(valorDer)); //de principio no quiero usar los prioritarios
		String regOP="EAX";
		if(getModo(valorDer).equals("16"))

		{
			regOP="AX";
			opASM="I"+opASM; //si es un dato de 16 bits toy tratando, en este contexto, con variables integer que son signadas
		}

		 if(regLibre!=null) {
			
			 //voy a usar el regLibre tanto para usar un posible inmediato como para guardar el resultado
			String factor=valorDer;
			//se toma como que el lado izquierdo es el multiplicando. Por lo que muevo el valorIzq a EAX/AX para poder operar.
			if(esVariable(valorIzq)) {
				generado="MOV " + regOP + "," + sufijoVariablesYFunciones +valorIzq+new_line_windows; 
			}else {
				if(esRegistro(valorIzq)) {
					regLibre=valorIzq;
				}
				generado="MOV " + regOP + "," + quitarSufijo(valorIzq)+new_line_windows; 
			}
			if(esVariable(valorDer)) {//si es variable puedo hacer la MUL
				factor=sufijoVariablesYFunciones+valorDer;
			}else {
				if(!esRegistro(valorDer)) { //Si llegue aca entonces es un inmediato. Voy a mover ese inmediato a regLibre antes de hacer la MUL
						 factor=regLibre;
						 generado+="MOV " +regLibre + "," + quitarSufijo(valorDer)+new_line_windows;
								
				}
			}
			
			generado+=opASM +" "+factor+new_line_windows; //ej, MUL _var, o MUL R1
					
			generado+= "MOV " + regLibre + "," + regOP+new_line_windows; //backup del dato
			setearOcupacionRegistro("EAX", false);
			registroOcupado=regLibre;
			
			codigo.add(generado);
		
		 }
		
		
		
		return registroOcupado;
	}
	private String generarDivision(String valorIzq,String valorDer){
		String registroOcupado = "";
		String opASM="DIV";
		String generado="";
		String regLibre=getRegistroLibre(false, getModo(valorDer)); //de principio no quiero usar los prioritarios
		String regOP="EAX";
		if(getModo(valorDer).equals("16"))

		{
			regOP="AX";
			opASM="I"+opASM; //IDIV. Si es de 16 bits es un integer y es un dato signado. Por lo que debo operarlo bajo esas reglas
		}

		 if(regLibre!=null) {
			
			 //voy a usar el regLibre tanto para usar un posible inmediato como para guardar el resultado
			String factor=valorDer;
			//se toma como que el lado izquierdo es el multiplicando. Por lo que muevo el valorIzq a EAX/AX para poder operar.
			if(esVariable(valorIzq)) {
				generado="MOV " + regOP + "," + sufijoVariablesYFunciones +valorIzq+new_line_windows; 
			}else {
				if(esRegistro(valorIzq)) {
					regLibre=valorIzq;
				}
				generado="MOV " + regOP + "," + quitarSufijo(valorIzq)+new_line_windows; 
			}
			if(esVariable(valorDer)) {//si es variable puedo hacer la MUL
				factor=sufijoVariablesYFunciones+valorDer;
			}else {
				if(!esRegistro(valorDer)) { //Si llegue aca entonces es un inmediato. Voy a mover ese inmediato a regLibre antes de hacer la MUL
						 factor=regLibre;
						 generado+="MOV " +regLibre + "," + quitarSufijo(valorDer)+new_line_windows;
								
				}
			}
			
			generado+=opASM +" "+factor+new_line_windows; //ej, MUL _var, o MUL R1
					
			generado+= "MOV " + regLibre + "," + regOP+new_line_windows; //backup del dato
			setearOcupacionRegistro("EAX", false);
			registroOcupado=regLibre;
			
			codigo.add(generado);
		
		 }
		
		
		
		return registroOcupado;
	}
	private String generarAsignacion(String valorIzq,String valorDer){
		String registroOcupado = "";
		//podria modificar la plantilla de operaciones pero la va a complicar demasiado, ademas se sabe que el lado izq es una variable
		//y solo hay que verificar si es entre dos variables o var y registro.
		String generado="";
		if(esRegistro(valorDer)) {
			//opero entre variable y reg
			generado="MOV "+ sufijoVariablesYFunciones+valorIzq+","+valorDer+new_line_windows; //Por ej, MOV _var,ECX
			setearOcupacionRegistro(valorDer, false);
		}else {
			//operacion entre variable y variable o variable y constante
			if(!esVariable(valorDer)) { //ASM SOPORTA MOV VAR,INMEDIATO
				generado="MOV "+sufijoVariablesYFunciones+valorIzq+","+quitarSufijo(valorDer)+new_line_windows;

			}else {
				//Toy operando entre dos variables. ASM no soport MOV var,var2 ; Debo moverla entre registros
				String regLibre=getRegistroLibre(false,getModo(valorDer));
				if(regLibre!=null) {
					setearOcupacionRegistro(regLibre, true);
					//generado="MOV "+regLibre+", _"+valorDer+new_line_windows; COMENTO ESTO. REVISAR SI VA POR ALGUNA RAZON
					generado="MOV "+regLibre+", "+sufijoVariablesYFunciones+valorDer+new_line_windows;
					generado+="MOV "+sufijoVariablesYFunciones+valorIzq+","+regLibre+new_line_windows;
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
	private String generarInvocacion(String valor){
		String registroOcupado = "";
		String generado = "CALL " + sufijoVariablesYFunciones + valor + new_line_windows;
		codigo.add(generado);
		registroOcupado = (String) tablaSimbolos.get(valor).get("Retorno");
		if ((registroOcupado != null)&&(registroOcupado.equals("")))
			registroOcupado = "@" + valor + "@ret";
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
	private String generarCasting(String valor){
		String registroOcupado = "";
		String generado = "";
		if (valor.equals("AX") || valor.equals("BX") || valor.equals("CX") || valor.equals("DX")){
			String registroObtenido = getRegistroLibre(false, "32");
			generado = "MOV " + registroObtenido + ", 0" + new_line_windows;
			generado += "MOV " + registroObtenido.substring(1) + ", " + valor + new_line_windows;
			generado += "MOV E" + valor + ", " + registroObtenido + new_line_windows;
			registroOcupado = "E" + valor;
		}
		else if (tablaSimbolos.get(valor).get("Token").equals("ID")){
			
			String nuevaClave = "uslinteger_" + valor; //se crea una nueva variable de 32 bits, tipo uslinteger, para agregar a la TS
			Atributos atts = new Atributos();
			atts.set("Tipo", "uslinteger");
			atts.set("Declarada", tablaSimbolos.get(valor).get("Declarada"));
			atts.set("Ambito", tablaSimbolos.get(valor).get("Ambito"));
			atts.set("Lexema", nuevaClave);
			atts.set("Token", tablaSimbolos.get(valor).get("Token"));
			
			tablaSimbolos.put(nuevaClave, atts);
			
			
			String registroObtenido = getRegistroLibre(false, "16");
			
			generado = "MOV " + registroObtenido + ", " + sufijoVariablesYFunciones + valor + new_line_windows;
			generado += "MOV " + sufijoVariablesYFunciones + nuevaClave + ", E" + registroObtenido + new_line_windows;
			generado += "MOV " + registroObtenido + ", 0" + new_line_windows;
			generado += "MOV " + sufijoVariablesYFunciones + nuevaClave + " + 2, E" + registroObtenido + new_line_windows;
			registroOcupado = nuevaClave;
			
		}
		else if (tablaSimbolos.get(valor).get("Token").equals("CTE_INTEGER")){
			String registroObtenido = getRegistroLibre(false, "32");
			
			generado += "MOV " + registroObtenido + " ," + tablaSimbolos.get(valor).get("Valor") + new_line_windows;
			
			setearOcupacionRegistro(registroObtenido, true);
			registroOcupado = registroObtenido;
		}
		codigo.add(generado);
		return registroOcupado;
	}


	public List<String> generarPreStartCode(Arbol raiz,String nombreAmbito){
		//raiz tiene la forma de main@f1@g por ejemplo
		codigo= new ArrayList<>(); //para generar cada arbol se usa el codigo global
		String[] aux=nombreAmbito.split("@");
		String nombre=nombreAmbito;
		nombre=aux[aux.length-1];//me quedo con el ultimo ambito, por ej main@f1@g se hace g. Si se comenta esta linea se mantiene el ambito
		codigo.add(new_line_windows+sufijoVariablesYFunciones+nombre+":"+new_line_windows);
		raiz.generarCodigo(this); // este metodo va a llamar a funciones de generador y a cambiar
		
		String retorno=""; //debo poner un retorno valido para cada tipo de funcion..... por ahora, VOID tiene retorno vacio y fun tiene retorno la label a una funcion 
		retorno= (String)tablaSimbolos.get(aux[aux.length-1]).get("Retorno"); // en vez de aux pudiera poner directamente nombre, pero en caso de comentar la linea se pueden tener problemas al no encontrarla en tabla de simbolos

		if((retorno!=null)&&(!retorno.equals(" "))) {
			codigo.add("MOV " + sufijoVariablesYFunciones+aux[aux.length-1]+sufijoVariablesYFunciones+"ret" +","+sufijoVariablesYFunciones+retorno+new_line_windows);
		}
		codigo.add("RET");
		return codigo;

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
				valor = (Integer) atts.get("Valor");
				if(valor!=null)//no debo generar datos para constantes enteras, se usa el valor directamente. Continue pasa a la proxima iteracion del for.
					continue;
				else
				{
				//si no tiene campo valor es una variable
				tipoDatos="DW";	
				
				}
			}
			
			   if(tipo.equals("uslinteger")) {
				   valor = (Long) atts.get("Valor");
					if(valor!=null)//no debo generar datos para constantes enteras, se usa el valor directamente. Continue pasa a la proxima iteracion del for.
						continue;
					else
					{
					//si no tiene campo valor es una variable
					tipoDatos="DD";	
					
					}
			}
			
			if(tipo.equals("cadena_caracteres")) {
				esCadena=true;
				tipoDatos="DB"; //por alguna razon los string se guardan como un DB, debe ser un puntero de 8 bits...
			}
			if(tipo.equals("fun")||tipo.equals("void")) {
				//toy tratand con direcciones de 32 bit
				tipoDatos="DD"; //32 bits direcciones
				
			}
			//String aux="_"+quitarSufijo(clave)+" "+tipoDatos +" "; //FALTA EL VALOR, EL TERMINAL NULL EN CASO DE CADENA O ? EN CASO DE VARIABLES
			
			String aux="";
			//le podria quitar el sufijo a la clave. Pero como no parece que esto se vaya a usar en si, si no para mostrar la tabla de simbolos. Puede que sea irrelevante.
			//En realidad nunca voy a usar algo como _1.... Si puedo generar el codigo directamente como inmediato
			if(esCadena) {
				aux=sufijoVariablesYFunciones+clave.replace(" ",textSeparator)+" "+tipoDatos +" ";//las cadenas se tratan distinto, hay que ponerles @ en cada separacion para que no rompa el ASM
				aux=aux + "\""+clave+"\"" + ",0" +new_line_windows; // "clave",0
				outCtes.add(aux);
			}else {

				if(valor!=null) {
					//es cte
					aux=sufijoVariablesYFunciones+clave+" "+tipoDatos +" ";
					aux+=valor+new_line_windows;
					outCtes.add(aux);
				}else {
					
					String uso=(String)atts.get("Uso");
					if(uso!=null && uso.equals("funcion")) {
						
						//estamos en presencia de una funcion. Lo correcto es generar una variable auxiliar para guardar el retorno
						//se puede chequear adicionalmente... si es de tipo void no genero una variable...
						//En la filmina de ejemplo está planteado el retorno en una variable independientemente del caso
						aux=sufijoVariablesYFunciones+clave+sufijoVariablesYFunciones+"ret"+" "+tipoDatos +" ";
						
					}else {
						aux=sufijoVariablesYFunciones+clave+" "+tipoDatos +" "; //variables de tablas de simbolos
					}
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
		//codigo.add(new_line_windows+".code"+new_line_windows);
		//ESPACIO EN BLANCO PARA GENERAR EL PRECODE: FUNCIONES Y DEMAS DE CLOSURE
		codigo.add(new_line_windows+"start:"+new_line_windows);

		for(String s: registers) {
			codigo.add("XOR " +s+","+s+new_line_windows); //limpio los registros para memory safety
		}

		
		/* PARA LIMPIAR LOS REGISTROS */
		/*
		codigo.add("XOR EAX, EAX" +new_line_windows );
		codigo.add("XOR EBX, EBX"+new_line_windows);
		codigo.add("XOR ECX, ECX"+new_line_windows);
		codigo.add("XOR EDX, EDX"+new_line_windows);*/

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
	/*public static void main(String[] args) {
		GeneradorCodigo g=new GeneradorCodigo(new Hashtable<>());
		setearOcupacionRegistro("EAX", true);
		for(String s: tablaDeOcupacion.keySet()) {
			System.out.println("valor " + s + " ocupado " + tablaDeOcupacion.get(s));

		}
	}*/
}
