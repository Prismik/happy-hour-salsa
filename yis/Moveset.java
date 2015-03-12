package yis;

class Moveset {
	private int north = 0;
	private int northEast = 0;
	private int east = 0;
	private int southEast = 0;
	private int south = 0;
	private int southWest = 0;
	private int west = 0;
	private int northWest = 0;

	// A moveset of 0 meanrs you cannot
	// move in the given direction.
	public Moveset() {
		north = 0;
		northEast = 0;
		east = 0;
		southEast = 0;
		south = 0;
		southWest = 0;
		west = 0;
		northWest = 0;
	}

	public Moveset(int n, int ne, int e, int se, int s, int sw, int w, int nw) {
		north = n;
		northEast = ne;
		east = e;
		southEast = se;
		south = s;
		southWest = sw;
		west = w;
		northWest = nw;
	}

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
}