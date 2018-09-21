
%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import resources.*;
%}

/* YACC Declarations */
%token ID CTE_INTEGER INTEGER CTE_USLINTEGER USLINTEGER ASIGN COMP_MAYOR_IGUAL COMP_MENOR_IGUAL COMP_DISTINTO CADENA_CARACTERES IF ELSE END_IF PRINT WHILE VOID FUN RETURN

%left NEG /* negation--unary minus */

/* Grammar follows */
%%


programa 	: 	conjunto_sentencias
		;

conjunto_sentencias	:	sentencia
				|	conjunto_sentencias sentencia
				;
sentencia 	: 	declarativa
		| 	ejecutable
		;
ejecutable 	: 	IF '(' condicion ')' bloque_sentencias END_IF
		|	IF '(' condicion ')' bloque_sentencias ELSE bloque_sentencias END_IF
		| 	WHILE '(' condicion ')' bloque_sentencias ','
		|	asignacion
		|	PRINT '(' CADENA_CARACTERES ')' ','
		|	ID '(' ')' ','
		;

declarativa 	: 	tipo_variable lista_variables ','
		|	tipo_closure	lista_variables ','
		|	declaracion_closure
		|	declaracion_funcion_simple
		;

tipo_variable	: 	INTEGER
		| 	USLINTEGER
		;

tipo_closure	:	FUN
		;

declaracion_closure			: 	tipo_closure ID '(' ')' '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' '}' 
					; 

declaracion_funcion_simple	:	VOID ID '(' ')' '{' conjunto_sentencias  '}' 
					;
					

retorno_closure	: 	ID'('')'
			| 	'{' conjunto_sentencias '}'
			;

lista_variables	:	ID
			|	lista_variables ';' ID 




bloque_sentencias 	:	ejecutable
| 	bloque_sentencias ejecutable
; 
condicion	:	expr '=' expr
		|	expr '<' expr
		|	expr '>' expr
		|	expr COMP_MENOR_IGUAL expr
		|	expr COMP_MAYOR_IGUAL expr
		|	expr COMP_DISTINTO expr
		;

expr 		: 	expr '+' term
		| 	expr '-' term
		|	CTE_USLINTEGER '('expr')'
		| 	term
		;

term	 	: 	term '*' factor
		| 	term '/' factor
		| 	factor
		;

factor		: 	ID
		| 	CTE_INTEGER
		|	CTE_USLINTEGER
		| 	'-' CTE_INTEGER
		| 	'-' CTE_USLINTEGER
		;

asignacion	:	ID ASIGN expr ','
		|	ID ASIGN ID '(' ')' ','
		;


%%

AnalizadorLexico AL = new AnalizadorLexico();
int yylex(){
	System.out.println(AL.yylex());


}

