import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public interface ServiceInterface extends Remote {
	
	//registrare uno spiker ad una sessione
	public int registry(String SpeakerName, int Sessione, int day)throws RemoteException;
	
	//stampo il programma del congresso
	public HashMap<Integer,ArrayList<LinkedList<String>>> getProgram()throws RemoteException;


}
