
import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainClass{
	
	public static void main(String[] args) throws IllegalArgumentException {
		
		//passo dal main il numero di conti correnti(int)
		int numeroContiCorrenti= Integer.parseInt(args[0]);
		int numeroMovimenti= 20;//stabilisco un massimo di 20 movimenti
		
		System.out.println("ci sono "+ numeroContiCorrenti+ " conti correnti");
		
		int mov;
		Causale causale;
		Movimento NuovoMovimento;
		ContoCorrente conto;
		int giorno,mese,anno;
		Random ran = new Random();
		
		//lista di tutti i conti correnti
		List<ContoCorrente> ContiCorrentiTotali= new LinkedList<ContoCorrente>();
	
		
		
		for(int i=0; i < numeroContiCorrenti; i++) {
			//Genero casualmente i nomi dei correntisti da una classe enum(possono esserci degli omonimi)
			int co= ran.nextInt(Correntisti.values().length);
			Correntisti name= Correntisti.values()[co];
			conto= new ContoCorrente(name);
			//per ogni conto scelgo casualmente il numero di movimenti(<= numeroMovimenti)
			mov=(int)(Math.random()*numeroMovimenti) +1;
			for(int k=0; k < mov; k++) {
				//scelgo casualmente giorno, mese, anno.
				mese=(int)(Math.random()*12) +1;
				if(mese==2)giorno=(int)(Math.random()*28) +1;
				else if(mese==11 || mese==4 || mese==6 || mese==9)giorno=(int)(Math.random()*30) +1;
				else giorno=(int)(Math.random()*31) +1;
				anno=2016+ran.nextInt(2018-2016+1);
				//scelgo casualmente la causale
				int ca = ran.nextInt(Causale.values().length);
				causale=Causale.values()[ca];
				NuovoMovimento=new Movimento(giorno,mese,anno,causale);
				conto.nuovoMovimento(NuovoMovimento);//aggiungo il nuovo movimento al conto
				}
			ContiCorrentiTotali.add(conto);//aggiungo il nuovo conto corrente
		}
		
		//stampo i conti correnti
		for(ContoCorrente cnt: ContiCorrentiTotali) {
			cnt.stampaConto();
		}

		
		
		//SERIALIZZAZIONE//
		
		int dimTot = 0;
		byte[] bt;
		ArrayList<Integer> Jlong= new ArrayList<Integer>();//per salvarmi la lunghezza di ogni jsonObject
		
			//faccio uso di FIleChannel per leggere/scrivere su file(non direttamente, ma passando da un buffer)
			//uso FileChannel.open() per creare direttamente un file:FileContiCorrenti
		try(FileChannel F= FileChannel.open(Paths.get("FileContiCorrenti"),StandardOpenOption.WRITE,StandardOpenOption.CREATE);){
			
			
			//voglio salvare la lunghezza di ogni JsonObject in byte nell'arrayLIst Jlong e la dimensione totale in dimTot
			dimTot=0;
			for(ContoCorrente c: ContiCorrentiTotali) {//scorro tutti i conti correnti
				int l=Integer.valueOf(c.joC().toJSONString().getBytes().length);//utilizzo il metodo .joC definito per la classe ContoCorrente per passare a ogg json
				Jlong.add(l);
				dimTot= dimTot+l;//qui avrò la dimensione totale di tutti i jsonobject
				
			}
			
			//alloco il buffer con dimensione sufficiente:dimTot
			ByteBuffer bb= ByteBuffer.allocate(dimTot);
			
		
			//faccio un ciclo e inserisco tutti i dati nel buffer
			for(ContoCorrente c: ContiCorrentiTotali) {
				bt=c.joC().toJSONString().getBytes();//metodo joC per passare a json object
				bb.put(bt);
				}
			
			bb.flip();//passo in modalità lettura
			
			while(bb.hasRemaining())F.write(bb);//fino a quando esistono elementi nel buffer, indico al canale di leggere i dati nel buffer
			
			bb.clear();//i dati non sono cancellati, ma saranno sovrascritti
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//creo e avvio un thread lettore
		Thread Lettore= new Thread(new Lettore("FileContiCorrenti",Jlong));//gli passo il file con i conti correnti e l'array list con le lunghezza in byte di ogni jsonobject
		Lettore.start();
		try {
			Lettore.join();//altrimenti il main termina prima che il thread abbia finito
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


