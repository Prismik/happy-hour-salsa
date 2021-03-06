package yis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Board {
	private static final int POSITION_MULTIPLIER = 15;
	private static final int CONNECTIVITY_MULTIPLIER = 5;
	private static final int EMPRISONMENT_MULTIPLIER = 5;
	private static final int DISTANCE_MULTIPLIER = 4;
	
	private final int WHITE = 4;
	private final int BLACK = 2;
	private Piece[][] board;
	private int player = 4; // default
	private int size;
	private int whitePieces, blackPieces;
	private ArrayList<Move> whiteMoveset;
	private ArrayList<Move> blackMoveset;
	private int currentTurn;
	
	public Board(int s) {
		size = s;
		board = new Piece[size][size];
		whitePieces = blackPieces = (s - 2) * 2;
		whiteMoveset = new ArrayList<Move>();
		blackMoveset = new ArrayList<Move>();
		currentTurn = 1;
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
		
		this.currentTurn = board.currentTurn;
	}

	public boolean hasWon(int player) {
		if (player == WHITE && whitePieces == 1)
			return true;
		else if (player == BLACK && blackPieces == 1)
			return true;
		else
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					if (board[i][j] != null && adjacentsOfType(i, j, player) == 0)
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
		boolean right = x + 1 < size;
		boolean bottom = y + 1 < size;
		boolean left = x - 1 >= 0;
		if (left) {
			if (board[x-1][y] != null && board[x-1][y].getPlayer() == player)
				count++;

			if (top && board[x-1][y-1] != null && board[x-1][y-1].getPlayer() == player)
				count++;

			if (bottom && board[x-1][y+1] != null && board[x-1][y+1].getPlayer() == player)
				count++;
		}

		if (top)
			if (board[x][y-1] != null && board[x][y-1].getPlayer() == player)
				count++;

		if (bottom)
			if (board[x][y+1] != null && board[x][y+1].getPlayer() == player)
				count++;

		if (right) {
			if (board[x+1][y] != null && board[x+1][y].getPlayer() == player)
				count++;

			if (top && board[x+1][y-1] != null && board[x+1][y-1].getPlayer() == player)
				count++;

			if (bottom && board[x+1][y+1] != null && board[x+1][y+1].getPlayer() == player)
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
		if (board[fromX][fromY].getPlayer() == this.player) ++currentTurn;
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

		//updateLinesOfAction(fromX, fromY, toX, toY);
		updateMovesets();
		//System.out.println(this.toString());
	}

	public void doMove(Tile[] tiles) {
		doMove(tiles[0].getX(), tiles[0].getY(), tiles[1].getX(), tiles[1].getY());
	}

	public void doMove(Move m) {
		Tile from = m.getFrom();
		Tile to = m.getTo();
		doMove(from.getX(), from.getY(), to.getX(), to.getY());
	}

	public void updateLinesOfAction(int x, int y, int toX, int toY) {
		ArrayList<Move> tempWhiteMovesets = new ArrayList<Move>(whiteMoveset);
		ArrayList<Move> tempBlackMovesets = new ArrayList<Move>(blackMoveset);
		Set<Tile> tiles = new HashSet<Tile>();
		tiles.addAll(tilesInLinesOfAction(x, y));
		tiles.addAll(tilesInLinesOfAction(toX, toY));
		for(Tile t : tiles) {
			int curX = t.getX();
			int curY = t.getY();
			for(Move m : tempWhiteMovesets) {
				Tile from = m.getFrom();
				Tile to = m.getTo();
				if (from.getX() == curX && from.getY() == curY || 
						to.getX() 	== curX && from.getY() == curY) {
					whiteMoveset.remove(m);
					whiteMoveset.addAll(updateMovesetOfPiece(curX, curY, WHITE));
				}
			}

			for(Move m : tempBlackMovesets) {
				Tile from = m.getFrom();
				Tile to = m.getTo();
				if (from.getX() == curX && from.getY() == curY || 
						to.getX() 	== curX && from.getY() == curY) {
					blackMoveset.remove(m);
					blackMoveset.addAll(updateMovesetOfPiece(curX, curY, BLACK));
				}
			}
		}
	}

	public Set<Tile> tilesInLinesOfAction(int x, int y) {
		Set<Tile> tiles = new HashSet<Tile>();
		tiles.addAll(verticalPiecesFrom(x, y));
		tiles.addAll(horizontalPiecesFrom(x, y));
		tiles.addAll(diagBotLeftPiecesFrom(x, y));
		tiles.addAll(diagTopLeftPiecesFrom(x, y));

		return tiles;
	}

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
	
	public int evaluate() {
		int value = 0;
		
		if (hasWon(this.player))
			value = Integer.MAX_VALUE;
		else if (hasWon(getOpponent()))
			value = Integer.MIN_VALUE;
		else {
			for (int i = 0; i < size; ++i) {
				for (int j = 0; j < size; ++j) {
					if (board[i][j] != null && board[i][j].getPlayer() == player)
						value += evaluatePosition(i, j, player);
					else if (board[i][j] != null && board[i][j].getPlayer() != player)
						value -= evaluatePosition(i, j, getOpponent());
				}
			}
		}
		
		return value;
	}
	
	public int evaluatePosition(int currentX, int currentY, int player) {
		int currentPosVal = 0;
		if ((currentX == 0 || currentX == 7) &&	(currentY == 0 || currentY == 7)) {
			currentPosVal = -5;
		} else if ((currentX == 0 || currentX == 7) || (currentY == 0 || currentY == 7)) {
			currentPosVal = -3;
		}
		
		currentPosVal *= POSITION_MULTIPLIER * (currentTurn * 0.1);
	
		int currentAdjVal = CONNECTIVITY_MULTIPLIER * adjacentsOfType(currentX, currentY, player) * (int)(currentTurn * 0.3);
		
		int currentEmprisonmentVal = EMPRISONMENT_MULTIPLIER * (int)(currentTurn * 0.3)
				* adjacentsOfType(currentX, currentY, (player == BLACK)? WHITE: BLACK);
		
		
		// Find nearest piece of the same player

		int bestDistance = getBestDistance(currentX, currentY, player) * DISTANCE_MULTIPLIER * (int)(currentTurn * 0.3);

		return currentPosVal + currentAdjVal - bestDistance - currentEmprisonmentVal;
	}
	
	public int getBestDistance(int currentX, int currentY, int player) {
		int bestDistanceTopLeft = 0, bestDistanceTop = 0, bestDistanceTopRight = 0,
				bestDistanceLeft = 0, bestDistanceRight = 0,
				bestDistanceBottomLeft = 0, bestDistanceBottom = 0, bestDistanceBottomRight = 0;
		boolean top = true, right = true, bottom = true,
			left = true, topLeft = true, topRight = true,
			bottomRight = true, bottomLeft = true;
		
		for (int i = 0; i < size; ++i) {
			if (top) top = currentY - i >= 0;
			if (right) right = currentX + i < size;
			if (bottom) bottom = currentY + i < size;
			if (left) left = currentX - i >= 0;
			if (topLeft) topLeft = top && left;
			if (topRight) topRight = top && right;
			if (bottomRight) bottomRight = bottom && right;
			if (bottomLeft) bottomLeft = bottom && left;
			
			// haut gauche
			if (topLeft) {
					if (board[currentX - i][currentY - i] == null 
						 || board[currentX - i][currentY - i].getPlayer() != player)
						++bestDistanceTopLeft;
					else
						topLeft = false;
			}
			
			// haut
			if (top) {
				if (board[currentX][currentY - i] == null 
					 || board[currentX][currentY - i].getPlayer() != player)
					++bestDistanceTop;
				else
					top = false;
		}
			
			// haut droite
			if (topRight) {
				if (board[currentX + i][currentY - i] == null 
					 || board[currentX + i][currentY - i].getPlayer() != player)
					++bestDistanceTopRight;
				else
					topRight = false;
		}
			
			// droite
			if (right) {
				if (board[currentX + i][currentY] == null 
					 || board[currentX + i][currentY].getPlayer() != player)
					++bestDistanceRight;
				else
					right = false;
		}
			
			// bas droite
			if (bottomRight) {
				if (board[currentX + i][currentY + i] == null 
					 || board[currentX + i][currentY + i].getPlayer() != player)
					++bestDistanceBottomRight;
				else
					bottomRight = false;
		}
			
			// bas
			if (bottom) {
				if (board[currentX][currentY + i] == null 
						 || board[currentX][currentY + i].getPlayer() != player)
						++bestDistanceBottom;
					else
						bottom = false;
			}
			
			// bas gauche
			if (bottomLeft) {
				if (board[currentX - i][currentY + i] == null 
					 || board[currentX - i][currentY + i].getPlayer() != player)
					++bestDistanceBottomLeft;
				else
					bottomLeft = false;
			}
			
			// gauche
			if (left) {
				if (board[currentX - i][currentY] == null 
					 || board[currentX - i][currentY].getPlayer() != player)
					++bestDistanceLeft;
				else
					left = false;
			}
		}

		// ajustement d'importance
		int bestDistance = Math.min(bestDistanceTopLeft, bestDistanceTop);
		bestDistance = Math.min(bestDistance, bestDistanceTopRight);
		bestDistance = Math.min(bestDistance, bestDistanceRight);
		bestDistance = Math.min(bestDistance, bestDistanceBottomRight);
		bestDistance = Math.min(bestDistance, bestDistanceBottom);
		bestDistance = Math.min(bestDistance, bestDistanceBottomLeft);
		bestDistance = Math.min(bestDistance, bestDistanceLeft);
		
		return bestDistance;
	}
	
	private int countVerticalPieces(int x, int y) { return verticalPiecesFrom(x, y).size(); }
	private Set<Tile> verticalPiecesFrom(int x, int y) {
		Set<Tile> tiles = new HashSet<Tile>();
		for (int i = 0; i < size; i++)
			if (board[i][y] != null)
				tiles.add(new Tile(i, y));

		return tiles;
	}

	private int countHorizontalPieces(int x, int y) { return horizontalPiecesFrom(x, y).size(); }
	private Set<Tile> horizontalPiecesFrom(int x, int y) {
		Set<Tile> tiles = new HashSet<Tile>();
		for (int i = 0; i < size; i++)
			if (board[x][i] != null)
				tiles.add(new Tile(x, i));

		return tiles;
	}

	private int countDiagonalBottomLeftPieces(int x, int y) { return diagBotLeftPiecesFrom(x, y).size(); }
	private Set<Tile> diagBotLeftPiecesFrom(int x, int y) {
		Set<Tile> tiles = new HashSet<Tile>();
		tiles.add(new Tile(x, y));
		int i = x + 1; // ligne x
		int j = y - 1; // colonnes y
		
		// left
		while (i < size && j >= 0) {
			if (board[i][j] != null) 
				tiles.add(new Tile(i, j));
			
			++i;
			--j;
		}
		
		i = x - 1;
		j = y + 1;
		
		// right
		while (i >= 0 && j < size) {
			if (board[i][j] != null) 
				tiles.add(new Tile(i, j));
			
			--i;
			++j;
		}
		
		return tiles;
	}

	// à tester
	private int countDiagonalTopLeftPieces(int x, int y) { return diagTopLeftPiecesFrom(x, y).size(); }
	private Set<Tile> diagTopLeftPiecesFrom(int x, int y) {
		Set<Tile> tiles = new HashSet<Tile>();
		tiles.add(new Tile(x, y));
		int i = x - 1; // ligne x
		int j = y - 1; // colonnes y
		
		// left
		while (i >= 0 && j >= 0) {
			if (board[i][j] != null) 
				tiles.add(new Tile(i, j));
			
			--i;
			--j;
		}
		
		i = x + 1;
		j = y + 1;
		
		// right
		while (i < size && j < size) {
			if (board[i][j] != null) 
				tiles.add(new Tile(i, j));
			
			++i;
			++j;
		}
		
		return tiles;
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