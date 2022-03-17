package util;

import components.*;

public class Parser {
	private final static String[][] KEY = { 
			{"a8","b8","c8","d8","e8","f8","g8","h8"},
			{"a7","b7","c7","d7","e7","f7","g7","h7"},
			{"a6","b6","c6","d6","e6","f6","g6","h6"},
			{"a5","b5","c5","d5","e5","f5","g5","h5"},
			{"a4","b4","c4","d4","e4","f4","g4","h4"},
			{"a3","b3","c3","d3","e3","f3","g3","h3"},
			{"a2","b2","c2","d2","e2","f2","g2","h2"},
			{"a1","b1","c1","d1","e1","f1","g1","h1"}};
	private final static int BOARDSIZE = 8;
	private static final int WHITE_THRESH = 6;
	
	public final static int PLAYER_1 = 1;
	public final static int PLAYER_2 = -1;
	
	public final static char[][] BLACK = {
			{'R','N','B','Q','K','B','N','R'},
			{'p','p','p','p','p','p','p','p'}};
	
	public final static char[][] WHITE = {
			{'p','p','p','p','p','p','p','p'},
			{'R','N','B','Q','K','B','N','R'}};
	public static final char DEFAULT_CHAR = 'Q';
	public static final char INVALID_CHAR = 'X';
	
	public static String translate(int x, int y) {
		return KEY[x][y];
	}
	public static int[] translate(String position){
		int[] statement = new int[2];
		for(int i = 0; i < BOARDSIZE; i++) {
			for(int j = 0; j < BOARDSIZE; j++) {
				if(KEY[i][j].equals(position)) {
					statement[0] = i;
					statement[1] = j;
					return statement;
				}
			}
		}
		return null;
	}

	public static ChessPiece getNewPiece(char type, ChessPiece old) {
		int[] positions = old.getPosition();
		int i = positions[0];
		int j = positions[1];
		int p = old.playerColor;
		System.out.println("yes, we are here and type is: "+type);
		ChessPiece statement = null;
		switch(type) {
		case 'R':
			statement = new Rook(i,j,type,p);
			break;
		case 'N':
			statement = new Knight(i,j,type,p);
			break;
		case 'B':
			statement = new Bishop(i,j,type,p);
			break;
		case 'Q':
			statement = new Queen(i,j,type,p);
			System.out.println("yes, we made a queen");
			break;
		case 'K':
			statement = new King(i,j,type,p);
			break;
		case 'p':
			statement = new Pawn(i,j,type,p);
			break;
		}

		return statement;
	}
	public static ChessPiece getNewPiece(int i, int j, int p) {
		char type = (p == PLAYER_1)? WHITE[i-WHITE_THRESH][j]:BLACK[i][j];
		ChessPiece statement = null;
		switch(type) {
		case 'R':
			statement = new Rook(i,j,type,p);
			break;
		case 'N':
			statement = new Knight(i,j,type,p);
			break;
		case 'B':
			statement = new Bishop(i,j,type,p);
			break;
		case 'Q':
			statement = new Queen(i,j,type,p);
			break;
		case 'K':
			statement = new King(i,j,type,p);
			break;
		case 'p':
			statement = new Pawn(i,j,type,p);
			break;
		}

		return statement;
	}
	
	public static <T> void printArray(T[] array) {
		for(T value : array) {
			System.out.print(value+" ");
		}
		System.out.println();
	}
	
	public static String[] adjustArraySize(String[] array, int size) {
		String[] statement = new String[size];
		for(int i = 0; i < size; i++) {
			statement[i] = array[i];
		}
		return statement;
	}

	public static boolean validInput(String command) {
		for(int r = 0; r < BOARDSIZE; r++) {
			for(int c = 0; c < BOARDSIZE; c++) {
				if(command.equals(KEY[r][c]))
					return true;
			}
		}
		return false;
	}

	public static boolean validType(String input) {
		if (input.length() == 1) {
			char type = input.charAt(0);
			for(int r = 0; r < BOARDSIZE; r++) {
				for(int c = 0; c < BOARDSIZE; c++) {
					if(type == BLACK[r][c]) {
						return true;
					}
				}
			}
		}

		return false;
	}
	public static boolean canPromoteWhite(int[] position) {
		return position[0] == 0;
	}
	public static boolean canPromoteBlack(int[] position) {
		return position[0] == 7;
	}

	

}
