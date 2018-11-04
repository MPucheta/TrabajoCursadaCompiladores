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






//#line 3 ".\gramatica.y"
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
    4,    9,    9,   10,   10,    8,    8,    8,   11,   11,
   11,   11,    5,    5,    5,    6,   12,   12,   12,    3,
    3,    3,   15,   15,   15,   15,   15,   18,   18,   20,
   16,   16,   16,   16,   16,   17,   17,   21,   21,   19,
   19,   13,   13,   13,   22,   22,   14,   14,   14,   14,
   14,   14,   14,   23,   23,   23,   25,   25,   25,   24,
   24,   24,   26,   26,   26,   26,   26,   26,    7,    7,
    7,   27,   27,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    2,    3,    3,    3,    2,    3,    3,    3,
    3,    3,    4,    6,    4,    3,    3,    3,    2,    1,
    1,    1,    3,    3,    3,    2,    2,    1,    1,    1,
   10,   10,    8,    9,    6,    5,    5,    1,    3,    1,
    3,    1,    3,    3,    1,    2,    3,    3,    3,    3,
    3,    3,    1,    3,    3,    1,    4,    4,    4,    3,
    3,    1,    1,    1,    1,    2,    2,    1,    4,    3,
    4,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   38,   39,    0,    0,    0,    0,   40,    0,
    1,    0,    5,    6,    7,    8,    9,   10,   11,    0,
   30,   31,   32,    0,    0,    0,    2,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    4,   13,
   12,   36,   50,    0,    0,    0,   37,    0,    0,    0,
    0,    0,   74,   75,    0,    0,   83,    0,    0,   78,
   72,    0,   15,   14,    0,   73,    0,    0,    0,    0,
    0,   52,    0,    0,   18,    0,    0,   16,   26,    0,
   33,   35,   51,    0,   34,   81,    0,    0,   77,   76,
    0,    0,    0,    0,   79,   28,    0,    0,    0,    0,
    0,    0,   27,    0,    0,   25,    0,   23,   21,   22,
   20,   19,    0,    0,    0,    0,    0,    0,    0,   70,
   71,    0,    0,    0,    0,    0,    0,   56,   54,   53,
    0,   47,   46,    0,   69,   68,   67,   24,   45,    0,
    0,   48,    0,    0,   43,    0,   49,   44,    0,   42,
   41,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   19,   20,
   35,   32,   73,   67,   21,   22,   23,   24,   25,   26,
  143,  105,   68,   59,   60,   61,   62,
};
final static short yysindex[] = {                      -136,
  183,  -38,    0,    0,  -36,  -28,  -36, -249,    0,    0,
    0,  183,    0,    0,    0,    0,    0,    0,    0,   -1,
    0,    0,    0, -176,  -27, -151,    0,  -39,  -31,  158,
  158, -118,  -35, -227,  -14, -118,  -19,  -85,    0,    0,
    0,    0,    0,  -22, -215, -205,    0,  -19,  -63,   -4,
   33,  -19,    0,    0,   31, -182,    0,   51,   11,    0,
    0,   43,    0,    0,   62,    0,   74,  -12,   78,  -38,
 -192,    0,  -87,   91,    0,   99,   -7,    0,    0,  183,
    0,    0,    0,  183,    0,    0,  178,  178,    0,    0,
  178,  178,  178,  178,    0,    0,  178,  178,  178,  178,
  178,  178,    0, -192, -111,    0, -118,    0,    0,    0,
    0,    0, -106, -157,   62,   54,  -17,   11,   11,    0,
    0,   51,   51,   51,   51,   51,   51,    0,    0,    0,
 -142,    0,    0,  -20,    0,    0,    0,    0,    0, -110,
  183,    0,   -6,    5,    0,    7,    0,    0, -100,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,   13,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    3,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  110,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   13,    0,    0,
    0,   69,    0,    0,    0,    0,    0,   89,    1,    0,
    0,  129,    0,    0,  -41,    0,    0,    0, -116,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -98,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   23,   47,    0,
    0,  -30,  -25,  -23,  138,  155,  160,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   58,    0,    0,   86,    0,    0,    0,    0,    0,   28,
    0,  154,  -21,  131,    0,    0,    0,    0,   77,    0,
    0,   61,   76,   52,    0,   66,    0,
};
final static int YYTABLESIZE=456;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   66,   29,    3,   31,   71,   56,   29,   37,   75,   64,
   61,   34,  141,  130,   79,   60,   45,   62,  133,  140,
   29,   81,   64,  137,  151,   91,   55,   92,   76,   78,
   91,   46,   92,  112,  146,   38,   46,   80,   77,   85,
   82,   66,   41,   66,   66,   66,   65,  101,  100,  102,
  149,   83,   93,   49,   46,   57,   50,   94,   27,   84,
   66,   66,   66,   64,   70,   64,   64,   64,   73,   39,
   88,   50,   87,   89,    5,   90,   86,    6,    7,   42,
   43,   63,   64,   64,   64,  131,   95,   65,   82,   65,
   65,   65,   61,   91,  135,   92,   91,   60,   92,   62,
   44,   87,   50,   58,   47,   48,   65,   65,   65,   17,
   73,   73,   73,   73,   96,   73,  134,   72,  103,    1,
    2,   72,    3,   66,    4,   66,  138,    3,   80,  147,
    5,  109,   82,    6,    7,    8,    9,  113,   70,  110,
   29,  114,  118,  119,  129,   64,   37,   64,    5,  132,
   29,    6,    7,   29,   29,  150,  104,   55,  120,  121,
   36,   69,  116,  117,  128,    0,    0,  142,  106,   65,
    0,   65,  122,  123,  124,  125,  126,  127,   57,    0,
  107,  108,    0,    0,    0,    0,    0,    0,    0,  104,
    0,    0,   72,   73,    0,   58,    0,    0,  144,    0,
   59,    0,   56,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   82,    0,   63,   51,   52,   53,   30,
   54,   55,   56,   28,   63,   63,   61,   33,   63,   63,
   74,   60,    0,   62,   17,  139,   61,    0,  136,   61,
   61,   60,    0,   62,   60,   60,   62,   62,  111,  145,
   97,   98,   99,   80,   40,    0,   66,   66,    3,   66,
   57,   66,  148,   66,   66,   66,    0,   66,   66,   66,
   66,   66,   66,   66,   66,    0,    3,   58,   64,   64,
    0,   64,   59,   64,    0,   64,   64,   64,    0,   64,
   64,   64,   64,   64,   64,   64,   64,    0,    0,    0,
    0,    0,   65,   65,    0,   65,    0,   65,    0,   65,
   65,   65,    0,   65,   65,   65,   65,   65,   65,   65,
   65,    0,    0,    0,   73,   73,    0,   73,    0,   73,
    0,    0,    0,    0,    0,   73,   73,   73,   73,   73,
   73,   73,   73,    0,   82,   82,    0,   82,    0,   82,
    0,    0,    0,    0,    0,   82,   82,   82,   82,   82,
   82,   82,   82,    0,    0,   17,   17,    0,   17,    0,
   17,    0,    0,    0,    0,    0,   17,   17,   17,   17,
   17,   17,   17,   17,   80,   80,    0,   80,    0,   80,
    0,    0,    0,    0,   57,   80,   80,   80,   80,   80,
   80,   80,   80,    0,   57,    0,    0,   57,   57,    0,
    0,   58,    0,   65,   66,   53,   59,   54,   55,    0,
    0,   58,    0,    0,   58,   58,   59,    0,    0,   59,
   59,    0,    0,  115,   66,   53,    0,   54,   55,    2,
    0,    3,    0,    4,    0,    0,    0,    0,    0,    5,
    0,    0,    6,    7,    8,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,    0,   40,  123,   45,  123,  257,   44,   41,
   41,   40,  123,  125,   36,   41,   44,   41,  125,   40,
   40,   44,    0,   41,  125,   43,  125,   45,  256,   44,
   43,   59,   45,   41,   41,    8,   59,  123,  266,   44,
  256,   41,   44,   43,   44,   45,    0,   60,   61,   62,
   44,  257,   42,   26,   59,   28,   44,   47,    1,  123,
   60,   61,   62,   41,  257,   43,   44,   45,    0,   12,
   40,   59,   40,  256,  267,  258,   44,  270,  271,  256,
  257,  123,   60,   61,   62,  107,   44,   41,    0,   43,
   44,   45,  123,   43,   41,   45,   43,  123,   45,  123,
   24,   40,   26,   28,  256,  257,   60,   61,   62,    0,
   42,   43,   44,   45,   41,   47,  274,   32,   41,  256,
  257,   36,  259,  123,  261,  125,  269,  125,    0,  125,
  267,   41,   44,  270,  271,  272,  273,   80,  257,   41,
  257,   84,   91,   92,  256,  123,  257,  125,  267,  256,
  267,  270,  271,  270,  271,  256,   71,  256,   93,   94,
    7,   31,   87,   88,  104,   -1,   -1,  140,  256,  123,
   -1,  125,   97,   98,   99,  100,  101,  102,   41,   -1,
  268,  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  104,
   -1,   -1,  107,  125,   -1,   41,   -1,   -1,  141,   -1,
   41,   -1,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  125,   -1,  257,  256,  257,  258,  256,
  260,  261,   45,  262,  256,  267,  257,  256,  270,  271,
  266,  257,   -1,  257,  125,  256,  267,   -1,  256,  270,
  271,  267,   -1,  267,  270,  271,  270,  271,  256,  256,
  263,  264,  265,  125,  256,   -1,  256,  257,  256,  259,
  123,  261,  256,  263,  264,  265,   -1,  267,  268,  269,
  270,  271,  272,  273,  274,   -1,  274,  123,  256,  257,
   -1,  259,  123,  261,   -1,  263,  264,  265,   -1,  267,
  268,  269,  270,  271,  272,  273,  274,   -1,   -1,   -1,
   -1,   -1,  256,  257,   -1,  259,   -1,  261,   -1,  263,
  264,  265,   -1,  267,  268,  269,  270,  271,  272,  273,
  274,   -1,   -1,   -1,  256,  257,   -1,  259,   -1,  261,
   -1,   -1,   -1,   -1,   -1,  267,  268,  269,  270,  271,
  272,  273,  274,   -1,  256,  257,   -1,  259,   -1,  261,
   -1,   -1,   -1,   -1,   -1,  267,  268,  269,  270,  271,
  272,  273,  274,   -1,   -1,  256,  257,   -1,  259,   -1,
  261,   -1,   -1,   -1,   -1,   -1,  267,  268,  269,  270,
  271,  272,  273,  274,  256,  257,   -1,  259,   -1,  261,
   -1,   -1,   -1,   -1,  257,  267,  268,  269,  270,  271,
  272,  273,  274,   -1,  267,   -1,   -1,  270,  271,   -1,
   -1,  257,   -1,  256,  257,  258,  257,  260,  261,   -1,
   -1,  267,   -1,   -1,  270,  271,  267,   -1,   -1,  270,
  271,   -1,   -1,  256,  257,  258,   -1,  260,  261,  257,
   -1,  259,   -1,  261,   -1,   -1,   -1,   -1,   -1,  267,
   -1,   -1,  270,  271,  272,  273,
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
"conjunto_sentencias : sentencia conjunto_sentencias",
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
"sentencia_impresion : PRINT cadena_cararacteres_entre_parentesis",
"sentencia_impresion : PRINT error ','",
"cadena_cararacteres_entre_parentesis : '(' CADENA_CARACTERES ')'",
"cadena_cararacteres_entre_parentesis : '(' CADENA_CARACTERES error",
"cadena_cararacteres_entre_parentesis : error CADENA_CARACTERES ')'",
"cadena_cararacteres_entre_parentesis : '(' error ')'",
"sentencia_if : IF condicion_entre_parentesis bloque_sentencias END_IF",
"sentencia_if : IF condicion_entre_parentesis bloque_sentencias ELSE bloque_sentencias END_IF",
"sentencia_if : IF condicion_entre_parentesis bloque_sentencias error",
"sentencia_while : WHILE condicion_entre_parentesis bloque_sentencias",
"condicion_entre_parentesis : '(' condicion ')'",
"condicion_entre_parentesis : error condicion ')'",
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
"sentencias_ejecutables : ejecutable sentencias_ejecutables",
"condicion : expr '=' expr",
"condicion : expr '<' expr",
"condicion : expr '>' expr",
"condicion : expr COMP_MENOR_IGUAL expr",
"condicion : expr COMP_MAYOR_IGUAL expr",
"condicion : expr COMP_DISTINTO expr",
"condicion : error",
"expr : expr '+' term",
"expr : expr '-' term",
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
"factor : casting",
"asignacion : ID ASIGN r_value_asignacion ','",
"asignacion : ID ASIGN r_value_asignacion",
"asignacion : ID ASIGN error ','",
"r_value_asignacion : expr",
"r_value_asignacion : id_invocacion",
};

//#line 273 ".\gramatica.y"


Hashtable<String, Atributos> tablaSimbolos;
AnalizadorLexico AL = null;
List<String> estructurasGramaticalesDetectadas;
List<String> tokensLeidos;
List<String> erroresDetallados;
List<String> erroresGenerales;
List<String> variablesADeclarar;
List<Arbol> sentenciasEjecutables;
Token t;
int ultimoTokenLeido;
Arbol raizArbolSintactico;

int yylex(){
	t = AL.getToken();
	yylval = new ParserVal(t);
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

	String leido= "Linea: " + t.nroLinea + ". Token leido: '" + ultimoTokenLeido + "' reconocido como: " + Token.tipoToken(ultimoTokenLeido) + "\n";
	tokensLeidos.add(leido);
	return ultimoTokenLeido;

}

void yyerror(String s)
{
	String err= "En linea: " + AL.nroLinea + ". Ocurrio un error de parsing ( "  + s + " ) al leer el token " + Token.tipoToken(ultimoTokenLeido) +"\n"; //tambien se puede usar yychar en vez de lo del ultimoTokenLeido
	this.erroresGenerales.add(err);
	System.out.println(err);
}

public Parser(AnalizadorLexico AL, Hashtable<String, Atributos> tablaSimbolos, Arbol raizArbolSintactico)

{
	//yydebug=true;
	tokensLeidos=new ArrayList<>();
	estructurasGramaticalesDetectadas=new ArrayList<>();
	erroresDetallados=new ArrayList<>();
	erroresGenerales=new ArrayList<>();
	variablesADeclarar = new ArrayList<>();
	sentenciasEjecutables = new ArrayList<>();
	this.AL=AL;
	this.tablaSimbolos = tablaSimbolos;
	this.raizArbolSintactico=raizArbolSintactico;
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

public Arbol getArbolSintactico(){
	//en presencia de un error sintactico puedo setear un boolean o algo que haga que la raiz sea null y no generar codigo
	return this.raizArbolSintactico;

}
private void agregarError(String e){
	erroresDetallados.add(e+"\n");
}
private void agregarEstructuraDetectada(String tipo){
	String toAdd=tipo + " en linea " + AL.nroLinea + "\n";
	this.estructurasGramaticalesDetectadas.add(toAdd);

}


private ParserVal agregarNodoRengo(String value, ParserVal primero){

	return new ParserVal(new NodoRengo(value,(Arbol)primero.obj));

}
private ParserVal agregarNodo(String value, ParserVal izquierdo, ParserVal derecho){
	return new ParserVal(new Nodo(value,(Arbol)izquierdo.obj,(Arbol)derecho.obj));

}

private ParserVal agregarHoja(String value){
	return new ParserVal(new Hoja(value));

}

private String obtenerLexema(ParserVal pv){
	return ((Token)pv.obj).claveTablaSimbolo;

}

private void cambiarTipo(ParserVal pv,String tipo){
	//metodo pensado para cambiar el tipo en vez de usar $$.sval=tipo (por ejemplo 'integer'). Ya que sería muy oscuro
	pv.sval=tipo;

}

private boolean verificarTipos(ParserVal p1, ParserVal p2, String tipoSentencia){
 //verdadero si son iguales, falso si son distintos
	if (p1.sval != p2.sval)
		System.out.println("Error de tipos en " + tipoSentencia + ": no se puede realizar entre "
												+ p1.sval+ " y " + p2.sval); /*cambiar*/
	else
		return true;
	return false;
}

private boolean verificarDeclaracion(ParserVal p1){
 //retorna verdadero si esta declarada
 	String claveTS = obtenerLexema(p1);
	if (tablaSimbolos.get(claveTS).get("Declarada").equals("No")) // se busca en la TS el atributo "Declarada" y se fija si tiene valor "No"
		System.out.println("Error: variable '_" + claveTS +  "' no declarada."); /*cambiar*/
	else
		return true;
	return false;
}


private void declararVariables(ParserVal tipo){
	for (String variable: variablesADeclarar){
		if (tablaSimbolos.get(variable).get("Declarada").equals("Si"))
			System.out.println("Error: redeclaracion de variable '_" + variable + "'.");
		else{
			tablaSimbolos.get(variable).set("Declarada", "Si");
			tablaSimbolos.get(variable).set("Tipo", tipo.sval);

		}
	}

	variablesADeclarar.clear(); //ya se declararon las variables, se vacia la lista de variables a declarar

}

private Arbol engancharSentencias(){
	Arbol salida = new Hoja(null);
	for (Arbol a: sentenciasEjecutables)
		salida = new Nodo("lista_sentencias", a, salida);
	return salida;
}
//#line 571 "Parser.java"
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
case 1:
//#line 21 ".\gramatica.y"
{	this.raizArbolSintactico = engancharSentencias();}
break;
case 6:
//#line 32 ".\gramatica.y"
{sentenciasEjecutables.add(0, (Arbol)val_peek(0).obj);}
break;
case 9:
//#line 38 ".\gramatica.y"
{agregarEstructuraDetectada("Asignacion");}
break;
case 12:
//#line 44 ".\gramatica.y"
{agregarEstructuraDetectada("Invocacion funcion");yyval = agregarHoja(obtenerLexema(val_peek(1))); cambiarTipo(yyval, "funcion");}
break;
case 13:
//#line 45 ".\gramatica.y"
{agregarError("Error: falta ',' en invocacion ejecutable. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 14:
//#line 49 ".\gramatica.y"
{yyval = agregarNodoRengo("invocacion", agregarHoja(obtenerLexema(val_peek(2)))); cambiarTipo(yyval, "funcion");}
break;
case 15:
//#line 50 ".\gramatica.y"
{agregarError("Error: falta ')' en invocacion o declaracion de closure/funcion. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 16:
//#line 54 ".\gramatica.y"
{agregarEstructuraDetectada("Impresion"); yyval = agregarNodoRengo("Impresion",val_peek(1));}
break;
case 17:
//#line 55 ".\gramatica.y"
{agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + ((Token) val_peek(0).obj).nroLinea); yyval = val_peek(0);}
break;
case 18:
//#line 56 ".\gramatica.y"
{agregarError("Error: sentencia de impresion erronea. Linea: " + ((Token) val_peek(2).obj).nroLinea);}
break;
case 19:
//#line 60 ".\gramatica.y"
{yyval = agregarHoja(obtenerLexema(val_peek(1)));}
break;
case 20:
//#line 61 ".\gramatica.y"
{agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 21:
//#line 62 ".\gramatica.y"
{agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(0);}
break;
case 22:
//#line 63 ".\gramatica.y"
{agregarError("Error: solo se pueden imprimir cadenas de caracteres. Linea: " + ((Token) val_peek(1).obj).nroLinea);yyval = val_peek(0);}
break;
case 23:
//#line 67 ".\gramatica.y"
{yyval = agregarNodo("if",val_peek(2),agregarNodoRengo("cuerpo",agregarNodoRengo("then",val_peek(1))));}
break;
case 24:
//#line 68 ".\gramatica.y"
{yyval = agregarNodo("if_else",val_peek(4),agregarNodo("cuerpo",agregarNodoRengo("then",val_peek(3)),agregarNodoRengo("else",val_peek(1))));}
break;
case 25:
//#line 69 ".\gramatica.y"
{agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + ((Token) val_peek(1).obj).nroLinea);yyval = val_peek(1);}
break;
case 26:
//#line 73 ".\gramatica.y"
{yyval = agregarNodo("while",val_peek(1),val_peek(0));}
break;
case 27:
//#line 76 ".\gramatica.y"
{agregarEstructuraDetectada("Condicion"); yyval = agregarNodoRengo("condicion",val_peek(1));}
break;
case 28:
//#line 77 ".\gramatica.y"
{agregarError("Error: falta '(' antes de la condicion. Linea: " + ((Token) val_peek(2).obj).nroLinea);yyval = val_peek(0);}
break;
case 29:
//#line 78 ".\gramatica.y"
{/*esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar*/
														 								/*que se coman tokens de mas e incluso no informar el errores*/
																						agregarError("Error: falta ')' luego de la condicion. Linea: " + ((Token) val_peek(0).obj).nroLinea);yyval = val_peek(0);}
break;
case 33:
//#line 90 ".\gramatica.y"
{ declararVariables(val_peek(2));
																														agregarEstructuraDetectada("Declaracion variable/s"); yyval = val_peek(0);}
break;
case 34:
//#line 92 ".\gramatica.y"
{ declararVariables(val_peek(2));
																														yyval = val_peek(0);}
break;
case 35:
//#line 95 ".\gramatica.y"
{agregarError("Error: declaracion de tipo erronea. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 36:
//#line 96 ".\gramatica.y"
{agregarError("Error: falta ID o ',' en la declaracion de variable/s. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 37:
//#line 97 ".\gramatica.y"
{agregarError("Error: definicion de closure erronea. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 38:
//#line 101 ".\gramatica.y"
{yyval = new ParserVal("integer");}
break;
case 39:
//#line 102 ".\gramatica.y"
{yyval = new ParserVal("uslinteger");}
break;
case 40:
//#line 106 ".\gramatica.y"
{/*lo hago aca para que tome la primer linea incluso en funcion closure*/
													yyval = new ParserVal("funcion");;
													agregarEstructuraDetectada("Declaracion de tipo closure"); }
break;
case 41:
//#line 112 ".\gramatica.y"
{yyval = val_peek(0);}
break;
case 42:
//#line 114 ".\gramatica.y"
{agregarError("Error: falta '}' de cierre de la declaracion de closure. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 43:
//#line 115 ".\gramatica.y"
{agregarError("Error: falta ')' luego del retorno del closure. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 44:
//#line 116 ".\gramatica.y"
{agregarError("Error: falta ',' luego del retorno del closure. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 45:
//#line 117 ".\gramatica.y"
{agregarError("Error: retorno no es de tipo closure. Se espera \"return( ID() )\" o \"return( {SENTENCIAS} )\". Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 46:
//#line 121 ".\gramatica.y"
{agregarEstructuraDetectada("Declaracion de funcion simple"); yyval = val_peek(0);}
break;
case 47:
//#line 122 ".\gramatica.y"
{agregarError("Error: falta '}' de cierre de la funcion. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 49:
//#line 127 ".\gramatica.y"
{yyval = val_peek(0);}
break;
case 50:
//#line 130 ".\gramatica.y"
{variablesADeclarar.add(obtenerLexema(val_peek(0)));}
break;
case 51:
//#line 131 ".\gramatica.y"
{ variablesADeclarar.add(obtenerLexema(val_peek(0))); yyval = val_peek(0);}
break;
case 52:
//#line 135 ".\gramatica.y"
{yyval = agregarNodo("lista_sentencias", val_peek(0), new ParserVal(new Hoja(null)));}
break;
case 53:
//#line 136 ".\gramatica.y"
{yyval = val_peek(1);}
break;
case 54:
//#line 137 ".\gramatica.y"
{agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " +((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 55:
//#line 141 ".\gramatica.y"
{yyval = agregarNodo("lista_sentencias", val_peek(0), new ParserVal(new Hoja(null)));}
break;
case 56:
//#line 142 ".\gramatica.y"
{yyval = agregarNodo("lista_sentencias", val_peek(1), val_peek(0));}
break;
case 57:
//#line 146 ".\gramatica.y"
{if (verificarTipos(val_peek(2), val_peek(0), "condicion '='"))
																									yyval = agregarNodo("=",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																								}
break;
case 58:
//#line 150 ".\gramatica.y"
{if (verificarTipos(val_peek(2), val_peek(0), "condicion '<'"))
																											yyval = agregarNodo("<",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																										}
break;
case 59:
//#line 154 ".\gramatica.y"
{if (verificarTipos(val_peek(2), val_peek(0), "condicion '>'"))
																											yyval = agregarNodo(">",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																										}
break;
case 60:
//#line 158 ".\gramatica.y"
{if (verificarTipos(val_peek(2), val_peek(0), "condicion '<='"))
																											yyval = agregarNodo("<=",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																										}
break;
case 61:
//#line 162 ".\gramatica.y"
{if (verificarTipos(val_peek(2), val_peek(0), "condicion '=>'"))
																											yyval = agregarNodo(">=",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																										}
break;
case 62:
//#line 166 ".\gramatica.y"
{if (verificarTipos(val_peek(2), val_peek(0), "condicion '!='"))
																											yyval = agregarNodo("!=",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																										}
break;
case 63:
//#line 170 ".\gramatica.y"
{agregarError("Error: condicion no valida. Incorrecta mezcla de expresiones y comparador. Linea: " + ((Token) val_peek(0).obj).nroLinea);}
break;
case 64:
//#line 174 ".\gramatica.y"
{
															if (verificarTipos(val_peek(2), val_peek(0), "operacion '+'")){
																	yyval = agregarNodo("+",val_peek(2),val_peek(0));
																	cambiarTipo(yyval, val_peek(2).sval);
																}
													}
break;
case 65:
//#line 181 ".\gramatica.y"
{
																if (verificarTipos(val_peek(2), val_peek(0), "operacion '/'")){
																		yyval = agregarNodo("/",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																		cambiarTipo(yyval, val_peek(2).sval);
																	}
																}
break;
case 67:
//#line 192 ".\gramatica.y"
{agregarEstructuraDetectada("Conversion explicita"); yyval = agregarNodoRengo("casting",val_peek(1)); cambiarTipo(yyval, "uslinteger");}
break;
case 68:
//#line 194 ".\gramatica.y"
{agregarError("Error: falta ')' en la conversion explicita. Linea: " + ((Token)val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 69:
//#line 196 ".\gramatica.y"
{agregarError("Error: tipo no valido para conversion. Linea: " + ((Token)val_peek(3).obj).nroLinea); yyval = val_peek(0);}
break;
case 70:
//#line 201 ".\gramatica.y"
{
																	if (verificarTipos(val_peek(2), val_peek(0), "operacion '*'")){
																			yyval = agregarNodo("*",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘*‘ ; T.ptr ; F.ptr )*/
																			cambiarTipo(yyval, val_peek(2).sval);
																		}
														}
break;
case 71:
//#line 207 ".\gramatica.y"
{
																if (verificarTipos(val_peek(2), val_peek(0), "operacion '/'")){
																		yyval = agregarNodo("/",val_peek(2),val_peek(0)); /*es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )*/
																		cambiarTipo(yyval, val_peek(2).sval);
																	}
														}
break;
case 73:
//#line 217 ".\gramatica.y"
{ yyval=agregarHoja(((Token)val_peek(0).obj).claveTablaSimbolo);
																			if (verificarDeclaracion(val_peek(0)))
																					cambiarTipo(yyval, (String)tablaSimbolos.get(obtenerLexema(val_peek(0))).get("Tipo"));
																				}
break;
case 74:
//#line 221 ".\gramatica.y"
{ yyval=agregarHoja(((Token)val_peek(0).obj).claveTablaSimbolo);
																			cambiarTipo(yyval, "integer");
																		 Atributos atts = tablaSimbolos.get(((Token)val_peek(0).obj).claveTablaSimbolo); /*$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS*/
																		 int valorInteger = (Integer) atts.get("Valor"); /*el valor en la posicion 1 es el número de la*/
																		 if (valorInteger > 32767) /*si se pasa del limite positivo*/

																				if (!tablaSimbolos.containsKey("32767_i")){
																					Atributos nuevosAtts = new Atributos();
																					nuevosAtts.set("Token", "CTE_INTEGER");nuevosAtts.set("Valor", 32767);
																					tablaSimbolos.put("32767_i", nuevosAtts);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + ((Token)val_peek(0).obj).nroLinea);
																				}

																			}
break;
case 75:
//#line 235 ".\gramatica.y"
{ yyval=agregarHoja(((Token)val_peek(0).obj).claveTablaSimbolo);
																		cambiarTipo(yyval, "uslinteger");}
break;
case 76:
//#line 237 ".\gramatica.y"
{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get(((Token)val_peek(0).obj).claveTablaSimbolo).get("Valor");
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			Atributos atts = new Atributos();
																			atts.set("Token", "CTE_INTEGER"); atts.set("Valor", new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, atts);
																			}
																		yyval = val_peek(0);
																		cambiarTipo(yyval, "integer");
																		}
break;
case 77:
//#line 248 ".\gramatica.y"
{agregarError("Error: negacion no permitida a este operando. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 79:
//#line 253 ".\gramatica.y"
{
																							if (verificarDeclaracion(val_peek(3))){  /*se fija si la variable del lado izquierdo esta declarada*/
																									cambiarTipo(val_peek(3), (String)tablaSimbolos.get(obtenerLexema(val_peek(3))).get("Tipo")); /*se le setea el tipo*/
																									verificarTipos(val_peek(3), val_peek(1), "asignacion"); 																		/*se verifica que los tipos de los dos lados sean iguales*/
																									yyval = agregarNodo(":=",agregarHoja(((Token)val_peek(3).obj).claveTablaSimbolo),val_peek(1));
																							}
																						}
break;
case 80:
//#line 261 ".\gramatica.y"
{agregarError("Error: falta ',' en asignacion. Linea: " + ((Token) val_peek(0).obj).nroLinea); yyval = val_peek(0);}
break;
case 81:
//#line 263 ".\gramatica.y"
{agregarError("Error: r-value de la asignacion mal definido. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(0);}
break;
case 83:
//#line 268 ".\gramatica.y"
{agregarEstructuraDetectada("Invocacion de funcion en asignacion");}
break;
//#line 1059 "Parser.java"
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
