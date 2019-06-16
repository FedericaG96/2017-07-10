package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo ;
	ArtsmiaDAO dao ;
	List<ArtObject> opere;
	List<Adiacenza> adiacenti;
	Map<Integer, ArtObject> idMapOpere;
	
	List<ArtObject> camminoOttimo;
	
	public Model() {
		this.grafo = new SimpleWeightedGraph<ArtObject,  DefaultWeightedEdge>( DefaultWeightedEdge.class);
		dao = new ArtsmiaDAO();
		idMapOpere = new HashMap<Integer, ArtObject>();
		opere = dao.listObjects(idMapOpere);
	
	}

	public void creaGrafo() {
		
		
		Graphs.addAllVertices(grafo, opere);
		
		adiacenti = dao.getCoEsposti();
		
		for(Adiacenza ad : adiacenti) {
			ArtObject opera1 = idMapOpere.get(ad.getId1());
			ArtObject opera2 = idMapOpere.get(ad.getId2());
			Graphs.addEdge(grafo, opera1, opera2, ad.getPeso());
		}		
	}

	public boolean isCorrect(Integer idInserito) {
		
		for (ArtObject ao : this.opere) {
			if (ao.getId() == idInserito)
				return true;
		}
		return false;
		/*
		if(idMapOpere.get(idInserito)==null)
			return false;
		else
			return true;
		*/
	}

	public Set<ArtObject> getComponenteConnessa(Integer idInserito) {
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> connInspector = new ConnectivityInspector<ArtObject, DefaultWeightedEdge>(grafo);
		return connInspector.connectedSetOf(idMapOpere.get(idInserito));
		
		/*
		//oppure
		ArtObject partenza = idMapOpere.get(idInserito);

		// visita il grafo
		Set<ArtObject> visitati = new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfv = new DepthFirstIterator<>(this.grafo, partenza);
		while (dfv.hasNext())
			visitati.add(dfv.next());

		return visitati;
		*/
	}

	public List<ArtObject> camminoPesoMassimo(int lunghezza, int idInserito) {
		camminoOttimo = new ArrayList<ArtObject>();
		
		List<ArtObject> parziale = new ArrayList<ArtObject>();
		
		ArtObject partenza = idMapOpere.get(idInserito);
		
		parziale.add(partenza);
		camminoOttimo.add(partenza);
		
		this.cercaCammino(parziale, 1, lunghezza);
		
		Collections.sort(camminoOttimo);
		return camminoOttimo;
	}

	private void cercaCammino(List<ArtObject> parziale, int livello, int lunghezza) {
		
		if(livello == lunghezza) {
			if(peso(parziale)> peso(camminoOttimo)) {
				this.camminoOttimo = new ArrayList<>(parziale);
			}
		}
		
		// trova vertici adiacenti all'ultimo
		ArtObject ultimo = parziale.get(parziale.size()-1);
		List<ArtObject> adiacenti = Graphs.neighborListOf(grafo, ultimo);
		
		for(ArtObject a : adiacenti) {
			if(!parziale.contains(a) && a.getClassification().equals(parziale.get(0).getClassification())) {
				parziale.add(a);
				this.cercaCammino(parziale, livello +1, lunghezza);
				parziale.remove(parziale.size()-1); //backtracking
			}
		}
		
		
	}

	public int peso(List<ArtObject> cammino) {
		int peso = 0;
		
		for(int i = 0; i< cammino.size()-1; i++) {
			DefaultWeightedEdge e = grafo.getEdge(cammino.get(i), cammino.get(i + 1));
			
			peso += (int) grafo.getEdgeWeight(e);
		}
		return peso;
	}
	
	

}
