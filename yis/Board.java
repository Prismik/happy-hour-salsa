package yis;

class Board {
	private Piece[][] board;
	private int player = 4; // default
	private int size;
	public Board(int s) {
		size = s;
		board = new Piece[size][size];
	}

	private int evalVictory() {
		return -1;
	}
	
	// 4 for white
	// 2 for black
	public void setSelf(int p) {
		player = p;
	}

	public void set(int x, int y, int v) {
		if (v == 0)
			board[x][y] = null;
		else
			board[x][y] = new Piece(v);
	}

	public void move(int fromX, int fromY, int toX, int toY) {
		Piece p = board[fromX][fromY];
		board[fromX][fromY] = null;
		board[toX][toY] = p;
	}

	public Moveset moveset(int x, int y) {
		int v = vertical(x, y);
		int h = horizontal(x, y);
		int dlr = diagonalLeftRight(x, y);
		int drl = diagonalRightLeft(x, y);

		return null;
	}

	// Optimisation possible, garder en memoire le nb de pieces
	// veritcal horizontal et diagonal pour chaque position
	private int vertical(int x, int y) {
		int count = 0;
		for (int i = 0; i != size - 1; i++)
			if (board[x][i] != null)
				count++;

		return count;
	}

	private int horizontal(int x, int y) {
		int count = 0;
		for (int i = 0; i != size - 1; i++)
			if (board[i][y] != null)
				count++;

		return count;
	} 

	// à tester
	private int diagonalLeftRight(int x, int y) {
		int dif = size - y;
		int count = 0;
		for (int i = 0, j = y; i != dif - 1; i++, j++)
			if (board[i][j] != null)
				count++;

		return count;
	}

	// à tester
	private int diagonalRightLeft(int x, int y) {
		int dif = size - y;
		int count = 0;
		for (int i = 0, j = y; i != dif - 1; i++, j--)
			if (board[i][j] != null)
				count++;

		return count;
	}

	public String toString() {
		String b = "";
		for (int i = 0; i != size - 1; i++) {
			for (int j = 0; j != size - 1; j++) {
				if (board[i][j] == null)
					b += ". ";
				else if (board[i][j].isBlack())
					b += "x ";
				else if (!board[i][j].isBlack())
					b += "o ";
			}

			b += "\n";
		}

		return b;
	}
}