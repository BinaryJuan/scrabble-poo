package modelo.scrabble;

import java.io.*;

public class Diccionario implements Serializable {

	private static final long serialVersionUID = 1L;
	private File palabras;
	
    //INTERFAZ
	
	private void porIndice(int primeraLetra) {
		
		if(primeraLetra >= 97 && primeraLetra <= 102) {
    		palabras = new File("src\\modelo\\diccionario\\A-F.txt");
    	}
    	else if(primeraLetra >= 103 && primeraLetra <= 109) {
    		palabras = new File("src\\modelo\\diccionario\\G-M.txt");
    	}
    	else if(primeraLetra >= 110 && primeraLetra <= 115) {
    		palabras = new File("src\\modelo\\diccionario\\N-S.txt");
    	}
    	else {
    		palabras = new File("src\\modelo\\diccionario\\T-Z.txt");
    	}
	}
    
    public boolean contieneA(String palabraBuscada) throws IOException{
    	
    	//Tomo la primera letra de la palabra a buscar
    	int primeraLetra = (int) palabraBuscada.toCharArray()[0];
    	
    	//La construyo por el indice
    	porIndice(primeraLetra);
  
    	//Busco la palabra
    	boolean esta = false;
    	BufferedReader diccionario = new BufferedReader(new FileReader(palabras));
    	String linea = diccionario.readLine();
    	if(linea.equals(palabraBuscada)) {
			esta = true;
		}
    	else {
    		while(linea != null && !esta) {
        		if(linea.equals(palabraBuscada)) {
        			esta = true;
        		}
        		linea = diccionario.readLine();
        	}
    	}
    	diccionario.close();
    	return esta;
    }

   
	
}
