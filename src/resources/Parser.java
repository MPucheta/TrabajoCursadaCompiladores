//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 3 "tokens.y"
package resources;
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

//#line 24 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
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
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    2,    2,    4,    4,    4,    4,
    4,    9,    9,   10,   10,    8,    8,   11,   11,   11,
    5,    5,    5,    6,   12,   12,   12,    3,    3,    3,
   15,   15,   15,   15,   15,   18,   18,   20,   16,   16,
   16,   16,   17,   17,   21,   21,   21,   21,   19,   19,
   13,   13,   13,   22,   22,   14,   14,   14,   14,   14,
   14,   23,   23,   23,   23,   23,   24,   24,   24,   25,
   25,   25,   25,    7,    7,   26,   26,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    2,    3,    3,    3,    3,    3,    3,    3,
    4,    6,    4,    3,    3,    2,    3,    1,    1,    1,
    3,    3,    3,    2,    2,    1,    1,    1,   10,   10,
    8,    9,    5,    5,    1,    3,    3,    2,    1,    3,
    1,    3,    3,    1,    2,    3,    3,    3,    3,    3,
    3,    3,    3,    4,    1,    4,    3,    3,    1,    1,
    1,    1,    2,    4,    3,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   36,   37,    0,    0,    0,    0,   38,    0,
    0,    3,    5,    6,    7,    8,    9,   10,   11,    0,
   28,   29,   30,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    4,   13,
   12,   34,   49,    0,    0,    0,   35,    0,    0,    0,
   75,    0,   71,   72,    0,    0,   77,    0,    0,   69,
    0,   15,   14,   70,    0,    0,   26,    0,    0,    0,
   51,    0,    0,    0,   17,   16,   24,    0,   31,   33,
   50,    0,   32,    0,   73,    0,    0,    0,    0,   74,
   27,    0,    0,    0,    0,    0,    0,   25,   54,    0,
   23,    0,   21,   20,   19,   18,    0,    0,    0,    0,
    0,   67,   68,    0,    0,    0,    0,    0,    0,   53,
   52,   55,    0,   44,   43,    0,   66,   64,   22,    0,
    0,    0,   45,    0,   48,    0,   41,    0,   47,   46,
   42,    0,   40,   39,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   19,   20,
   35,   32,   72,   65,   21,   22,   23,   24,   25,   26,
  134,  100,   66,   59,   60,   61,
};
final static short yysindex[] = {                      -195,
 -169,  -24,    0,    0,   -4,   -3,   -4, -187,    0,    0,
 -169,    0,    0,    0,    0,    0,    0,    0,    0,  -26,
    0,    0,    0, -151,   36, -142, -169,  -44,  -30,  -10,
  -17,  -77, -192, -179,    1,  -77,   51,    4,    0,    0,
    0,    0,    0,   37, -157, -137,    0,   51,    5,   49,
    0,   51,    0,    0,   82, -134,    0,   11,   21,    0,
   86,    0,    0,    0,   85,  -28,    0,  100,  -24, -160,
    0, -156,  106,  -14,    0,    0,    0, -169,    0,    0,
    0, -169,    0,  -10,    0,  -39,  -39,  -39,  -39,    0,
    0,  -10,  -10,  -10,  -10,  -10,  -10,    0,    0,  -75,
    0,  -77,    0,    0,    0,    0, -103, -188,   -1,   21,
   21,    0,    0,   11,   11,   11,   11,   11,   11,    0,
    0,    0, -121,    0,    0,  109,    0,    0,    0, -100,
   -2, -169,    0,    2,    0,  -84,    0,    3,    0,    0,
    0,  -72,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,   50,    0,    0,    0,    0,    0,    0,    0,    0,
  150,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  151,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   50,    0,    0,
    0,   95,    0,    0,    0,    0,    0,  108,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -36,
  -31,    0,    0,  114,  118,  119,  121,  122,  124,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   47,   38,    0,   19,    0,    0,    0,    0,    0,   31,
    0,  159,   16,  140,    0,    0,    0,    0,   34,    0,
    0,    0,   39,   30,   55,    0,
};
final static int YYTABLESIZE=259;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         65,
   56,   65,   65,   65,   62,   56,   62,   62,   62,   63,
   63,   63,   63,   63,   86,   29,   87,   41,   65,   65,
   65,  125,  132,   62,   62,   62,  106,   56,   63,   63,
   63,   96,   95,   97,   56,   31,   34,   29,   38,  128,
  140,   86,  138,   87,   76,   70,  142,   27,   39,  121,
   71,   77,  144,   86,   71,   87,   49,   44,   57,   50,
    1,    2,   88,    3,   39,    4,   58,   89,    2,   37,
    3,    5,    4,   73,    6,    7,    8,    9,    5,   45,
   79,    6,    7,    8,    9,  126,   74,    2,   99,    3,
   29,    4,   83,   49,   46,   46,   69,    5,   80,  101,
    6,    7,    8,    9,   42,   43,    5,   46,   49,    6,
    7,  102,  103,   47,   48,  110,  111,  123,  122,   81,
   71,   84,  109,   85,  107,   91,   78,   82,  108,   90,
  114,  115,  116,  117,  118,  119,   70,   70,   70,   70,
   98,   70,  112,  113,   39,   39,  104,  129,  130,    1,
    2,   76,  124,    2,   60,    3,  131,    4,   59,   61,
  133,   56,   57,    5,   58,   36,    6,    7,    8,    9,
   68,  139,    2,   39,    3,    0,    4,    0,  136,   69,
  120,   69,    5,  143,    0,    6,    7,    8,    9,    5,
    0,    5,    6,    7,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   51,   52,   53,   65,   54,   55,   64,   53,   62,
   54,   65,   65,   65,   63,   62,   62,   62,   62,   40,
    0,   63,   63,   63,   92,   93,   94,   28,   67,   64,
   53,  105,   54,   55,    0,    0,   64,   53,    0,   54,
   55,   30,   33,  135,  127,    0,   75,  137,  141,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   45,   43,   44,   45,   41,   45,   43,   44,   45,   41,
   41,   43,   44,   45,   43,   40,   45,   44,   60,   61,
   62,  125,  123,   60,   61,   62,   41,   45,   60,   61,
   62,   60,   61,   62,   45,   40,   40,   40,    8,   41,
  125,   43,   41,   45,   44,  123,   44,    1,   11,  125,
   32,   36,  125,   43,   36,   45,   26,   24,   28,   26,
  256,  257,   42,  259,   27,  261,   28,   47,  257,  257,
  259,  267,  261,  266,  270,  271,  272,  273,  267,   44,
   44,  270,  271,  272,  273,  274,  266,  257,   70,  259,
   40,  261,   44,   44,   59,   59,  257,  267,  256,  256,
  270,  271,  272,  273,  256,  257,  267,   59,   59,  270,
  271,  268,  269,  256,  257,   86,   87,  102,  100,  257,
  102,   40,   84,  258,   78,   41,  123,  123,   82,   44,
   92,   93,   94,   95,   96,   97,   42,   43,   44,   45,
   41,   47,   88,   89,  107,  108,   41,  269,   40,    0,
    0,   44,  256,  257,   41,  259,  257,  261,   41,   41,
  130,   41,   41,  267,   41,    7,  270,  271,  272,  273,
   31,  256,  257,  136,  259,   -1,  261,   -1,  132,  257,
  256,  257,  267,  256,   -1,  270,  271,  272,  273,  267,
   -1,  267,  270,  271,  270,  271,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,  256,  260,  261,  257,  258,  256,
  260,  263,  264,  265,  256,  256,  263,  264,  265,  256,
   -1,  263,  264,  265,  263,  264,  265,  262,  256,  257,
  258,  256,  260,  261,   -1,   -1,  257,  258,   -1,  260,
  261,  256,  256,  256,  256,   -1,  256,  256,  256,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE_INTEGER","INTEGER",
"CTE_USLINTEGER","USLINTEGER","ASIGN","COMP_MAYOR_IGUAL","COMP_MENOR_IGUAL",
"COMP_DISTINTO","CADENA_CARACTERES","IF","ELSE","END_IF","PRINT","WHILE","VOID",
"FUN","RETURN","NEG",
};
final static String yyrule[] = {
"$accept : programa",
"programa : conjunto_sentencias",
"programa : error conjunto_sentencias",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"sentencia : declarativa",
"sentencia : ejecutable",
"ejecutable : sentencia_if",
"ejecutable : sentencia_while",
"ejecutable : asignacion",
"ejecutable : sentencia_impresion",
"ejecutable : invocacion",
"invocacion : id_invocacion ','",
"invocacion : id_invocacion error",
"id_invocacion : ID '(' ')'",
"id_invocacion : ID '(' error",
"sentencia_impresion : PRINT cadena_cararacteres_entre_parentesis ','",
"sentencia_impresion : PRINT cadena_cararacteres_entre_parentesis error",
"cadena_cararacteres_entre_parentesis : '(' CADENA_CARACTERES ')'",
"cadena_cararacteres_entre_parentesis : '(' CADENA_CARACTERES error",
"cadena_cararacteres_entre_parentesis : error CADENA_CARACTERES ')'",
"sentencia_if : IF condicion_entre_parentesis bloque_sentencias END_IF",
"sentencia_if : IF condicion_entre_parentesis bloque_sentencias ELSE bloque_sentencias END_IF",
"sentencia_if : IF condicion_entre_parentesis bloque_sentencias error",
"sentencia_while : WHILE condicion_entre_parentesis bloque_sentencias",
"condicion_entre_parentesis : '(' condicion ')'",
"condicion_entre_parentesis : '(' error",
"condicion_entre_parentesis : error condicion ')'",
"declarativa : declaracion_variables",
"declarativa : declaracion_closure",
"declarativa : declaracion_funcion_simple",
"declaracion_variables : tipo_variable lista_variables ','",
"declaracion_variables : tipo_closure lista_variables ','",
"declaracion_variables : lista_variables ',' error",
"declaracion_variables : tipo_variable error",
"declaracion_variables : tipo_closure error",
"tipo_variable : INTEGER",
"tipo_variable : USLINTEGER",
"tipo_closure : FUN",
"declaracion_closure : tipo_closure id_invocacion '{' conjunto_sentencias RETURN '(' retorno_closure ')' ',' '}'",
"declaracion_closure : tipo_closure id_invocacion '{' conjunto_sentencias RETURN '(' retorno_closure ')' ',' error",
"declaracion_closure : tipo_closure id_invocacion '{' conjunto_sentencias RETURN '(' retorno_closure error",
"declaracion_closure : tipo_closure id_invocacion '{' conjunto_sentencias RETURN '(' retorno_closure ')' error",
"declaracion_funcion_simple : VOID id_invocacion '{' conjunto_sentencias '}'",
"declaracion_funcion_simple : VOID id_invocacion '{' conjunto_sentencias error",
"retorno_closure : id_invocacion",
"retorno_closure : '{' conjunto_sentencias '}'",
"retorno_closure : '{' conjunto_sentencias error",
"retorno_closure : ID error",
"lista_variables : ID",
"lista_variables : lista_variables ';' ID",
"bloque_sentencias : ejecutable",
"bloque_sentencias : '{' sentencias_ejecutables '}'",
"bloque_sentencias : '{' sentencias_ejecutables error",
"sentencias_ejecutables : ejecutable",
"sentencias_ejecutables : sentencias_ejecutables ejecutable",
"condicion : expr '=' expr",
"condicion : expr '<' expr",
"condicion : expr '>' expr",
"condicion : expr COMP_MENOR_IGUAL expr",
"condicion : expr COMP_MAYOR_IGUAL expr",
"condicion : expr COMP_DISTINTO expr",
"expr : expr '+' term",
"expr : expr '-' term",
"expr : USLINTEGER '(' expr ')'",
"expr : term",
"expr : USLINTEGER '(' expr error",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : CTE_INTEGER",
"factor : CTE_USLINTEGER",
"factor : '-' CTE_INTEGER",
"asignacion : ID ASIGN r_value_asignacion ','",
"asignacion : ID ASIGN error",
"r_value_asignacion : expr",
"r_value_asignacion : id_invocacion",
};

//#line 183 "tokens.y"


Hashtable<String, List<Object>> tablaSimbolos;
AnalizadorLexico AL = null;
List<String> estructurasGramaticalesDetectadas;
List<String> tokensLeidos;
List<String> erroresDetallados;
List<String> erroresGenerales;
Token t;
int ultimoTokenLeido;

int yylex(){
	t = AL.getToken();
	yylval = new ParserVal(t.claveTablaSimbolo);
	ultimoTokenLeido= t.tipoDeToken;
	//la siguiente condicion se debe hacer porque estas estructuras (ej, if, while) ocupan varias lineas de texto
	//por lo que cuando el parsing detecta finalmente que un if termina en un end_if, el AL.nroLinea ya avanzo.
	//Por lo tanto sin esto el nroLinea mostrado seria el del fin de la estructura y no del comienzo
	if(ultimoTokenLeido==Token.IF || ultimoTokenLeido ==Token.WHILE){
		agregarEstructuraDetectada("Sentencia " + Token.tipoToken(ultimoTokenLeido));
	}
	String leido= "Linea: " + AL.nroLinea + " Token leido: " + ultimoTokenLeido + " designado como: " + Token.tipoToken(ultimoTokenLeido) + "\n";
	tokensLeidos.add(leido);
	return ultimoTokenLeido;

}

void yyerror(String s)
{
	String err= "En linea: " + AL.nroLinea + ". Ocurrio un error de parsing ( "  + s + " ) al leer el token " + Token.tipoToken(ultimoTokenLeido) +"\n"; //tambien se puede usar yychar en vez de lo del ultimoTokenLeido
	this.erroresGenerales.add(err);
	System.out.println(err);
}

public Parser(AnalizadorLexico AL, Hashtable<String, List<Object>> tablaSimbolos)

{
	//yydebug=true;
	tokensLeidos=new ArrayList<>();
	estructurasGramaticalesDetectadas=new ArrayList<>();
	erroresDetallados=new ArrayList<>();
	erroresGenerales=new ArrayList<>();
	this.AL=AL;
	this.tablaSimbolos = tablaSimbolos;

}

public List<String> getEstructurasGramaticalesDetectadas(){
	return this.estructurasGramaticalesDetectadas;
}
public List<String> getTokensLeidos(){
	return this.tokensLeidos;
}
public List<String> getErroresDetallados(){
	return this.erroresDetallados;
}
public List<String> getErroresGenerales(){
	return this.erroresGenerales;
}
private void agregarError(String e){
	erroresDetallados.add(e+"\n");
}
private void agregarEstructuraDetectada(String tipo){
	String toAdd=tipo + " en linea " + AL.nroLinea + "\n";
	this.estructurasGramaticalesDetectadas.add(toAdd);

}
//#line 432 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 9:
//#line 36 "tokens.y"
{agregarEstructuraDetectada("Asignacion");}
break;
case 12:
//#line 41 "tokens.y"
{agregarEstructuraDetectada("Invocacion funcion");}
break;
case 13:
//#line 42 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta ','");}
break;
case 15:
//#line 45 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta )");}
break;
case 16:
//#line 47 "tokens.y"
{agregarEstructuraDetectada("Impresion");}
break;
case 17:
//#line 48 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta ','");}
break;
case 19:
//#line 52 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta )");}
break;
case 20:
//#line 53 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta (");}
break;
case 23:
//#line 58 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " Error en sentencia IF, falta end_if");}
break;
case 26:
//#line 69 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta )");}
break;
case 27:
//#line 70 "tokens.y"
{/*el -1 es porque ya se paso en la lectura*/
																		agregarError("En linea: " + (AL.nroLinea-1) + " falta (");}
break;
case 31:
//#line 81 "tokens.y"
{agregarEstructuraDetectada("Declaracion variable");}
break;
case 32:
//#line 82 "tokens.y"
{agregarEstructuraDetectada("Declaracion de tipo closure");}
break;
case 33:
//#line 83 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " Error en declaracion de tipo");}
break;
case 34:
//#line 84 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " Error en declaracion de tipo, falta ID o ',' ");}
break;
case 35:
//#line 85 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " Error en definicion de closure");}
break;
case 39:
//#line 95 "tokens.y"
{agregarEstructuraDetectada("Declaracion de tipo closure");}
break;
case 40:
//#line 96 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 41:
//#line 97 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
break;
case 42:
//#line 98 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta , ");}
break;
case 44:
//#line 105 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 47:
//#line 111 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 48:
//#line 112 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " Retorno no es de tipo closure, se espera ID()");}
break;
case 53:
//#line 123 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 64:
//#line 139 "tokens.y"
{agregarEstructuraDetectada("Conversion explicita en línea: " + AL.nroLinea);}
break;
case 66:
//#line 141 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
break;
case 71:
//#line 150 "tokens.y"
{List<Object> atts = tablaSimbolos.get(val_peek(0).sval); /*$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS*/
																		 int valorInteger = (Integer) atts.get(1); /*el valor en la posicion 1 es el número de la*/
																		 if (valorInteger > 32767) /*si se pasa del limite positivo*/

																				if (!tablaSimbolos.containsKey("32767_i")){
																					List<Object> nuevosAtributos = new ArrayList<Object>();
																					nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(32767);
																					tablaSimbolos.put("32767_i", nuevosAtributos);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + AL.nroLinea);
																				}

																			}
break;
case 73:
//#line 163 "tokens.y"
{
																		int valorInteger = (Integer) tablaSimbolos.get(val_peek(0).sval).get(1);
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			List<Object> nuevosAtributos = new ArrayList<Object>();
																			nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, nuevosAtributos);
																			}
																		}
break;
case 75:
//#line 175 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " Error detectado de lado derecho de asignacion ");}
break;
//#line 717 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
