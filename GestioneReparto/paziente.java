//classe che modella i pazienti dell'ospedale. Possono avere codici diversi
public class paziente extends Thread {
	
	gestore gestoreReparto;
	int priorit�;//mi dice se il paziente ha il codice bianco, giallo o rosso
	int medico;//indica il medico o i medici che dovranno visitare il paziente
	int accessi;//numero di accessi al reparto che il paziente deve fare
	
	public paziente(gestore Gestore, int codice){//costruttore
		gestoreReparto=Gestore;
		priorit�= codice;
		accessi=(int)(Math.random()*5)+1;//il numero di accessi al reparto � casuale
	}
	
	
	public void run() {
		
		if(priorit�==3) medico=11;//mi serve per comunicare che ha bisogno di tutti i medici a disposizione
		else if(priorit�==2) medico=(int)(Math.random()*9);//ha bisogno di un medico specifico
		else medico=11;//gli va bene qualsiasi medico, per adesso non gli � stato ancora assegnato
		
		int j;
		
		for(j=0; j<accessi; j++) {//fino a quando non ho effettuato tutti gli accessi
			if(priorit�==1)medico=gestoreReparto.inizioVisita(priorit�, this, medico);
			else gestoreReparto.inizioVisita(priorit�, this, medico);
			//il fatto che mi restituisca il medico mi serve solo se ho a che fare con un codice bianco per
			//sapere il medico che gli � stato assegnato per poi "rilasciarlo" in seguito
			try {
				Thread.sleep((int)(Math.random()*20));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//simulo la durata della visita
			
			gestoreReparto.fineVisita(priorit�,this, medico);
			
			try {
				Thread.sleep((int)(Math.random()*90));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//simulo il tempo che intercorre tra  un accesso e il successivo
			
			
			
			
		}
		
		
	}
	
	
	

}
