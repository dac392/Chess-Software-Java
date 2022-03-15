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
	
	private final static int PLAYER_1 = 1;
	private final static int PLAYER_2 = -1;
	
	
	public final static char[][] BLACK = {
			{'R','N','B','Q','K','B','N','R'},
			{'p','p','p','p','p','p','p','p'}};
	
	public final static char[][] WHITE = {
			{'p','p','p','p','p','p','p','p'},
			{'R','N','B','Q','K','B','N','R'}};
	
	public static String translate(int x, int y) {
		return KEY[x][y];
	}

	public static ChessPiece getNewPiece(int i, int j, int p) {
		char type = (p == PLAYER_1)? WHITE[i][j]:BLACK[i][j];
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

}
