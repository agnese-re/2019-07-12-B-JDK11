package it.polito.tdp.food.model;

public class TestModel {

	public static void main(String args[]) {
		
		Model m = new Model();
		
		m.creaGrafo(4);
		
		Food foodUtente = new Food(23559,"Ground beef (95% lean)");
		System.out.println(m.get5Vicini(foodUtente));
	}
}
