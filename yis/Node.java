package yis;

import java.util.ArrayList;

class Node {
	private ArrayList<Node> childs;
	private int value;
	private Move move;

	public Node() {
		childs = new ArrayList<Node>();
		value = 0;
		move = null;
	}

	public Node(Move m) {
		childs = new ArrayList<Node>();
		value = 0;
		move = m;
	}

	public void addChild(Node child) { childs.add(child); }
	public ArrayList<Node> getChilds() { return childs; }
	public void setMove(Move m) { move = m; }
	public Move getMove() { return move; }
	public void setValue(int v) { value = v; }
	public int getValue() { return value; }
}