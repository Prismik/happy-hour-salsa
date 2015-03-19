package yis;

import java.util.ArrayList;
import java.util.Random;

class Board {
	private final int WHITE = 4;
	private final int BLACK = 2;
	private Piece[][] board;
	private int player = 4; // default
	private int size;
	private int whitePieces, blackPieces;
	private ArrayList<Move> whiteMoveset;
	private ArrayList<Move> blackMoveset;
	
	public Board(int s) {
		size = s;
		board = new Piece[size][size];
		whitePieces = blackPieces = (s - 2) * 2;
		whiteMoveset = new ArrayList<Move>();
		blackMoveset = new ArrayList<Move>();
	}
	
	public Board(Board board) {
		this.size = board.size;
		this.board = new Piece[this.size][this.size];
		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				this.board[i][j] = board.board[i][j];
		
		this.player = board.player;
		this.whitePieces = board.whitePieces;
		this.blackPieces = board.blackPieces;
		
		this.whiteMoveset = new ArrayList<Move>();
		for (Move move: board.whiteMoveset) {
			this.whiteMoveset.add(move);
		}
		
		this.blackMoveset = new ArrayList<Move>();
		for (Move move: board.blackMoveset) {
			this.blackMoveset.add(move);
		}
	}

	private boolean hasWon(int player, Piece[][] testBoard) {
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
	
	public int getPlayer() { return player; }
	public int getOpponent() { return (player == WHITE)? BLACK : WHITE; }
	
	public ArrayList<Move> getPlayerMoveset(int player) { 
		return (player == WHITE) ? this.whiteMoveset : this.blackMoveset; 
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
	public void init(int p) {
		player = p;
		updateMovesets();
	}

	public void set(int x, int y, int player) {
		if (player == 0)
			board[x][y] = null;
		else
			board[x][y] = new Piece(player);
	}

	public void doMove(int fromX, int fromY, int toX, int toY) {
		Piece p = board[fromX][fromY];
		board[fromX][fromY] = null;
		if (board[toX][toY] != null) {
			int player = board[toX][toY].getPlayer();
			if (player == BLACK)
				blackPieces--;
			else
				whitePieces--;
		}

		board[toX][toY] = p;
		updateMovesets();
		System.out.println(this.toString());
	}

	public void doMove(Tile[] tiles) {
		doMove(tiles[0].getX(), tiles[0].getY(), tiles[1].getX(), tiles[1].getY());
	}

	public void doMove(Move m) {
		Tile from = m.getFrom();
		Tile to = m.getTo();
		doMove(from.getX(), from.getY(), to.getX(), to.getY());
	}

	/*
	public String getNextMove() {
		Piece currentPiece;
		Tile tileToGo = null;
		int x, y;
		x = y = 0;
		
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				currentPiece = board[i][j];
				if (currentPiece != null) {
					for (Tile move : currentPiece.getMoveset()) {
						if ((tileToGo == null || tileToGo.getValue() < move.getValue())
							&& currentPiece.getPlayer() == player) {
							x = i;
							y = j;
							tileToGo = move;
						}
					}
				}
			}
		}
		
		if (tileToGo != null) {
			doMove(x, y, tileToGo.getX(), tileToGo.getY());
			return formatMove(x, y, tileToGo.getX(), tileToGo.getY());
		}
		else
			return "";
	}*/

	public void updateMovesets() {
		whiteMoveset.clear();
		blackMoveset.clear();
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				Piece p = board[i][j];
				if (p != null) {
					if (p.getPlayer() == WHITE)
						whiteMoveset.addAll(updateMovesetOfPiece(i, j, WHITE));
					else
						blackMoveset.addAll(updateMovesetOfPiece(i, j, BLACK));
				}
			}
		}
	}
	
	private ArrayList<Move> updateMovesetOfPiece(int x, int y, int player) {
		int mvmtV = countVerticalPieces(x, y);
		int mvmtH = countHorizontalPieces(x, y);
		int mvmtDbl = countDiagonalBottomLeftPieces(x, y);
		int mvmtDbr = countDiagonalTopLeftPieces(x, y);
		
		// clockwise looking
		ArrayList<Move> moveset = new ArrayList<Move>();
		Tile from = new Tile(x, y);
		Tile to = null;
		if ((to = lookUp(x, y, mvmtV, player)) != null)
			moveset.add(new Move(from, to));
		
		if ((to = lookUpRight(x, y, mvmtDbl, player)) != null)
			moveset.add(new Move(from, to));
		
		if ((to = lookRight(x, y, mvmtH, player)) != null)
			moveset.add(new Move(from ,to));
		
		if ((to = lookDownRight(x, y, mvmtDbr, player)) != null)
			moveset.add(new Move(from, to));
		
		if ((to = lookDown(x, y, mvmtV, player)) != null)
			moveset.add(new Move(from, to));

		if ((to = lookDownLeft(x, y, mvmtDbl, player)) != null)
			moveset.add(new Move(from, to));

		if ((to = lookLeft(x, y, mvmtH, player)) != null)
			moveset.add(new Move(from, to));
		
		if ((to = lookUpLeft(x, y, mvmtDbr, player)) != null)
			moveset.add(new Move(from, to));
		
		return moveset;
	}
	
	public int evaluatePlayer(int player) {
		int value = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (board[i][j] != null && board[i][j].getPlayer() == player)
					value += evaluatePosition(i, j, player);
			}
		}
		
		return value;
	}
	
	public int evaluatePosition(int currentX, int currentY, int player) {
		int currentPosVal = 8;
		if ((currentX == 0 || currentX == 7) &&	(currentY == 0 || currentY == 7)) {
			currentPosVal = 3;
		} else if ((currentX == 0 || currentX == 7) || (currentY == 0 || currentY == 7)) {
			currentPosVal = 5;
		}
	
		int currentAdjVal = adjacentsOfType(currentX, currentY, player);
		
		return currentPosVal + currentAdjVal;
	}
	
	private int countVerticalPieces(int x, int y) {
		int count = 0;
		for (int i = 0; i < size; i++)
			if (board[i][y] != null)
				count++;

		return count;
	}

	private int countHorizontalPieces(int x, int y) {
		int count = 0;
		for (int i = 0; i < size; i++)
			if (board[x][i] != null)
				count++;

		return count;
	} 

	// à tester
	private int countDiagonalBottomLeftPieces(int x, int y) {
		int count = 1;
		int i = x + 1; // ligne x
		int j = y - 1; // colonnes y
		
		// left
		while (i < size && j >= 0) {
			if (board[i][j] != null) 
				++count;
			
			++i;
			--j;
		}
		
		i = x - 1;
		j = y + 1;
		
		// right
		while (i >= 0 && j < size) {
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
		while (i < size && j < size) {
			if (board[i][j] != null) 
				++count;
			
			++i;
			++j;
		}
		
		return count;
	}
	
	public Tile lookUp(int x, int y, int mov, int p) {
		for (int i = x; i >= 0 && i >= x - mov; --i) {
			if (i == x - mov && (board[i][y] == null || board[i][y].getPlayer() != p))
				return new Tile(i, y);
			else if (board[i][y] != null && board[i][y].getPlayer() != p)
				return null;
		}
			
		return null;
	}
	
	public Tile lookUpRight(int x, int y, int mov, int p) {
		int i = x;
		int j = y;
		
		for (int nbMoves = 0; i >= 0 && j < size && nbMoves < mov; ++nbMoves) {
			if (nbMoves == mov && (board[i][j] == null || board[i][j].getPlayer() != p))
				return new Tile(i, j);
			else if (board[i][j] != null && board[i][j].getPlayer() != p)
				return null;
			
			--i;
			++j;
		}
		
		return null;
	}
	
	public Tile lookRight(int x, int y, int mov, int p) {
		for (int i = y; i < size && i <= y + mov; ++i) {
			if (i == y + mov && (board[x][i] == null || board[x][i].getPlayer() != p))
				return new Tile(x, i);
			else if (board[x][i] != null && board[x][i].getPlayer() != p)
				return null;
		}
			
		return null;
	}
	
	public Tile lookDownRight(int x, int y, int mov, int p) {
		int i = x;
		int j = y;
		
		for (int nbMoves = 0; i < size && j < size && nbMoves < mov; ++nbMoves) {
			if (nbMoves == mov && (board[i][j] == null || board[i][j].getPlayer() != p))
				return new Tile(i, j);
			else if (board[i][j] != null && board[i][j].getPlayer() != p)
				return null;
			
			++i;
			++j;
		}
		
		return null;
	}
	
	public Tile lookDown(int x, int y, int mov, int p) {
		for (int i = x; i < size && i <= x + mov; ++i) {
			if (i == x + mov && (board[i][y] == null || board[i][y].getPlayer() != p))
				return new Tile(i, y);
			else if (board[i][y] != null && board[i][y].getPlayer() != p)
				return null;
		}
			
		return null;
	}
	
	public Tile lookDownLeft(int x, int y, int mov, int p) {
		int i = x;
		int j = y;
		
		for (int nbMoves = 0; i < size && j >= 0 && nbMoves < mov; ++nbMoves) {
			if (nbMoves == mov && (board[i][j] == null || board[i][j].getPlayer() != p))
				return new Tile(i, j);
			else if (board[i][j] != null && board[i][j].getPlayer() != p)
				return null;
			
			++i;
			--j;
		}
		
		return null;
	}
	
	public Tile lookLeft(int x, int y, int mov, int p) {
		for (int i = y; i >= 0 && i >= y - mov; --i) {
			if (i == y - mov && (board[x][i] == null || board[x][i].getPlayer() != p))
				return new Tile(x, i);
			else if (board[x][i] != null && board[x][i].getPlayer() != p)
				return null;
		}
			
		return null;
	}
	
	public Tile lookUpLeft(int x, int y, int mov, int p) {
		int i = x;
		int j = y;
		
		for (int nbMoves = 0; i >= 0 && j >= 0 && nbMoves < mov; ++nbMoves) {
			if (nbMoves == mov && (board[i][j] == null || board[i][j].getPlayer() != p))
				return new Tile(i, j);
			else if (board[i][j] != null && board[i][j].getPlayer() != p)
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