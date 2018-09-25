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
    0,    1,    1,    2,    2,    2,    4,    4,    4,    4,
    4,    9,    9,    8,    8,   10,   10,   10,    5,    5,
    5,    6,   11,   11,   11,    3,    3,    3,   14,   14,
   17,   17,   19,   15,   15,   15,   15,   16,   16,   20,
   20,   20,   18,   18,   12,   12,   12,   21,   21,   13,
   13,   13,   13,   13,   13,   22,   22,   22,   22,   22,
   23,   23,   23,   24,   24,   24,   24,    7,    7,   25,
   25,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    2,    1,    1,    1,    1,
    1,    3,    3,    3,    3,    3,    3,    3,    4,    6,
    4,    3,    3,    2,    3,    1,    1,    1,    3,    3,
    1,    1,    1,   10,   10,    8,    9,    5,    5,    1,
    3,    3,    1,    3,    1,    3,    3,    1,    2,    3,
    3,    3,    3,    3,    3,    3,    3,    4,    1,    4,
    3,    3,    1,    1,    1,    1,    2,    4,    4,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,   31,   32,    0,    0,    0,    0,   33,    0,
    0,    2,    4,    5,    7,    8,    9,   10,   11,   26,
   27,   28,    0,    0,    6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    3,   43,    0,    0,
    0,    0,    0,   65,    0,    0,   71,    0,    0,   63,
    0,   13,   12,   64,    0,    0,   24,    0,    0,   45,
    0,    0,    0,   15,   14,   22,    0,   29,    0,    0,
   30,    0,   67,    0,    0,    0,    0,   69,   68,   25,
    0,    0,    0,    0,    0,    0,   23,   48,    0,   21,
    0,   19,   18,   17,   16,    0,   44,    0,    0,   66,
    0,    0,   61,   62,    0,    0,    0,    0,    0,    0,
   47,   46,   49,    0,    0,   38,    0,   60,   58,   20,
    0,    0,   40,    0,    0,   36,    0,    0,   41,   37,
    0,   35,   34,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   19,   33,
   30,   61,   55,   20,   21,   22,   23,   39,   24,  124,
   89,   56,   49,   50,   51,
};
final static short yysindex[] = {                      -139,
   36,  -22,    0,    0,  -40,  -15,  -40, -195,    0,    0,
 -139,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -192, -173,    0,   -4,  -28,    6,   -8,  -75,
 -180, -172,  -24,  -75,   52,  -21,    0,    0,   44,   52,
  -10,   57,   52,    0,   71, -123,    0,   18,   68,    0,
   -5,    0,    0,    0,   95,  -26,    0,   97, -130,    0,
 -126,   98,  -12,    0,    0,    0, -139,    0, -113, -139,
    0,    6,    0,   22,   22,   22,   22,    0,    0,    0,
    6,    6,    6,    6,    6,    6,    0,    0,  -73,    0,
  -75,    0,    0,    0,    0, -101,    0, -174,   -1,    0,
   68,   68,    0,    0,   18,   18,   18,   18,   18,   18,
    0,    0,    0, -124,   36,    0,  106,    0,    0,    0,
  -64, -139,    0,  -11,  -82,    0,    3,   36,    0,    0,
  -80,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  147,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   70,
    0,    0,   34,    0,  -39,    0,    0,    9,  -34,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -29,   13,    0,    0,  107,  108,  109,  110,  112,  113,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    2,   27,    0,   30,    0,    0,    0,    0,   42,    0,
  150,   21,  130,    0,    0,    0,    0,  137,    0,    0,
    0,   23,   16,   -6,    0,
};
final static int YYTABLESIZE=290;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         29,
   39,   66,   66,   66,   66,   66,   59,   66,   59,   59,
   59,   56,   53,   56,   56,   56,   74,   27,   75,   65,
   66,   66,   66,  116,   32,   59,   59,   59,   95,  127,
   56,   56,   56,   85,   84,   86,   46,   37,   79,  119,
   46,   74,  129,   75,  133,   42,  131,   59,   48,   36,
   46,  112,   70,   57,   66,   57,   57,   57,  122,   60,
   74,   35,   75,   60,   38,   41,   46,   47,   96,  103,
  104,   98,   57,   57,   57,   64,   64,   64,   64,   25,
   64,    1,    2,   40,    3,   62,    4,   68,   88,  101,
  102,   27,    5,   63,   99,    6,    7,    8,    9,  117,
   71,   67,   69,  105,  106,  107,  108,  109,  110,   76,
   72,  114,   70,   43,   77,   69,    1,    2,  113,    3,
   60,    4,   37,  125,   37,   39,    2,    5,   43,   90,
    6,    7,    8,    9,   73,   80,    5,   87,   93,    6,
    7,   91,   92,   97,  120,  121,    1,   54,   53,   55,
   50,   37,   51,   52,  115,    2,   34,    3,   58,    4,
   42,    0,  123,    0,    0,    5,    0,    0,    6,    7,
    8,    9,    0,  128,    2,  132,    3,    0,    4,    0,
    0,    2,  111,    2,    5,    0,    0,    6,    7,    8,
    9,    5,   35,    5,    6,    7,    6,    7,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   28,   66,    0,    0,    0,
    0,   59,    0,   66,   66,   66,   56,   52,   59,   59,
   59,   64,    0,   56,   56,   56,   81,   82,   83,   26,
   31,    0,    0,   94,  126,    0,    0,   57,   54,   44,
   78,   45,   43,   44,  118,   45,   39,   39,  130,   39,
   42,   39,   54,   44,   70,   45,    0,   39,   57,    0,
   39,   39,   39,   39,   39,   57,   57,   57,   54,   44,
    0,  100,    0,    0,    0,    0,    0,    0,    0,   64,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   42,   43,   44,   45,   41,   47,   43,   44,
   45,   41,   41,   43,   44,   45,   43,   40,   45,   44,
   60,   61,   62,  125,   40,   60,   61,   62,   41,   41,
   60,   61,   62,   60,   61,   62,   45,   11,   44,   41,
   45,   43,  125,   45,  125,   41,   44,  123,   26,    8,
   45,  125,   44,   41,   34,   43,   44,   45,  123,   30,
   43,  257,   45,   34,  257,   24,   45,   26,   67,   76,
   77,   70,   60,   61,   62,   42,   43,   44,   45,   44,
   47,  256,  257,  257,  259,  266,  261,   44,   59,   74,
   75,   40,  267,  266,   72,  270,  271,  272,  273,  274,
   44,  123,   59,   81,   82,   83,   84,   85,   86,   42,
   40,   91,  123,   44,   47,   59,  256,  257,   89,  259,
   91,  261,   96,  122,   98,  125,  257,  267,   59,  256,
  270,  271,  272,  273,  258,   41,  267,   41,   41,  270,
  271,  268,  269,  257,  269,   40,    0,   41,   41,   41,
   41,  125,   41,   41,  256,  257,    7,  259,   29,  261,
   24,   -1,  121,   -1,   -1,  267,   -1,   -1,  270,  271,
  272,  273,   -1,  256,  257,  256,  259,   -1,  261,   -1,
   -1,  257,  256,  257,  267,   -1,   -1,  270,  271,  272,
  273,  267,  257,  267,  270,  271,  270,  271,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  256,   -1,   -1,   -1,
   -1,  256,   -1,  263,  264,  265,  256,  256,  263,  264,
  265,  256,   -1,  263,  264,  265,  263,  264,  265,  262,
  256,   -1,   -1,  256,  256,   -1,   -1,  256,  257,  258,
  256,  260,  257,  258,  256,  260,  256,  257,  256,  259,
  256,  261,  257,  258,  256,  260,   -1,  267,  256,   -1,
  270,  271,  272,  273,  274,  263,  264,  265,  257,  258,
   -1,  260,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
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
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"sentencia : declarativa",
"sentencia : ejecutable",
"sentencia : error ','",
"ejecutable : sentencia_if",
"ejecutable : sentencia_while",
"ejecutable : asignacion",
"ejecutable : sentencia_impresion",
"ejecutable : id_invocacion",
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
"expr : CTE_USLINTEGER '(' expr ')'",
"expr : term",
"expr : CTE_USLINTEGER '(' expr error",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : CTE_INTEGER",
"factor : CTE_USLINTEGER",
"factor : '-' CTE_INTEGER",
"asignacion : ID ASIGN r_value_asignacion ','",
"asignacion : ID ASIGN r_value_asignacion error",
"r_value_asignacion : expr",
"r_value_asignacion : id_invocacion",
};

//#line 177 "tokens.y"


Hashtable<String, List<Object>> tablaSimbolos;
AnalizadorLexico AL = null;
List<String> estructurasGramaticalesDetectadas;
List<String> tokensLeidos;
List<String> errores;
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
 System.out.println("En linea: " + AL.nroLinea + ". Ocurrio un error de parsing ( "  + s + " ) al leer el token " + Token.tipoToken(ultimoTokenLeido));
}

public Parser(AnalizadorLexico AL, Hashtable<String, List<Object>> tablaSimbolos)

{
	//yydebug=true;
	tokensLeidos=new ArrayList<>();
	estructurasGramaticalesDetectadas=new ArrayList<>();
	errores=new ArrayList<>();
	this.AL=AL;
	this.tablaSimbolos = tablaSimbolos;

}

public List<String> getEstructurasGramaticalesDetectadas(){
	return this.estructurasGramaticalesDetectadas;
}
public List<String> getTokensLeidos(){
	return this.tokensLeidos;
}
public List<String> getErrores(){
	return this.errores;
}
private void agregarError(String e){
	errores.add(e+"\n");
}
private void agregarEstructuraDetectada(String tipo){
	String toAdd=tipo + " en linea " + AL.nroLinea + "\n";
	this.estructurasGramaticalesDetectadas.add(toAdd);

}
//#line 422 "Parser.java"
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
//#line 37 "tokens.y"
{agregarEstructuraDetectada("Asignacion");}
break;
case 10:
//#line 38 "tokens.y"
{agregarEstructuraDetectada("Impresion");}
break;
case 11:
//#line 39 "tokens.y"
{agregarEstructuraDetectada("Invocacion funcion");}
break;
case 13:
//#line 44 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta )");}
break;
case 15:
//#line 47 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta ','");}
break;
case 17:
//#line 51 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta )");}
break;
case 18:
//#line 52 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta (");}
break;
case 21:
//#line 57 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " Error en sentencia IF, falta end_if");}
break;
case 24:
//#line 68 "tokens.y"
{agregarError("En linea: " + AL.nroLinea + " falta )");}
break;
case 25:
//#line 69 "tokens.y"
{/*el -1 es porque ya se paso en la lectura*/
																		agregarError("En linea: " + (AL.nroLinea-1) + " falta (");}
break;
case 26:
//#line 75 "tokens.y"
{agregarEstructuraDetectada("Declaracion variable");}
break;
case 33:
//#line 88 "tokens.y"
{agregarEstructuraDetectada("Declaracion de tipo closure");}
break;
case 35:
//#line 92 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 36:
//#line 93 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
break;
case 37:
//#line 94 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta , ");}
break;
case 39:
//#line 101 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 42:
//#line 107 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 47:
//#line 118 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
break;
case 60:
//#line 136 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
break;
case 65:
//#line 145 "tokens.y"
{List<Object> atts = tablaSimbolos.get(val_peek(0).sval); /*$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS*/
																 int valorInteger = (Integer) atts.get(1); /*el valor en la posicion  es el número de la*/
																 if (valorInteger > 32767) /*si se pasa del limite positivo*/

																		if (!tablaSimbolos.containsKey("32767_i")){
																			List<Object> nuevosAtributos = new ArrayList<Object>();
																			nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(32767);
																			tablaSimbolos.put("32767_i", nuevosAtributos);
																			agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + AL.nroLinea);
																		}
																	}
break;
case 67:
//#line 157 "tokens.y"
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
case 69:
//#line 169 "tokens.y"
{agregarError("En linea: " + (AL.nroLinea) + " falta , ");}
break;
//#line 678 "Parser.java"
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
