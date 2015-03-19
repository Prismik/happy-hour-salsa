package yis;

import java.io.*;
import java.net.*;

class Client {
	private static Board board;
	private static Socket sock;
	private static BufferedInputStream in;
	private static BufferedOutputStream out;

	private static BufferedReader console;  
	private static void handleBeginMsg(int p) throws IOException {
		byte[] buffer = new byte[1024];
		int size = in.available();
		//System.out.println("size " + size);
		in.read(buffer, 0, size);
		String s = new String(buffer).trim();
		String[] boardValues;
		boardValues = s.split(" ");
		int x = 0,y = 0;
		for(int i= 0; i < boardValues.length; i++) {
			board.set(x, y, Integer.parseInt(boardValues[i]));
			x++;
			if(x == 8) {
				x = 0;
				y++;
			}
		}
		
		board.init(p);
	}
	
	public static Tile[] parseMsg(String msg) {
		String[] pos = msg.trim().split(" - ");
		int fromX = Character.getNumericValue(pos[0].charAt(0)) - 10;
		int fromY = Character.getNumericValue(pos[0].charAt(1)) - 1;
		int toX = Character.getNumericValue(pos[1].charAt(0)) - 10;
		int toY = Character.getNumericValue(pos[1].charAt(1)) - 1;

        Tile[] tiles = new Tile[2];
        tiles[0] = new Tile(fromX, fromY);
        tiles[1] = new Tile(toX, toY);
        return tiles;
	}

	// We generate the move list and select
	// the best possible move in here
	private static void play() {
		try {
			String move = null;
			move = board.getNextMove();
            System.out.println("Next move is: " + move);
			out.write(move.getBytes(), 0, move.length());
			out.flush();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private static void buildMiniMaxTree() {
		
	}

	public static void main(String[] args) {
		try {
			board = new Board(8);
			sock = new Socket("localhost", 8888);
			in = new BufferedInputStream(sock.getInputStream());
			out = new BufferedOutputStream(sock.getOutputStream());
			console = new BufferedReader(new InputStreamReader(System.in));

			while(true) {
				char cmd = 0;
				cmd = (char)in.read();
				
				// Début de la partie en joueur blanc
				if(cmd == '1') {
					handleBeginMsg(4);
					System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
					play();
					System.out.println(board.toString());
				}

				// Début de la partie en joueur Noir
				if(cmd == '2') {
					System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
					handleBeginMsg(2);
				}

				// Le serveur demande le prochain coup
				// Le message contient aussi le dernier coup joué.
				if(cmd == '3') {
					byte[] buffer = new byte[16];
					int size = in.available();
					//System.out.println("size " + size);
					in.read(buffer, 0, size);
					
					String s = new String(buffer);
					System.out.println("Dernier coup : " + s);
					board.move(parseMsg(s));

					System.out.println("Entrez votre coup : ");
					play();
				}

				// Le dernier coup est invalide
				if(cmd == '4') {
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					play();
				}
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}

