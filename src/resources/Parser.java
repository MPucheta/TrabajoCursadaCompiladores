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
    0,    1,    1,    2,    2,    4,    4,    4,    4,    4,
    4,    3,    3,    3,    3,    8,    8,   10,   11,   12,
   13,   13,    9,    9,    6,    6,   14,   14,    5,    5,
    5,    5,    5,    5,   15,   15,   15,   15,   16,   16,
   16,   17,   17,   17,   17,   17,    7,    7,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    6,    8,    5,    1,    5,
    4,    3,    3,    1,    1,    1,    1,    1,   12,    7,
    3,    3,    1,    3,    1,    3,    1,    2,    3,    3,
    3,    3,    3,    3,    3,    3,    4,    1,    3,    3,
    1,    1,    1,    1,    2,    2,    4,    6,
};
final static short yydefred[] = {                         0,
    0,   16,   17,    0,    0,    0,    0,   18,    0,    0,
    2,    4,    5,    9,    0,    0,   14,   15,    0,    0,
    0,    0,    0,    0,    3,   23,    0,    0,    0,    0,
   43,    0,    0,    0,    0,   41,    0,   42,    0,    0,
    0,    0,    0,   12,    0,    0,   13,    0,    0,   45,
   46,   47,    0,    0,    0,    0,   11,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   24,    0,    0,
    0,   44,    0,    0,   39,   40,    0,   25,    0,    0,
    0,    0,    0,    0,    0,   10,    8,    0,    0,   48,
   37,   27,    0,    0,    6,    0,    0,   26,   28,    0,
   20,    0,    7,    0,    0,    0,    0,    0,    0,    0,
   21,   22,    0,   19,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   39,   79,   14,   15,   27,   16,
   17,   18,  107,   93,   40,   35,   36,
};
final static short yysindex[] = {                      -148,
  -25,    0,    0,   39,   70,   72, -198,    0,    0, -148,
    0,    0,    0,    0, -143, -141,    0,    0,  -22,   76,
  -18, -146,  -18,   78,    0,    0,   -2,   81,    3,   86,
    0,   87, -171,   53,   31,    0,   84,    0,   88,   -6,
   89,   90,   91,    0, -124,   93,    0,   94,  -18,    0,
    0,    0,  -12,  -12,  -12,  -12,    0,  -95,  -18,  -18,
  -18,  -18,  -18,  -18,   92,  -95,   14,    0,   15,   95,
   45,    0,   31,   31,    0,    0, -199,    0, -223,   49,
   49,   49,   49,   49,   49,    0,    0, -148, -148,    0,
    0,    0,  -87,  -95,    0, -114, -166,    0,    0, -129,
    0,  101,    0, -106,  102, -148,  103,  105, -107,  104,
    0,    0,   24,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  155,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    4,    0,   22,
    0,  -41,    0,    0,  -36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -31,   -9,    0,    0,    0,    0,    0,  120,
  126,  127,  128,  130,  132,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -45,    6,    0,  -17,  151,  -44,    0,    0,  161,    0,
    0,    0,    0,    0,   21,   46,   19,
};
final static int YYTABLESIZE=259;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         44,
   44,   44,   44,   44,   38,   44,   38,   38,   38,   35,
  101,   35,   35,   35,   20,   25,  106,  112,   44,   44,
   44,   87,   33,   38,   38,   38,   33,   77,   35,   35,
   35,   36,   33,   36,   36,   36,   53,   98,   54,   34,
   78,   44,   96,   97,   94,   95,   47,   23,   78,  100,
   36,   36,   36,   63,   62,   64,   45,    1,   24,   92,
  109,   45,   23,   42,   42,   42,   42,    4,   42,   71,
    5,    6,   55,   75,   76,   99,   78,   56,   21,   80,
   81,   82,   83,   84,   85,   91,   50,   53,   51,   54,
    1,   53,    2,   54,    3,   53,   52,   54,   73,   74,
    4,   25,   25,    5,    6,    7,    8,  102,    1,   22,
    2,   23,    3,   26,   25,   28,   37,   43,    4,   41,
   46,    5,    6,    7,    8,   48,   49,   57,   58,   65,
   66,   67,   68,   69,   70,   86,   88,   89,   90,  103,
  104,  108,    1,  110,    2,  111,    3,  113,  114,    1,
  105,    2,    4,    3,    1,    5,    6,    7,    8,    4,
   33,    1,    5,    6,    7,    8,   32,   34,   29,    1,
   30,    4,   31,   42,    5,    6,   29,    0,    0,    4,
    0,    0,    5,    6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   44,   44,   44,    0,    0,   38,   38,   38,    0,
    0,   35,   35,   35,   30,   31,   19,   32,   38,   31,
    0,   32,    0,    0,   38,   31,    0,   72,    0,    0,
    0,    0,    0,   36,   36,   36,   59,   60,   61,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
  125,   43,   44,   45,   40,   10,  123,  125,   60,   61,
   62,   66,   45,   60,   61,   62,   45,  123,   60,   61,
   62,   41,   45,   43,   44,   45,   43,  125,   45,   19,
   58,   44,   88,   89,  268,  269,   44,   44,   66,   94,
   60,   61,   62,   60,   61,   62,   59,  257,  257,   77,
  106,   59,   59,   42,   43,   44,   45,  267,   47,   49,
  270,  271,   42,   55,   56,   93,   94,   47,   40,   59,
   60,   61,   62,   63,   64,   41,  258,   43,  260,   45,
  257,   43,  259,   45,  261,   43,   44,   45,   53,   54,
  267,   96,   97,  270,  271,  272,  273,  274,  257,   40,
  259,   40,  261,  257,  109,  257,   41,   40,  267,  266,
   40,  270,  271,  272,  273,   40,   40,   44,   41,   41,
   41,   41,  257,   41,   41,   44,  123,  123,   44,  269,
   40,   40,  257,   41,  259,   41,  261,   44,  125,  257,
  257,  259,  267,  261,    0,  270,  271,  272,  273,  267,
   41,  257,  270,  271,  272,  273,   41,   41,   41,  257,
   41,  267,   41,   23,  270,  271,   16,   -1,   -1,  267,
   -1,   -1,  270,  271,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  263,  264,  265,   -1,   -1,  263,  264,  265,   -1,
   -1,  263,  264,  265,  257,  258,  262,  260,  257,  258,
   -1,  260,   -1,   -1,  257,  258,   -1,  260,   -1,   -1,
   -1,   -1,   -1,  263,  264,  265,  263,  264,  265,
};
}
final static short YYFINAL=9;
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
"ejecutable : IF '(' condicion ')' bloque_sentencias END_IF",
"ejecutable : IF '(' condicion ')' bloque_sentencias ELSE bloque_sentencias END_IF",
"ejecutable : WHILE '(' condicion ')' bloque_sentencias",
"ejecutable : asignacion",
"ejecutable : PRINT '(' CADENA_CARACTERES ')' ','",
"ejecutable : ID '(' ')' ','",
"declarativa : tipo_variable lista_variables ','",
"declarativa : tipo_closure lista_variables ','",
"declarativa : declaracion_closure",
"declarativa : declaracion_funcion_simple",
"tipo_variable : INTEGER",
"tipo_variable : USLINTEGER",
"tipo_closure : FUN",
"declaracion_closure : tipo_closure ID '(' ')' '{' conjunto_sentencias RETURN '(' retorno_closure ')' ',' '}'",
"declaracion_funcion_simple : VOID ID '(' ')' '{' conjunto_sentencias '}'",
"retorno_closure : ID '(' ')'",
"retorno_closure : '{' conjunto_sentencias '}'",
"lista_variables : ID",
"lista_variables : lista_variables ';' ID",
"bloque_sentencias : ejecutable",
"bloque_sentencias : '{' sentencias_ejecutables '}'",
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
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : CTE_INTEGER",
"factor : CTE_USLINTEGER",
"factor : '-' CTE_INTEGER",
"factor : '-' CTE_USLINTEGER",
"asignacion : ID ASIGN expr ','",
"asignacion : ID ASIGN ID '(' ')' ','",
};

//#line 111 ".\tokens.y"


AnalizadorLexico AL = null;
String estructurasGramaticalesDetectadas="";


int yylex(){
	int result=AL.yylex();
	//la siguiente condicion se debe hacer porque estas estructuras (ej, if, while) ocupan varias lineas de texto
	//por lo que cuando el parsing detecta finalmente que un if termina en un end_if, el AL.nroLinea ya avanzo.
	//Por lo tanto sin esto el nroLinea mostrado seria el del fin de la estructura y no del comienzo
	if(result==Token.IF || result ==Token.WHILE){
		agregarEstructuraDetectada("Sentencia " + Token.tipoToken(result));
	}
	System.out.println("Linea: " + AL.nroLinea + " Token leido: " + result + " designado como: " + Token.tipoToken(result) );
	return result;

}

void yyerror(String s)
{
 System.out.println("En linea: " + AL.nroLinea + ". Ocurrio un error de parsing " + s);
}

public Parser(AnalizadorLexico AL)

{
	this.AL=AL;
  
}

public String getEstructurasGramaticalesDetectadas(){
	return this.estructurasGramaticalesDetectadas;
}

private void agregarEstructuraDetectada(String tipo){
	this.estructurasGramaticalesDetectadas=this.estructurasGramaticalesDetectadas + " " + tipo + " en linea " + AL.nroLinea + "\n";

}
//#line 358 "Parser.java"
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
//#line 34 ".\tokens.y"
{agregarEstructuraDetectada("Asignacion");}
break;
case 11:
//#line 36 ".\tokens.y"
{agregarEstructuraDetectada("Invocacion funcion");}
break;
case 12:
//#line 40 ".\tokens.y"
{agregarEstructuraDetectada("Declaracion variable");}
break;
case 18:
//#line 50 ".\tokens.y"
{agregarEstructuraDetectada("Declaracion de tipo closure");}
break;
//#line 523 "Parser.java"
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
