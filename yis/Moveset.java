package yis;

import java.util.ArrayList;

class Moveset {
	private ArrayList<Point> moves;

	// A moveset of 0 meanrs you cannot
	// move in the given direction.
	public Moveset() {
		moves = new ArrayList<Point>();
	}

	public Moveset(ArrayList<Point> moves) {
		this.moves = moves;
	}
	
	public void addMove(Point move) { moves.add(move); }
	public void clear() { moves.clear(); }
	
/*
	public int getNorth() { return north; }
	public int getNorthEast() { return northEast; }
	public int getEast() { return east; }
	public int getSouthEast() { return southEast; }
	public int getSouth() { return south; }
	public int getSouthWest() { return southWest; }
	public int getWest() { return west; }
	public int getNorthWest() { return northWest; }

	public void setNorth(int v) { north = v; }
	public void setNorthEast(int v) { northEast = v; }
	public void setEast(int v) { east = v; }
	public void setSouthEast(int v) { southEast = v; }
	public void setSouth(int v) { south = v; }
	public void setSouthWest(int v) { southWest = v; }
	public void setWest(int v) { west = v; }
	public void setNorthWest(int v) { northWest = v; }
*/
}