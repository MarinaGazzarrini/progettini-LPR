import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class gestore {
	
	final ReentrantLock Lock;//per accedere in modo esclusivo
	final Condition CondRosso;//attesa pazienti codice rosso
	final Condition[] CondGiallo;//attesa pazienti codice giallo
	final Condition CondBianco;//attesa pazienti codice bianco
	int Red;//mi dice se un paziente con codice rosso sta facendo la visita(può essere 0 o 1)
	int visiteBianco;//mi dice quanti pazienti con codice bianco vengono visitati
	int visiteGiallo; //mi dice quanti pazienti col codice giallo vengono visitati
	paziente medici[];//mi dice lo "stato" dei medici(se stanno visitando o meno qualcuno e chi)
	
	
	public gestore() {//costruttore
		Lock=new ReentrantLock();
		CondRosso=Lock.newCondition();
		CondBianco=Lock.newCondition();
		CondGiallo= new Condition[10]; //ho 10 medici a disposizione
			for(int j=0; j<10; j++) {
				CondGiallo[j]= Lock.newCondition();
			}
		Red=0;//all'inizio non ho pazienti in codice rosso
		visiteBianco=0;//nessun paziente con codice bianco
		visiteGiallo=0;//nessun paziente con codice giallo
		medici=new paziente[10];
			for(int i=0; i<10; i++) {//tutti i dottori sono disponibili
				medici[i]=null;
			}
	}
	
	

	//mi restituisce il numero di medici disponibili
	int mediciDisponibili(paziente a[]) {
		int liberi=10;//tutti i medici sono liberi
		for(int i=0;i<10;i++) {
			if (a[i]!=null)liberi--;
		}
		return liberi;
		}
	
	
	//mi restituisce il primo medico disponibile, -1 altrimenti
	int primoMedicoDisponibile() {
		if(Red==1)return -1;//se c'è un codice rosso tutti i medici sono occupati con quello
		else {
			for(int i=0; i<10; i++) {
				if (medici[i]==null) return i; 
			}
			return -1;
		}
	}
	
	
	//stampa lo stato del reparto: 0 ->libero ; 1->occupato con un paziente con codice bianco
	//2->occupato con un paziente con codice giallo 3->occupato con un paziente con codice rosso
	//e il numero di pazienti con un determinato codice
		void statoReparto() {
			String reparto=" ";
			for(int i=0;i<10;i++) {
				if(medici[i]==null)reparto=reparto+0;//quel medico non è occupato
				else reparto=reparto + medici[i].priorità;
			}
			System.out.println("Stato del reparto: "+ reparto);
			System.out.println("Numero pazienti con il codice rosso: " + Red);
			System.out.println("Numero pazienti con il codice giallo: " + visiteGiallo);
			System.out.println("Numero pazienti con il codice bianco: " + visiteBianco);
			System.out.println();
		}
	
		
		
		
		public int inizioVisita(int codice,paziente p,int medico) {
			
			if (codice==3) {//CODICE ROSSO
				Lock.lock();
				while(mediciDisponibili(medici)<10) {//se non tutti i medici sono disponibili
					try {
						CondRosso.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Red=1;//tutti i medici sono a disposizione del codice rosso
				for(int i=0;i<10;i++) {
					medici[i]=p;
				}
				statoReparto();//stampo stato del reparto
				Lock.unlock();//rilascio la lock
				return -1;//in questo caso è indifferente cosa restituisco
				
			}else if(codice==2) {//CODICE GIALLO
				Lock.lock();
				//finchè ci sono codici rossi in attesa oppure il medico richiesto è occupato aspetto
				while(Lock.hasWaiters(CondRosso) || medici[medico] != null ) {
					try {
						CondGiallo[medico].await();//mi metto in attesa di un medico specifico
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
				medici[medico]=p;//dottore occupato con paziente
				visiteGiallo++; 
				statoReparto();//stampo stato lab
				Lock.unlock();
				return -1;//anche qua cosa restituisco non è importante, lo sarà solo se ho un codice bianco
				
				
			}else{// CODICE BIANCO
				Lock.lock();
				//finchè ci sono codici rossi in attesa oppure nemmeno un medico è disponibile
				while(Lock.hasWaiters(CondRosso) || mediciDisponibili(medici)==0 ) {
					try {
						CondBianco.await();//mi metto in attesa
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
				//vado dal primo medico disponibile
				int medicoAssegnato=primoMedicoDisponibile();
				medici[medicoAssegnato]=p;
				visiteBianco++;
				statoReparto();//stampa di controllo stato reparto
				Lock.unlock();
				return medicoAssegnato;//restituisco il medico assegnato alpaziente
				
			}
		}
		
	
		
		public void fineVisita(int codice,paziente p,int medico) {
			if(codice==3) {//CODICE ROSSO
				Lock.lock();
				Red=0;
				for(int i=0; i<10;i++) {//tutti i medici tornano disponibili
					medici[i]=null;
				}
				//prima controlla se ci sono altri pazienti con il codice rosso in attesa e nel caso li risveglia
				if(Lock.hasWaiters(CondRosso)) {
					CondRosso.signal();
					}else {//altrimenti risveglio gli altri pazienti
						for(int i=0; i< CondGiallo.length;i++) {
							CondGiallo[i].signal();
						}
						CondBianco.signal();
					}
				Lock.unlock();
				
			}else if(codice==2) {//CODICE GIALLO
				Lock.lock();
				medici[medico]=null;//medico torna disponibile
				if(Lock.hasWaiters(CondRosso)) {//se ci sono pazienti in codice rosso in attesa li sveglia
					CondRosso.signal();
				}else if(Lock.hasWaiters(CondGiallo[medico])) {
					//se non ci sono pazienti con codice rosso in attesa e ci sono pazienti con codice
					//giallo che richiedono quel medico li sveglio
					CondGiallo[medico].signal();
				}else {//altrimenti risveglio i pazienti con codice bianco in attesa
					CondBianco.signal();
				}
				visiteGiallo--;
				Lock.unlock();
				
			}else if(codice==1) {// CODICE BIANCO
				Lock.lock();
				medici[medico]=null;//medico torna disponibile
				if(Lock.hasWaiters(CondRosso)) CondRosso.signal();//sveglio pazienti cod rosso se ci sono
				//altrimenti se ci sono pazienti cod giallo in attesa per quel medico li sveglio
				if(Lock.hasWaiters(CondGiallo[medico])) CondGiallo[medico].signal(); 
				//elstrimenti risveglio i pazienti con codice bianco
				else CondBianco.signal();
				visiteBianco--;
				Lock.unlock();
				
				
			}
		}
		



}
