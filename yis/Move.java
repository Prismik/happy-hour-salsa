package yis;

import yis.Moveset;

class Move {
	private Tile from;
	private Tile to;
	public Move(Tile f, Tile t) {
		from = f;
		to = t;
	}
	
	public Tile getFrom() { return from; }
	public Tile getTo() { return to; }
}