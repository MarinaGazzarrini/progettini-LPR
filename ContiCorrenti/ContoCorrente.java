import java.util.*;
import java.io.Serializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//ContoCorrente: nomeCorrentista, listaMovimenti
public class ContoCorrente implements Serializable {
	

	private static final long serialVersionUID = 1L;//default
	private final Correntisti NomeCorrentista;//nome del correntista
	private final  LinkedList<Movimento> movimenti;//lista di movimenti associati al correntista
	
	public ContoCorrente(Correntisti name) {//costruttore
		NomeCorrentista=name;
		movimenti=new LinkedList<Movimento>();
	}

	//inserire un nuovo movimento
	void nuovoMovimento(Movimento movimento) {
		movimenti.add(movimento);
	}
	
	//restituisce una lista con tutte le causali(senza data associata) di quel conto corrente
	public LinkedList<Causale> restituisciCausale() {
		LinkedList<Causale> temp= new LinkedList<Causale>();
		for(Movimento m: movimenti) {
			temp.add(m.restituisciCausaleuno());//linked list con le causali(senza data)
			}
		return temp;//restituisco lista causali
		
	}
	
	//stamppa i dati relativi al conto corrente
	public void stampaConto() {
		String tmp="";
		tmp=tmp + "Correntista"+": "+ NomeCorrentista+"  ";
		for(Movimento m: movimenti) {
			tmp= tmp+ "[" + m.restituisciCausaleuno() + " "+ m.getDay()+"/"+m.getMonth()+"/"+m.getYear()+"]" + ";";
			}
		System.out.println(tmp);
	}
	
	
	//da java a json
	@SuppressWarnings("unchecked")
	public JSONObject joC() {
		JSONObject oj= new JSONObject();//nuovo ogg json
		oj.put("NomeC", NomeCorrentista.toString()); //chiave:Nome ;valore:NomeCorrentista(stringa)
		JSONArray Moviments= new JSONArray();//array di oggetti json
		for(Movimento m: movimenti) {//inserisco tutti i movimenti
			JSONObject obj= new JSONObject();//creo un oggetto jason
			obj.put("Giorno",m.getDay());//coppie <chiave,valore>
			obj.put("Mese",m.getMonth());
			obj.put("Anno",m.getYear());
			obj.put("Causale",m.restituisciCausaleuno().toString());
			Moviments.add(obj);//inserisco i movimenti sotto forma di oggetti json
		}
		oj.put("movimenti", Moviments);
		return oj;//mi restituisce un oggetto json corrispondente ad un conto corrente
	}
	
	//da json a java
	public static ContoCorrente jaC(JSONObject joC) {
		JSONArray Moviments= (JSONArray)joC.get("movimenti");
		String NameC= (String) joC.get("NomeC");
		ContoCorrente cc=new ContoCorrente((Correntisti)Correntisti.valueOf(NameC));//uso valueOf perchè non voglio una stringa, ma un NomeCorrentisti
		int dim= Moviments.size();
		for(int i=0; i<dim; i++) {//aggiungo i movimenti  
			JSONObject job=(JSONObject)Moviments.get(i);
			String st=(String)job.get("Causale");
			Causale ca= Causale.valueOf(st);//passo da stringa a causale
			long g=(long)job.get("Giorno");
			long m=(long)job.get("Mese");
			long a=(long)job.get("Anno");
			Movimento mo= new Movimento((int)g,(int)m,(int)a,ca);
			cc.nuovoMovimento(mo);
		}
		return cc;
	}
	
	
}