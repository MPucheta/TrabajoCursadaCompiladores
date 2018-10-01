
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
										|	conjunto_sentencias sentencia {$$ = $2;}
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

invocacion	:	id_invocacion ',' {agregarEstructuraDetectada("Invocacion funcion");$$ = $2;}
						| id_invocacion error {agregarError("Error: falta ',' en invocacion ejecutable. Linea: " + ((Token) $1.obj).nroLinea);}
						;

id_invocacion				:	ID '('')' {$$ = $3;}
										| ID '(' error	{agregarError("Error: falta ')' en invocacion o declaracion de closure/funcion. Linea: " + ((Token) $2.obj).nroLinea);}
										;
sentencia_impresion	:	PRINT  cadena_cararacteres_entre_parentesis ','	{agregarEstructuraDetectada("Impresion"); $$ = $3;}
										|	PRINT  cadena_cararacteres_entre_parentesis  {agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + ((Token) $2.obj).nroLinea); $$ = $2;}
										| PRINT error ','{agregarError("Error: sentencia de impresion erronea. Linea: " + ((Token) $1.obj).nroLinea);}
										;

cadena_cararacteres_entre_parentesis	:	'(' CADENA_CARACTERES ')' {$$ = $3;}
																			|	'(' CADENA_CARACTERES error {agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + ((Token) $2.obj).nroLinea); $$ = $2;}
																			|	 error CADENA_CARACTERES ')' {agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + ((Token) $2.obj).nroLinea); $$ = $3;}
																			| '(' error ')' {agregarError("Error: solo se pueden imprimir cadenas de caracteres. Linea: " + ((Token) $2.obj).nroLinea);$$ = $3;}
																			;

sentencia_if	:	IF condicion_entre_parentesis bloque_sentencias END_IF {$$ = $4;}
							| IF condicion_entre_parentesis bloque_sentencias ELSE bloque_sentencias END_IF {$$ = $5;}
							|	IF condicion_entre_parentesis bloque_sentencias error  {agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + ((Token) $3.obj).nroLinea);$$ = $3;}
							;



sentencia_while	:	WHILE condicion_entre_parentesis bloque_sentencias {$$ = $3;}
								;



condicion_entre_parentesis	:	'(' condicion ')'	{agregarEstructuraDetectada("Condicion"); $$ = $3;}
														| 	error condicion ')' {agregarError("Error: falta '(' antes de la condicion. Linea: " + ((Token) $1.obj).nroLinea);$$ = $3;}
														|	'(' condicion {//esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar
														 								//que se coman tokens de mas e incluso no informar el errores
																						agregarError("Error: falta ')' luego de la condicion. Linea: " + ((Token) $2.obj).nroLinea);$$ = $2;}
														;



declarativa 	: 	declaracion_variables
		|	declaracion_closure
		|	declaracion_funcion_simple
		;

declaracion_variables :	tipo_variable lista_variables ','	{agregarEstructuraDetectada("Declaracion variable/s"); $$ = $3;}
											|	tipo_closure	lista_variables ',' {$$ = $3;}
											|	 lista_variables ',' error  {agregarError("Error: declaracion de tipo erronea. Linea: " + ((Token) $2.obj).nroLinea); $$ = $2;}
											| tipo_variable error {agregarError("Error: falta ID o ',' en la declaracion de variable/s. Linea: " + ((Token) $1.obj).nroLinea);}
											| tipo_closure error {agregarError("Error: definicion de closure erronea. Linea: " + ((Token) $1.obj).nroLinea);}
											;

tipo_variable	: 	INTEGER
							| 	USLINTEGER
							;

tipo_closure	:	FUN	 {//lo hago aca para que tome la primer linea incluso en funcion closure
													agregarEstructuraDetectada("Declaracion de tipo closure"); }
							;

declaracion_closure			: 	tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' '}' {$$ = $10;}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' error {agregarError("Error: falta '}' de cierre de la declaracion de closure. Linea: " + ((Token) $9.obj).nroLinea); $$ = $9;}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure error	{agregarError("Error: falta ')' luego del retorno del closure. Linea: " + ((Token) $7.obj).nroLinea); $$ = $7;}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' error 	{agregarError("Error: falta ',' luego del retorno del closure. Linea: " + ((Token) $8.obj).nroLinea); $$ = $8;}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN  error 	{agregarError("Error: retorno no es de tipo closure. Se espera \"return( ID() )\" o \"return( {SENTENCIAS} )\". Linea: " + ((Token) $5.obj).nroLinea); $$ = $5;}/**/
												;




declaracion_funcion_simple	:	VOID id_invocacion '{' conjunto_sentencias  '}'	{agregarEstructuraDetectada("Declaracion de funcion simple"); $$ = $5;}
														|	VOID id_invocacion '{' conjunto_sentencias  error {agregarError("Error: falta '}' de cierre de la funcion. Linea: " + ((Token) $4.obj).nroLinea); $$ = $4;}
														;


retorno_closure	: 	id_invocacion
			| 	'{' conjunto_sentencias '}' {$$ = $3;}
			;

lista_variables		:	ID
									|	lista_variables ';' ID {$$ = $3;}
									;



bloque_sentencias 	:	ejecutable
			| 	'{' sentencias_ejecutables '}'			{$$ = $3;}
			|		'{' sentencias_ejecutables error	 {agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " +((Token) $2.obj).nroLinea); $$ = $2;}
			;
sentencias_ejecutables 	:	ejecutable
			|	sentencias_ejecutables ejecutable {$$ = $2;}
			;

condicion	:	expr '=' expr								{$$ = $3;}
		|	expr '<' expr											{$$ = $3;}
		|	expr '>' expr											{$$ = $3;}
		|	expr COMP_MENOR_IGUAL expr				{$$ = $3;}
		|	expr COMP_MAYOR_IGUAL expr				{$$ = $3;}
		|	expr COMP_DISTINTO expr						{$$ = $3;}
		|	error {agregarError("Error: condicion no valida. Incorrecta mezcla de expresiones y comparador. Linea: " + ((Token) $1.obj).nroLinea);}
		;

expr 		: 	expr '+' term 			{$$ = $3;}
		| 	expr '-' term 					{$$ = $3;}
		|		casting
		| 	term
		;

casting :	USLINTEGER '('expr')' {agregarEstructuraDetectada("Conversion explicita"); $$ = $4;}
				|	USLINTEGER '('expr error {agregarError("Error: falta ')' en la conversion explicita. Linea: " + ((Token)$3.obj).nroLinea); $$ = $3;}
				|	error '('expr')'	{agregarError("Error: tipo no valido para conversion. Linea: " + ((Token)$1.obj).nroLinea); $$ = $4;}
				;

term	 	: 	term '*' factor {$$ = $3;}
		| 	term '/' factor {$$ = $3;}
		| 	factor
		;

factor				:	 	ID
							| 	CTE_INTEGER				{List<Object> atts = tablaSimbolos.get(((Token)$1.obj).claveTablaSimbolo); //$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS
																		 int valorInteger = (Integer) atts.get(1); //el valor en la posicion 1 es el número de la
																		 if (valorInteger > 32767) //si se pasa del limite positivo

																				if (!tablaSimbolos.containsKey("32767_i")){
																					List<Object> nuevosAtributos = new ArrayList<Object>();
																					nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(32767);
																					tablaSimbolos.put("32767_i", nuevosAtributos);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + ((Token)$1.obj).nroLinea);
																				}

																			}
							|	CTE_USLINTEGER
							| '-' CTE_INTEGER		{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get(((Token)$2.obj).claveTablaSimbolo).get(1);
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			List<Object> nuevosAtributos = new ArrayList<Object>();
																			nuevosAtributos.add("CTE_INTEGER");nuevosAtributos.add(new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, nuevosAtributos);
																			}
																		$$ = $2;
																		}
							|	'-' error {agregarError("Error: negacion no permitida a este operando. Linea: " + ((Token) $1.obj).nroLinea);}
							;

asignacion	:	ID ASIGN r_value_asignacion ',' {$$ = $3;}
						|	ID ASIGN r_value_asignacion		{agregarError("Error: falta ',' en asignacion. Linea: " + ((Token) $3.obj).nroLinea); $$ = $3;}
						|	ID ASIGN error ',' 	{agregarError("Error: r-value de la asignacion mal definido. Linea: " + ((Token) $3.obj).nroLinea); $$ = $4;}
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
