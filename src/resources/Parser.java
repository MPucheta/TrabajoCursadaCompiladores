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
    4,    9,   10,   10,    8,    8,   11,   11,   11,    5,
    5,    5,    6,   12,   12,    3,    3,    3,   15,   15,
   15,   15,   15,   18,   18,   20,   16,   16,   16,   16,
   16,   17,   17,   21,   21,   19,   19,   13,   13,   13,
   22,   22,   14,   14,   14,   14,   14,   14,   14,   23,
   23,   23,   23,   25,   25,   25,   24,   24,   24,   26,
   26,   26,   26,   26,    7,    7,   27,   27,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    3,    3,    3,    3,    3,    3,    3,    4,
    6,    4,    3,    3,    2,    1,    1,    1,    3,    3,
    3,    2,    2,    1,    1,    1,   10,   10,    8,    9,
    6,    5,    5,    1,    3,    1,    3,    1,    3,    3,
    1,    2,    3,    3,    3,    3,    3,    3,    1,    3,
    3,    1,    1,    4,    4,    4,    3,    3,    1,    1,
    1,    1,    2,    2,    4,    4,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   34,   35,    0,    0,    0,    0,   36,    0,
    0,    3,    5,    6,    7,    8,    9,   10,   11,    0,
   26,   27,   28,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    4,   12,   32,
   46,    0,    0,    0,   33,    0,    0,    0,    0,    0,
   71,   72,    0,    0,   78,    0,    0,   62,   69,    0,
   14,   13,    0,   70,    0,    0,    0,    0,   48,    0,
    0,    0,   16,   15,   23,    0,   29,   31,   47,    0,
   30,    0,    0,   74,   73,    0,    0,    0,    0,   76,
   75,   24,    0,    0,    0,    0,    0,    0,   51,    0,
   22,    0,   20,   19,   18,   17,    0,    0,    0,    0,
    0,    0,   67,   68,    0,    0,    0,    0,    0,    0,
   50,   49,   52,    0,   43,   42,    0,   66,   65,   64,
   21,   41,    0,    0,   44,    0,    0,   39,    0,   45,
   40,    0,   38,   37,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   19,   20,
   34,   31,   70,   65,   21,   22,   23,   24,   25,   26,
  136,  100,   56,   57,   58,   59,   60,
};
final static short yysindex[] = {                       -66,
 -152,  -31,    0,    0,  -15,  -39,  -15, -205,    0,    0,
 -152,    0,    0,    0,    0,    0,    0,    0,    0,   28,
    0,    0,    0, -215,   15, -143, -152,   40,  -24,   48,
  -86, -180, -170,  -38,  -86,   60,  -42,    0,    0,    0,
    0,   17, -153, -141,    0,   60,    3,   19,   94,   60,
    0,    0,   96, -209,    0,   44,    8,    0,    0,  -37,
    0,    0,   94,    0,   91,   30,  -31, -169,    0, -185,
   97,  -23,    0,    0,    0, -152,    0,    0,    0, -152,
    0,   54,   54,    0,    0,   59,   59,   59,   59,    0,
    0,    0,   54,   54,   54,   54,   54,   54,    0, -102,
    0,  -86,    0,    0,    0,    0, -114,   61,   67,   -7,
    8,    8,    0,    0,   44,   44,   44,   44,   44,   44,
    0,    0,    0, -132,    0,    0,  -35,    0,    0,    0,
    0,    0,  -69, -152,    0,  -19,  -95,    0,  -36,    0,
    0, -115,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,   20,    0,    0,    0,    0,    0,    0,    0,    0,
  140,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  148,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   20,    0,    0,    0,   23,
    0,    0,    0,    0,    0,    4,  -41,    0,    0,    0,
    0,    0,  -12,    0,  -84,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -29,  -17,    0,    0,   -6,   -1,    5,   10,   16,   21,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   55,   42,    0,   92,    0,    0,    0,    0,    0,   69,
    0,  144,  -22,    0,    0,    0,    0,    0,   34,    0,
    0,    0,  116,   38,    0,   41,    0,
};
final static int YYTABLESIZE=335;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   33,   63,   63,   63,  133,   74,   91,  142,   29,  144,
  126,   60,   75,   60,   60,   60,   62,  106,   63,   63,
   63,  139,  122,   61,   30,   61,   61,   61,   59,  140,
   60,   60,   60,  130,   57,   86,   68,   87,   25,   56,
   40,   41,   61,   61,   61,   58,   84,   77,   85,   88,
   53,   36,   38,  134,   89,   27,   54,   42,   43,   48,
   77,   55,   81,   46,   70,   70,   70,   70,   38,   70,
  101,   39,   86,   44,   87,   44,   37,   44,   46,  124,
   76,   63,  102,  103,   54,   71,   86,   67,   87,   97,
   96,   98,   54,   60,   47,   72,   55,    5,   54,   29,
    6,    7,   78,   54,    2,   61,    3,  128,    4,   86,
   59,   87,   45,   46,    5,   79,   57,    6,    7,    8,
    9,   56,   69,  111,  112,   80,   69,   58,  113,  114,
  107,   92,   53,   82,  108,   83,  131,  104,   54,    1,
  143,  125,    2,   55,    3,   66,    4,    2,   38,   38,
   35,    0,    5,  121,   67,    6,    7,    8,    9,   99,
    0,    2,    0,    3,    5,    4,    0,    6,    7,    0,
   67,    5,   25,    0,    6,    7,    8,    9,   38,    0,
    5,    0,   25,    6,    7,   25,   25,   36,  137,    1,
    2,  123,    3,   69,    4,    0,    0,  109,  110,    0,
    5,  135,    0,    6,    7,    8,    9,    0,  115,  116,
  117,  118,  119,  120,   63,   63,   32,   73,   90,  141,
  132,   63,   63,   63,    0,   63,   60,   60,   63,   63,
   28,   61,  105,   60,   60,   60,  138,   60,   61,   61,
   60,   60,    0,    0,   59,   61,   61,   61,  129,   61,
   57,    0,   61,   61,   59,   56,    0,   59,   59,   77,
   57,   58,    0,   57,   57,   56,   53,    0,   56,   56,
    0,   58,   54,    0,   58,   58,   53,   55,   70,   53,
   53,    0,   54,    0,    0,   54,   54,   55,    0,    0,
   55,   55,   93,   94,   95,   49,   50,   51,    0,   52,
   53,    0,    0,   63,   64,   51,    0,   52,   53,   49,
   64,   51,    0,   52,   53,   64,   51,    2,   52,    3,
    0,    4,    0,    0,    0,    0,    0,    5,    0,    0,
    6,    7,    8,    9,  127,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   44,   45,   40,   44,   44,   44,   40,  125,
  125,   41,   35,   43,   44,   45,   41,   41,   60,   61,
   62,   41,  125,   41,   40,   43,   44,   45,   41,  125,
   60,   61,   62,   41,   41,   43,  123,   45,  123,   41,
  256,  257,   60,   61,   62,   41,  256,   44,  258,   42,
   41,  257,   11,  123,   47,    1,   41,   24,   44,   26,
   44,   41,   44,   44,   42,   43,   44,   45,   27,   47,
  256,   44,   43,   59,   45,   59,    8,   59,   59,  102,
  123,  123,  268,  269,   45,  266,   43,  257,   45,   60,
   61,   62,   45,  123,   26,  266,   28,  267,   45,   40,
  270,  271,  256,   45,  257,  123,  259,   41,  261,   43,
  123,   45,  256,  257,  267,  257,  123,  270,  271,  272,
  273,  123,   31,   86,   87,  123,   35,  123,   88,   89,
   76,   41,  123,   40,   80,   40,  269,   41,  123,    0,
  256,  256,  257,  123,  259,   30,  261,    0,  107,  108,
    7,   -1,  267,  256,  257,  270,  271,  272,  273,   68,
   -1,  257,   -1,  259,  267,  261,   -1,  270,  271,   -1,
  257,  267,  257,   -1,  270,  271,  272,  273,  137,   -1,
  267,   -1,  267,  270,  271,  270,  271,  257,  134,  256,
  257,  100,  259,  102,  261,   -1,   -1,   82,   83,   -1,
  267,  133,   -1,  270,  271,  272,  273,   -1,   93,   94,
   95,   96,   97,   98,  256,  257,  256,  256,  256,  256,
  256,  263,  264,  265,   -1,  267,  256,  257,  270,  271,
  262,  256,  256,  263,  264,  265,  256,  267,  256,  257,
  270,  271,   -1,   -1,  257,  263,  264,  265,  256,  267,
  257,   -1,  270,  271,  267,  257,   -1,  270,  271,  256,
  267,  257,   -1,  270,  271,  267,  257,   -1,  270,  271,
   -1,  267,  257,   -1,  270,  271,  267,  257,  256,  270,
  271,   -1,  267,   -1,   -1,  270,  271,  267,   -1,   -1,
  270,  271,  263,  264,  265,  256,  257,  258,   -1,  260,
  261,   -1,   -1,  256,  257,  258,   -1,  260,  261,  256,
  257,  258,   -1,  260,  261,  257,  258,  257,  260,  259,
   -1,  261,   -1,   -1,   -1,   -1,   -1,  267,   -1,   -1,
  270,  271,  272,  273,  274,
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
"condicion_entre_parentesis : '(' condicion",
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
"declaracion_closure : tipo_closure id_invocacion '{' conjunto_sentencias RETURN error",
"declaracion_funcion_simple : VOID id_invocacion '{' conjunto_sentencias '}'",
"declaracion_funcion_simple : VOID id_invocacion '{' conjunto_sentencias error",
"retorno_closure : id_invocacion",
"retorno_closure : '{' conjunto_sentencias '}'",
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
"condicion : error",
"expr : expr '+' term",
"expr : expr '-' term",
"expr : casting",
"expr : term",
"casting : USLINTEGER '(' expr ')'",
"casting : USLINTEGER '(' expr error",
"casting : error '(' expr ')'",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : CTE_INTEGER",
"factor : CTE_USLINTEGER",
"factor : '-' CTE_INTEGER",
"factor : '-' error",
"asignacion : ID ASIGN r_value_asignacion ','",
"asignacion : ID ASIGN r_value_asignacion error",
"r_value_asignacion : expr",
"r_value_asignacion : id_invocacion",
};

//#line 189 "tokens.y"


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
	switch(ultimoTokenLeido){
		case(Token.IF):
				agregarEstructuraDetectada("Sentencia " + Token.tipoToken(ultimoTokenLeido)); break;
		case(Token.WHILE):
				agregarEstructuraDetectada("Sentencia " + Token.tipoToken(ultimoTokenLeido));break;
		default:
				break;

	}

	String leido= "Linea: " + AL.nroLinea + ". Token leido: '" + ultimoTokenLeido + "' reconocido como: " + Token.tipoToken(ultimoTokenLeido) + "\n";
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
//#line 456 "Parser.java"
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
case 14:
//#line 45 "tokens.y"
{agregarError("Error: falta ')' en invocacion. Linea: " + AL.nroLinea);}
break;
case 15:
//#line 47 "tokens.y"
{agregarEstructuraDetectada("Impresion");}
break;
case 16:
//#line 48 "tokens.y"
{agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + AL.nroLinea);}
break;
case 18:
//#line 52 "tokens.y"
{agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + AL.nroLinea);}
break;
case 19:
//#line 53 "tokens.y"
{agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + AL.nroLinea);}
break;
case 22:
//#line 58 "tokens.y"
{agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + AL.nroLinea);}
break;
case 24:
//#line 68 "tokens.y"
{agregarEstructuraDetectada("Condicion");}
break;
case 25:
//#line 69 "tokens.y"
{/*esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar*/
														 								/*que se coman tokens de mas e incluso no informar el errores*/
																						agregarError("Error: falta ')' luego de la condicion. Linea: " + AL.nroLinea);}
break;
case 29:
//#line 81 "tokens.y"
{agregarEstructuraDetectada("Declaracion variable/s");}
break;
case 31:
//#line 83 "tokens.y"
{agregarError("Error: declaracion de tipo erronea. Linea: " + AL.nroLinea);}
break;
case 32:
//#line 84 "tokens.y"
{agregarError("Error: falta ID o ',' en la declaracion de variable/s. Linea: " + AL.nroLinea);}
break;
case 33:
//#line 85 "tokens.y"
{agregarError("Error: definicion de closure erronea. Linea: " + AL.nroLinea);}
break;
case 36:
//#line 92 "tokens.y"
{/*lo hago aca para que tome la primer linea incluso en funcion closure*/
													agregarEstructuraDetectada("Declaracion de tipo closure"); }
break;
case 38:
//#line 97 "tokens.y"
{agregarError("Error: falta '}' de cierre de la declaracion de closure. Linea: " + AL.nroLinea);}
break;
case 39:
//#line 98 "tokens.y"
{agregarError("Error: falta ')' luego del retorno del closure. Linea: " + AL.nroLinea);}
break;
case 40:
//#line 99 "tokens.y"
{agregarError("Error: falta ',' luego del retorno del closure. Linea: " + AL.nroLinea);}
break;
case 41:
//#line 100 "tokens.y"
{agregarError("Error: retorno no es de tipo closure. Se espera \"return( ID() )\" o \"return( {SENTENCIAS} )\"");}
break;
case 42:
//#line 106 "tokens.y"
{agregarEstructuraDetectada("Declaracion de funcion simple");}
break;
case 43:
//#line 107 "tokens.y"
{agregarError("Error: falta '}' de cierre de la funcion. Linea: " + AL.nroLinea);}
break;
case 50:
//#line 123 "tokens.y"
{agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " + AL.nroLinea);}
break;
case 59:
//#line 135 "tokens.y"
{agregarError("Error: condicion no válida. Incorrecta mezcla de expresiones y comparador. Linea: " + AL.nroLinea);}
break;
case 64:
//#line 144 "tokens.y"
{agregarEstructuraDetectada("Conversion explicita");}
break;
case 65:
//#line 145 "tokens.y"
{agregarError("Error: falta ')' en la conversion explicita. Linea: " + AL.nroLinea);}
break;
case 66:
//#line 146 "tokens.y"
{agregarError("Error: tipo no válido para conversion. Linea: " + AL.nroLinea);}
break;
case 71:
//#line 155 "tokens.y"
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
//#line 168 "tokens.y"
{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get(val_peek(0).sval).get(1);
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			List<Object> nuevosAtributos = new ArrayList<Object>();
																			nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, nuevosAtributos);
																			}
																		}
break;
case 74:
//#line 177 "tokens.y"
{agregarError("Error: negacion no permitida a este operando. Linea: " + AL.nroLinea);}
break;
case 76:
//#line 181 "tokens.y"
{agregarError("Error: lado derecho de la asignacion mal definido. Linea: " + AL.nroLinea);}
break;
case 78:
//#line 184 "tokens.y"
{agregarEstructuraDetectada("Invocacion de funcion en asignacion");}
break;
//#line 751 "Parser.java"
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
