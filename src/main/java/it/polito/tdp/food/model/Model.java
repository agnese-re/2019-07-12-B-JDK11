package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private Graph<Food,DefaultWeightedEdge> grafo;
	private FoodDao dao;
	
	private Map<Integer,Food> mappaFood;
	
	public Model() {
		dao = new FoodDao();
		mappaFood = new HashMap<Integer,Food>();
		for(Food food: dao.listAllFoods())
			mappaFood.put(food.getFood_code(), food);
		
	}
	
	public String creaGrafo(int porzioni) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		/* AGGIUNGO VERTICI */
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(mappaFood, porzioni));
		
		/* AGGIUNGO ARCHI - GRAFO ORIENTATO -> (A:B) <> (B:A) */
		List<Adiacenza> adiacenze = this.dao.getArchi(mappaFood, porzioni);
		/* se diff < 0 il valor medio di grassi saturi del primo cibo e' inferiore. L'arco andra'
		 * dal secondo cibo (con media di grassi maggiore) al primo cibo (con media grassi minore) */
		for(Adiacenza a: adiacenze)
			if(a.getDifferenzaGrassi() != 0)	// se 0 non aggiungo l'arco
				if(a.getDifferenzaGrassi() > 0)	// media grassi food1 > media grassi food2
					Graphs.addEdgeWithVertices(this.grafo, a.getFood1(), a.getFood2(), a.getDifferenzaGrassi());
				else // media grassi food2 > media grassi food1
					Graphs.addEdgeWithVertices(this.grafo, a.getFood2(), a.getFood1(), a.getDifferenzaGrassi());
		
		String msg = "Grafo creato " + "(" + this.grafo.vertexSet().size() + " vertici " + 
				this.grafo.edgeSet().size() + " archi)"; 
		return msg;
	}
	
	public List<Food> getVertici() {
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	public List<Vicino> get5Vicini(Food ciboUtente) {
		List<Vicino> result = new ArrayList<Vicino>();
		List<Food> vicini = Graphs.neighborListOf(this.grafo, ciboUtente);
		for(Food vicino: vicini) 
			if(result.size() < 5) {
				Food minimo = ricercaMinimo(ciboUtente, vicini, result);
				// vicini.remove(minimo);	-> java.util.ConcurrentModificationException
				if(this.grafo.containsEdge(this.grafo.getEdge(vicino, ciboUtente))) {
					DefaultWeightedEdge e = this.grafo.getEdge(vicino, ciboUtente);
					result.add(new Vicino(minimo,this.grafo.getEdgeWeight(e)));
				} else {
					DefaultWeightedEdge e = this.grafo.getEdge(ciboUtente, vicino);
					result.add(new Vicino(minimo,this.grafo.getEdgeWeight(e)));
				}	
			}
		Collections.sort(result);
		return result;
	}

	private Food ricercaMinimo(Food ciboUtente, List<Food> vicini, List<Vicino> giaMinimi) {
		double minimo = Integer.MAX_VALUE;
		Food minimoFood = null;
		
		for(Food vicino: vicini) 
			if(!giaMinimi.contains(new Vicino(vicino,0.0))) {
				if(this.grafo.containsEdge(this.grafo.getEdge(vicino, ciboUtente))) {
					DefaultWeightedEdge e = this.grafo.getEdge(vicino, ciboUtente);
					if(Math.abs(this.grafo.getEdgeWeight(e)) < minimo) {
						minimo = this.grafo.getEdgeWeight(e);
						minimoFood = vicino;
					}
				} else { // (this.grafo.containsEdge(this.grafo.getEdge(ciboUtente, vicino))) 
						DefaultWeightedEdge e = this.grafo.getEdge(ciboUtente, vicino);
						if(Math.abs(this.grafo.getEdgeWeight(e)) < minimo) {
							minimo = this.grafo.getEdgeWeight(e);
							minimoFood = vicino;
					}
				}	
			}
		return minimoFood;
	}
}
