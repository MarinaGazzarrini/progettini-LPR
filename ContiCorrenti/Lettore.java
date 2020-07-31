import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

//legge dal file che gli viene passato gli oggetti “conto corrente” e li passa, uno per volta,
//ai thread della thread pool.
public class Lettore implements Runnable {
	
	private String file;
	ArrayList<Integer> Jlong= new ArrayList<Integer>();//conterrà la lunghezza in byte di ogni jsonobject
	ExecutorService e;
	//contatore globale thread safe che associa ad ogni causale la sua frequenza:
	private CopyOnWriteArrayList<CausaleFrequenza> contatore;
	
	
	public Lettore(String f, ArrayList<Integer> jl) {//costruttore
		this.file=f;//file con gli oggetti conto corrente
		Jlong=jl;
		e=Executors.newFixedThreadPool(10);
		//inizializzo contatore(ogni causale corrisponde ad una posizione)
		contatore= new CopyOnWriteArrayList<CausaleFrequenza>();
		CausaleFrequenza i= new CausaleFrequenza(Causale.Bollettino,0);
		contatore.add(0,i);//posizione 0->bollettino
		CausaleFrequenza j= new CausaleFrequenza(Causale.Bonifico,0);
		contatore.add(1,j);//posizione 1->bonifico
		CausaleFrequenza k= new CausaleFrequenza(Causale.Accredito,0);
		contatore.add(2,k);//posizione 2->accredito
		CausaleFrequenza h= new CausaleFrequenza(Causale.F24,0);
		contatore.add(3,h);//posizione 3->f24
		CausaleFrequenza x= new CausaleFrequenza(Causale.PagoBancomat,0);
		contatore.add(4,x);//posizione 4->pagobancomat
	}
	
	public void run() {
		
		try {
			FileChannel F= FileChannel.open(Paths.get(file), StandardOpenOption.READ);
			JSONParser p= new JSONParser();
			byte[] thisByte;
			ByteBuffer bb;
			
			
//ho bisogno di conoscere la lunghezza in byte di ogni jsonobject e utilizzo Jlong dove avevo salvato precedentemente questa info			
			for(int i: Jlong) {
				bb=ByteBuffer.allocate(i);//i corrisponde alla lunghezza dell'oggetto
				F.read(bb);//indico al canale di scrivere nel buffer passato nella read
				bb.flip();//lettura
				thisByte = new byte[i];
				bb.get(thisByte,0,i);//metto il contenuto del buffer in thisByte
				String Spars= new String(thisByte);//ho bisogno di una stringa per poter utilizzare parse
				e.execute(new Contatore(ContoCorrente.jaC((JSONObject)p.parse(Spars)),contatore));
				bb.clear();
				//il metodo parse ha come parametro una stringa che rappresenta un oggetto JSON e restituisce un JSONObject a cui posso applicare i metodi della classe JSONObject
				//utilizzo il metodo statico jaC che mi sono definita nella classe ContoCorrente e che mi permette di passare da jsonObject a java object
				
			}}catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.shutdown();
		
			while(!e.isTerminated()) { }//altrimenti stampa prima che abbiano finito i thread
			stampaFreq(contatore);
	
	}
	
	
	//stampa le causali e le frequenze associate
	public void stampaFreq(CopyOnWriteArrayList<CausaleFrequenza> contat) {
		for(CausaleFrequenza cf: contat) {
			System.out.println("Occorrenze "+ cf.getCausale()+ ": "+cf.getFrequenza());
		}
	}
	
	
	
	

}
