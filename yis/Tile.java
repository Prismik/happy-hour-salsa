package yis;

public class Tile {
	private int x;
	private int y;
	private int value;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.value = 0;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getValue() { return this.value; }
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setValue(int value) { this.value = value; }
}
