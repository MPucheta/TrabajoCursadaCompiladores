package resources;







//#line 3 ".\tokens.y"
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;

//#line 22 "Parser.java"




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
   13,   13,    9,    9,    6,    6,    5,    5,    5,    5,
    5,    5,   14,   14,   14,   14,   15,   15,   15,   16,
   16,   16,   16,   16,    7,    7,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    6,    8,    6,    1,    5,
    4,    3,    3,    1,    1,    1,    1,    1,   12,    7,
    3,    3,    1,    3,    1,    2,    3,    3,    3,    3,
    3,    3,    3,    3,    4,    1,    3,    3,    1,    1,
    1,    1,    2,    2,    4,    6,
};
final static short yydefred[] = {                         0,
    0,   16,   17,    0,    0,    0,    0,   18,    0,    0,
    2,    4,    5,    9,    0,    0,   14,   15,    0,    0,
    0,    0,    0,    0,    3,   23,    0,    0,    0,    0,
   41,    0,    0,    0,    0,   39,    0,   40,    0,    0,
    0,    0,    0,   12,    0,    0,   13,    0,    0,   43,
   44,   45,    0,    0,    0,    0,   11,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   24,    0,    0,
    0,   42,    0,    0,   37,   38,   25,    0,    0,    0,
    0,    0,    0,    0,   10,    0,    0,    0,   46,   35,
    0,    6,   26,    8,    0,    0,    0,   20,    0,    7,
    0,    0,    0,    0,    0,    0,    0,   21,   22,    0,
   19,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   39,   78,   14,   15,   27,   16,
   17,   18,  104,   40,   35,   36,
};
final static short yysindex[] = {                      -164,
  -38,    0,    0,  -23,  -18,   -1, -229,    0,    0, -164,
    0,    0,    0,    0, -214, -210,    0,    0,  -42,   15,
  -16, -206,  -16,   59,    0,    0,  -10,   64,   11,   78,
    0,   85, -162,    8,    3,    0,   82,    0,   87,    1,
   88,  104,  109,    0, -106,  111,    0,  113,  -16,    0,
    0,    0,  -12,  -12,  -12,  -12,    0, -140,  -16,  -16,
  -16,  -16,  -16,  -16,  112, -140,   35,    0,   36,  116,
   91,    0,    3,    3,    0,    0,    0, -155,   76,   76,
   76,   76,   76,   76,    0,  -44, -164, -164,    0,    0,
 -140,    0,    0,    0, -124, -186, -147,    0,  121,    0,
 -119,  122, -164,  123,  124, -104,  126,    0,    0,   46,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  172,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   30,    0,   97,
    0,  -35,    0,    0,  -30,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -25,   -3,    0,    0,    0,    0,  132,  133,
  134,  135,  136,  137,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   -9,   -5,    0,   14,  156,  -43,    0,    0,  164,    0,
    0,    0,    0,    5,   23,   27,
};
final static int YYTABLESIZE=266;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         94,
   98,   20,   33,  103,   25,   42,   42,   42,   42,   42,
   36,   42,   36,   36,   36,   33,   21,   33,   33,   33,
  109,   22,   86,   34,   42,   42,   42,   24,   33,   36,
   36,   36,   33,   44,   33,   33,   33,   34,   23,   34,
   34,   34,   26,   53,   55,   54,   28,   97,   45,   56,
   53,   52,   54,   71,   47,   37,   34,   34,   34,   41,
   63,   62,   64,   79,   80,   81,   82,   83,   84,   45,
    1,   77,    2,   23,    3,   73,   74,   95,   96,   77,
    4,   75,   76,    5,    6,    7,    8,   99,   23,   25,
   25,   93,    1,  106,    2,   50,    3,   51,   43,   93,
   25,    1,    4,   46,   77,    5,    6,    7,    8,    1,
   93,    4,   91,   92,    5,    6,    1,   48,   53,    4,
   54,  100,    5,    6,   49,   57,    4,   58,   65,    5,
    6,   90,    1,   53,    2,   54,    3,  102,   40,   40,
   40,   40,    4,   40,   66,    5,    6,    7,    8,   67,
   68,   69,    1,   70,    2,   85,    3,   87,   88,   89,
  101,  105,    4,  107,  108,    5,    6,    7,    8,  110,
  111,    1,   31,   30,   32,   27,   28,   29,   42,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,   30,   31,    0,   32,    0,    0,
    0,    0,    4,   19,    0,    5,    6,   42,   42,   42,
    0,    0,   36,   36,   36,    0,    0,   33,   33,   33,
   38,   31,    0,   32,   38,   31,    0,   72,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   34,
   34,   34,    0,   59,   60,   61,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
  125,   40,   45,  123,   10,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,   41,   40,   43,   44,   45,
  125,   40,   66,   19,   60,   61,   62,  257,   45,   60,
   61,   62,   45,   44,   60,   61,   62,   41,   40,   43,
   44,   45,  257,   43,   42,   45,  257,   91,   59,   47,
   43,   44,   45,   49,   44,   41,   60,   61,   62,  266,
   60,   61,   62,   59,   60,   61,   62,   63,   64,   59,
  257,   58,  259,   44,  261,   53,   54,   87,   88,   66,
  267,   55,   56,  270,  271,  272,  273,  274,   59,   95,
   96,   78,  257,  103,  259,  258,  261,  260,   40,   86,
  106,  257,  267,   40,   91,  270,  271,  272,  273,  257,
   97,  267,  268,  269,  270,  271,  257,   40,   43,  267,
   45,  269,  270,  271,   40,   44,  267,   41,   41,  270,
  271,   41,  257,   43,  259,   45,  261,  257,   42,   43,
   44,   45,  267,   47,   41,  270,  271,  272,  273,   41,
  257,   41,  257,   41,  259,   44,  261,  123,  123,   44,
   40,   40,  267,   41,   41,  270,  271,  272,  273,   44,
  125,    0,   41,   41,   41,   41,   41,   41,   23,   16,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,   -1,  257,  258,   -1,  260,   -1,   -1,
   -1,   -1,  267,  262,   -1,  270,  271,  263,  264,  265,
   -1,   -1,  263,  264,  265,   -1,   -1,  263,  264,  265,
  257,  258,   -1,  260,  257,  258,   -1,  260,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  263,
  264,  265,   -1,  263,  264,  265,
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
"ejecutable : WHILE '(' condicion ')' bloque_sentencias ','",
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
"bloque_sentencias : bloque_sentencias ejecutable",
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

//#line 100 ".\tokens.y"

AnalizadorLexico AL = null;
int yylex(){
	int result=AL.yylex();
	System.out.println("token leido: " + result);
	return result;

}

void yyerror(String s)
{
 System.out.println("par:"+s);
}

//#line 325 "Parser.java"
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
public Parser(AnalizadorLexico AL)

{
	this.AL=AL;
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
