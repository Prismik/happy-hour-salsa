package yis;


class Move {
	private Tile from;
	private Tile to;
	public Move(Tile f, Tile t) {
		from = f;
		to = t;
	}
	
	public Tile getFrom() { return from; }
	public Tile getTo() { return to; }
	
	private String tileToString(int x, int y) {
		//System.out.println("Format is: " + Integer.toString(y + 1));
		return Character.toChars(x + 65)[0] + Integer.toString(y + 1);
	}
	
	public String formatMove() {
		//System.out.println("Before the parse: " + from.getX() + " " + from.getY() + " TO " + to.getX() + " " + to.getY());
		return tileToString(from.getX(), from.getY()) + " - " + tileToString(to.getX(), to.getY());
	}
}