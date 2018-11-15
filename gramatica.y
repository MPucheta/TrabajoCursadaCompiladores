
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


programa 			: 	conjunto_sentencias {	this.raizArbolSintactico = engancharSentencias();
																				for (String ambito: arbolesDeFunciones.keySet()){
																					Arbol a = engancharSentencias(ambito); //se enganchan las sentencias de dentro de cada ambito
																					todosLosArboles.put(ambito, a); //se agrega el arbol a la lista de salida
																					}
																				}
							|		error conjunto_sentencias
							;


conjunto_sentencias	:	sentencia //{$$ = agregarNodo("lista_sentencias", $1, new ParserVal(new Hoja(null)));}
										|	sentencia conjunto_sentencias //{$$ = agregarNodo("lista_sentencias",$1,$2);}
										;


sentencia 	: 	declarativa
						| 	ejecutable {sentenciasEjecutables.add(0, (Arbol)$1.obj);
														if (arbolesDeFunciones.get(ambitoActual) == null)
															arbolesDeFunciones.put(ambitoActual, new ArrayList<Arbol>());
														arbolesDeFunciones.get(ambitoActual).add(0, (Arbol)$1.obj); }
						;


ejecutable 	: 	sentencia_if
						|		sentencia_while
						|		asignacion 	{agregarEstructuraDetectada("Asignacion");}
						|		sentencia_impresion
						|		invocacion
						;


invocacion	:	id_invocacion ',' {				Arbol arbol = (Arbol) $1.obj;
																				if (arbol.getValor() != null){ //si no es hoja error
																						Atributos atts = tablaSimbolos.get(arbol.getValor());
																						String declarada = "No";
																						String ambito = "*";
																						if (atts != null){
																								declarada = (String) atts.get("Declarada");
																								ambito = (String) atts.get("Ambito");
																								if (ambito == null)
																									ambito = "*";
																							}
																						if (declarada.equals("Si")){
																									agregarEstructuraDetectada("Invocacion funcion");
																									$$ = agregarNodoRengo("invocacion", $1);
																									String clave=((Arbol)$1.obj).getValor();
																									if(!tablaSimbolos.get(clave).get("Tipo").equals("fun")){
																											String aux="Error: '_" + clave + "' no es de tipo fun. Solo se permiten invocaciones de tipo fun. Linea: " + nroLinea($1);
																											agregarErrorChequeoSemantico(aux);
																											System.out.println(aux);
																									}
																									else{
																											cambiarTipo($$, "invocacion");
																											}
																									if (!ambito.equals("*"))
																										if (!ambitoActual.contains(ambito)){
																											agregarErrorChequeoSemantico("Error: variable '" + atts.get("Lexema") + "' utilizada fuera de su ambito. Linea: " + nroLinea($1));
																											System.out.println("Error: variable '" + atts.get("Lexema") + "' utilizada fuera de su ambito. Linea: " + nroLinea($1));
																										}

																					}else{
																						agregarErrorChequeoSemantico("Error: funcion '_" + "' no declarada. Linea: " + nroLinea($1));
																						$$ = hojaError();
																					}

																				}else
																					$$ = hojaError();//NO SE AGREGA ERROR PORQUE SI LLEGA ACA SIGNIFICA QUE YA VIENE UN ERROR DE ANTES EN ID_INVOCACION
																				setNroLinea($$, (Token) $2.obj);
																			}/*falta agregar algo para diferenciar que es una invocacion*/
						| id_invocacion error {agregarError("Error: falta ',' en invocacion ejecutable. Linea: " + nroLinea($1));}
						;


id_invocacion				:	ID '('')' {
																	$$ = agregarHoja(obtenerLexema($1));
																	cambiarTipo($$, "fun");
																	setNroLinea($$, (Token) $3.obj);
																}
										| ID '(' error	{agregarError("Error: falta ')' en invocacion o declaracion de closure/funcion. Linea: " + ((Token) $2.obj).nroLinea); $$ = hojaError();setNroLinea($$, (Token) $2.obj);}
										;


sentencia_impresion	:	PRINT  cadena_cararacteres_entre_parentesis ','	{agregarEstructuraDetectada("Impresion"); $$ = agregarNodoRengo("impresion",$2);} //se quiere agarrar el string de cadena entre parentesis
										|	PRINT  cadena_cararacteres_entre_parentesis  {agregarError("Error: falta ',' luego de sentencia de impresion. Linea: " + nroLinea($2));$$ = hojaError(); setNroLinea($$, $2);}
										| PRINT error ','{agregarError("Error: sentencia de impresion erronea. Linea: " + ((Token) $1.obj).nroLinea);$$ = hojaError();setNroLinea($$, (Token) $3.obj);}
										;


cadena_cararacteres_entre_parentesis	:	'(' CADENA_CARACTERES ')' {$$ = agregarHoja(obtenerLexema($2));setNroLinea($$, (Token) $3.obj);} //$2 es un token, uso el metodo obtenerLexema para sacar lo que dice.
																			|	'(' CADENA_CARACTERES error {agregarError("Error: falta ')' luego de la cadena de caracteres. Linea: " + ((Token) $2.obj).nroLinea); setNroLinea($$, (Token) $2.obj);}
																			|	 error CADENA_CARACTERES ')' {agregarError("Error: falta '(' antes de la cadena de caracteres. Linea: " + ((Token) $2.obj).nroLinea); setNroLinea($$, (Token) $3.obj);}
																			| '(' error ')' {agregarError("Error: solo se pueden imprimir cadenas de caracteres. Linea: " + ((Token) $2.obj).nroLinea);setNroLinea($$, (Token) $3.obj);}
																			;


sentencia_if	:	IF condicion_entre_parentesis bloque_sentencias END_IF {$$ = agregarNodo("if",$2,agregarNodoRengo("cuerpo",agregarNodoRengo("then",$3)));setNroLinea($$, (Token) $4.obj);}
							| IF condicion_entre_parentesis bloque_sentencias ELSE bloque_sentencias END_IF {$$ = agregarNodo("if_else",$2,agregarNodo("cuerpo",agregarNodoRengo("then",$3),agregarNodoRengo("else",$5)));setNroLinea($$, (Token) $6.obj);} //genero el nodo if y el nodo cuerpo generando las ramas en la misma linea. Esto puede ser confuso, cualquier cosa ver el diagrama de arbol con checkpoints de la catedra de gen de codigo
							|	IF condicion_entre_parentesis bloque_sentencias error  {agregarError("Error: falta \"end_if\" de la sentencia IF. Linea: " + nroLinea($3));$$ = hojaError();setNroLinea($$, $3); }
							;


sentencia_while	:	WHILE condicion_entre_parentesis bloque_sentencias {$$ = agregarNodo("while",$2,$3); }
								;

condicion_entre_parentesis	:	'(' condicion ')'	{agregarEstructuraDetectada("Condicion"); $$ = agregarNodoRengo("condicion",$2); setNroLinea($$, (Token) $3.obj);}
														| 	error condicion ')' {agregarError("Error: falta '(' antes de la condicion. Linea: " + ((Token) $1.obj).nroLinea);$$ = hojaError();setNroLinea($$, (Token) $3.obj);}
														|	'(' condicion {//esta solucion no es muy agradable, pero usar '(' condicion error puede ocasionar
														 								//que se coman tokens de mas e incluso no informar el errores
																						agregarError("Error: falta ')' luego de la condicion. Linea: " + nroLinea($2));$$ = hojaError();setNroLinea($$, $2);}
														;


declarativa 	: 	declaracion_variables
							|		declaracion_closure	{declararFuncionesPendientes("main","fun");}
							|		declaracion_funcion_simple
							;


declaracion_variables :	tipo_variable lista_variables ','	{ declararVariables($1,"");
																														agregarEstructuraDetectada("Declaracion variable/s"); setNroLinea($$, (Token) $3.obj);}
											|	tipo_closure	lista_variables ',' { declararVariables($1,"variable");
																														setNroLinea($$, (Token) $3.obj);}

											|	 lista_variables ',' error  {agregarError("Error: declaracion de tipo erronea. Linea: " + ((Token) $2.obj).nroLinea);$$ = hojaError(); setNroLinea($$, (Token) $2.obj);}
											| tipo_variable error {agregarError("Error: falta ID o ',' en la declaracion de variable/s. Linea: " + nroLinea($1));$$ = hojaError();}
											| tipo_closure error {agregarError("Error: definicion de closure erronea. Linea: " + nroLinea($1));$$ = hojaError();}
											;


tipo_variable	: 	INTEGER 		{$$ = new ParserVal("integer"); setNroLinea($$, (Token) $1.obj);}
							| 	USLINTEGER	{$$ = new ParserVal("uslinteger"); setNroLinea($$, (Token) $1.obj);}
							;


tipo_closure	:	FUN	 {//lo hago aca para que tome la primer linea incluso en funcion closure
													$$ = new ParserVal("fun");
													agregarEstructuraDetectada("Declaracion de tipo closure");
												setNroLinea($$, (Token) $1.obj);}
							;


declaracion_closure			: 	tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' '}' {
	//cambiarAmbitoVariablesInternas(((Arbol)$2.obj).getValor());
																																																												declararFuncionesPendientes(((Arbol)$2.obj).getValor(),"void");
																																																												funcionesADeclarar.add(((Arbol)$2.obj).getValor());
																																																												cambiarTipo($$,"closure");
																																																												eliminarUltimoAmbito();
																																																												setNroLinea($$, (Token) $10.obj);
																																																												}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' ',' error {agregarError("Error: falta '}' de cierre de la declaracion de closure. Linea: " + ((Token) $9.obj).nroLinea); $$ = hojaError();setNroLinea($$, (Token) $9.obj);eliminarUltimoAmbito();}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure error	{agregarError("Error: falta ')' luego del retorno del closure. Linea: " + nroLinea($7)); $$ = hojaError();setNroLinea($$, $7);eliminarUltimoAmbito();}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN '('  retorno_closure  ')' error 	{agregarError("Error: falta ',' luego del retorno del closure. Linea: " + ((Token) $8.obj).nroLinea); $$ = hojaError();setNroLinea($$, (Token) $8.obj);eliminarUltimoAmbito();}
												|		tipo_closure id_invocacion '{' conjunto_sentencias RETURN  error 	{agregarError("Error: retorno no es de tipo closure. Se espera \"return( ID() )\" o \"return( {SENTENCIAS} )\". Linea: " + ((Token) $5.obj).nroLinea); $$ = hojaError();setNroLinea($$, (Token) $5.obj);eliminarUltimoAmbito();}
												;


declaracion_funcion_simple	:	VOID id_invocacion '{' conjunto_sentencias  '}'	{
																																								agregarEstructuraDetectada("Declaracion de funcion simple");
																																							//cambiarAmbitoVariablesInternas(((Arbol)$2.obj).getValor());
																																								cambiarTipo($$,"void");
																																							  funcionesADeclarar.add(((Arbol)$2.obj).getValor());
																																								eliminarUltimoAmbito();
																																								setNroLinea($$, (Token) $5.obj);
													}

														|	VOID id_invocacion '{' conjunto_sentencias  error {agregarError("Error: falta '}' de cierre de la funcion. Linea: " + nroLinea($4)); $$ = hojaError();setNroLinea($$, $4);}
														;


retorno_closure	: 	id_invocacion	{
											String clave=((Arbol)$1.obj).getValor(); //esto es porque dependiendo del parsing puede que sea null.

											if(clave!=null&&tablaSimbolos.get(clave).get("Declarada").equals("Si")&&!tablaSimbolos.get(clave).get("Tipo").equals("void")){
												//lo anterior es un poco guaso, pero basicamente tengo que devolver funciones que ya
												//fueron declaradas. Si dejo solo la segunda parte el parsing ascendente lo rompe
												//por alguna razon no estaba declarada, por ahi se iba por la regla de la definicion

												String aux="Error: El retorno de closure debe ser tipo void. En Linea " + nroLinea($1) ;
												agregarErrorChequeoSemantico(aux);
												System.out.println(aux);
											}

								}
								| 	'{'  conjunto_sentencias '}' {setNroLinea($$, (Token) $3.obj); 	eliminarUltimoAmbito();}
								;

lista_variables		:	ID 											{variablesADeclarar.add(obtenerLexema($1));}
									|	lista_variables ';' ID { variablesADeclarar.add(obtenerLexema($3)); setNroLinea($$, (Token) $3.obj);}
									;


bloque_sentencias 	:	ejecutable {$$ = agregarNodo("lista_sentencias", $1, new ParserVal(new Hoja(null))); setNroLinea($$,$1);}
										| 	'{' sentencias_ejecutables '}'			{$$ = $2; setNroLinea($$,(Token) $3.obj);}
										|		'{' sentencias_ejecutables error	 {agregarError("Error: falta '}' de cierre de bloque de sentencias. Linea: " +nroLinea($2)); $$ = $2;}
										;


sentencias_ejecutables 	:	ejecutable	{$$ = agregarNodo("lista_sentencias", $1, new ParserVal(new Hoja(null)));setNroLinea($$, $1);}
												|	 ejecutable sentencias_ejecutables{$$ = agregarNodo("lista_sentencias", $1, $2); setNroLinea($$, $2);}
												;


condicion	:	expr '=' expr											{if (verificarTipos($1, $3, "condicion '='"))
																									$$ = agregarNodo("=",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																								setNroLinea($$, $3);}

					|	expr '<' expr											{if (verificarTipos($1, $3, "condicion '<'"))
																											$$ = agregarNodo("<",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																										setNroLinea($$, $3);}

					|	expr '>' expr											{if (verificarTipos($1, $3, "condicion '>'"))
																											$$ = agregarNodo(">",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																									setNroLinea($$, $3);	}

					|	expr COMP_MENOR_IGUAL expr				{if (verificarTipos($1, $3, "condicion '<='"))
																											$$ = agregarNodo("<=",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																									setNroLinea($$, $3);	}

					|	expr COMP_MAYOR_IGUAL expr				{if (verificarTipos($1, $3, "condicion '=>'"))
																											$$ = agregarNodo(">=",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																										setNroLinea($$, $3);}

					|	expr COMP_DISTINTO expr						{if (verificarTipos($1, $3, "condicion '!='"))
																											$$ = agregarNodo("!=",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																									setNroLinea($$, $3);	}

					|	error {agregarError("Error: condicion no valida. Incorrecta mezcla de expresiones y comparador. Linea: " + ((Token) $1.obj).nroLinea);$$ = hojaError(); setNroLinea($$, (Token)$1.obj);}
					;


expr 		: 	expr '+' term	{
															if (verificarTipos($1, $3, "operacion '+'")){
																	$$ = agregarNodo("+",$1,$3);
																	cambiarTipo($$, $1.sval);
																}
																else
																	$$ = hojaError();
																setNroLinea($$, $3);
													}

				| 	expr '-' term 		{
																if (verificarTipos($1, $3, "operacion '/'")){
																		$$ = agregarNodo("-",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘-‘ ; T.ptr ; F.ptr )
																		cambiarTipo($$, $1.sval);
																	}
																	else
																		$$ = hojaError();
																	setNroLinea($$, $3);
																}

				| 	term              	{setNroLinea($$, $1);}
				;


casting :	USLINTEGER '('expr')' {agregarEstructuraDetectada("Conversion explicita");
																if ($3.sval.equals("integer")){
																	$$ = agregarNodoRengo("casting",$3);
																	cambiarTipo($$, "uslinteger");
																}
																else {
																	agregarErrorChequeoSemantico("Error: no se puede hacer la conversion de " + $3.ival + " a uslinteger. Linea: " + nroLinea($3));
																	$$ = hojaError();
																}
																setNroLinea($$, (Token) $4.obj);
																}

				|	USLINTEGER '('expr error {agregarError("Error: falta ')' en la conversion explicita. Linea: " + nroLinea($3)); $$ = hojaError();setNroLinea($$, $3);}

				|	error '('expr')'	{agregarError("Error: tipo no valido para conversion. Linea: " + ((Token)$1.obj).nroLinea); $$ = hojaError();setNroLinea($$, (Token) $4.obj);}

				;


term	 	: 	term '*' factor {
																	if (verificarTipos($1, $3, "operacion '*'")){
																			$$ = agregarNodo("*",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘*‘ ; T.ptr ; F.ptr )
																			cambiarTipo($$, $1.sval);
																		}
																		else
																			$$ = hojaError();
																		setNroLinea($$, $3);
														}
				| 	term '/' factor {
																if (verificarTipos($1, $3, "operacion '/'")){
																		$$ = agregarNodo("/",$1,$3); //es lo denominado  T.ptr = crear_nodo( ‘/‘ ; T.ptr ; F.ptr )
																		cambiarTipo($$, $1.sval);
																	}
																	else
																	 $$ = hojaError();
																	setNroLinea($$, $3);
														}
				| 	factor
				;


factor				:	 	ID								{ $$=agregarHoja(((Token)$1.obj).claveTablaSimbolo);
																			if (verificarDeclaracion($1)){
																					cambiarTipo($$, (String)tablaSimbolos.get(obtenerLexema($1)).get("Tipo"));
																					verificarAmbito($1, ambitoActual);
																				}
																			else
																				$$ = hojaError();
																			setNroLinea($$, (Token) $1.obj);
																				}
							| 	CTE_INTEGER				{ $$=agregarHoja(((Token)$1.obj).claveTablaSimbolo);
																			cambiarTipo($$, "integer");
																		 Atributos atts = tablaSimbolos.get(((Token)$1.obj).claveTablaSimbolo); //$1 es de tipo ParserVal, agarro su valor de string para buscar en la TS
																		 int valorInteger = (Integer) atts.get("Valor"); //el valor en la posicion 1 es el número de la
																		 if (valorInteger > 32767) //si se pasa del limite positivo

																				if (!tablaSimbolos.containsKey("32767_i")){
																					Atributos nuevosAtts = new Atributos();
																					nuevosAtts.set("Token", "CTE_INTEGER");nuevosAtts.set("Valor", 32767);
																					tablaSimbolos.put("32767_i", nuevosAtts);
																					agregarError("Warning: constante integer fuera de rango. Reemplazo en linea: " + ((Token)$1.obj).nroLinea);
																				}
																				setNroLinea($$, (Token) $1.obj);
																			}
							|	CTE_USLINTEGER		{ $$=agregarHoja(((Token)$1.obj).claveTablaSimbolo);
																		cambiarTipo($$, "uslinteger");
																	setNroLinea($$, (Token) $1.obj);}
							| '-' CTE_INTEGER		{	agregarEstructuraDetectada("Negacion de operando");
																		int valorInteger = (Integer) tablaSimbolos.get(((Token)$2.obj).claveTablaSimbolo).get("Valor");
																		String nuevaClave = "-" + valorInteger + "_i";
																		if (!tablaSimbolos.containsKey(nuevaClave)){
																			Atributos atts = new Atributos();
																			atts.set("Token", "CTE_INTEGER"); atts.set("Valor", new Integer(-valorInteger));
																			atts.set("Tipo","integer");
																			tablaSimbolos.put(nuevaClave, atts);
																			}

																		$$ =agregarNodoRengo("-",agregarHoja(((Token)$2.obj).claveTablaSimbolo)); //agrego dos nodos de una, un - unario y una hoja con el valor en si
																		cambiarTipo($$, "integer");
																		setNroLinea($$, (Token) $2.obj);
																		}
							|	'-' error {agregarError("Error: negacion no permitida a este operando. Linea: " + ((Token) $1.obj).nroLinea);$$ = hojaError();setNroLinea($$, (Token) $1.obj);}
							|		casting
							;


asignacion	:	ID ASIGN r_value_asignacion ',' {

																							if (verificarDeclaracion($1)&&verificarAccesibilidadPorAmbito($1)){  //se fija si la variable del lado izquierdo esta declarada
																									cambiarTipo($1, (String)tablaSimbolos.get(obtenerLexema($1)).get("Tipo")); //se le setea el tipo
																									verificarTipos($1, $3, "asignacion"); 																		//se verifica que los tipos de los dos lados sean iguales
																									$$ = agregarNodo(":=",agregarHoja(((Token)$1.obj).claveTablaSimbolo),$3);
																							}
																							else
																								$$ = hojaError();
																							setNroLinea($$, (Token) $4.obj);
																						}

						|	ID ASIGN r_value_asignacion		{agregarError("Error: falta ',' en asignacion. Linea: " + nroLinea($3)); $$ = hojaError();setNroLinea($$, $3);}

						|	ID ASIGN error ',' 	{agregarError("Error: r-value de la asignacion mal definido. Linea: " + ((Token) $3.obj).nroLinea); $$ = hojaError();setNroLinea($$, (Token) $4.obj);}
						;


r_value_asignacion:	expr
									| id_invocacion	{agregarEstructuraDetectada("Invocacion de funcion en asignacion"); $$ = agregarNodoRengo("invocacion", $1); setNroLinea($$, $1); cambiarTipo($$, "fun");}
									;


%%


Hashtable<String, Atributos> tablaSimbolos;
AnalizadorLexico AL = null;
List<String> estructurasGramaticalesDetectadas;
List<String> tokensLeidos;
List<String> erroresDetallados;
List<String> erroresGenerales;
List<String> variablesADeclarar;
String ambitoActual;
List<String> funcionesADeclarar;
List<Arbol> sentenciasEjecutables;
Hashtable<String, List<Arbol>> arbolesDeFunciones; // va a tener todas las sentencias sueltas de cada funcion
Hashtable<String, Arbol> todosLosArboles; // va a tener los arboles sintacticos de cada funcion, con las sentencias ya enganchadas
Token t;
int ultimoTokenLeido;
Arbol raizArbolSintactico;
boolean nuevaPosibleFuncion;
boolean posibleFuncion;
boolean posibleFuncionSinNombre;
String ultimoAmbitoPosible;
boolean errorSintaxis;
List<String> erroresChequeoSemantico;
String ultimaFunc;
int yylex(){
	t = AL.getToken();
	yylval = new ParserVal(t);
	ultimoTokenLeido= t.tipoDeToken;
	//la siguiente condicion se debe hacer porque estas estructuras (ej, if, while) ocupan varias lineas de texto
	//por lo que cuando el parsing detecta finalmente que un if termina en un end_if, el AL.nroLinea ya avanzo.
	//Por lo tanto sin esto el nroLinea mostrado seria el del fin de la estructura y no del comienzo
	if(posibleFuncion && ultimoTokenLeido==AL.ASCIIToken('(')){
		agregarAmbito(ultimoAmbitoPosible);
		posibleFuncionSinNombre = false;
		posibleFuncion=false;
 	}else
		posibleFuncion = false;
 if (posibleFuncionSinNombre && ultimoTokenLeido==AL.ASCIIToken('{')){
		String[] ambitos = ambitoActual.split("@");
		String nombreFuncion = "FUNCRETORNO_" + ambitos[ambitos.length - 1]; //aca se puede cambiar el nombre de del retorno

		Atributos atts = new Atributos();
		atts.set("Declarada", "Si");
		atts.set("Tipo", "void");
		atts.set("Ambito", ambitoActual);
		atts.set("Uso", "funcion");
		atts.set("Lexema", nombreFuncion);
		atts.set("Retorno", " ");
		//atts.set("Token", "ID");
		tablaSimbolos.put(nombreFuncion, atts);
		ultimaFunc=nombreFuncion;
		ambitoActual = ambitoActual + "@" + nombreFuncion;
		posibleFuncionSinNombre = false;
	}
	else
		posibleFuncionSinNombre = false;

	if(nuevaPosibleFuncion && ultimoTokenLeido==Token.ID){
				posibleFuncion=true;
				nuevaPosibleFuncion=false;
				ultimoAmbitoPosible=t.claveTablaSimbolo; //t refiere al token nuevo, ultimoTokenLeido refiere al numero TIPO DE TOKEN
	}else
		if(nuevaPosibleFuncion && ultimoTokenLeido==AL.ASCIIToken('(')){
					posibleFuncionSinNombre=true;
					nuevaPosibleFuncion=false;
		}else
			nuevaPosibleFuncion=false;

	switch(ultimoTokenLeido){
		case(Token.IF):
				agregarEstructuraDetectada("Sentencia " + Token.tipoToken(ultimoTokenLeido)); break;
		case(Token.WHILE):
				agregarEstructuraDetectada("Sentencia " + Token.tipoToken(ultimoTokenLeido));break;
		case(Token.FUN):
				nuevaPosibleFuncion=true;break;
		case(Token.VOID):
				nuevaPosibleFuncion=true;break;
		case(Token.RETURN):
				nuevaPosibleFuncion=true;break;
		default:
				break;

	}



	String leido= "Linea: " + t.nroLinea + ". Token leido: '" + ultimoTokenLeido + "' reconocido como: " + Token.tipoToken(ultimoTokenLeido) + "\n";
	tokensLeidos.add(leido);
	return ultimoTokenLeido;

}

void yyerror(String s)
{
	if(errorSintaxis==false){
		errorSintaxis=!errorSintaxis;
	}
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
	variablesADeclarar = new ArrayList<>();
	ambitoActual="main";
  funcionesADeclarar = new ArrayList<>();
	sentenciasEjecutables = new ArrayList<>();

	arbolesDeFunciones = new Hashtable<>();
	arbolesDeFunciones.put(ambitoActual, new ArrayList<Arbol>());

	todosLosArboles = new Hashtable<String, Arbol>();

	nuevaPosibleFuncion=false;
	posibleFuncion=false;
	posibleFuncionSinNombre = false;
	ultimoAmbitoPosible="main";

	errorSintaxis=false;
	erroresChequeoSemantico=new ArrayList<>();
	ultimaFunc="";
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

public List<String> getErroresChequeoSemantico(){
	return this.erroresChequeoSemantico;

}
/*
public Arbol getArbolSintactico(){
	//en presencia de un error sintactico puedo setear un boolean o algo que haga que la raiz sea null y no generar codigo
	if(errorSintaxis){
		return new Hoja("null");
	}
	return this.raizArbolSintactico;

}*/

public Hashtable<String, Arbol> getArbolesSintacticos(){
	if (errorSintaxis){
		return new Hashtable<String, Arbol>();
	}
	return todosLosArboles;
}
private void agregarError(String e){
	errorSintaxis = true;
	erroresDetallados.add(e+"\n");
}

private void agregarErrorChequeoSemantico(String e){

	erroresChequeoSemantico.add(e+"\n");
}
private void agregarEstructuraDetectada(String tipo){
	String toAdd=tipo + " en linea " + AL.nroLinea + "\n";
	this.estructurasGramaticalesDetectadas.add(toAdd);

}


private ParserVal agregarNodoRengo(String value, ParserVal primero){
  if(errorSintaxis){
		return new ParserVal(new Hoja(null));
	}
	return new ParserVal(new NodoRengo(value,(Arbol)primero.obj));

}
private ParserVal agregarNodo(String value, ParserVal izquierdo, ParserVal derecho){
	if(errorSintaxis){
		return new ParserVal(new Hoja(null));
	}
	return new ParserVal(new Nodo(value,(Arbol)izquierdo.obj,(Arbol)derecho.obj));

}

private ParserVal agregarHoja(String value){
	if(errorSintaxis){
		return new ParserVal(new Hoja(null));
	}
	return new ParserVal(new Hoja(value));

}

private String obtenerLexema(ParserVal pv){
	return ((Token)pv.obj).claveTablaSimbolo;

}

private void cambiarTipo(ParserVal pv,String tipo){
	//metodo pensado para cambiar el tipo en vez de usar $$.sval=tipo (por ejemplo 'integer'). Ya que sería muy oscuro
	pv.sval=tipo;

}

private boolean verificarTipos(ParserVal p1, ParserVal p2, String tipoSentencia){
 //verdadero si son iguales, falso si son distintos
	if (p1.sval != p2.sval){
		String aux="Error de tipos en " + tipoSentencia + ": no se puede realizar entre "
												+ p1.sval+ " y " + p2.sval;// + ". En linea " + nroLinea(p1);
		agregarErrorChequeoSemantico(aux);
		System.out.println(aux); /*cambiar*/
	}else
		return true;
	return false;
}

private boolean verificarDeclaracion(ParserVal p1){
 //retorna verdadero si esta declarada
 	String claveTS = obtenerLexema(p1);
	if (tablaSimbolos.get(claveTS).get("Declarada").equals("No")){ // se busca en la TS el atributo "Declarada" y se fija si tiene valor "No"
		String aux="Error: variable '_" + claveTS +  "' no declarada. ";// + nroLinea(p1);
		agregarErrorChequeoSemantico(aux);
		System.out.println(aux); /*cambiar*/
	}else{

		return true;
	}

	return false;
}

private boolean verificarAccesibilidadPorAmbito(ParserVal p1){
	String claveTS = obtenerLexema(p1);
	String ambitoVariable=(String)tablaSimbolos.get(claveTS).get("Ambito");

	//System.out.println("AMBITO " + ambitoVariable +" var " + claveTS + " ambito actual " + ambitoActual);
	if(ambitoActual.toLowerCase().contains(ambitoVariable.toLowerCase())){
		//podria retornar de una esto pero bue. por ejemplo si la variable esta en main@f1 puede usar variables de main.
		//ya que "main@f1" contiene a "main" (hablando de strings)
		//ahora bien si la variable esta en el ambito "main@f1@g". "main" no puede usar esa variable
		//ya que "main" no contiene a "main@f1@g". Es justamente al revez, "main" es subtring de "main@f1@g"

		return true;
	}else{
		String error="Error: variable/funcion '_" + obtenerLexema(p1) + "' no accesible en el ambito " + ambitoActual + ". Linea: " + ((Token)p1.obj).nroLinea;
		agregarErrorChequeoSemantico(error);
		System.out.println(error);
	}
	return false;
}
private void declararVariables(ParserVal tipo, String uso){
	for (String variable: variablesADeclarar){
		if (tablaSimbolos.get(variable).get("Declarada").equals("Si")){
			String aux="Error: redeclaracion de variable '_" + variable + "'. En linea " + nroLinea(tipo);
			agregarErrorChequeoSemantico(aux);
			System.out.println(aux); //ERROR CHEQUEO SEMANTICO
		}else{
			tablaSimbolos.get(variable).set("Declarada", "Si");
			tablaSimbolos.get(variable).set("Tipo", tipo.sval);
			tablaSimbolos.get(variable).set("Ambito", ambitoActual);
			if(!uso.equals("")){
				tablaSimbolos.get(variable).set("Uso", uso); //el uso solo es distinto en el caso de los closure
			}

		}
	}

	variablesADeclarar.clear(); //ya se declararon las variables, se vacia la lista de variables a declarar

}

private Arbol engancharSentencias(){
	Arbol salida = new Hoja(null);
	for (Arbol a: sentenciasEjecutables)
		salida = new Nodo("lista_sentencias", a, salida);
	return salida;
}

private Arbol engancharSentencias(String ambito){
	Arbol salida = new Hoja(null);
	for (Arbol a: arbolesDeFunciones.get(ambito))
		salida = new Nodo("lista_sentencias", a, salida);
	return salida;
}

/*
private void cambiarAmbitoVariablesInternas(String ambito){
		for (String variable: variablesInternasFunciones){
				tablaSimbolos.get(variable).set("Ambito",ambito);
		}

		variablesInternasFunciones.clear();

}
*/
private void declararFuncionesPendientes(String ambito,String tipo){ /*REVISAR*/
	if(funcionesADeclarar.size()!=1){/*
		String aux="El numero de funciones debe ser exactamente igual a uno (1). En ambito " + ambito;
		agregarErrorChequeoSemantico(aux);
		System.out.println(aux); //ERROR SEMANTICO*/

	}else{
		String func=funcionesADeclarar.get(0);

	  if(func!=null){
			if (tablaSimbolos.get(func).get("Declarada").equals("Si")){ //ver si agrego de preguntar sobre los ambitos
				String aux="Error: nombre previamente usado '" + func + "'.";
				agregarErrorChequeoSemantico(aux);
				System.out.println(aux); //ERROR CHEQUEO SEMANTICO
			}
			else{
				tablaSimbolos.get(func).set("Declarada", "Si");
				tablaSimbolos.get(func).set("Tipo", tipo);
				String retorno=" ";

				if(tablaSimbolos.get(func).get("Tipo").equals("void")){
					ultimaFunc=func;
				}
				if(tablaSimbolos.get(func).get("Tipo").equals("fun")){
						//si llege hasta aca es que toy tratando una funcion. No es necesario chequear el atributo en la TS.
						retorno=ultimaFunc;
						ultimaFunc="";
				}
				tablaSimbolos.get(func).set("Retorno",retorno);
				}

				String[] partes=ambitoActual.split("@");

				String aux="";
				for(int i=0;i<partes.length;i++){
					aux+=partes[i];

					if(partes[i].equals(ambito)){
						break;
					}
					aux+="@";
				}
				tablaSimbolos.get(func).set("Ambito",aux); //obtengo los ambitos hasta lo que me interesa
				tablaSimbolos.get(func).set("Uso", "funcion"); //el uso solo es distinto en el caso de los closure

			}

			funcionesADeclarar.clear();

		}


}

private void eliminarUltimoAmbito(){
	String[] partes=ambitoActual.split("@");

	if(partes.length!=0){
		ambitoActual=partes[0];
		for(int i=1; i<partes.length-1;i++){
			ambitoActual=ambitoActual+"@"+partes[i];

		}
	}
	else
		ambitoActual="main";


}

private void agregarAmbito(String ambito){
	ambitoActual=ambitoActual+"@"+ambito;
	if (arbolesDeFunciones.get(ambitoActual) == null)
		arbolesDeFunciones.put(ambitoActual, new ArrayList<Arbol>());

}

private void setNroLinea(ParserVal p1, ParserVal p2){
	p1.ival = p2.ival;

}

private void setNroLinea(ParserVal p1, int val){
	p1.ival = val;

}

private void setNroLinea(ParserVal p1, Token t){
	p1.ival = t.nroLinea;

}

private int nroLinea(ParserVal pv){
	return pv.ival;
}

private ParserVal hojaError(){
	return new ParserVal(new Hoja(null));

}

private void verificarAmbito(ParserVal var, String ambito){
	if (!ambito.contains(((String)tablaSimbolos.get(((Token)var.obj).claveTablaSimbolo).get("Ambito"))) && !ambito.equals(((String)tablaSimbolos.get(((Token)var.obj).claveTablaSimbolo).get("Ambito")))){
		String s = "Error: variable '_" + ((Token)var.obj).claveTablaSimbolo + "' no se puede utilizar en el ambito " + ambito + ", ya que pertenece al ambito " + tablaSimbolos.get(((Token)var.obj).claveTablaSimbolo).get("Ambito") +". Linea: " + ((Token)var.obj).nroLinea;
		agregarErrorChequeoSemantico(s);
		System.out.println(s);
	}
}
