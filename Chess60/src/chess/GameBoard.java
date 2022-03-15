package chess;

import java.util.Arrays;
import java.util.HashMap;

import components.ChessPiece;
import util.Parser;

public class GameBoard {
	private HashMap<String, ChessPiece> white;
	private HashMap<String, ChessPiece> black;
	private String gameBoard [][];
	private int player;
	
	private static final int BOARD_SIZE = 8;
	private static final int BLACK_THRESH = 2;
	private static final int WHITE_THRESH = 6;
	private static final int PLAYER_1 = 1;
	private static final int PLAYER_2 = -1;
	
	public GameBoard() {
		this.player = 1;
		this.gameBoard = new String[8][8];
		this.white = new HashMap<>();
		this.black = new HashMap<>();
		initGameboard();
		
	}
	
	
	private void initGameboard() {
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				if(i < BLACK_THRESH) {
					ChessPiece blackpiece = Parser.getNewPiece(i,j,-1);
					this.gameBoard[i][j] = blackpiece.toString();
					this.black.put(blackpiece.stringPosition(), blackpiece);
				}else if(i >= WHITE_THRESH) {
					ChessPiece whitepiece = Parser.getNewPiece(i-WHITE_THRESH, j, 1);
					this.gameBoard[i][j] = whitepiece.toString();
					this.white.put(whitepiece.stringPosition(), whitepiece);
				}else {
					this.gameBoard[i][j] = "";
				}
				
			}
		}
	}
	
	public void printGameBoard() {
		String message = (player == PLAYER_1)? "\nWhite's move:":"\nBlack's move:";
		System.out.println();
		int alternate = 1;
		int numbers = 8;
		String letters[ ]= {" a", "  b", "  c", "  d", "  e", "  f", "  g", "  h\n"};
		for(String[] row : this.gameBoard) {
			for(String square : row) {
				if(!square.isBlank()) {
					System.out.print(square);
				}else if(alternate == 1){
					System.out.print("   ");
				}else {
					System.out.print("## ");
				}
				alternate*= -1;
			}
			
			alternate*=-1;
			System.out.println(numbers);
			numbers--;
		}
		for(String letter : letters) {
			System.out.print(letter);
		}
		System.out.println(message);
	}

	public void printHashMaps() {
		System.out.println("\nBLACK MAP");
		for(String key : black.keySet()) {
			String value = black.get(key).getName();
			System.out.println(key+" "+value);
		}
		System.out.println("\nWHITE MAP");
		for(String key : white.keySet()) {
			String value = white.get(key).getName();
			System.out.println(key+" "+value);
		}
	}
}
