package resources;

import java.util.Hashtable;

public class Atributos {
	Hashtable<String, Object> atts;
	
	public Atributos(){
		atts = new Hashtable<String, Object>();
	}
	
	public Object get(String att){
		return atts.get(att);
	}
	
	public void set(String att, Object valor){
		atts.put(att, valor);
	}

	
	public String toString(){
		String s = "";
		for (String a: atts.keySet())
			s = s + a + ": " + atts.get(a) + " | ";
		return s;
	}
}
