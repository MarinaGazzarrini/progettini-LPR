import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

//3 giornate, 12 sessioni per ogni giornata e 5 possibili speaker per sessione
public class OrganizzazioneCongresso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//chiave della hashmap è il giorno. il valore è un arraylist in cui ogni indice corrisponde ad una sessione e contiene una linkedlist contenente i nomi degli speaker
	private static  HashMap<Integer,ArrayList<LinkedList<String>>> programma;
	private static ArrayList<LinkedList<String>> sessioni;
	LinkedList<String> speaker;
	
	public OrganizzazioneCongresso(){//costruttore
		
		programma= new HashMap<Integer,ArrayList<LinkedList<String>>> (3);
		
		for(int j=0;j<3;j++) {
			sessioni= new ArrayList<LinkedList<String>>(12);
			programma.put(j, sessioni);
			for(int i=0;i<12;i++) {
				speaker= new LinkedList<String>();
				speaker.add("nessuno");
				sessioni.add(i,speaker);
			}
			
		}
		
	}
	
	public int registrazione(String nome,int giorno,int sessione) {
	 
		ArrayList<LinkedList<String>> tmp=programma.get(giorno);//mi restituisce il valore associato alla chiave giorno
		LinkedList<String> t=tmp.get(sessione);//mi restituisce la linkedlist alla posizione sessione
		if(t.size()>4)return 1;//non ci sono posti disponibili
			else if(t.get(0).equals("nessuno")) {
				programma.get(giorno).get(sessione).remove(0);
				programma.get(giorno).get(sessione).add(0,nome);
				return 0;
					}else {
						programma.get(giorno).get(sessione).add(0,nome);
						return 0;
			}
		}
	
	public HashMap<Integer,ArrayList<LinkedList<String>>> getP(){//restituisce la hashmap
		return programma;
	}
	
	

}
