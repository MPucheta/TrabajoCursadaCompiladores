package resources;
import java.util.*; //hash, list

public class AnalizadorLexico {
	public int pos = 0;
	public int nroLinea = 1; //se comienza a leer el archivo desde la línea 1 
	public String buffer;
	public int punteroTablaSimbolos = 0; //este se debe incrementar por cada entrada hecha en la tabla de simbolos 
	static final int FINAL = 100;
	static final int ERROR = -1;
	static final AccionSemantica1 AS1 = new AccionSemantica1();
	static final AccionSemantica2 AS2 = new AccionSemantica2();
	static final AccionSemantica3 AS3 = new AccionSemantica3();
	static final AccionSemantica4 AS4 = new AccionSemantica4();
	static final AccionSemantica5 AS5 = new AccionSemantica5();
	static final AccionSemantica6 AS6 = new AccionSemantica6();
	static final AccionSemantica7 AS7 = new AccionSemantica7();
	static final AccionSemantica8 AS8 = new AccionSemantica8();
	static final AccionSemantica9 AS9 = new AccionSemantica9();
	
	String fuente;
	public Hashtable<String , List<Object>> tablaSimbolos; 
	
	//no descartar definir una interfaz llamada Symbol 
	//List Object va a estar conformada por... <tipoToken, Lexema> <String, valor concreto (Character, Integer)>
	public Hashtable<String,Integer> mapeoTipoTokens; //por ejemplo, ID 50, CTE 60. IF 20. Son distintos a los que hay en equivalencia. Repensar
	
	Set<String> palabrasReservadas;
	
	
	static final int[][] matrizTransicionEstados = {
		/* 0*/	{ERROR,     10,      3,     10,     10,     10,      1,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,      6,      7,      8,      9,     12,     11,      0,      0,      0},
		/* 1*/	{    2,      2,      2,      2,      2,      2,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR},
		/* 2*/	{    2,      2,      2,      2,      2,      2,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL},
		/* 3*/	{ERROR,  ERROR,      3,  ERROR,  ERROR,  ERROR,      4,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR},
		/* 4*/	{ERROR,  ERROR,  ERROR,  FINAL,  ERROR,      5,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR},
		/* 5*/	{ERROR,  ERROR,  ERROR,  ERROR,  FINAL,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR},
		/* 6*/	{ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  FINAL,  ERROR,  ERROR,  FINAL,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR},
		/* 7*/	{FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL},
		/* 8*/	{FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL},
		/* 9*/	{ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  FINAL,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR,  ERROR},
		/*10*/	{FINAL,     10,  FINAL,     10,     10,     10,     10,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL,  FINAL},
		/*11*/	{   11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,     11,      0,     11,     11,     11},
		/*12*/	{   12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,     12,  FINAL,     12,     12,     12,  ERROR}
		/*        letra   minus dígito       i       l       u       _       +	     -	     *	     /	     {	     }	     (	     )	     ;	     ,	     =	     :	      <	     >	     !	     '	     #	 blanco	    tab	    \n
		 */	};
	
	static final AccionSemantica[][] matrizAccionesSemanticas = {
			/* 0*/	{ null,    AS3,    AS3,    AS3,    AS3,    AS3,   null,    AS7,    AS7,    AS7,    AS7,    AS7,    AS7,    AS7,    AS7,    AS7,    AS7,    AS7,    AS3,    AS3,    AS3,    AS3,   null,   null,   null,   null,    AS9},
			/* 1*/	{  AS3,    AS3,    AS3,    AS3,    AS3,    AS3,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null},
			/* 2*/	{  AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1,    AS1},
			/* 3*/	{ null,   null,    AS3,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null},
			/* 4*/	{ null,   null,   null,    AS4,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null},
			/* 5*/	{ null,   null,   null,   null,    AS5,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null},
			/* 6*/	{ null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,    AS7,   null,   null,   null,   null,   null,   null,   null,   null,   null},
			/* 7*/	{  AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS7,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8},
			/* 8*/	{  AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS7,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8,    AS8},
			/* 9*/	{ null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,    AS7,   null,   null,   null,   null,   null,   null,   null,   null,   null},
			/*10*/	{  AS6,    AS3,    AS6,    AS3,    AS3,    AS3,    AS3,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6,    AS6},
			/*11*/	{ null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,   null,    AS9},
			/*12*/	{  AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS3,    AS2,    AS3,    AS3,    AS3,   null}
			/*        letra   minus dígito       i       l       u       _       +	     -	     *	     /	     {	     }	     (	     )	     ;	     ,	     =	     :	      <	     >	     !	     '	     #	 blanco	    tab	    \n
			 */
	};
	
	
	
	public AnalizadorLexico(String programa){ //se agrega tabla de simbolos a los parametros?
		pos = 0;
		fuente = programa;
		tablaSimbolos= new Hashtable<>();
		mapeoTipoTokens= new Hashtable<>();
		
		palabrasReservadas = new HashSet<>();
		palabrasReservadas.add("if");
		palabrasReservadas.add("else");
		palabrasReservadas.add("end_if");
		palabrasReservadas.add("print");
		palabrasReservadas.add("integer");
		palabrasReservadas.add("uslinteger");
		palabrasReservadas.add("while");
		palabrasReservadas.add("void");
		palabrasReservadas.add("fun");
		palabrasReservadas.add("return");
		
		mapeoTipoTokens.put("if",(int) Tokens.IF);
		mapeoTipoTokens.put("else",(int) Tokens.ELSE);
		mapeoTipoTokens.put("end_if",(int) Tokens.END_IF);
		mapeoTipoTokens.put("print",(int) Tokens.PRINT);
		mapeoTipoTokens.put("integer",(int) Tokens.INTEGER);
		mapeoTipoTokens.put("uslinteger",(int) Tokens.USLINTEGER);
		mapeoTipoTokens.put("while",(int) Tokens.WHILE);
		mapeoTipoTokens.put("void",(int) Tokens.VOID);
		mapeoTipoTokens.put("fun",(int) Tokens.FUN);
		mapeoTipoTokens.put("return",(int) Tokens.RETURN);
		
		mapeoTipoTokens.put(":=",(int) Tokens.ASIGN);
		mapeoTipoTokens.put("!=",(int) Tokens.COMP_DISTINTO);
		mapeoTipoTokens.put(">=",(int) Tokens.COMP_MAYOR_IGUAL);
		mapeoTipoTokens.put("<=",(int) Tokens.COMP_MENOR_IGUAL);
		
	}
	
	
	
	public int yylex(){
		
		char c ;
		buffer = "";
		Token token=null;
		for (int estado = 0; (estado != FINAL) && (estado != ERROR);){
			if(pos>=fuente.length()) //poniendo este chequeo aca evita que salten excepciones y permite que devuelva el token de error, indicando que el token que aparecio no es valido
				return -1;
			c = fuente.charAt(pos++);
			int columna = equivalencia(c);
			int proximoEstado = matrizTransicionEstados[estado][columna];
			if(matrizAccionesSemanticas[estado][columna]!=null)
				token = matrizAccionesSemanticas[estado][columna].ejecutar(this, c);
			
			
			estado = proximoEstado;
		
		
		if (estado == FINAL) {
			
			return (token!=null)? token.tipoDeToken:-1;
			
			}
		}
		return -1;
	}
	
	private int equivalencia(char c){
		if (c == 'i')
			return 3;
		if (c == 'l')
			return 4;
		if (c == 'u')
			return 5;
		if ((int)c >= 97 && (int)c <= 122) //letras minusculas
			return 1;
		if ((int)c >= 65 && (int)c <= 90) //otras letras 
			return 0;
		if ((int)c >= 48 && (int)c <= 57) //dígitos
			return 2;
		if (c == '_')
			return 6;
		if (c == '+')
			return 7;
		if (c == '-')
			return 8;
		if (c == '*')
			return 9;
		if (c == '/')
			return 10;
		if (c == '{')
			return 11;
		if (c == '}')
			return 12;
		if (c == '(')
			return 13;
		if (c == ')')
			return 14;
		if (c == ';')
			return 15;
		if (c == ',')
			return 16;
		if (c == '=')
			return 17;
		if (c == ':')
			return 18;
		if (c == '<')
			return 19;
		if (c == '>')
			return 20;
		if (c == '!')
			return 21;
		if (c == '\'')
			return 22;
		if (c == '#')
			return 23;
		if ((int)c == 32) //espacio en blanco
			return 24;
		if ((int)c == 9)  //tab
			return 25;
		if ((int)c == 10) //salto de linea,\n. Si el programa tiene \r\n, se debe reemplazar con String.replaceAll
			return 26;
		
		return ERROR;
	}
	
	public boolean existePalabraReservada(String palabra){
		return palabrasReservadas.contains(palabra);
	}
	
	public void altaEnTablaSimbolos(String clave, Object ... atributos) {
		//metodo pensado para dar de alta en la tabla de simbolos. La cantidad de atributos es variable, de esta manera
		//no es necesario cambiar en cada archivo como se da de alta.
		
		
		//SE ESPERA DE IGUAL MANERA QUE LOS ATRIBUTOS SEAN PRESENTADOS COMO TIPODATO,TIPOTOKEN,LEXEMA
		List<Object> nuevosAtributos= new ArrayList<Object>();
		
		for(Object o: atributos) {
			nuevosAtributos.add(o);
			
		}
		this.tablaSimbolos.put(clave, nuevosAtributos);
	}
	
	public void inicializarBuffer(){
		buffer = "";
	}
	
	public int ASCIIToken(char c) {
		return (int) c;
		
	}
	
}
