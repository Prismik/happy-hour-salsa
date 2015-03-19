package yis;

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

	public Node getBestChild() {
		int val = Integer.MIN_VALUE;
		Node best = null;
		for(Node n : childs) {
			if (n.getValue() > val) {
				best = n;
				val = best.getValue();
			}
		}

		return best;
	}

	public Node getWorstChild() {
		int val = Integer.MAX_VALUE;
		Node worst = null;
		for(Node n : childs) {
			if (n.getValue() < val) {
				worst = n;
				val = worst.getValue();
			}
		}

		return worst;
	}

	public void addChild(Nove child) { childs.add(child); }
	public ArrayList<Node> getChilds() { return childs; }
	public setMove(Move m) { move = m; }
	public Move getMove() { return move; }
	public setValue(int v) { value = v; }
	public int getValue() { return value; }
}