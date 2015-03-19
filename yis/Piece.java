package yis;
import java.util.ArrayList;


class Piece {
	private int player = 4; // default
	public Piece(int p) {
		player = p;
	}
	
	public int getPlayer() { return player; }
}