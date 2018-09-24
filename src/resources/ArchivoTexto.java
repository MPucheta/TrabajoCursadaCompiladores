package resources;

import java.io.*;
import java.util.Iterator;
import java.util.List;



public class ArchivoTexto {
	//si se manda un string al constructor se asume que es un archivo de lectura
	//si no se manda nada se asume que el archivo es de salida
	BufferedReader br;
	private static final String windowsLineSeparator="\r\n"; //esto es mas que nada para documentar. Los strings debieran tener esto en vez de \n si quiero escribir varias lineas
	
	public ArchivoTexto(String nombreArchivo) throws IOException{
		/*File dir = new File(".");
		System.out.println("Cargando archivo '" + dir.getCanonicalPath() + File.separator + nombreArchivo +"'" );
		File fin = new File(dir.getCanonicalPath() + File.separator + nombreArchivo);
		*/
		
		File dir=new File(nombreArchivo);
		System.out.println("Cargando archivo "+ dir.getCanonicalPath() +"\n" );
		this.cargarArchivo(dir);
		
	
	}
	
	
	
	public static void escribirEnDisco(String dir,String contenido)throws IOException  {
		contenido=contenido.replace("\n", windowsLineSeparator);
		BufferedWriter out;
		
		out = new BufferedWriter(new PrintWriter(dir));
		out.write(contenido);
		out.close();
		
	
		
	}
	public static void escribirEnDisco(String dir,List<String> contenido) throws IOException {
		BufferedWriter out;
		out = new BufferedWriter(new PrintWriter(dir));
		for(String s: contenido) {
			s=s.replace("\n", windowsLineSeparator);
			out.write(s);
		}
		
		
		out.close();
	
		
	}
	public void cargarArchivo(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		br = new BufferedReader(new InputStreamReader(fis));
	}
	
	
	
	public String next() throws IOException{
		String line = null;
		line = br.readLine();
		
		if (line == null)
			br.close();
		return line;
	}
	
	
	
	public char nextChar() throws IOException{
		
		int charLeido=br.read(); //bufferedReader.read() devuelve un int, hay que castearlo.
		
		return (char) charLeido;
		
	}
	
	
	//espacio dedicado para hacer un iterator
	
	public Iterator<Character> getIterator(){
		
		
		return new IteradorPorChar();
		
	}
	
	

	private class IteradorPorChar implements Iterator<Character>{
		
		private int leido;
		public boolean hasNext() {
			try {
				br.mark(1);
				leido=br.read();
				
				br.reset();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return (leido!=-1);
		}

		@Override
		public Character next() {
			
			try {
				return new Character(nextChar());
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			
			return null;
		}
		
		
	}
	
	public String leerArchivo(){
		String salida = "";
		Iterator<Character> it= this.getIterator();
		while(it.hasNext()) {
				salida += it.next();
		}
		return salida;
	}
	
	
	
	
	public static void main(String[] args) {
		
		try {
		
			ArchivoTexto test= new ArchivoTexto("test.txt");
			/*
			Iterator<Character> it= test.getIterator();
			String aux="";
			while(it.hasNext()) {
				aux+=it.next();
					
				
				
				
			}*/
			String aux= "testing out ouput \n lets see if it works";
			
			ArchivoTexto.escribirEnDisco("test.txt",aux);
			
			System.out.print(aux);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
