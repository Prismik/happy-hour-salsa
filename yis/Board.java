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

	public void set(int x, int y, int player) {
		if (player == 0)
			board[x][y] = null;
		else
			board[x][y] = new Piece(player);
	}

	public void move(int fromX, int fromY, int toX, int toY) {
		Piece p = board[fromX][fromY];
		board[fromX][fromY] = null;
		board[toX][toY] = p;
	}

	public Moveset getMoveset(int x, int y) {
		int mvmtV = countVerticalPieces(x, y);
		int mvmtH = countHorizontalPieces(x, y);
		int mvmtDbl = countDiagonalBottomLeftPieces(x, y);
		int mvmtDbr = countDiagonalTopLeftPieces(x, y);
		
		// clockwise looking
		Moveset moveset = board[x][y].getMoveset();
		moveset.clear();
		Point p;
		if ((p = lookUp(x, y, mvmtV)) != null) moveset.addMove(p);
		if ((p = lookUpRight(x, y, mvmtDbl)) != null) moveset.addMove(p);
		if ((p = lookRight(x, y, mvmtH)) != null) moveset.addMove(p);
		if ((p = lookDownRight(x, y, mvmtDbr)) != null) moveset.addMove(p);
		if ((p = lookDown(x, y, mvmtV)) != null) moveset.addMove(p);
		if ((p = lookDownLeft(x, y, mvmtDbl)) != null) moveset.addMove(p);
		if ((p = lookLeft(x, y, mvmtH)) != null) moveset.addMove(p);
		if ((p = lookUpLeft(x, y, mvmtDbr)) != null) moveset.addMove(p);
	
		return moveset;
	}

	// Optimisation possible, garder en memoire le nb de pieces
	// veritcal horizontal et diagonal pour chaque position
	private int countVerticalPieces(int x, int y) {
		int count = 0;
		for (int i = 0; i != size - 1; i++)
			if (board[x][i] != null)
				count++;

		return count;
	}

	private int countHorizontalPieces(int x, int y) {
		int count = 0;
		for (int i = 0; i != size - 1; i++)
			if (board[i][y] != null)
				count++;

		return count;
	} 

	// à tester
	private int countDiagonalBottomLeftPieces(int x, int y) {
		int count = 1;
		int i = x + 1; // ligne x
		int j = y - 1; // colonnes y
		
		// left
		while (i < 8 && j >= 0) {
			if (board[i][j] != null) 
				++count;
			
			++i;
			--j;
		}
		
		i = x - 1;
		j = y + 1;
		
		// right
		while (i >= 0 && j < 8) {
			if (board[i][j] != null) 
				++count;
			
			--i;
			++j;
		}
		
		return count;
	}

	// à tester
	private int countDiagonalTopLeftPieces(int x, int y) {
		int count = 1;
		int i = x - 1; // ligne x
		int j = y - 1; // colonnes y
		
		// left
		while (i >= 0 && j >= 0) {
			if (board[i][j] != null) 
				++count;
			
			--i;
			--j;
		}
		
		i = x + 1;
		j = y + 1;
		
		// right
		while (i < 8 && j < 8) {
			if (board[i][j] != null) 
				++count;
			
			++i;
			++j;
		}
		
		return count;
	}
	
	public Point lookUp(int x, int y, int mov) {
		for (int i = x; i >= 0 && i >= x - mov; --i) {
			if (i == x - mov && (board[i][y] == null || board[i][y].getPlayer() != player))
				return new Point(i, y);
			else if (board[i][y] != null && board[i][y].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Point lookUpRight(int x, int y, int mov) {
		return null;
	}
	
	public Point lookRight(int x, int y, int mov) {
		for (int i = y; i < 8 && i <= y + mov; ++i) {
			if (i == y + mov && (board[x][i] == null || board[x][i].getPlayer() != player))
				return new Point(x, i);
			else if (board[x][i] != null && board[x][i].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Point lookDownRight(int x, int y, int mov) {
		return null;
	}
	
	public Point lookDown(int x, int y, int mov) {
		for (int i = x; i < 8 && i <= x + mov; ++i) {
			if (i == x + mov && (board[i][y] == null || board[i][y].getPlayer() != player))
				return new Point(i, y);
			else if (board[i][y] != null && board[i][y].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Point lookDownLeft(int x, int y, int mov) {
		return null;
	}
	
	public Point lookLeft(int x, int y, int mov) {
		for (int i = y; i >= 0 && i >= y + mov; ++i) {
			if (i == y + mov && (board[x][i] == null || board[x][i].getPlayer() != player))
				return new Point(x, i);
			else if (board[x][i] != null && board[x][i].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Point lookUpLeft(int x, int y, int mov) {
		/*int i = 0;
		int j = 0;
		
		while (i >= 0 && j >= 0 && i  y + mov) {
			
			
			--i;
			--j;
		}*/
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