package yis;

class Board {
	private final int WHITE = 4;
	private final int BLACK = 2;
	private Piece[][] board;
	private int player = 4; // default
	private int size;
	private int whitePieces, blackPieces;
	public Board(int s) {
		size = s;
		board = new Piece[size][size];
		whitePieces = blackPieces = (s - 2) * 2;
	}

	private boolean hasWon(int player) {
		if (player == WHITE && whitePieces == 1)
			return true;
		else if (player == BLACK && blackPieces == 1)
			return true;
		else
			for (int i = 0; i != size - 1; i++)
				for (int j = 0; j != size -1; j++)
					if (adjacentsOfType(i, j, player) == 0)
						return false;	
		
		return true;
	}
	
	public int adjacentsOfType(int x, int y, int player) {
		int count = 0;
		boolean top = y - 1 >= 0;
		boolean right = x + 1 <= size - 1;
		boolean bottom = y + 1 <= size - 1;
		boolean left = x - 1 >= 0;
		if (left) {
			if (board[x-1][y].getPlayer() == player)
				count++;

			if (top && board[x-1][y-1].getPlayer() == player)
				count++;

			if (bottom && board[x-1][y+1].getPlayer() == player)
				count++;
		}

		if (top)
			if (board[x][y-1].getPlayer() == player)
				count++;

		if (bottom)
			if (board[x][y+1].getPlayer() == player)
				count++;

		if (right) {
			if (board[x+1][y].getPlayer() == player)
				count++;

			if (top && board[x+1][y-1].getPlayer() == player)
				count++;

			if (bottom && board[x+1][y+1].getPlayer() == player)
				count++;
		}
		
		return count;
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
		Tile p;
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
	
	public Tile lookUp(int x, int y, int mov) {
		for (int i = x; i >= 0 && i >= x - mov; --i) {
			if (i == x - mov && (board[i][y] == null || board[i][y].getPlayer() != player))
				return new Tile(i, y);
			else if (board[i][y] != null && board[i][y].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Tile lookUpRight(int x, int y, int mov) {
		return null;
	}
	
	public Tile lookRight(int x, int y, int mov) {
		for (int i = y; i < 8 && i <= y + mov; ++i) {
			if (i == y + mov && (board[x][i] == null || board[x][i].getPlayer() != player))
				return new Tile(x, i);
			else if (board[x][i] != null && board[x][i].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Tile lookDownRight(int x, int y, int mov) {
		return null;
	}
	
	public Tile lookDown(int x, int y, int mov) {
		for (int i = x; i < 8 && i <= x + mov; ++i) {
			if (i == x + mov && (board[i][y] == null || board[i][y].getPlayer() != player))
				return new Tile(i, y);
			else if (board[i][y] != null && board[i][y].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Tile lookDownLeft(int x, int y, int mov) {
		int i = x;
		int j = y;
		
		for (int nbMoves = 0; i < 8 && j < 8 && nbMoves < mov; ++nbMoves) {
			if (nbMoves == mov && (board[i][j] == null || board[i][j].getPlayer() != player))
				return new Tile(i, j);
			else if (board[i][j] != null && board[i][j].getPlayer() != player)
				return null;
			
			++i;
			++j;
		}
		
		return null;
	}
	
	public Tile lookLeft(int x, int y, int mov) {
		for (int i = y; i >= 0 && i >= y + mov; ++i) {
			if (i == y + mov && (board[x][i] == null || board[x][i].getPlayer() != player))
				return new Tile(x, i);
			else if (board[x][i] != null && board[x][i].getPlayer() != player)
				return null;
		}
			
		return null;
	}
	
	public Tile lookUpLeft(int x, int y, int mov) {
		int i = x;
		int j = y;
		
		for (int nbMoves = 0; i >= 0 && j >= 0 && nbMoves < mov; ++nbMoves) {
			if (nbMoves == mov && (board[i][j] == null || board[i][j].getPlayer() != player))
				return new Tile(i, j);
			else if (board[i][j] != null && board[x][i].getPlayer() != player)
				return null;
			
			--i;
			--j;
		}
		
		return null;
	}

	public String toString() {
		String b = "";
		for (int i = 0; i != size; i++) {
			for (int j = 0; j != size; j++) {
				if (board[i][j] == null)
					b += ". ";
				else if (board[i][j].getPlayer() == WHITE)
					b += "x ";
				else if (board[i][j].getPlayer() == BLACK)
					b += "o ";
			}

			b += "\n";
		}

		return b;
	}
}