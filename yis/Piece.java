package yis;
import yis.Moveset;

class Piece {
	private Moveset moves = new Moveset();
	private int player = 4; // default
	public Piece(int p) {
		player = p;
	}

	public int getPlayer() { return player; }
}