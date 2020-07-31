import java.util.*;
import java.util.concurrent.*;

/*Faccio uso di un contatore locale utilizzando un arraylist e di un contatore globale
  thread safe in scrittura. Ogni indice corrisponde ad una precisa causale:
  0->bollettino, 1->bonifico, 2->accredito, 3->F24, 4->pagobancomat
  Nei contatori associo ogni causale alla corrispondente frequenza(nel conto corrente per 
  quanto riguarda il contatore locale e nell'insieme di tutti i conti correnti per quanto
  riguarda il contatore globale)*/

public class Contatore implements Runnable {
	
	private ContoCorrente conto;
	
	//contatore locale
	private ArrayList<CausaleFrequenza> contaLocale;
	
	//la classe si trasforma in thread-safe al momento dell’accesso
	//in scrittura (implementazione di un ArrayList sincronizzato)
	private CopyOnWriteArrayList<CausaleFrequenza> contaGlobale;//contatore globale
	
	public Contatore(ContoCorrente c,CopyOnWriteArrayList<CausaleFrequenza> con) {//costruttore
		this.conto= c;
		contaLocale= new ArrayList<CausaleFrequenza>();
		contaGlobale= con;
		
		//inizializzo gli arraylist che utilizzerò come contatori locali
			CausaleFrequenza i= new CausaleFrequenza(Causale.Bollettino,0);
			contaLocale.add(0,i);
			CausaleFrequenza j= new CausaleFrequenza(Causale.Bonifico,0);
			contaLocale.add(1,j);
			CausaleFrequenza k= new CausaleFrequenza(Causale.Accredito,0);
			contaLocale.add(2,k);
			CausaleFrequenza h= new CausaleFrequenza(Causale.F24,0);
			contaLocale.add(3,h);
			CausaleFrequenza x= new CausaleFrequenza(Causale.PagoBancomat,0);
			contaLocale.add(4,x);
	//inizialmente la frequenza sarà per tutti uguale a zero	
		
		
	}
	
	
	public void run() {
		LinkedList<Causale> cs= conto.restituisciCausale();//lista con le causali del conto
		
		//AGGIORNAMENTO LOCALE
		for(int i=0; i < conto.restituisciCausale().size();i++) {//scorro tutte le causali di quel conto
			if (cs.get(i).equals(Causale.Bollettino)) {          //e aumento la frequenza corrispondente
			  contaLocale.get(0).aumentaFrequenza();			 //alle causali trovate
		}else if (cs.get(i).equals(Causale.Bonifico)) {
			contaLocale.get(1).aumentaFrequenza();
		}else if(cs.get(i).equals(Causale.Accredito)) {
			contaLocale.get(2).aumentaFrequenza();	
		}else if(cs.get(i).equals(Causale.F24)) {
			contaLocale.get(3).aumentaFrequenza();
		}else{
		contaLocale.get(4).aumentaFrequenza();
		}
		}
		
			//AGGIORNAMENTO GLOBALE
			for(int i=0;i<5;i++) {
				contaGlobale.get(i).modificaFrequenza(contaGlobale.get(i).getFrequenza()+contaLocale.get(i).getFrequenza());
			}
		
	}
	
	
	

}
