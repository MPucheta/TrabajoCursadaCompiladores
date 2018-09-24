
%{
package resources;
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

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
		|	error ','
		;



ejecutable 	: 	sentencia_if
		|	sentencia_while
		|	asignacion	{agregarEstructuraDetectada("Asignacion");}
		|	sentencia_impresion	{agregarEstructuraDetectada("Impresion");}
		|	id_invocacion {agregarEstructuraDetectada("Invocacion funcion");}
		;


id_invocacion				:	ID '('')'
										| ID '(' error	{agregarError("En linea: " + AL.nroLinea + " falta )");}
										;
sentencia_impresion	:	PRINT  cadena_cararacteres_entre_parentesis ','
										|	PRINT  cadena_cararacteres_entre_parentesis error {agregarError("En linea: " + AL.nroLinea + " falta ','");}
										;

cadena_cararacteres_entre_parentesis	:	'(' CADENA_CARACTERES ')'
																			|	'(' CADENA_CARACTERES error {agregarError("En linea: " + AL.nroLinea + " falta )");}
																			|	 error CADENA_CARACTERES ')' {agregarError("En linea: " + AL.nroLinea + " falta (");}
																			;

sentencia_if	:	IF condicion_entre_parentesis bloque_sentencias END_IF
							| IF condicion_entre_parentesis bloque_sentencias ELSE bloque_sentencias END_IF
							|	IF condicion_entre_parentesis bloque_sentencias error  {agregarError("En linea: " + AL.nroLinea + " Error en sentencia IF, falta end_if");}
							;



sentencia_while	:	WHILE condicion_entre_parentesis bloque_sentencias
		;



condicion_entre_parentesis	:	'(' condicion ')'
														|	'(' error {agregarError("En linea: " + AL.nroLinea + " falta )");}
														|	error condicion ')' {//el -1 es porque ya se paso en la lectura
																		agregarError("En linea: " + (AL.nroLinea-1) + " falta (");}
														;



declarativa 	: 	declaracion_variables	{agregarEstructuraDetectada("Declaracion variable");}
		|	declaracion_closure
		|	declaracion_funcion_simple
		;

declaracion_variables :	tipo_variable lista_variables ','
											|	tipo_closure	lista_variables ','
											;

tipo_variable	: 	INTEGER
							| 	USLINTEGER
							;

tipo_closure	:	FUN	{agregarEstructuraDetectada("Declaracion de tipo closure");}
		;

declaracion_closure			: 	tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' '}'
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' error {agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure error	{agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' error 	{agregarError("En linea: " + (AL.nroLinea) + " falta , ");}
												;


					;

declaracion_funcion_simple	:	VOID id_invocacion '{' conjunto_sentencias  '}'
														|	VOID id_invocacion '{' conjunto_sentencias  error {agregarError("En linea: " + (AL.nroLinea) + " falta } ");} 
														;


retorno_closure	: 	id_invocacion
			| 	'{' conjunto_sentencias '}'
			| 	'{' conjunto_sentencias error {agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
			;

lista_variables		:	ID
			|	lista_variables ';' ID
			;



bloque_sentencias 	:	ejecutable
			| 	'{' sentencias_ejecutables '}'
			|		'{' sentencias_ejecutables error	 {agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
			;
sentencias_ejecutables 	:	ejecutable
			|	sentencias_ejecutables ejecutable
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
		|	CTE_USLINTEGER '('expr error {agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
		;

term	 	: 	term '*' factor
		| 	term '/' factor
		| 	factor
		;

factor		: 	ID
					| 	CTE_INTEGER
					|	CTE_USLINTEGER
					| 	'-' CTE_INTEGER
					;

asignacion	:	ID ASIGN r_value_asignacion ','
						|	ID ASIGN r_value_asignacion error {agregarError("En linea: " + (AL.nroLinea) + " falta , ");}
						;
r_value_asignacion:	expr
									| id_invocacion
									;


%%


AnalizadorLexico AL = null;
List<String> estructurasGramaticalesDetectadas;
List<String> tokensLeidos;
List<String> errores;
int ultimoTokenLeido;

int yylex(){
	ultimoTokenLeido=AL.yylex();
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

public Parser(AnalizadorLexico AL)

{
	//yydebug=true;
	tokensLeidos=new ArrayList<>();
	estructurasGramaticalesDetectadas=new ArrayList<>();
	errores=new ArrayList<>();
	this.AL=AL;

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
