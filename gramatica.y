
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


programa 			: 	conjunto_sentencias {	this.raizArbolSintactico=(Arbol)$1.obj;}

							|		error conjunto_sentencias
							;

conjunto_sentencias	:	sentencia {$$ = agregarNodo("lista_sentencias", $1, new ParserVal(new Hoja(null)));}
										|	sentencia conjunto_sentencias {$$ = agregarNodo("lista_sentencias",$1,$2);}
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

invocacion	:	id_invocacion ',' {agregarEstructuraDetectada("Invocacion funcion");$$ = agregarHoja(obtenerLexema($1));}/*falta agregar algo para diferenciar que es una invocacion*/
						| id_invocacion error {agregarError("Error: falta ',' en invocacion ejecutable. Linea: " + ((Token) $1.obj).nroLinea);}
						;

id_invocacion				:	ID '('')' {$$ = $1;} //ROMPO ACÁ, VALOR ANTERIOR $3
										| ID '(' error	{agregarError("Error: falta ')' en invocacion o declaracion de closure/funcion. Linea: " + ((Token) $2.obj).nroLinea);}
										;
sentencia_impresion	:	PRINT  cadena_cararacteres_entre_parentesis ','	{agregarEstructuraDetectada("Impresion"); $$ = agregarNodoRengo("Impresion",$2);} //se quiere agarrar el string de cadena entre parentesis
										|	PRINT  cadena_cararacteres_entre_parentesis  {agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + ((Token) $2.obj).nroLinea); $$ = $2;}
										| PRINT error ','{agregarError("Error: sentencia de impresion erronea. Linea: " + ((Token) $1.obj).nroLinea);}
										;

cadena_cararacteres_entre_parentesis	:	'(' CADENA_CARACTERES ')' {$$ = agregarHoja(obtenerLexema($2));} //$2 es un token, uso el metodo obtenerLexema para sacar lo que dice.ROMPO ACÁ. VALOR ANTERIOR $3
																			|	'(' CADENA_CARACTERES error {agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + ((Token) $2.obj).nroLinea); $$ = $2;}
																			|	 error CADENA_CARACTERES ')' {agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + ((Token) $2.obj).nroLinea); $$ = $3;}
																			| '(' error ')' {agregarError("Error: solo se pueden imprimir cadenas de caracteres. Linea: " + ((Token) $2.obj).nroLinea);$$ = $3;}
																			;

sentencia_if	:	IF condicion_entre_parentesis bloque_sentencias END_IF {$$ = agregarNodo("if",$2,agregarNodoRengo("cuerpo",agregarNodoRengo("then",$3)));}
							| IF condicion_entre_parentesis bloque_sentencias ELSE bloque_sentencias END_IF {$$ = agregarNodo("if_else",$2,agregarNodo("cuerpo",agregarNodoRengo("then",$3),agregarNodoRengo("else",$5)));} //genero el nodo if y el nodo cuerpo generando las ramas en la misma linea. Esto puede ser confuso, cualquier cosa ver el diagrama de arbol con checkpoints de la catedra de gen de codigo
							|	IF condicion_entre_parentesis bloque_sentencias error  {agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + ((Token) $3.obj).nroLinea);$$ = $3;}
							;



sentencia_while	:	WHILE condicion_entre_parentesis bloque_sentencias {$$ = agregarNodo("while",$2,$3);}
								;



condicion_entre_parentesis	:	'(' condicion ')'	{agregarEstructuraDetectada("Condicion"); $$ = agregarNodoRengo("condicion",$2);}
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



bloque_sentencias 	:	ejecutable {$$ = agregarNodo("lista_sentencias", $1, new ParserVal(new Hoja(null)));}
			| 	'{' sentencias_ejecutables '}'			{$$ = $2;}//ROMPO ACA. Valor previo $$=$3
			|		'{' sentencias_ejecutables error	 {agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " +((Token) $2.obj).nroLinea); $$ = $2;}
			;
sentencias_ejecutables 	:	ejecutable	{$$ = agregarNodo("lista_sentencias", $1, new ParserVal(new Hoja(null)));}
			|	 ejecutable sentencias_ejecutables{$$ = agregarNodo("lista_sentencias", $1, $2);} //ROMPO ACA. valor previo $$=$2
			;

condicion	:	expr '=' expr								{$$ = agregarNodo("=",$1,$3);}
		|	expr '<' expr											{$$ = agregarNodo("<",$1,$3);}
		|	expr '>' expr											{$$ = agregarNodo(">",$1,$3);}
		|	expr COMP_MENOR_IGUAL expr				{$$ = agregarNodo(((Token)$2.obj).claveTablaSimbolo,$1,$3);}
		|	expr COMP_MAYOR_IGUAL expr				{$$ = agregarNodo(((Token)$2.obj).claveTablaSimbolo,$1,$3);}
		|	expr COMP_DISTINTO expr						{$$ = agregarNodo(((Token)$2.obj).claveTablaSimbolo,$1,$3);}
		|	error {agregarError("Error: condicion no valida. Incorrecta mezcla de expresiones y comparador. Linea: " + ((Token) $1.obj).nroLinea);}
		;

expr 		: 	expr '+' term 			{$$ = agregarNodo("+",$1,$3);}
		| 	expr '-' term 					{$$ = agregarNodo("-",$1,$3);}
		|		casting
		| 	term
		;

casting :	USLINTEGER '('expr')' {agregarEstructuraDetectada("Conversion explicita"); $$ = agregarNodoRengo("casting",$3);}
				|	USLINTEGER '('expr error {agregarError("Error: falta ')' en la conversion explicita. Linea: " + ((Token)$3.obj).nroLinea); $$ = $3;}
				|	error '('expr')'	{agregarError("Error: tipo no valido para conversion. Linea: " + ((Token)$1.obj).nroLinea); $$ = $4;}
				;

term	 	: 	term '*' factor {$$ = agregarNodo("*",$1,$3);}
		| 	term '/' factor {$$ = agregarNodo("/",$1,$3);} //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
		| 	factor
		;

factor				:	 	ID								{ $$=agregarHoja(((Token)$1.obj).claveTablaSimbolo);}
							| 	CTE_INTEGER				{ $$=agregarHoja(((Token)$1.obj).claveTablaSimbolo);
																		 Atributos atts = tablaSimbolos.get(((Token)$1.obj).claveTablaSimbolo); //$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS
																		 int valorInteger = (Integer) atts.get("Valor"); //el valor en la posicion 1 es el número de la
																		 if (valorInteger > 32767) //si se pasa del limite positivo

																				if (!tablaSimbolos.containsKey("32767_i")){
																					Atributos nuevosAtts = new Atributos();
																					nuevosAtts.set("Token", "CTE_INTEGER");nuevosAtts.set("Valor", 32767);
																					tablaSimbolos.put("32767_i", nuevosAtts);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + ((Token)$1.obj).nroLinea);
																				}

																			}
							|	CTE_USLINTEGER		{ $$=agregarHoja(((Token)$1.obj).claveTablaSimbolo);}
							| '-' CTE_INTEGER		{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get(((Token)$2.obj).claveTablaSimbolo).get("Valor");
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			Atributos atts = new Atributos();
																			atts.set("Token", "CTE_INTEGER"); atts.set("Valor", new Integer(-valorInteger));
																			tablaSimbolos.put(nuevaClave, atts);
																			}
																		$$ = $2;
																		}
							|	'-' error {agregarError("Error: negacion no permitida a este operando. Linea: " + ((Token) $1.obj).nroLinea);}
							;

asignacion	:	ID ASIGN r_value_asignacion ',' {$$ = agregarNodo(":=",agregarHoja(((Token)$1.obj).claveTablaSimbolo),$3);}
						|	ID ASIGN r_value_asignacion		{agregarError("Error: falta ',' en asignacion. Linea: " + ((Token) $3.obj).nroLinea); $$ = $3;}
						|	ID ASIGN error ',' 	{agregarError("Error: r-value de la asignacion mal definido. Linea: " + ((Token) $3.obj).nroLinea); $$ = $4;}
						;
r_value_asignacion:	expr
									| id_invocacion	{agregarEstructuraDetectada("Invocacion de funcion en asignacion");}
									;


%%


Hashtable<String, Atributos> tablaSimbolos;
AnalizadorLexico AL = null;
List<String> estructurasGramaticalesDetectadas;
List<String> tokensLeidos;
List<String> erroresDetallados;
List<String> erroresGenerales;
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
