package it.polito.tdp.seriea.model;

public class S_Punteggio {
	private String descrizione_stagione;
	private Integer punteggio;
	public S_Punteggio(String descrizione_stagione, Integer punteggio) {
		super();
		this.descrizione_stagione = descrizione_stagione;
		this.punteggio = punteggio;
	}
	public String getDescrizione_stagione() {
		return descrizione_stagione;
	}
	public void setDescrizione_stagione(String descrizione_stagione) {
		this.descrizione_stagione = descrizione_stagione;
	}
	public Integer getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(Integer punteggio) {
		this.punteggio = punteggio;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descrizione_stagione == null) ? 0 : descrizione_stagione.hashCode());
		result = prime * result + ((punteggio == null) ? 0 : punteggio.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		S_Punteggio other = (S_Punteggio) obj;
		if (descrizione_stagione == null) {
			if (other.descrizione_stagione != null)
				return false;
		} else if (!descrizione_stagione.equals(other.descrizione_stagione))
			return false;
		if (punteggio == null) {
			if (other.punteggio != null)
				return false;
		} else if (!punteggio.equals(other.punteggio))
			return false;
		return true;
	}
	
	
}
