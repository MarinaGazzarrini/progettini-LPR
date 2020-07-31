import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class server extends UnicastRemoteObject implements ServiceInterface{

	private static final long serialVersionUID = 1L;
	static OrganizzazioneCongresso oc;
	
	public server() throws RemoteException{//costruttore
		super();// è qui che il server viene esportato
		
	}
	
	
	public int registry(String SpeakerName, int Sessione, int day)throws RemoteException {
		int r=oc.registrazione(SpeakerName, day, Sessione);
		return r;
		}
	
	public HashMap<Integer,ArrayList<LinkedList<String>>> getProgram(){
		HashMap<Integer,ArrayList<LinkedList<String>>> programma= oc.getP();
		return programma;
	}
	
	public static void main(String[] args){
		
		oc= new OrganizzazioneCongresso();//creazione istanza oggetto
		
		server s;
		try {
			s = new server();
			
			//esportazione dell'oggetto non devo farla perchè ho esteso UnicastRemoteObject quindi è implicita
			
			//Creazione di un registry sulla porta 6666
			 LocateRegistry.createRegistry(6666);
			 Registry r=LocateRegistry.getRegistry(6666); 
			 
			 // Pubblicazione dello stub nel registry 
			 r.rebind("SERVER", s);
			 
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	

		
	}

}
