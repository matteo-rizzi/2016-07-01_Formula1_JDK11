package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {

	private FormulaOneDAO dao;
	private Map<Integer, Driver> idMap;
	private Graph<Driver, DefaultWeightedEdge> grafo;
	private Integer max;
	private Driver migliore;
	private Integer minimo;
	private List<Driver> best;

	public Model() {
		this.dao = new FormulaOneDAO();
		this.idMap = new HashMap<>();
		this.dao.getAllDrivers(this.idMap);
	}

	public List<Season> getAllSeasons() {
		return this.dao.getAllSeasons();
	}

	public void creaGrafo(int anno) {
		this.grafo = new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getDriversByYear(anno, idMap));

		// aggiungo gli archi
		for (Adiacenza a : this.dao.getAdiacenze(anno, idMap)) {
			Graphs.addEdge(this.grafo, a.getPrimo(), a.getSecondo(), a.getPeso());
		}
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public void migliorRisultato() {
		this.migliore = new Driver();
		this.max = Integer.MIN_VALUE;
		for (Driver d : this.grafo.vertexSet()) {
			int peso = 0;
			for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(d)) {
				peso += this.grafo.getEdgeWeight(e);
			}
			for (DefaultWeightedEdge e : this.grafo.incomingEdgesOf(d)) {
				peso -= this.grafo.getEdgeWeight(e);
			}
			if (peso > max) {
				max = peso;
				this.migliore = d;
			}
		}

	}

	public Integer getMax() {
		return max;
	}

	public Driver getMigliore() {
		return migliore;
	}

	public List<Driver> trovaDreamTeam(int K) {
		this.best = new ArrayList<>();
		this.minimo = null;

		List<Driver> parziale = new ArrayList<>();

		this.cerca(parziale, K);

		return best;
	}

	private void cerca(List<Driver> parziale, int K) {
		// caso terminale
		if (parziale.size() == K) {
			if (minimo == null || tassoSconfitta(parziale) < minimo) {
				minimo = tassoSconfitta(parziale);
				this.best = new ArrayList<>(parziale);
			}
			return;
		}

		// caso intermedio
		for (Driver d : this.grafo.vertexSet()) {
			if(!parziale.contains(d)) {
				parziale.add(d);
				this.cerca(parziale, K);
				parziale.remove(d);
			}
		}

	}

	private Integer tassoSconfitta(List<Driver> parziale) {
		int tassoSconfitta = 0;
		for (Driver nonAppartenente : this.grafo.vertexSet()) {
			for (Driver appartenente : this.grafo.vertexSet()) {
				if (!parziale.contains(nonAppartenente) && parziale.contains(appartenente) && this.grafo.getEdge(nonAppartenente, appartenente) != null) {
					tassoSconfitta += this.grafo.getEdgeWeight(this.grafo.getEdge(nonAppartenente, appartenente));
				}
			}
		}
		return tassoSconfitta;
	}

	public Integer getMinimo() {
		return minimo;
	}

}
