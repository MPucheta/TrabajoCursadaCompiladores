package resources;



public class Token {
	public int tipoDeToken; //por ejemplo, para el literal =, el numero 17
	public String claveTablaSimbolo; //esto es por si en algunos casos se debe retornar cosas como <cte,punteroTablaSimbolo>
	
	
	public final static short ID=257;
	public final static short CTE_INTEGER=258;
	public final static short INTEGER=259;
	public final static short CTE_USLINTEGER=260;
	public final static short USLINTEGER=261;
	public final static short ASIGN=262;
	public final static short COMP_MAYOR_IGUAL=263;
	public final static short COMP_MENOR_IGUAL=264;
	public final static short COMP_DISTINTO=265;
	public final static short CADENA_CARACTERES=266;
	public final static short IF=267;
	public final static short ELSE=268;
	public final static short END_IF=269;
	public final static short PRINT=270;
	public final static short WHILE=271;
	public final static short VOID=272;
	public final static short FUN=273;
	public final static short RETURN=274;
	public final static short NEG=275;
	public final static short YYERRCODE=256;
	
	public static String tipoToken(int nro){
		switch (nro){
		case 256: return "***ERROR***";
		case 257: return "ID";
		case 258: return "CTE_INTEGER";
		case 259: return "INTEGER";
		case 260: return "CTE_USLINTEGER";
		case 261: return "USLINTEGER";
		case 262: return "ASIGN";
		case 263: return "COMP_MAYOR_IGUAL";
		case 264: return "COMP_MENOR_IGUAL";
		case 265: return "COMP_DISTINTO";
		case 266: return "CADENA_CARACTERES";
		case 267: return "IF";
		case 268: return "ELSE";
		case 269: return "END_IF";
		case 270: return "PRINT";
		case 271: return "WHILE";
		case 272: return "VOID";
		case 273: return "FUN";
		case 274: return "RETURN";
		case 275: return "ID";
		case 0: return "FIN_DE_ARCHIVO";
		
		}
		return Character.toString((char)nro);
	}

	//como se implementa la TS como un hashtable, no necesito un puntero si no la clave de acceso
	public Token(int numAsignado, String clave) {
		tipoDeToken=numAsignado;
		claveTablaSimbolo=clave;
			
	}
	
}
