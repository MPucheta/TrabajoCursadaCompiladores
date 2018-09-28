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
   16,   16,   16,   17,   17,   21,   21,   19,   19,   13,
   13,   13,   22,   22,   14,   14,   14,   14,   14,   14,
   14,   23,   23,   23,   23,   25,   25,   25,   24,   24,
   24,   26,   26,   26,   26,   26,    7,    7,    7,   27,
   27,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    2,    3,    3,    3,    2,    3,    3,    3,
    4,    6,    4,    3,    3,    2,    2,    1,    1,    1,
    3,    3,    3,    2,    2,    1,    1,    1,   10,   10,
    8,    9,    6,    5,    5,    1,    3,    1,    3,    1,
    3,    3,    1,    2,    3,    3,    3,    3,    3,    3,
    1,    3,    3,    1,    1,    4,    4,    4,    3,    3,
    1,    1,    1,    1,    2,    2,    4,    3,    4,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,   36,   37,    0,    0,    0,    0,   38,    0,
    0,    3,    5,    6,    7,    8,    9,   10,   11,    0,
   28,   29,   30,    0,    0,    0,    0,    0,    0,    0,
   72,   73,   74,    0,    0,    0,    0,    0,    0,    0,
   64,   71,    0,    0,    0,    0,    0,    0,    4,   13,
   12,   34,   48,    0,    0,    0,   35,    0,    0,    0,
    0,    0,   81,    0,    0,   15,   14,    0,    0,    0,
   76,   75,    0,    0,   50,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   16,
   24,    0,   31,   33,   49,    0,   32,   79,   77,    0,
    0,    0,   25,   53,    0,   23,    0,   21,    0,    0,
    0,    0,    0,    0,    0,    0,   69,   70,   20,   19,
   18,    0,    0,   68,   67,   66,   52,   51,   54,    0,
   45,   44,    0,   22,   43,    0,    0,   46,    0,    0,
   41,    0,   47,   42,    0,   40,   39,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   19,   20,
   45,   37,   76,   38,   21,   22,   23,   24,   25,   26,
  139,  105,   39,   40,   41,   42,   65,
};
final static short yysindex[] = {                       192,
  217,  -34,    0,    0,   -5,  -38,   -5, -240,    0,    0,
  217,    0,    0,    0,    0,    0,    0,    0,    0,  -37,
    0,    0,    0, -155,  -32, -153,  217,  158,   -7,   -4,
    0,    0,    0,   -1,  176, -201,  -93,   -8,  -40,  -28,
    0,    0, -223, -217,   29,  -93,   36,  -52,    0,    0,
    0,    0,    0,   16, -141, -140,    0,   36,    2,   21,
  -15,   36,    0,   27,   75,    0,    0,  186,  186,   81,
    0,    0,  -34, -136,    0, -215,    0,  186,  186,  186,
  186,  186,  186,   41,   41,   41,   41,   87,    7,    0,
    0,  217,    0,    0,    0,  217,    0,    0,    0,   -4,
   54,  -17,    0,    0,  -74,    0,  -93,    0,   27,   27,
   27,   27,   27,   27,  -28,  -28,    0,    0,    0,    0,
    0, -117,  199,    0,    0,    0,    0,    0,    0, -139,
    0,    0,  -36,    0,    0, -114,  217,    0,   11, -110,
    0,  -13,    0,    0, -115,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,   37,    0,    0,    0,    0,    0,    0,    0,    0,
  132,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  145,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,  110,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,   69,    0,   89,  129,    0,    0,    0,    0,  -86,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -30,  -25,
  -23,  138,  154,  160,   23,   47,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   31,   67,    0,   13,    0,    0,    0,    0,    0,   30,
    0,  131,  -33,  117,    0,    0,    0,    0,   53,    0,
    0,    0,  130,   52,    0,   19,    0,
};
final static int YYTABLESIZE=490;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         61,
   65,   44,   84,  136,   85,   29,   51,  132,  137,  147,
   59,   55,   91,   86,  143,   58,   47,   60,   87,   82,
   81,   83,   62,  126,   68,   84,   56,   85,   98,   74,
  145,   27,   77,   67,   35,   68,   27,   48,   69,   36,
  106,   65,   88,   65,   65,   65,   63,  121,   89,   75,
  128,  142,  107,  108,   71,   59,   72,   63,   75,   93,
   65,   65,   65,   62,   97,   62,   62,   62,   72,   84,
   92,   85,   90,  130,   56,   29,   54,   49,   60,   56,
   48,   61,   62,   62,   62,   36,  104,   63,   80,   63,
   63,   63,   59,   49,  124,   48,   84,   58,   85,   60,
   52,   53,   57,   58,  117,  118,   63,   63,   63,   17,
   72,   72,   72,   72,   94,   72,   95,  129,   99,   75,
   73,  103,  122,   65,   96,   65,  123,  119,   78,  134,
    5,    1,   80,    6,    7,  115,  116,   46,  131,    2,
  146,    3,   47,    4,    2,   62,    2,   62,    3,    5,
    4,   70,    6,    7,    8,    9,    5,   64,    0,    6,
    7,    8,    9,   73,    0,  138,    0,  140,    0,   63,
   27,   63,    0,    5,    0,    0,    6,    7,   55,    0,
   27,  127,   73,   27,   27,    0,    0,    0,   49,   49,
    0,    0,    5,   72,   56,    6,    7,  101,  102,    0,
   57,    0,   36,    0,    0,    0,   49,  109,  110,  111,
  112,  113,  114,   80,    0,   61,    0,   43,   50,  135,
   36,    0,   78,   79,   80,   61,   59,   28,   61,   61,
   36,   58,    0,   60,   17,    0,   59,    0,  125,   59,
   59,   58,  144,   60,   58,   58,   60,   60,   66,    0,
   30,   31,   32,   78,   33,   34,   65,   65,    0,   65,
   55,   65,  120,   65,   65,   65,  141,   65,   65,   65,
   65,   65,   65,   65,   65,    0,   56,    0,   62,   62,
    0,   62,   57,   62,    0,   62,   62,   62,    0,   62,
   62,   62,   62,   62,   62,   62,   62,   31,   32,    0,
   33,    0,   63,   63,    0,   63,    0,   63,    0,   63,
   63,   63,    0,   63,   63,   63,   63,   63,   63,   63,
   63,    0,    0,    0,   72,   72,    0,   72,    0,   72,
    0,    0,    0,    0,    0,   72,   72,   72,   72,   72,
   72,   72,   72,    0,   80,   80,    0,   80,    0,   80,
    0,    0,    0,    0,    0,   80,   80,   80,   80,   80,
   80,   80,   80,    0,    0,   17,   17,    0,   17,    0,
   17,    0,    0,    0,    0,    0,   17,   17,   17,   17,
   17,   17,   17,   17,   78,   78,    0,   78,    0,   78,
    0,    0,    0,    0,   55,   78,   78,   78,   78,   78,
   78,   78,   78,    0,   55,    0,    0,   55,   55,    0,
   56,    0,    0,   61,   62,   32,   57,   33,   34,    0,
   56,    0,    0,   56,   56,    0,   57,    0,    0,   57,
   57,   30,   31,   32,    0,   33,   34,    0,    0,    0,
    0,  100,   31,   32,    0,   33,   34,    1,    2,    0,
    3,    0,    4,    0,    0,    2,    0,    3,    5,    4,
    0,    6,    7,    8,    9,    5,    0,    0,    6,    7,
    8,    9,  133,    2,    0,    3,    0,    4,    0,    0,
    0,    0,    0,    5,    0,    0,    6,    7,    8,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   43,   40,   45,   40,   44,  125,  123,  125,
   41,   44,   46,   42,  125,   41,  257,   41,   47,   60,
   61,   62,    0,   41,   40,   43,   59,   45,   44,  123,
   44,    1,   41,   41,   40,   40,  123,    8,   40,   45,
  256,   41,  266,   43,   44,   45,    0,   41,  266,   37,
  125,   41,  268,  269,  256,   26,  258,   28,   46,   44,
   60,   61,   62,   41,   44,   43,   44,   45,    0,   43,
  123,   45,   44,  107,   59,   40,   24,   11,   26,   59,
   44,  123,   60,   61,   62,   45,   74,   41,    0,   43,
   44,   45,  123,   27,   41,   59,   43,  123,   45,  123,
  256,  257,  256,  257,   86,   87,   60,   61,   62,    0,
   42,   43,   44,   45,  256,   47,  257,  105,   44,  107,
  257,   41,   92,  123,  123,  125,   96,   41,    0,  269,
  267,    0,   44,  270,  271,   84,   85,    7,  256,  257,
  256,  259,  257,  261,    0,  123,  257,  125,  259,  267,
  261,   35,  270,  271,  272,  273,  267,   28,   -1,  270,
  271,  272,  273,  257,   -1,  136,   -1,  137,   -1,  123,
  257,  125,   -1,  267,   -1,   -1,  270,  271,   41,   -1,
  267,  256,  257,  270,  271,   -1,   -1,   -1,  122,  123,
   -1,   -1,  267,  125,   41,  270,  271,   68,   69,   -1,
   41,   -1,   45,   -1,   -1,   -1,  140,   78,   79,   80,
   81,   82,   83,  125,   -1,  257,   -1,  256,  256,  256,
   45,   -1,  263,  264,  265,  267,  257,  262,  270,  271,
   45,  257,   -1,  257,  125,   -1,  267,   -1,  256,  270,
  271,  267,  256,  267,  270,  271,  270,  271,  256,   -1,
  256,  257,  258,  125,  260,  261,  256,  257,   -1,  259,
  123,  261,  256,  263,  264,  265,  256,  267,  268,  269,
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
  271,  256,  257,  258,   -1,  260,  261,   -1,   -1,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  256,  257,   -1,
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
"condicion_entre_parentesis : condicion ')'",
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

//#line 194 "tokens.y"


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
//#line 491 "Parser.java"
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
case 4:
//#line 27 "tokens.y"
{yyval = val_peek(0);}
break;
case 9:
//#line 37 "tokens.y"
{agregarEstructuraDetectada("Asignacion");}
break;
case 12:
//#line 42 "tokens.y"
{agregarEstructuraDetectada("Invocacion funcion");yyval = val_peek(0);}
break;
case 13:
//#line 43 "tokens.y"
{agregarError("Error: falta ',' en invocacion ejecutable. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 14:
//#line 46 "tokens.y"
{yyval = val_peek(0);}
break;
case 15:
//#line 47 "tokens.y"
{agregarError("Error: falta ')' en invocacion. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 16:
//#line 49 "tokens.y"
{agregarEstructuraDetectada("Impresion"); yyval = val_peek(0);}
break;
case 17:
//#line 50 "tokens.y"
{agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + ((Token) val_peek(0).obj).nroLinea); yyval = val_peek(0);}
break;
case 18:
//#line 53 "tokens.y"
{yyval = val_peek(0);}
break;
case 19:
//#line 54 "tokens.y"
{agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 20:
//#line 55 "tokens.y"
{agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(0);}
break;
case 21:
//#line 58 "tokens.y"
{yyval = val_peek(0);}
break;
case 22:
//#line 59 "tokens.y"
{yyval = val_peek(1);}
break;
case 23:
//#line 60 "tokens.y"
{agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + ((Token) val_peek(1).obj).nroLinea);yyval = val_peek(1);}
break;
case 24:
//#line 65 "tokens.y"
{yyval = val_peek(0);}
break;
case 25:
//#line 70 "tokens.y"
{agregarEstructuraDetectada("Condicion"); yyval = val_peek(0);}
break;
case 26:
//#line 71 "tokens.y"
{agregarError("Error: falta '(' antes de la condicion. Linea: " + ((Token) val_peek(0).obj).nroLinea);yyval = val_peek(0);}
break;
case 27:
//#line 72 "tokens.y"
{/*esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar*/
														 								/*que se coman tokens de mas e incluso no informar el errores*/
																						agregarError("Error: falta ')' luego de la condicion. Linea: " + ((Token) val_peek(0).obj).nroLinea);yyval = val_peek(0);}
break;
case 31:
//#line 84 "tokens.y"
{agregarEstructuraDetectada("Declaracion variable/s"); yyval = val_peek(0);}
break;
case 32:
//#line 85 "tokens.y"
{yyval = val_peek(0);}
break;
case 33:
//#line 86 "tokens.y"
{agregarError("Error: declaracion de tipo erronea. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 34:
//#line 87 "tokens.y"
{agregarError("Error: falta ID o ',' en la declaracion de variable/s. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 35:
//#line 88 "tokens.y"
{agregarError("Error: definicion de closure erronea. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 38:
//#line 95 "tokens.y"
{/*lo hago aca para que tome la primer linea incluso en funcion closure*/
													agregarEstructuraDetectada("Declaracion de tipo closure"); }
break;
case 39:
//#line 99 "tokens.y"
{yyval = val_peek(0);}
break;
case 40:
//#line 100 "tokens.y"
{agregarError("Error: falta '}' de cierre de la declaracion de closure. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 41:
//#line 101 "tokens.y"
{agregarError("Error: falta ')' luego del retorno del closure. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 42:
//#line 102 "tokens.y"
{agregarError("Error: falta ',' luego del retorno del closure. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 43:
//#line 103 "tokens.y"
{agregarError("Error: retorno no es de tipo closure. Se espera \"return( ID() )\" o \"return( {SENTENCIAS} )\". Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 44:
//#line 109 "tokens.y"
{agregarEstructuraDetectada("Declaracion de funcion simple"); yyval = val_peek(0);}
break;
case 45:
//#line 110 "tokens.y"
{agregarError("Error: falta '}' de cierre de la funcion. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 47:
//#line 115 "tokens.y"
{yyval = val_peek(0);}
break;
case 49:
//#line 119 "tokens.y"
{yyval = val_peek(0);}
break;
case 51:
//#line 125 "tokens.y"
{yyval = val_peek(0);}
break;
case 52:
//#line 126 "tokens.y"
{agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " +((Token) val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 54:
//#line 129 "tokens.y"
{yyval = val_peek(0);}
break;
case 55:
//#line 132 "tokens.y"
{yyval = val_peek(0);}
break;
case 56:
//#line 133 "tokens.y"
{yyval = val_peek(0);}
break;
case 57:
//#line 134 "tokens.y"
{yyval = val_peek(0);}
break;
case 58:
//#line 135 "tokens.y"
{yyval = val_peek(0);}
break;
case 59:
//#line 136 "tokens.y"
{yyval = val_peek(0);}
break;
case 60:
//#line 137 "tokens.y"
{yyval = val_peek(0);}
break;
case 61:
//#line 138 "tokens.y"
{agregarError("Error: condicion no valida. Incorrecta mezcla de expresiones y comparador. Linea: " + ((Token) val_peek(0).obj).nroLinea);}
break;
case 62:
//#line 141 "tokens.y"
{yyval = val_peek(0);}
break;
case 63:
//#line 142 "tokens.y"
{yyval = val_peek(0);}
break;
case 66:
//#line 147 "tokens.y"
{agregarEstructuraDetectada("Conversion explicita"); yyval = val_peek(0);}
break;
case 67:
//#line 148 "tokens.y"
{agregarError("Error: falta ')' en la conversion explicita. Linea: " + ((Token)val_peek(1).obj).nroLinea); yyval = val_peek(1);}
break;
case 68:
//#line 149 "tokens.y"
{agregarError("Error: tipo no valido para conversion. Linea: " + ((Token)val_peek(3).obj).nroLinea); yyval = val_peek(0);}
break;
case 69:
//#line 152 "tokens.y"
{yyval = val_peek(0);}
break;
case 70:
//#line 153 "tokens.y"
{yyval = val_peek(0);}
break;
case 73:
//#line 158 "tokens.y"
{List<Object> atts = tablaSimbolos.get(((Token)val_peek(0).obj).claveTablaSimbolo); /*$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS*/
																		 int valorInteger = (Integer) atts.get(1); /*el valor en la posicion 1 es el número de la*/
																		 if (valorInteger > 32767) /*si se pasa del limite positivo*/

																				if (!tablaSimbolos.containsKey("32767_i")){
																					List<Object> nuevosAtributos = new ArrayList<Object>();
																					nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(32767);
																					tablaSimbolos.put("32767_i", nuevosAtributos);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + ((Token)val_peek(0).obj).nroLinea);
																				}

																			}
break;
case 75:
//#line 171 "tokens.y"
{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get(((Token)val_peek(0).obj).claveTablaSimbolo).get(1);
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			List<Object> nuevosAtributos = new ArrayList<Object>();
																			nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, nuevosAtributos);
																			}
																		yyval = val_peek(0);
																		}
break;
case 76:
//#line 181 "tokens.y"
{agregarError("Error: negacion no permitida a este operando. Linea: " + ((Token) val_peek(1).obj).nroLinea);}
break;
case 77:
//#line 184 "tokens.y"
{yyval = val_peek(1);}
break;
case 78:
//#line 185 "tokens.y"
{agregarError("Error: falta ',' en asignacion. Linea: " + ((Token) val_peek(0).obj).nroLinea); yyval = val_peek(0);}
break;
case 79:
//#line 186 "tokens.y"
{agregarError("Error: r-value de la asignacion mal definido. Linea: " + ((Token) val_peek(1).obj).nroLinea); yyval = val_peek(0);}
break;
case 81:
//#line 189 "tokens.y"
{agregarEstructuraDetectada("Invocacion de funcion en asignacion");}
break;
//#line 891 "Parser.java"
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
