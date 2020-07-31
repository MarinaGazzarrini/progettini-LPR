
public class mainClass {

public static void main(String[] args) {
		
		//passo come argomento del main il numero di pazienti con codice bianco, giallo e rosso
	
		int numPazientiBianco= Integer.parseInt(args[0]);
		int numPazientiGiallo=Integer.parseInt(args[1]);
		int numPazientiRosso=Integer.parseInt(args[2]);
		
		System.out.println("Pazienti con il codice bianco: "+ numPazientiBianco);
		System.out.println("Pazienti con il codice giallo: "+ numPazientiGiallo);
		System.out.println("Pazienti con il codice rosso: "+ numPazientiRosso);
		System.out.println();
		
		
		
		gestore gestoreReparto= new gestore();
		
		//attivo un thread per ogni paziente
		int i;
		
		//pazienti con il codice bianco(corrisponde al numero 1)
		for(i=0; i< numPazientiBianco; i++) {
			paziente pazienteCodiceBianco= new paziente(gestoreReparto,1);
			Thread pazienteBianco= new Thread(pazienteCodiceBianco);
			pazienteBianco.start();
		}

	
		//pazienti con il codice giallo(corrisponde al numero 2)
		for(i=0; i<numPazientiGiallo; i++) {
			paziente pazienteCodiceGiallo= new paziente(gestoreReparto,2);
			Thread pazienteGiallo= new Thread(pazienteCodiceGiallo);
			pazienteGiallo.start();
		}
		
		//pazienti con il codice rosso(corrisponde al numero 3)
		for(i=0; i<numPazientiRosso; i++) {
			paziente pazienteCodiceRosso= new paziente(gestoreReparto,3);
			Thread pazienteRosso= new Thread(pazienteCodiceRosso);
			pazienteRosso.start();
		}

	}
}
