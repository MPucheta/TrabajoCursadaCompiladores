
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


programa 			: 	conjunto_sentencias
							|		error conjunto_sentencias
							;

conjunto_sentencias	:	sentencia
										|	conjunto_sentencias sentencia
										;
sentencia 	: 	declarativa
						| 	ejecutable
						;



ejecutable 	: 	sentencia_if
		|	sentencia_while
		|	asignacion 	{agregarEstructuraDetectada("Asignacion");}
		|	sentencia_impresion
		|	invocacion
		;

invocacion	:	id_invocacion ',' {agregarEstructuraDetectada("Invocacion funcion");}
						;

id_invocacion				:	ID '('')'
										| ID '(' error	{agregarError("En linea: " + AL.nroLinea + " falta ) en invocacion");}
										;
sentencia_impresion	:	PRINT  cadena_cararacteres_entre_parentesis ','	{agregarEstructuraDetectada("Impresion");}
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



condicion_entre_parentesis	:	'(' condicion ')'	{agregarEstructuraDetectada("Condicion");}
														|	'(' condicion {//esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar
														 								//que se coman tokens de mas e incluso no informar el errores
																						agregarError("En linea: " + AL.nroLinea + " falta ) en condicion");}
														;



declarativa 	: 	declaracion_variables
		|	declaracion_closure
		|	declaracion_funcion_simple
		;

declaracion_variables :	tipo_variable lista_variables ','	{agregarEstructuraDetectada("Declaracion variable/s");}
											|	tipo_closure	lista_variables ','
											|	 lista_variables ',' error  {agregarError("En linea: " + (AL.nroLinea) + " Declaracion de tipo erronea ");}
											| tipo_variable error {agregarError("En linea: " + (AL.nroLinea) + " Error en declaracion de tipo, falta ID o ',' ");}
											| tipo_closure error {agregarError("En linea: " + (AL.nroLinea) + " Error en definicion de closure");}
											;

tipo_variable	: 	INTEGER
							| 	USLINTEGER
							;

tipo_closure	:	FUN	 {//lo hago aca para que tome la primer linea incluso en funcion closure
													agregarEstructuraDetectada("Declaracion de tipo closure"); }
							;

declaracion_closure			: 	tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' '}'
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' error {agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure error	{agregarError("En linea: " + (AL.nroLinea) + " falta ) ");}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' error 	{agregarError("En linea: " + (AL.nroLinea) + " falta , ");}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN  error 	{agregarError("En linea: " + (AL.nroLinea) + " Retorno no es de tipo closure, se espera return( ID() ) o return( {sentencias} )");}
												;




declaracion_funcion_simple	:	VOID id_invocacion '{' conjunto_sentencias  '}'	{agregarEstructuraDetectada("Declaracion de funcion simple");}
														|	VOID id_invocacion '{' conjunto_sentencias  error {agregarError("En linea: " + (AL.nroLinea) + " falta } ");}
														;


retorno_closure	: 	id_invocacion
			| 	'{' conjunto_sentencias '}'
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
		|	error {agregarError("En linea: " + (AL.nroLinea) + "  Condicion no valida: Incorrecta mezcla de operandos y expresiones");}
		;

expr 		: 	expr '+' term
		| 	expr '-' term
		|		casting
		| 	term
		;

casting :	USLINTEGER '('expr')' {agregarEstructuraDetectada("Conversion explicita");}
				|	USLINTEGER '('expr error {agregarError("En linea: " + (AL.nroLinea) + " falta ) en casteo ");}
				|	error '('expr')'	{agregarError("En linea: " + (AL.nroLinea) + " tipo no valido para casting  ");}
				;

term	 	: 	term '*' factor
		| 	term '/' factor
		| 	factor
		;

factor				:	 	ID
							| 	CTE_INTEGER				{List<Object> atts = tablaSimbolos.get($1.sval); //$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS
																		 int valorInteger = (Integer) atts.get(1); //el valor en la posicion 1 es el número de la
																		 if (valorInteger > 32767) //si se pasa del limite positivo

																				if (!tablaSimbolos.containsKey("32767_i")){
																					List<Object> nuevosAtributos = new ArrayList<Object>();
																					nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(32767);
																					tablaSimbolos.put("32767_i", nuevosAtributos);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + AL.nroLinea);
																				}

																			}
							|	CTE_USLINTEGER
							| '-' CTE_INTEGER		{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get($2.sval).get(1);
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			List<Object> nuevosAtributos = new ArrayList<Object>();
																			nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, nuevosAtributos);
																			}
																		}
							|	'-' error {agregarError("En linea: " + (AL.nroLinea) + " Negacion no permitida con este operando  ");}
							;

asignacion	:	ID ASIGN r_value_asignacion ','
						|	ID ASIGN r_value_asignacion error 	{agregarError("En linea: " + (AL.nroLinea) + " Error detectado de lado derecho de asignacion ");}
						;
r_value_asignacion:	expr
									| id_invocacion	{agregarEstructuraDetectada("Invocacion de funcion en asignacion");}
									;


%%


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

	String leido= "Linea: " + AL.nroLinea + " Token leido: " + ultimoTokenLeido + " designado como: " + Token.tipoToken(ultimoTokenLeido) + "\n";
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
