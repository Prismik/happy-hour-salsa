package yis;

import java.util.ArrayList;

class Moveset {
	private ArrayList<Tile> moves;
	public Moveset() {
		moves = new ArrayList<Tile>();
	}

	public Moveset(ArrayList<Tile> moves) {
		this.moves = moves;
	}
	
	public void addMove(Tile move) { moves.add(move); }
	public void clear() { moves.clear(); }
}