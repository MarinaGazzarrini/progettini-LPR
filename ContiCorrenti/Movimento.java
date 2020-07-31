//movimento: <giorno,mese,anno>, causale

import java.io.Serializable;

public class Movimento implements Serializable {
	

	private static final long serialVersionUID = 1L;//dafault serial version id
	private final int giorno;
	private final int mese;
	private final int anno;
	private final Causale causale;
	
	public Movimento(int g,int m, int a, Causale causal) {//costruttore
		this.giorno=g;
		this.mese=m;
		this.anno=a;
		this.causale=causal;
	}
	
	//metodo che mi restituisce la causale di un movimento
	public Causale restituisciCausaleuno() {
		return causale;
	}
	
	//metodo che mi restituisce il giorno
	public int getDay() {
		return giorno;
	}
	
	//metodo che mi restituisce il mese
	public int getMonth() {
		return mese;
	}
	
	//metodo che mi restituisce l'anno
	public int getYear() {
		return anno;
	}

}

