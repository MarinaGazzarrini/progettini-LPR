// CausaleFrequenza: <nomeCausale,frequenzaAssociata>
public class CausaleFrequenza {
	Causale c;
	private int frequenza;
	
	public CausaleFrequenza(Causale ca, int f) {//costruttore
		this.c=ca;
		this.frequenza=f;
	}
	
	//restituisce la frequenza
	public int getFrequenza() {
		return frequenza;
	}
	
	//restituisce la causale
	public Causale getCausale() {
		return c;
	}
	
	//aumenta la frequenza di uno
	public void aumentaFrequenza() {
		this.frequenza=this.frequenza +1;
	}
	
	//permette di assegnare un valore nuovo alla frequenza
	public void modificaFrequenza(int fr) {
		this.frequenza=fr;
	}
}
