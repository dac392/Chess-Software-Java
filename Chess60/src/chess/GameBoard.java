package chess;

import java.util.Arrays;
import java.util.HashMap;

import components.*;
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
	private static final int ERROR = 0;
	private static final int CORRECT = 1;
	
	public GameBoard() {
		this.player = 1;
		this.gameBoard = new String[8][8];
		this.white = new HashMap<>();
		this.black = new HashMap<>();
		initGameboard();
		
	}
	
	public int getPlayer() {
		return player;
	}
	
	private void initGameboard() {
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				if(i < BLACK_THRESH) {
					ChessPiece blackpiece = Parser.getNewPiece(i,j,PLAYER_2);
					this.gameBoard[i][j] = blackpiece.toString();
					this.black.put(blackpiece.stringPosition(), blackpiece);
				}else if(i >= WHITE_THRESH) {
					ChessPiece whitepiece = Parser.getNewPiece(i, j, PLAYER_1);
					this.gameBoard[i][j] = whitepiece.toString();
					this.white.put(whitepiece.stringPosition(), whitepiece);
				}else {
					this.gameBoard[i][j] = "";
				}
				
			}
		}
	}
		
	public void printGameBoard() {
		
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
		System.out.println();
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

	
	public int blackMoveManager(String in) {	// if you make a move, you should update player*=-1
		HashMap<String, ChessPiece> allyTeam = this.black;
		HashMap<String, ChessPiece> enemyTeam = this.white;
		if(regularMove(in, allyTeam, enemyTeam))
			return CORRECT;
		
		return ERROR;
	}
	
	public int whiteMoveManager(String in) {	// if you make a move, you should update player*=-1
		HashMap<String, ChessPiece> allyTeam = this.white;
		HashMap<String, ChessPiece> enemyTeam = this.black;
		if(regularMove(in, allyTeam, enemyTeam))
			return CORRECT;
		
		return ERROR;
	}
	public boolean regularMove(String in, HashMap<String, ChessPiece> allyTeam, HashMap<String, ChessPiece> enemyTeam) {
		String[] inputs = in.split(" ");
		String initial = inputs[0];
		String end = inputs[1];
//		System.out.println("allyTeam contains "+initial+" : "+allyTeam.containsKey(initial));
//		System.out.println(allyTeam.keySet());
		if(allyTeam.containsKey(initial)) {
			ChessPiece p = allyTeam.get(initial);
//			System.out.println(!this.emptySpot(end)); 
//			System.out.println(enemyTeam.containsKey(end));
			if(!this.emptySpot(end) && enemyTeam.containsKey(end)) {
				System.out.println("Check");
				if(p.canCapture(end, this.gameBoard)) {
					this.move(p,end, inputs, allyTeam);
					enemyTeam.remove(end);
					this.player*=-1;
					return true;
				}
			}else if(this.emptySpot(end)) {
				if(p.canMoveTo(end, this.gameBoard)) {
					this.move(p,end, inputs, allyTeam);
					this.player*=-1;
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	private void move(ChessPiece p, String target, String[] inputs, HashMap<String, ChessPiece> allyTeam) {
		int[] origin = p.getPosition();
		int[] dest = Parser.translate(target);
		this.gameBoard[origin[0]][origin[1]] = "";
		this.gameBoard[dest[0]][dest[1]] = p.toString();
		p.updatePosition(dest);
//		System.out.println("this should have changed");
//		System.out.println(java.util.Arrays.toString(p.getPosition()));
		allyTeam.remove(target);	//hm shouldn't this be initial?
		allyTeam.put(target, p);
		if(p instanceof Pawn) {
			char promotionType = ((Pawn)p).wasMoved(inputs);
			if(promotionType != Parser.INVALID_CHAR) {
				allyTeam.remove(target);
				ChessPiece promoted = Parser.getNewPiece(promotionType, promotionType, promotionType);
				allyTeam.put(target, promoted);
			}
		}
	}

	private boolean emptySpot(String end) {
		int[] position = Parser.translate(end);
		if(this.gameBoard[position[0]][position[1]].isBlank()) {
			return true;
		}
		return false;
	}

	
}
