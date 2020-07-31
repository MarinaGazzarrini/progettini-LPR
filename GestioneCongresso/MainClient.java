import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MainClient {
	
/* richieste interattive fatte dall'utente:
   registrazione->significa che voglio registrare uno speaker ad una sessione. sarà seguito dal nome dello speker(stringa), dal giorno(int) e dalla sessione(int)
   programma->significa che voglio il programma del congresso
   termina->condizione di terminazione. non ho più richieste 
	*/
		public static void main(String args[]) {//non viene passato nulla da main
			System.out.println("Start: inserisci comando");//posso iniziare a fare richieste interattivamente
			
			try {
				
				//non metto anche ind ip perchè sono in locale
				Registry r = LocateRegistry.getRegistry(6666);
				Remote RemoteObject=r.lookup("SERVER");//usato dal client per interrogare il registro RMI
				ServiceInterface serverObject=(ServiceInterface) RemoteObject;
				
				BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
				String k=buf.readLine();
				
				while(!k.equals("termina")) {//fino a che ho richieste
				
					if(k.equals("registrazione")) {//voglio registrare uno speaker ad una sessione
						System.out.println("Richiesta registrazione, inserisci nome speaker:");
						int controllo = -1;//per vedere se la registrazione è andata a buon fine
						
						String speaker=buf.readLine();
						System.out.println("inserisci giorno:");// se dovessi inserire un giorno non valido me lo comunicherà dopo aver inserito anche la sessione
						int day=Integer.parseInt(buf.readLine());
						System.out.println("inserisci sessione");
						int session=Integer.parseInt(buf.readLine());
						
		
//se giorno e/o sessione non risultano validi la registrazione non può essere effettuata. 						
						if(day< 0 || day > 2) System.out.println("Giorno non valido: "+ day+",registrazione non effettuata");
							else if(session<0 || session >11) System.out.println("Sessione non valida: "+ session+", registrazione non effettuata");
								else { controllo=serverObject.registry(speaker,session,day);
									if(controllo==1)System.out.println("Nessuna disponibilità per il giorno "+ day+",nella sessione "+session);
									else System.out.println("Registrazione effettuata: Speaker: "+ speaker +" Giorno: "+day+" Sessione: "+ session);
								}
						k=buf.readLine();
					}
					else if(k.equals("programma")){//chiedo il programma del congresso
						
						System.out.println("Richiesta Programma");
						
						HashMap<Integer,ArrayList<LinkedList<String>>> programma=serverObject.getProgram();
						
						for(int j=0;j<3; j++) {
							ArrayList<LinkedList<String>> a= programma.get(j);
							for(int s=0; s<12; s++) {
								LinkedList<String> l= a.get(s);
								
								System.out.print("GIORNO: "+j+" / ");
								System.out.print("SESSIONE: "+s+" / ");
								System.out.print("SPEAKER: ");
								
								for(int z=0;z<l.size();z++) {
									System.out.print(l.get(z)+" ");
								}
								
								System.out.println("");//per un fattore estetico di stampa
								System.out.println("");
								
							}
						}
						
						k=buf.readLine();
					}else {
						System.out.println("Comando non valido: "+k);
						k=buf.readLine();
					}
				}
				
			} catch (NotBoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			

			
		}

}
