package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	
	private SerieADAO dao;
	
	private List<Team>elencoSquadre;
	private List<Season>elencoStagioni;
	private List<Match>elencoPartite;
	
	private List<Season> percorsoMigliore;
	private List<Season>stagioneConsecutive;
	
	private Map<String, Team>idMapTeam;
	private Map<Integer, Season>idMapStagioni;
	private Map<Season, Integer>stagioniPunti;
	
	private Graph<Season, DefaultWeightedEdge>grafo;
	private Season best;
	private Integer differenzaPesiBest;
	
	
	public Model() {
		dao= new SerieADAO();
		elencoSquadre=new ArrayList<Team>(this.dao.listTeams());
		elencoStagioni=new ArrayList<Season>(this.dao.listAllSeasons());
		
		
		
		idMapTeam=new HashMap<String, Team>();
		idMapStagioni=new HashMap<Integer, Season>();
		for(Team t: this.elencoSquadre) {
			if(!idMapTeam.containsKey(t.getTeam())){
				idMapTeam.put(t.getTeam(), t);
			}
		}
		for(Season s: this.elencoStagioni) {
			if(!idMapStagioni.containsKey(s.getSeason())) {
				idMapStagioni.put(s.getSeason(), s);
			}
		}
		
		
	}
	
	public List<Team>getElencoSquadre(){
		return this.elencoSquadre;
	}
	public Map<Season, Integer> puntiSquadra(Team squadraSel){
		this.stagioniPunti=new HashMap<Season,Integer>();
		dao=new SerieADAO();
		
		this.elencoPartite=new LinkedList<Match>(this.dao.elencoMatchPerSquadra(squadraSel, idMapStagioni, idMapTeam));
		
		for(Match m: elencoPartite) {
			Season stagione=m.getSeason();
			int punteggio=0;
			if(m.getFtr().equals("D")) {
				punteggio=1;
			}else {
				if((m.getHomeTeam().equals(squadraSel)&& m.getFtr().equals("H"))||(m.getAwayTeam().equals(squadraSel) && m.getFtr().equals("A")))
					punteggio=3;
			}
			
			Integer attuale=stagioniPunti.get(stagione);
			if(attuale==null)
				attuale=0;
			stagioniPunti.put(stagione,attuale+punteggio);
		}
		return stagioniPunti;
	}
	
	public void creaGrafo() {
		this.grafo=new SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.stagioniPunti.keySet());
		
		for(Season s1: stagioniPunti.keySet()) {
			for(Season s2: stagioniPunti.keySet()) {
				if(!s1.equals(s2)) {
					int punti1=this.stagioniPunti.get(s1);
					int punti2=this.stagioniPunti.get(s2);
					if(punti1>punti2) {
						Graphs.addEdgeWithVertices(this.grafo, s2, s1, punti1-punti2);
						//System.out.println(" "+ s1+" "+s2+" "+(punti1-punti2));
					}else {
							Graphs.addEdgeWithVertices(this.grafo, s1, s2, punti2-punti1);
							//System.out.println(" "+ s2+" "+s1+" "+(punti2-punti1));
					}
					
				}
			}
		}
		System.out.println(String.format("#Vertici: %d",this.vertexNumber()));
		System.out.println(String.format("#Archi: %d",this.edgeNumber()));
		
		this.best=null;
		this.differenzaPesiBest=0;
		for(Season s: this.grafo.vertexSet()) {
			int diffPeso=this.sommaPeso(s);
			if(diffPeso>differenzaPesiBest) {
				differenzaPesiBest=diffPeso;
				best=s;
			}
		}
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
		
	}
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
		
	}

	public Season getBest() {
		return best;
	}

	public void setBest(Season best) {
		this.best = best;
	}

	public Integer getDifferenzaPesiBest() {
		return differenzaPesiBest;
	}

	public void setDifferenzaPesiBest(Integer differenzaPesiBest) {
		this.differenzaPesiBest = differenzaPesiBest;
	}
	public Integer sommaPeso(Season s) {
		int sommaE=0;
		int sommaU=0;
		for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(s)) {
			sommaE=(int) (sommaE+this.grafo.getEdgeWeight(e));
		}
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(s)) {
			sommaU=(int) (sommaU+this.grafo.getEdgeWeight(e));
		}
		return sommaE-sommaU;
		
	}
	
	

	public List<Season> camminoVirtuoso() {
		this.stagioneConsecutive=new ArrayList<Season>(this.stagioniPunti.keySet());
		Collections.sort(stagioneConsecutive);
		
		
		//cammino interno del grafo--> lista di vertici 
		//preparo le variabili utili alla ricorsione
		List<Season>parziale=new LinkedList<Season>();
		this.percorsoMigliore=new LinkedList<Season>();
		
		//Itero il livello zero della ricorsione
		
		for(Season s: this.grafo.vertexSet()) {
			parziale.add(s);
			this.cerca(1, parziale);
			parziale.remove(0);
		}	
		return percorsoMigliore;
	}
	
	
	
	
	
	private void cerca(int livello,List<Season>parziale) {
		/*parto dal generare nuove soluzioni ed eventualmente valuto caso terminale*/
		Season ultimo=parziale.get(livello-1);
		boolean trovato=false;
		
		for(Season prossimo: Graphs.successorListOf(this.grafo, ultimo)) {
			if(!parziale.contains(prossimo)) {
				//devono essere consecutivi ultimo indice 6 e prossimo con indice 7
				if(this.stagioneConsecutive.indexOf(ultimo)+1==stagioneConsecutive.lastIndexOf(prossimo)) {
					//avvio la ricorsione --> candidato accettabile
					trovato=true;
					parziale.add(prossimo);
					this.cerca(livello+1, parziale);
					parziale.remove(livello);
				}
			}
			
		}
		
		//caso terminale 
		if(!trovato) {
			if(parziale.size()>this.percorsoMigliore.size()) {
				percorsoMigliore= new LinkedList<Season>(parziale);//clonazione della lista ricordiamocelo!
			}
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
