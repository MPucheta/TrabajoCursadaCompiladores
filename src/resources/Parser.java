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






//#line 3 ".\tokens.y"
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
    5,    5,    5,    6,   12,   12,    3,    3,    3,   15,
   15,   15,   15,   15,   18,   18,   20,   16,   16,   16,
   16,   16,   17,   17,   21,   21,   19,   19,   13,   13,
   13,   22,   22,   14,   14,   14,   14,   14,   14,   14,
   23,   23,   23,   23,   25,   25,   25,   24,   24,   24,
   26,   26,   26,   26,   26,    7,    7,    7,   27,   27,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    2,    3,    3,    3,    2,    3,    3,    3,
    4,    6,    4,    3,    3,    2,    1,    1,    1,    3,
    3,    3,    2,    2,    1,    1,    1,   10,   10,    8,
    9,    6,    5,    5,    1,    3,    1,    3,    1,    3,
    3,    1,    2,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    1,    1,    4,    4,    4,    3,    3,    1,
    1,    1,    1,    2,    2,    4,    3,    4,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   35,   36,    0,    0,    0,    0,   37,    0,
    0,    3,    5,    6,    7,    8,    9,   10,   11,    0,
   27,   28,   29,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    4,   13,   12,
   33,   47,    0,    0,    0,   34,    0,    0,    0,    0,
    0,   72,   73,    0,    0,   80,    0,    0,   63,   70,
    0,   15,   14,    0,   71,    0,    0,    0,    0,   49,
    0,    0,    0,   16,   24,    0,   30,   32,   48,    0,
   31,   78,    0,    0,   75,   74,    0,    0,    0,    0,
   76,   25,    0,    0,    0,    0,    0,    0,   52,    0,
   23,    0,   21,   20,   19,   18,    0,    0,    0,    0,
    0,    0,    0,   68,   69,    0,    0,    0,    0,    0,
    0,   51,   50,   53,    0,   44,   43,    0,   67,   66,
   65,   22,   42,    0,    0,   45,    0,    0,   40,    0,
   46,   41,    0,   39,   38,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   19,   20,
   34,   31,   71,   66,   21,   22,   23,   24,   25,   26,
  137,  100,   57,   58,   59,   60,   61,
};
final static short yysindex[] = {                       182,
  207,  -34,    0,    0,   15,  -38,   15, -176,    0,    0,
  207,    0,    0,    0,    0,    0,    0,    0,    0,  -37,
    0,    0,    0, -185,  -32, -162,  207,   -5,   -7,  158,
  -93, -167, -163,   60,  -93,   65,   -8,    0,    0,    0,
    0,    0,  -27, -150, -140,    0,   65,   -4,   -1,  -11,
   65,    0,    0,   85, -220,    0,   11,  -28,    0,    0,
   84,    0,    0,   90,    0,   91,  -40,  -34, -197,    0,
 -133,   93,   -6,    0,    0,  207,    0,    0,    0,  207,
    0,    0,  176,  176,    0,    0,   41,   41,   41,   41,
    0,    0,  176,  176,  176,  176,  176,  176,    0,  -74,
    0,  -93,    0,    0,    0,    0, -117,  189,   90,   34,
  -17,  -28,  -28,    0,    0,   11,   11,   11,   11,   11,
   11,    0,    0,    0, -132,    0,    0,  -36,    0,    0,
    0,    0,    0, -114,  207,    0,    7, -110,    0,  -13,
    0,    0, -115,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    6,    0,    0,    0,    0,    0,    0,    0,    0,
  159,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  166,    0,    0,    0,
    0,    0,    0,  110,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    6,    0,    0,    0,
   69,    0,    0,    0,    0,    0,   89,    1,    0,    0,
  129,    0,    0,  -41,    0,  -86,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   23,   47,    0,    0,  -30,  -25,  -23,  138,  154,
  160,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   51,   14,    0,   18,    0,    0,    0,    0,    0,   31,
    0,  131,  -22,    0,    0,    0,    0,    0,   52,    0,
    0,    0,  115,    9,    0,   12,    0,
};
final static int YYTABLESIZE=480;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   64,   33,   87,  134,   88,   29,   40,  127,  135,  145,
   58,   44,   75,   89,  141,   57,   77,   59,   90,   97,
   96,   98,   61,  131,   38,   87,   45,   88,   83,   69,
  143,   45,   82,   63,  106,   85,   26,   86,   37,   55,
   38,   64,   81,   64,   64,   64,   62,  140,   70,   47,
  123,   27,   70,   87,   30,   88,   48,   45,   56,   68,
   64,   64,   64,   61,   47,   61,   61,   61,   71,    5,
   41,   42,    6,    7,  129,   43,   87,   49,   88,  125,
   36,   60,   61,   61,   61,   55,   99,   62,   79,   62,
   62,   62,   58,   46,   47,  112,  113,   57,   72,   59,
  114,  115,   73,   74,   29,   78,   62,   62,   62,   17,
   71,   71,   71,   71,   76,   71,   79,  124,   80,   70,
   38,   38,  101,   64,   84,   64,  107,   91,   77,   83,
  108,   92,   79,  104,  102,  103,  132,   35,  126,    2,
  144,    3,   36,    4,   67,   61,    2,   61,    3,    5,
    4,   38,    6,    7,    8,    9,    5,    0,    1,    6,
    7,    8,    9,   68,  136,    2,    0,    0,    0,   62,
   26,   62,    0,    5,    0,    0,    6,    7,   54,    0,
   26,  122,   68,   26,   26,  138,    0,    0,    0,    0,
    0,    0,    5,   71,   55,    6,    7,  110,  111,    0,
   56,    0,   55,    0,    0,    0,    0,  116,  117,  118,
  119,  120,  121,   79,    0,   60,    0,   32,   39,  133,
   55,    0,   93,   94,   95,   60,   58,   28,   60,   60,
    0,   57,    0,   59,   17,    0,   58,    0,  130,   58,
   58,   57,  142,   59,   57,   57,   59,   59,   62,  105,
   50,   51,   52,   77,   53,   54,   64,   64,    0,   64,
   54,   64,  139,   64,   64,   64,    0,   64,   64,   64,
   64,   64,   64,   64,   64,    0,   55,    0,   61,   61,
    0,   61,   56,   61,    0,   61,   61,   61,    0,   61,
   61,   61,   61,   61,   61,   61,   61,   65,   52,    0,
   53,    0,   62,   62,    0,   62,    0,   62,    0,   62,
   62,   62,    0,   62,   62,   62,   62,   62,   62,   62,
   62,    0,    0,    0,   71,   71,    0,   71,    0,   71,
    0,    0,    0,    0,    0,   71,   71,   71,   71,   71,
   71,   71,   71,    0,   79,   79,    0,   79,    0,   79,
    0,    0,    0,    0,    0,   79,   79,   79,   79,   79,
   79,   79,   79,    0,    0,   17,   17,    0,   17,    0,
   17,    0,    0,    0,    0,    0,   17,   17,   17,   17,
   17,   17,   17,   17,   77,   77,    0,   77,    0,   77,
    0,    0,    0,    0,   54,   77,   77,   77,   77,   77,
   77,   77,   77,    0,   54,    0,    0,   54,   54,    0,
   55,    0,    0,   64,   65,   52,   56,   53,   54,    0,
   55,    0,    0,   55,   55,    0,   56,    0,    0,   56,
   56,  109,   65,   52,    0,   53,   54,    1,    2,    0,
    3,    0,    4,    0,    0,    2,    0,    3,    5,    4,
    0,    6,    7,    8,    9,    5,    0,    0,    6,    7,
    8,    9,  128,    2,    0,    3,    0,    4,    0,    0,
    0,    0,    0,    5,    0,    0,    6,    7,    8,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   43,   40,   45,   40,   44,  125,  123,  125,
   41,   44,   35,   42,  125,   41,   44,   41,   47,   60,
   61,   62,    0,   41,   11,   43,   59,   45,   40,  123,
   44,   59,   44,   41,   41,  256,  123,  258,    8,   45,
   27,   41,   44,   43,   44,   45,    0,   41,   31,   44,
  125,    1,   35,   43,   40,   45,   26,   59,   28,  257,
   60,   61,   62,   41,   59,   43,   44,   45,    0,  267,
  256,  257,  270,  271,   41,   24,   43,   26,   45,  102,
  257,  123,   60,   61,   62,   45,   69,   41,    0,   43,
   44,   45,  123,  256,  257,   87,   88,  123,  266,  123,
   89,   90,  266,   44,   40,  256,   60,   61,   62,    0,
   42,   43,   44,   45,  123,   47,  257,  100,  123,  102,
  107,  108,  256,  123,   40,  125,   76,   44,    0,   40,
   80,   41,   44,   41,  268,  269,  269,    7,  256,  257,
  256,  259,  257,  261,   30,  123,  257,  125,  259,  267,
  261,  138,  270,  271,  272,  273,  267,   -1,    0,  270,
  271,  272,  273,  257,  134,    0,   -1,   -1,   -1,  123,
  257,  125,   -1,  267,   -1,   -1,  270,  271,   41,   -1,
  267,  256,  257,  270,  271,  135,   -1,   -1,   -1,   -1,
   -1,   -1,  267,  125,   41,  270,  271,   83,   84,   -1,
   41,   -1,   45,   -1,   -1,   -1,   -1,   93,   94,   95,
   96,   97,   98,  125,   -1,  257,   -1,  256,  256,  256,
   45,   -1,  263,  264,  265,  267,  257,  262,  270,  271,
   -1,  257,   -1,  257,  125,   -1,  267,   -1,  256,  270,
  271,  267,  256,  267,  270,  271,  270,  271,  256,  256,
  256,  257,  258,  125,  260,  261,  256,  257,   -1,  259,
  123,  261,  256,  263,  264,  265,   -1,  267,  268,  269,
  270,  271,  272,  273,  274,   -1,  123,   -1,  256,  257,
   -1,  259,  123,  261,   -1,  263,  264,  265,   -1,  267,
  268,  269,  270,  271,  272,  273,  274,  257,  258,   -1,
  260,   -1,  256,  257,   -1,  259,   -1,  261,   -1,  263,
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
  257,   -1,   -1,  256,  257,  258,  257,  260,  261,   -1,
  267,   -1,   -1,  270,  271,   -1,  267,   -1,   -1,  270,
  271,  256,  257,  258,   -1,  260,  261,  256,  257,   -1,
  259,   -1,  261,   -1,   -1,  257,   -1,  259,  267,  261,
   -1,  270,  271,  272,  273,  267,   -1,   -1,  270,  271,
  272,  273,  274,  257,   -1,  259,   -1,  261,   -1,   -1,
   -1,   -1,   -1,  267,   -1,   -1,  270,  271,  272,  273,
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
"sentencia_impresion : PRINT cadena_cararacteres_entre_parentesis",
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
"asignacion : ID ASIGN r_value_asignacion",
"asignacion : ID ASIGN error ','",
"r_value_asignacion : expr",
"r_value_asignacion : id_invocacion",
};

//#line 192 ".\tokens.y"


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
//#line 486 "Parser.java"
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
//#line 37 ".\tokens.y"
{agregarEstructuraDetectada("Asignacion");}
break;
case 12:
//#line 42 ".\tokens.y"
{agregarEstructuraDetectada("Invocacion funcion");}
break;
case 13:
//#line 43 ".\tokens.y"
{agregarError("Error: falta ',' en invocacion ejecutable. Linea: " + AL.nroLinea);}
break;
case 15:
//#line 47 ".\tokens.y"
{agregarError("Error: falta ')' en invocacion. Linea: " + AL.nroLinea);}
break;
case 16:
//#line 49 ".\tokens.y"
{agregarEstructuraDetectada("Impresion");}
break;
case 17:
//#line 50 ".\tokens.y"
{agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + AL.nroLinea);}
break;
case 19:
//#line 54 ".\tokens.y"
{agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + AL.nroLinea);}
break;
case 20:
//#line 55 ".\tokens.y"
{agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + AL.nroLinea);}
break;
case 23:
//#line 60 ".\tokens.y"
{agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + AL.nroLinea);}
break;
case 25:
//#line 70 ".\tokens.y"
{agregarEstructuraDetectada("Condicion");}
break;
case 26:
//#line 71 ".\tokens.y"
{/*esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar*/
														 								/*que se coman tokens de mas e incluso no informar el errores*/
																						agregarError("Error: falta ')' luego de la condicion. Linea: " + AL.nroLinea);}
break;
case 30:
//#line 83 ".\tokens.y"
{agregarEstructuraDetectada("Declaracion variable/s");}
break;
case 32:
//#line 85 ".\tokens.y"
{agregarError("Error: declaracion de tipo erronea. Linea: " + AL.nroLinea);}
break;
case 33:
//#line 86 ".\tokens.y"
{agregarError("Error: falta ID o ',' en la declaracion de variable/s. Linea: " + AL.nroLinea);}
break;
case 34:
//#line 87 ".\tokens.y"
{agregarError("Error: definicion de closure erronea. Linea: " + AL.nroLinea);}
break;
case 37:
//#line 94 ".\tokens.y"
{/*lo hago aca para que tome la primer linea incluso en funcion closure*/
													agregarEstructuraDetectada("Declaracion de tipo closure"); }
break;
case 39:
//#line 99 ".\tokens.y"
{agregarError("Error: falta '}' de cierre de la declaracion de closure. Linea: " + AL.nroLinea);}
break;
case 40:
//#line 100 ".\tokens.y"
{agregarError("Error: falta ')' luego del retorno del closure. Linea: " + AL.nroLinea);}
break;
case 41:
//#line 101 ".\tokens.y"
{agregarError("Error: falta ',' luego del retorno del closure. Linea: " + AL.nroLinea);}
break;
case 42:
//#line 102 ".\tokens.y"
{agregarError("Error: retorno no es de tipo closure. Se espera \"return( ID() )\" o \"return( {SENTENCIAS} )\"");}
break;
case 43:
//#line 108 ".\tokens.y"
{agregarEstructuraDetectada("Declaracion de funcion simple");}
break;
case 44:
//#line 109 ".\tokens.y"
{agregarError("Error: falta '}' de cierre de la funcion. Linea: " + AL.nroLinea);}
break;
case 51:
//#line 125 ".\tokens.y"
{agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " + AL.nroLinea);}
break;
case 60:
//#line 137 ".\tokens.y"
{agregarError("Error: condicion no valida. Incorrecta mezcla de expresiones y comparador. Linea: " + AL.nroLinea);}
break;
case 65:
//#line 146 ".\tokens.y"
{agregarEstructuraDetectada("Conversion explicita");}
break;
case 66:
//#line 147 ".\tokens.y"
{agregarError("Error: falta ')' en la conversion explicita. Linea: " + AL.nroLinea);}
break;
case 67:
//#line 148 ".\tokens.y"
{agregarError("Error: tipo no valido para conversion. Linea: " + AL.nroLinea);}
break;
case 72:
//#line 157 ".\tokens.y"
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
case 74:
//#line 170 ".\tokens.y"
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
case 75:
//#line 179 ".\tokens.y"
{agregarError("Error: negacion no permitida a este operando. Linea: " + AL.nroLinea);}
break;
case 77:
//#line 183 ".\tokens.y"
{agregarError("Error: falta ',' en asignacion. Linea: " + AL.nroLinea);}
break;
case 78:
//#line 184 ".\tokens.y"
{agregarError("Error: r-value de la asignacion mal definido. Linea: " + AL.nroLinea);}
break;
case 80:
//#line 187 ".\tokens.y"
{agregarEstructuraDetectada("Invocacion de funcion en asignacion");}
break;
//#line 789 "Parser.java"
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
