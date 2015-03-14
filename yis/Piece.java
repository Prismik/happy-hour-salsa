package yis;
import java.util.ArrayList;

import yis.Moveset;

class Piece {
	private ArrayList<Tile> moves = new ArrayList<Tile>();
	private int player = 4; // default
	public Piece(int p) {
		player = p;
	}
	
	public ArrayList<Tile> getMoveset() { return moves; }
	
	public int getPlayer() { return player; }

	
	public void addMove(Tile move) { moves.add(move); }
	public void clearMoves() { moves.clear(); }
}