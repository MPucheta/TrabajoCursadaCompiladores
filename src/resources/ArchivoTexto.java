package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class ArchivoTexto {
	
	BufferedReader br;
	
	public ArchivoTexto(String nombreArchivo) throws IOException{
		/*File dir = new File(".");
		System.out.println("Cargando archivo '" + dir.getCanonicalPath() + File.separator + nombreArchivo +"'" );
		File fin = new File(dir.getCanonicalPath() + File.separator + nombreArchivo);
		*/
		
		File dir=new File(nombreArchivo);
		System.out.println("Cargando archivo "+ dir.getCanonicalPath()  );
		this.cargarArchivo(dir);
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
	public static void main(String[] args) {
		
		try {
		
			ArchivoTexto test= new ArchivoTexto("test.txt");
			
			Iterator<Character> it= test.getIterator();
			String aux="";
			while(it.hasNext()) {
				aux+=it.next();
					
				
				
				
			}
			
			System.out.print(aux);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
