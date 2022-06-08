package it.polito.tdp.food.model;

public class Vicino implements Comparable<Vicino> {

	private Food vicino;
	private Double differenzaGrassi;
	
	public Vicino(Food vicino, Double differenzaGrassi) {
		this.vicino = vicino;
		this.differenzaGrassi = differenzaGrassi;
	}

	public Food getVicino() {
		return vicino;
	}

	public void setVicino(Food vicino) {
		this.vicino = vicino;
	}

	public Double getDifferenzaGrassi() {
		return differenzaGrassi;
	}

	public void setDifferenzaGrassi(Double differenzaGrassi) {
		this.differenzaGrassi = differenzaGrassi;
	}

	@Override
	public String toString() {
		return vicino.getDisplay_name() + " " + differenzaGrassi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vicino == null) ? 0 : vicino.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Vicino other = (Vicino) obj;
		if (vicino == null) {
			if (other.vicino != null) {
				return false;
			}
		} else if (!vicino.equals(other.vicino)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Vicino o) {
		return this.getDifferenzaGrassi().compareTo(o.getDifferenzaGrassi());	// delego a metodo di Double
	}
	
}
