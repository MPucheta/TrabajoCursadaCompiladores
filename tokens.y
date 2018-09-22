
%{
package resources;
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;


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


ejecutable 	: 	IF '(' condicion ')' bloque_sentencias END_IF	{agregarEstructuraDetectada("Sentencia if");}
		|	IF '(' condicion ')' bloque_sentencias ELSE bloque_sentencias END_IF	{agregarEstructuraDetectada("Sentencia if-else");}
		| 	WHILE '(' condicion ')' bloque_sentencias {agregarEstructuraDetectada("Sentencia while");}
		|	asignacion	{agregarEstructuraDetectada("Asignacion");}
		|	PRINT '(' CADENA_CARACTERES ')' ','
		|	ID '(' ')' ','
		;


declarativa 	: 	tipo_variable lista_variables ','	{agregarEstructuraDetectada("Declaracion variable");}
		|	tipo_closure	lista_variables ','	{agregarEstructuraDetectada("Declaracion variable closure");}
		|	declaracion_closure			{agregarEstructuraDetectada("Closure");}
		|	declaracion_funcion_simple		{agregarEstructuraDetectada("Funcion simple");}
		;

tipo_variable	: 	INTEGER
		| 	USLINTEGER
		;

tipo_closure	:	FUN
		;

declaracion_closure			: 	tipo_closure ID '(' ')' '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' '}' 
					; 


					;

declaracion_funcion_simple	:	VOID ID '(' ')' '{' conjunto_sentencias  '}' 
					;
					

retorno_closure	: 	ID'('')'
			| 	'{' conjunto_sentencias '}'
			;

lista_variables	:	ID
			|	lista_variables ';' ID 




bloque_sentencias 	:	ejecutable
			| 	'{' bloque_sentencias '}'
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


AnalizadorLexico AL = null;
String estructurasGramaticalesDetectadas="";
int yylex(){
	int result=AL.yylex();
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