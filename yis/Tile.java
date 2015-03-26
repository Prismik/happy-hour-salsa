package yis;

public class Tile {
	private int x;
	private int y;
	private int value;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getValue() { return this.value; }

	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setValue(int value) { this.value = value; }

	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Tile))return false;
		Tile tile = (Tile)other;
		if (x == tile.getX() && y == tile.getY())
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return 21 * x + 4 * y;
	}
}
