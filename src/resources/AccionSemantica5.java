package resources;


public class AccionSemantica5 implements AccionSemantica{
	//esta accion semantica se define como:
		/*
		 * >verificar si cte entra en rango definido para ui (enteros sin signo)
		 * 		si no, pisar con maximo valor e informar warning
		 * >verificar si existe en tabla simbolo
		 * 	Si: retornar <cte, puntTS>
		 *  No: dar de alta en TS y retornar <cte,punts>
		 * @see resources.AccionSemantica#ejecutar(resources.AnalizadorLexico, char)
		 */
		private double minValor = 0;
		private double maxValor = 4294967295.0;
		public Token ejecutar(AnalizadorLexico AL, char nuevoChar) {

			String cte=AL.buffer;
			double valorCte = (new Double(cte)).doubleValue();
			String claveTS=cte+"_ul";
			Token devuelto=null;

			if (valorCte< minValor || valorCte> maxValor ) { //chequeo de rangos
				valorCte=maxValor; //tecnica de reemplazo
				claveTS=valorCte+"_ul"; //esto es tricky, si reemplace el valor, debo crear una nueva clave, asi evito usar claves que nunca voy a acceder.
				AL.agregarError("Warning: constante uslinteger fuera de rango. Reemplazo en linea: " + AL.nroLinea);
			}

			if(!AL.TS.containsKey(claveTS)) {
				Atributos atts = new Atributos();
				atts.set("Token", "CTE_USLINTEGER");
				atts.set("Valor", valorCte);
				AL.altaEnTablaSimbolos(claveTS, atts);
			}

			devuelto= new Token(Token.CTE_USLINTEGER,claveTS, AL.nroLinea);
			//si no existia, se creo antes, si existia devuelvo el TIPO DE TOKEN y su clave de acceso a la tabla de simbolos
			//Se incrementa para que se pueda hacer otra entrada a la TablaSimbolos con otra clave

			return devuelto;





		}
}
