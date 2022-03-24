package chess;

import java.util.Arrays;
import java.util.HashMap;

import components.*;
import util.Parser;
/**
 * GameBoard class used as the game board of the chess object. Handles logic regarding the chess board.
 * @author DiegoCastellanos dac392
 * @author AbidAzad aa2177
 */
public class GameBoard {
	private HashMap<String, ChessPiece> white;
	private HashMap<String, ChessPiece> black;
	private String gameBoard [][];
	private int player;
	int[] whiteKingPosition;
	int[] blackKingPosition;
	int ATTACK;
	ChessPiece contester;
	String whitePosition;
	String blackPosition;
	private static final int BOARD_SIZE = 8;
	private static final int BLACK_THRESH = 2;
	private static final int WHITE_THRESH = 6;
	private static final int PLAYER_1 = 1;
	private static final int PLAYER_2 = -1;
	private static final int ERROR = 0;
	private static final int CORRECT = 1;
	
	/**
	 * takes no arguments, and initiates a chess board along with the correct initial pieces.
	 * the game board either contain and empty string "", or the a piece in the form Color and Type "bK"
	 */
	public GameBoard() {
		this.player = 1;
		this.gameBoard = new String[8][8];
		this.white = new HashMap<>();
		this.black = new HashMap<>();
		initGameboard();
		
	}
	/**
	 * returns integer as the player number
	 * @return player as an integer 1 for PLAYER_1 and -1 for PLAYER_2
	 */
	public int getPlayer() {
		return player;
	}
	/**
	 * handles the main game board settup by:
	 * 1. populating the gamboard with either the piece name of an empty string
	 * 2. initializing all piece and populating black and white team hashmaps
	 * 
	 */
	private void initGameboard() {
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				if(i < BLACK_THRESH) {
					ChessPiece blackpiece = Parser.getNewPiece(i,j,PLAYER_2);
					this.gameBoard[i][j] = blackpiece.toString();
					this.black.put(blackpiece.stringPosition(), blackpiece);
					if(blackpiece.name.equals("bK")) {
						blackKingPosition = blackpiece.getPosition();
						blackPosition = blackpiece.stringPosition();
						
					}
				}else if(i >= WHITE_THRESH) {
					ChessPiece whitepiece = Parser.getNewPiece(i, j, PLAYER_1);
					this.gameBoard[i][j] = whitepiece.toString();
					this.white.put(whitepiece.stringPosition(), whitepiece);
					if(whitepiece.name.equals("wK")) {
						whiteKingPosition = whitepiece.getPosition();
						whitePosition = whitepiece.stringPosition();
					}
				}else {
					this.gameBoard[i][j] = "";
				}
				
			}
		}
	}
	
	/**
	 * prints the gameboard to the console and decides the correct pattern for all empty spaces.
	 */
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
	/**
	 * debuging statement used for print black and white team pieces
	 */
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

	/**
	 * driver for black team moves
	 * @param instruction string
	 * @return valid move status; 0 for ERROR and 1 for CORRECT
	 */
	public int blackMoveManager(String in) {	// if you make a move, you should update player*=-1
		HashMap<String, ChessPiece> allyTeam = this.black;
		HashMap<String, ChessPiece> enemyTeam = this.white;
		if(regularMove(in, allyTeam, enemyTeam))
			return CORRECT;
		
		return ERROR;
	}
	/**
	 * driver for white team moves
	 * @param instruction string
	 * @return valid move status; 0 for ERROR and 1 for CORRECT
	 */
	public int whiteMoveManager(String in) {	// if you make a move, you should update player*=-1
		HashMap<String, ChessPiece> allyTeam = this.white;
		HashMap<String, ChessPiece> enemyTeam = this.black;
		if(regularMove(in, allyTeam, enemyTeam))
			return CORRECT;
		
		return ERROR;
	}
	/**
	 * called by both whiteMoveManager and blackMoveManager, handles all actual logic for deciding moves.
	 * @param input string
	 * @param allyTeam hashmap pieces
	 * @param enemyTeam hashmap pieces
	 * @return boolean denoting the semantics of the input and the validity of a move
	 */
	public boolean regularMove(String in, HashMap<String, ChessPiece> allyTeam, HashMap<String, ChessPiece> enemyTeam) {
		String[] inputs = in.split(" ");
		String initial = inputs[0];
		String end = inputs[1];
//		System.out.println("allyTeam contains "+initial+" : "+allyTeam.containsKey(initial));
//		System.out.println(allyTeam.keySet());
		String letters[ ]= {"a", "b", "c", "d", "e", "f", "g", "h"};
		boolean isItPawn = false;
		ChessPiece originalPawn = null;
		
		
		
		if(allyTeam.containsKey(initial)) {
			
			ChessPiece p = allyTeam.get(initial);
			//CASTLING
			if( p instanceof King && ((King)p).hasntMoved){
				if(player == PLAYER_1 && (end.contains("c1")|| end.contains("g1"))) {
					if(allyTeam.containsKey("a1")) {
						
						ChessPiece r = allyTeam.get("a1");
						
						if(r instanceof Rook && ((Rook)r).hasntMoved) {
							
							if(end.contains("c1") && this.emptySpot("b1") && this.emptySpot("c1") && this.emptySpot("d1") && !whiteKingCheck()) {
								String in2 = "a1 d1";
								String[] inputs2 = in2.split(" ");
								this.move(p, initial, end, inputs, allyTeam);
								this.move(r, "a1", "d1", inputs2, allyTeam);
								
								if(whiteKingCheck()) {
									this.move(p, end, initial, inputs, allyTeam);
									this.move(r, "d1", "a1", inputs2, allyTeam);
									return false;
								}
								((King)p).moved();
								((Rook)r).moved();
								this.player*=-1;
								if(p.name.equals("bK"))
								{
									blackKingPosition = p.getPosition();
									blackPosition = p.stringPosition();
								}
								if(p.name.equals("wK"))
								{
									whiteKingPosition = p.getPosition();
									whitePosition = p.stringPosition();
								}
								return true;
							}
						}
					}
					
					if(allyTeam.containsKey("h1")) {
						
						ChessPiece r = allyTeam.get("h1");
						
						if(r instanceof Rook && ((Rook)r).hasntMoved) {
							
							if(end.contains("g1") && this.emptySpot("f1") && this.emptySpot("g1") &&  !whiteKingCheck()) {
								String in2 = "h1 f1";
								String[] inputs2 = in2.split(" ");
								this.move(p, initial, end, inputs, allyTeam);
								this.move(r, "h1", "f1", inputs2, allyTeam);
								
								if(whiteKingCheck()) {
									this.move(p, end, initial, inputs, allyTeam);
									this.move(r, "f1", "h1", inputs2, allyTeam);
									return false;
								}
								((King)p).moved();
								((Rook)r).moved();
								this.player*=-1;
								if(p.name.equals("bK"))
								{
									blackKingPosition = p.getPosition();
									blackPosition = p.stringPosition();
								}
								if(p.name.equals("wK"))
								{
									whiteKingPosition = p.getPosition();
									whitePosition = p.stringPosition();
								}
								return true;
							}
						}
					}					
				}
				if(player == PLAYER_2 && (end.contains("c8")|| end.contains("g8"))) {
					if(allyTeam.containsKey("a8")) {
						
						ChessPiece r = allyTeam.get("a8");
						
						if(r instanceof Rook && ((Rook)r).hasntMoved) {
							
							if(end.contains("c8") && this.emptySpot("b8") && this.emptySpot("c8") && this.emptySpot("d8") && !whiteKingCheck()) {
								String in2 = "a8 d8";
								String[] inputs2 = in2.split(" ");
								this.move(p, initial, end, inputs, allyTeam);
								this.move(r, "a8", "d8", inputs2, allyTeam);
								
								if(whiteKingCheck()) {
									this.move(p, end, initial, inputs, allyTeam);
									this.move(r, "d8", "a8", inputs2, allyTeam);
									return false;
								}
								((King)p).moved();
								((Rook)r).moved();
								this.player*=-1;
								if(p.name.equals("bK"))
								{
									blackKingPosition = p.getPosition();
									blackPosition = p.stringPosition();
								}
								if(p.name.equals("wK"))
								{
									whiteKingPosition = p.getPosition();
									whitePosition = p.stringPosition();
								}
								return true;
							}
						}
					}
					
					if(allyTeam.containsKey("h8")) {
						
						ChessPiece r = allyTeam.get("h8");
						
						if(r instanceof Rook && ((Rook)r).hasntMoved) {
							
							if(end.contains("g8") && this.emptySpot("f8") && this.emptySpot("g8") &&  !whiteKingCheck()) {
								String in2 = "h8 f8";
								String[] inputs2 = in2.split(" ");
								this.move(p, initial, end, inputs, allyTeam);
								this.move(r, "h8", "f8", inputs2, allyTeam);
								
								if(whiteKingCheck()) {
									this.move(p, end, initial, inputs, allyTeam);
									this.move(r, "f1", "h1", inputs2, allyTeam);
									return false;
								}
								((King)p).moved();
								((Rook)r).moved();
								this.player*=-1;
								if(p.name.equals("bK"))
								{
									blackKingPosition = p.getPosition();
									blackPosition = p.stringPosition();
								}
								if(p.name.equals("wK"))
								{
									whiteKingPosition = p.getPosition();
									whitePosition = p.stringPosition();
								}
								return true;
							}
						}
					}					
				}				
			}

			if(p instanceof Pawn) {
				((Pawn) p).revert();
				
				isItPawn = true;
				int number = 1;
				if(p.getName().charAt(0) == 'b')
					number = -1;
				originalPawn = new ChessPiece(p.getPosition()[0], p.getPosition()[1], p.getName().charAt(1),number, p.getTotalMoves()); 
			}
			
			//PASSANT
			if(p instanceof Pawn) {
				if(player == PLAYER_1 && initial.contains("5")) {
					int[] start = Parser.translate(initial);
					int[] location = Parser.translate(end);
					
					if(location[0] == (start[0]-1) && (location[1] == (start[1]-1) || location[1] == (start[1]+1)) ) {
						if((start[1] -1 ) >= 0) {
							String condition = letters[start[1]-1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.player *=-1;
										return true;
										
									}
								}
							}
						}
						if((start[1] +1 ) <= 7) {
							String condition = letters[start[1]+1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.player *=-1;
										return true;
										
									}
								}
							}
						}
					}
				}
				
				if(player == PLAYER_2 && initial.contains("4")) {
					int[] start = Parser.translate(initial);
					int[] location = Parser.translate(end);
					
					if(location[0] == (start[0]+1) && (location[1] == (start[1]-1) || location[1] == (start[1]+1)) ) {
						if((start[1] -1 ) >= 0) {
							String condition = letters[start[1]-1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.player *=-1;
										return true;
										
									}
								}
							}
						}
						if((start[1] +1 ) <= 7) {
							String condition = letters[start[1]+1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.player *=-1;
										return true;
										
									}
								}
							}
						}
					}
				}
			}
//			System.out.println(!this.emptySpot(end)); 
//			System.out.println(enemyTeam.containsKey(end));
			if(!this.emptySpot(end) && enemyTeam.containsKey(end)) {
				if(p.canCapture(end, this.gameBoard)) {
					this.move(p,initial, end, inputs, allyTeam);
					ChessPiece removed = enemyTeam.get(end);
					enemyTeam.remove(end);
					
					if(p.name.equals("bK"))
					{
						blackKingPosition = p.getPosition();
						blackPosition = p.stringPosition();
					}
					if(p.name.equals("wK"))
					{
						whiteKingPosition = p.getPosition();
						whitePosition = p.stringPosition();
					}
					
					if(player == PLAYER_1 && whiteKingCheck()) {         //These two methods will probably screw up with the pawn promotion....
						this.move(p, end,initial, inputs, allyTeam);
						enemyTeam.put(end, removed);
						int[] dest= removed.getPosition();
						this.gameBoard[dest[0]][dest[1]] = removed.toString();
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK"))
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					
					if(player == PLAYER_2 && blackKingCheck()) {	
						this.move(p, end,initial, inputs, allyTeam);
						enemyTeam.put(end, removed);
						int[] dest= removed.getPosition();
						this.gameBoard[dest[0]][dest[1]] = removed.toString();
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK"))
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					this.player*=-1;
					if(p instanceof Pawn)
						((Pawn)allyTeam.get(end)).firstMoveDone = true;
					if(p instanceof King)
						((King)p).moved();
					if(p instanceof Rook)
						((Rook)p).moved();
					return true;
				}
			}else if(this.emptySpot(end)) {
				if(p.canMoveTo(end, this.gameBoard)) {
					this.move(p,initial, end, inputs, allyTeam);
					
					if(p.name.equals("bK"))
					{
						blackKingPosition = p.getPosition();
						blackPosition = p.stringPosition();
					}
					if(p.name.equals("wK")) 
					{
						whiteKingPosition = p.getPosition();
						whitePosition = p.stringPosition();
					}

					if(player == PLAYER_1 && whiteKingCheck())
					{
						this.move(p, end,initial, inputs, allyTeam);
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK")) 
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					
					if(player == PLAYER_2 && blackKingCheck()) {
						this.move(p, end,initial, inputs, allyTeam);
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK")) 
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					
					this.player*=-1;
					if(p instanceof Pawn)
						((Pawn)allyTeam.get(end)).firstMoveDone = true;
					if(p instanceof King)
						((King)p).moved();
					if(p instanceof Rook)
						((Rook)p).moved();
					for(String key : enemyTeam.keySet()) {
						ChessPiece update = enemyTeam.get(key);
						if(update instanceof Pawn)
							((Pawn)update).passantPassed();
					}
					return true;
				}
			}
			
		}
		
		return false;
	}

	/**
	 * 
	 * @param in
	 * @param safe2
	 * @param safe
	 * @return boolean deciding whether a move is safe to be made
	 */
	public boolean safeMove(String in, HashMap<String, ChessPiece> safe2, HashMap<String, ChessPiece> safe) { //Theoretically should perform a move and check if the king is safe. Regardless, should return board back to what it was before returning.
		String[] inputs = in.split(" ");
		String initial = inputs[0];
		String end = inputs[1];
		HashMap<String, ChessPiece> allyTeam = new HashMap<>();
		allyTeam.putAll(safe2);
		HashMap<String, ChessPiece> enemyTeam = new HashMap<>();
		enemyTeam.putAll(safe);
		boolean isItPawn = false;
		String letters[ ]= {"a", "b", "c", "d", "e", "f", "g", "h"};
		ChessPiece originalPawn = null;
//		System.out.println("allyTeam contains "+initial+" : "+allyTeam.containsKey(initial));
//		System.out.println(allyTeam.keySet());
		if(!this.emptySpot(end) && allyTeam.containsKey(end))
			return false;
		if(allyTeam.containsKey(initial)) {
			ChessPiece p = allyTeam.get(initial);
			if(p instanceof Pawn) {
				((Pawn)p).revert();
				isItPawn = true;
				int number = 1;
				if(p.getName().charAt(0) == 'b')
					number = -1;
				originalPawn = new ChessPiece(p.getPosition()[0], p.getPosition()[1], p.getName().charAt(1),number, p.getTotalMoves()); 
			}
			if(p instanceof Pawn) {
				if(player == PLAYER_1 && initial.contains("5")) {
					int[] start = Parser.translate(initial);
					int[] location = Parser.translate(end);
					
					if(location[0] == (start[0]-1) && (location[1] == (start[1]-1) || location[1] == (start[1]+1)) ) {
						if((start[1] -1 ) >= 0) {
							String condition = letters[start[1]-1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.move(p,end, initial, inputs, allyTeam);
										enemyTeam.put(condition, r);
										this.gameBoard[dest[0]][dest[1]] = r.toString();
										if(isItPawn && originalPawn != null) {
											allyTeam.replace(initial, originalPawn);
										}
										
									}
								}
							}
						}
						if((start[1] +1 ) <= 7) {
							String condition = letters[start[1]+1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.move(p,end, initial, inputs, allyTeam);
										enemyTeam.put(condition, r);
										this.gameBoard[dest[0]][dest[1]] = r.toString();
										if(isItPawn && originalPawn != null) {
											allyTeam.replace(initial, originalPawn);
										}
										
									}
								}
							}
						}
					}
				}
				
				if(player == PLAYER_2 && initial.contains("4")) {
					int[] start = Parser.translate(initial);
					int[] location = Parser.translate(end);
					
					if(location[0] == (start[0]+1) && (location[1] == (start[1]-1) || location[1] == (start[1]+1)) ) {
						if((start[1] -1 ) >= 0) {
							String condition = letters[start[1]-1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.move(p,end, initial, inputs, allyTeam);
										enemyTeam.put(condition, r);
										this.gameBoard[dest[0]][dest[1]] = r.toString();
										if(isItPawn && originalPawn != null) {
											allyTeam.replace(initial, originalPawn);
										}
										
									}
								}
							}
						}
						if((start[1] +1 ) <= 7) {
							String condition = letters[start[1]+1] +(8- (start[0]));
							if(enemyTeam.containsKey(condition)) {
								ChessPiece r = enemyTeam.get(condition);
								if(r instanceof Pawn) {
									if(((Pawn)r).firstMoveDone &&((Pawn)r).passantable && this.emptySpot(end)) {
										this.move(p,initial, end, inputs, allyTeam);
										int[] dest= r.getPosition();
										this.gameBoard[dest[0]][dest[1]] = "";
										enemyTeam.remove(condition);
										if(whiteKingCheck()) {
											this.move(p,end, initial, inputs, allyTeam);
											enemyTeam.put(condition, r);
											this.gameBoard[dest[0]][dest[1]] = r.toString();
											if(isItPawn && originalPawn != null) {
												allyTeam.replace(initial, originalPawn);
											}
											return false;
										}
										this.move(p,end, initial, inputs, allyTeam);
										enemyTeam.put(condition, r);
										this.gameBoard[dest[0]][dest[1]] = r.toString();
										if(isItPawn && originalPawn != null) {
											allyTeam.replace(initial, originalPawn);
										}
										
									}
								}
							}
						}
					}
				}
			}			
//			System.out.println(!this.emptySpot(end)); 
			if(!this.emptySpot(end) && enemyTeam.containsKey(end)) {
				if(p.canCapture(end, this.gameBoard)) {
					this.move(p,initial, end, inputs, allyTeam);
					ChessPiece removed = enemyTeam.get(end);
					enemyTeam.remove(end);
					
					if(p.name.equals("bK"))
					{
						blackKingPosition = p.getPosition();
						blackPosition = p.stringPosition();
					}
					if(p.name.equals("wK"))
					{
						whiteKingPosition = p.getPosition();
						whitePosition = p.stringPosition();
					}
					
					if(player == PLAYER_1 && whiteKingCheck()) {         //These two methods will probably screw up with the pawn promotion....
						this.move(p, end,initial, inputs, allyTeam);
						enemyTeam.put(end, removed);
						int[] dest= removed.getPosition();
						this.gameBoard[dest[0]][dest[1]] = removed.toString();
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK"))
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					
					if(player == PLAYER_2 && blackKingCheck()) {	
						this.move(p, end,initial, inputs, allyTeam);
						enemyTeam.put(end, removed);
						int[] dest= removed.getPosition();
						this.gameBoard[dest[0]][dest[1]] = removed.toString();
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK"))
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					this.move(p, end,initial, inputs, allyTeam);
					enemyTeam.put(end, removed);
					int[] dest= removed.getPosition();
					this.gameBoard[dest[0]][dest[1]] = removed.toString();
					if(isItPawn && originalPawn != null) {
						allyTeam.replace(initial, originalPawn);
					}
					
					if(p.name.equals("bK"))
					{
						blackKingPosition = p.getPosition();
						blackPosition = p.stringPosition();
					}
					if(p.name.equals("wK"))
					{
						whiteKingPosition = p.getPosition();
						whitePosition = p.stringPosition();
					}
						
				}
				else
					return false;
			}else if(this.emptySpot(end)) {
				if(p.canMoveTo(end, this.gameBoard)) {
					this.move(p,initial, end, inputs, allyTeam);

					if(p.name.equals("bK"))
					{
						blackKingPosition = p.getPosition();
						blackPosition = p.stringPosition();
					}
					if(p.name.equals("wK")) 
					{
						whiteKingPosition = p.getPosition();
						whitePosition = p.stringPosition();
					}

					if(player == PLAYER_1 && whiteKingCheck())
					{
						this.move(p, end,initial, inputs, allyTeam);
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
					
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK"))
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					
					if(player == PLAYER_2 && blackKingCheck()) {
						this.move(p, end,initial, inputs, allyTeam);
						if(isItPawn && originalPawn != null) {
							allyTeam.replace(initial, originalPawn);
						}
						if(p.name.equals("bK"))
						{
							blackKingPosition = p.getPosition();
							blackPosition = p.stringPosition();
						}
						if(p.name.equals("wK"))
						{
							whiteKingPosition = p.getPosition();
							whitePosition = p.stringPosition();
						}
						return false;
					}
					this.move(p,end, initial, inputs, allyTeam);
					if(isItPawn && originalPawn != null) {
						allyTeam.replace(initial, originalPawn);
					}
					if(p.name.equals("bK"))
					{
						blackKingPosition = p.getPosition();
						blackPosition = p.stringPosition();
					}
					if(p.name.equals("wK")) 
					{
						whiteKingPosition = p.getPosition();
						whitePosition = p.stringPosition();
					}

				}
			}
			
		}
		return true;
	}	
	/**
	 * Checks if the white king is under check condition
	 * @return boolean deciding if whiteKing is under check or not
	 */
	public boolean whiteKingCheck() {
		int whiteKingX = whiteKingPosition[0];
		int whiteKingY = whiteKingPosition[1];
		HashMap<String, ChessPiece> allyTeam = this.white;
		HashMap<String, ChessPiece> enemyTeam = this.black;
		int attackers = 0;
		boolean checked = false;
		String letters[ ]= {"a", "b", "c", "d", "e", "f", "g", "h"};
		//Rook+Queen+King Check
			boolean first = true;
			for(int x = whiteKingX-1; x >= 0; x--) {
				String attacker = this.gameBoard[x][whiteKingY]; 
				if(attacker.contains("w")) {
					break;
				}
				if(attacker.contains("b")) {
					if(attacker.contains("R") || attacker.contains("Q")) {
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[whiteKingY]+(8-x));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[whiteKingY]+(8-x));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
			first = true;
			for(int x = whiteKingX+1; x <= 7; x++) {
				String attacker = this.gameBoard[x][whiteKingY]; 
				if(attacker.contains("w")) {
					break;
				}
				if(attacker.contains("b")) {
					if(attacker.contains("R") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[whiteKingY]+(8-x));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[whiteKingY]+(8-x));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
			first = true;
			for(int x = whiteKingY-1; x >= 0; x--) {
				String attacker = this.gameBoard[whiteKingX][x]; 
				if(attacker.contains("w")) {
					break;
				}
				if(attacker.contains("b")) {
					if(attacker.contains("R") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-whiteKingX));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-whiteKingX));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
			first = true;
			for(int x = whiteKingY+1; x <= 7; x++) {
				String attacker = this.gameBoard[whiteKingX][x]; 
				if(attacker.contains("w")) {
					break;
				}
				if(attacker.contains("b")) {
					if(attacker.contains("R") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-whiteKingX));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-whiteKingX));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
		//Pawn+King Check
			if((whiteKingX-1 >= 0)) {
				if(whiteKingY - 1 >= 0 && (this.gameBoard[whiteKingX-1][whiteKingY-1].contains("bp") ||this.gameBoard[whiteKingX-1][whiteKingY-1].contains("bK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY-1]+(8-(whiteKingX-1)));
				}
				if(whiteKingY + 1 <= 7 && (this.gameBoard[whiteKingX-1][whiteKingY+1].contains("bp") || this.gameBoard[whiteKingX-1][whiteKingY+1].contains("bK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY+1]+(8-(whiteKingX-1)));
				}
			}
			
			if((whiteKingX+1 <= 7)) {
				if(whiteKingY - 1 >= 0 && (this.gameBoard[whiteKingX+1][whiteKingY-1].contains("bp") || this.gameBoard[whiteKingX+1][whiteKingY-1].contains("bK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY-1]+(8-(whiteKingX+1)));
				}
				if(whiteKingY + 1 <= 7 && (this.gameBoard[whiteKingX+1][whiteKingY+1].contains("bp") || this.gameBoard[whiteKingX+1][whiteKingY+1].contains("bK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY+1]+(8-(whiteKingX+1)));
				}
			}
		
		//Bishop+Queen Check
			int diagonalX = whiteKingX+1;
			int diagonalY = whiteKingY+1;
			while(diagonalX <=7 && diagonalY <= 7){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("w")) 
					break;
				if(attacker.contains("b")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX++;
				diagonalY++;
			}
			
			diagonalX = whiteKingX+1;
			diagonalY = whiteKingY-1;
			while(diagonalX <=7 && diagonalY >= 0){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("w")) 
					break;
				if(attacker.contains("b")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX++;
				diagonalY--;
			}
			
			diagonalX = whiteKingX-1;
			diagonalY = whiteKingY+1;
			while(diagonalX >=0 && diagonalY <= 7){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("w")) 
					break;
				if(attacker.contains("b")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX--;
				diagonalY++;
			}
			
			diagonalX = whiteKingX-1;
			diagonalY = whiteKingY-1;
			while(diagonalX >=0 && diagonalY >= 0){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("w")) 
					break;
				if(attacker.contains("b")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX--;
				diagonalY--;
			}
		//Knight Check
			if(whiteKingX+2 <= 7)
			{
				if(whiteKingY+1 <=7 && this.gameBoard[whiteKingX+2][whiteKingY+1].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY+1]+(8-(whiteKingX+2)));
				}
				if(whiteKingY-1 >=0 && this.gameBoard[whiteKingX+2][whiteKingY-1].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY-1]+(8-(whiteKingX+2)));
				}
			}
			if(whiteKingX-2 >= 0)
			{
				if(whiteKingY+1 <=7 && this.gameBoard[whiteKingX-2][whiteKingY+1].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY+1]+(8-(whiteKingX-2)));
				}
	
				if(whiteKingY-1 >=0 && this.gameBoard[whiteKingX-2][whiteKingY-1].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY-1]+(8-(whiteKingX-2)));
				}
			}
			if(whiteKingY+2 <= 7)
			{
				if(whiteKingX+1 <=7 && this.gameBoard[whiteKingX+1][whiteKingY+2].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY+2]+(8-(whiteKingX+1)));
				}
				if(whiteKingX-1 >=0 && this.gameBoard[whiteKingX-1][whiteKingY+2].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY+2]+(8-(whiteKingX-1)));
				}
			}	
			if(whiteKingY-2 >= 0)
			{
				if(whiteKingX+1 <=7 && this.gameBoard[whiteKingX+1][whiteKingY-2].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY-2]+(8-(whiteKingX+1)));
				}
				if(whiteKingX-1 >=0 && this.gameBoard[whiteKingX-1][whiteKingY-2].contains("bN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[whiteKingY-2]+(8-(whiteKingX-1)));
				}
			}
		ATTACK = attackers;
		if(checked) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the black king is under check condition
	 * @return boolean deciding if blackKing is under check or not
	 */
	public boolean blackKingCheck() {
		int blackKingX = blackKingPosition[0];
		int blackKingY = blackKingPosition[1];
		HashMap<String, ChessPiece> allyTeam = this.black;
		HashMap<String, ChessPiece> enemyTeam = this.white;
		int attackers = 0;
		boolean checked = false;
		String letters[ ]= {"a", "b", "c", "d", "e", "f", "g", "h"};
		//Rook+Queen+King Check
			boolean first = true;
			for(int x = blackKingX-1; x >= 0; x--) {
				String attacker = this.gameBoard[x][blackKingY]; 
				if(attacker.contains("b")) {
					break;
				}
				if(attacker.contains("w")) {
					if(attacker.contains("R") || attacker.contains("Q")) {
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[blackKingY]+(8-x));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[blackKingY]+(8-x));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
			first = true;
			for(int x = blackKingX+1; x <= 7; x++) {
				String attacker = this.gameBoard[x][blackKingY]; 
				if(attacker.contains("b")) {
					break;
				}
				if(attacker.contains("w")) {
					if(attacker.contains("R") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[blackKingY]+(8-x));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[blackKingY]+(8-x));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
			first = true;
			for(int x = blackKingY-1; x >= 0; x--) {
				String attacker = this.gameBoard[blackKingX][x]; 
				if(attacker.contains("b")) {
					break;
				}
				if(attacker.contains("w")) {
					if(attacker.contains("R") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-blackKingX));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-blackKingX));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
			first = true;
			for(int x = blackKingY+1; x <= 7; x++) {
				String attacker = this.gameBoard[blackKingX][x]; 
				if(attacker.contains("b")) {
					break;
				}
				if(attacker.contains("w")) {
					if(attacker.contains("R") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-blackKingX));
						break;
					}
					else if(first && attacker.contains("K")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[x]+(8-blackKingX));
						break;
					}
					else
						break;
				}
				first = false;
			}
			
		//Pawn+King Check
			if((blackKingX-1 >= 0)) {
				if(blackKingY - 1 >= 0 && (this.gameBoard[blackKingX-1][blackKingY-1].contains("wp") ||this.gameBoard[blackKingX-1][blackKingY-1].contains("wK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY-1]+(8-(blackKingX-1)));
				}
				if(blackKingY + 1 <= 7 && (this.gameBoard[blackKingX-1][blackKingY+1].contains("wp") || this.gameBoard[blackKingX-1][blackKingY+1].contains("wK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY+1]+(8-(blackKingX-1)));
				}
			}
			
			if((blackKingX+1 <= 7)) {
				if(blackKingY - 1 >= 0 && (this.gameBoard[blackKingX+1][blackKingY-1].contains("wp") || this.gameBoard[blackKingX+1][blackKingY-1].contains("wK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY-1]+(8-(blackKingX+1)));
				}
				if(blackKingY + 1 <= 7 && (this.gameBoard[blackKingX+1][blackKingY+1].contains("wp") || this.gameBoard[blackKingX+1][blackKingY+1].contains("wK"))){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY+1]+(8-(blackKingX+1)));
				}
			}
		
		//Bishop+Queen Check
			int diagonalX = blackKingX+1;
			int diagonalY = blackKingY+1;
			while(diagonalX <=7 && diagonalY <= 7){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("b")) 
					break;
				if(attacker.contains("w")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX++;
				diagonalY++;
			}
			
			diagonalX = blackKingX+1;
			diagonalY = blackKingY-1;
			while(diagonalX <=7 && diagonalY >= 0){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("b")) 
					break;
				if(attacker.contains("w")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX++;
				diagonalY--;
			}
			
			diagonalX = blackKingX-1;
			diagonalY = blackKingY+1;
			while(diagonalX >=0 && diagonalY <= 7){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("b")) 
					break;
				if(attacker.contains("w")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX--;
				diagonalY++;
			}
			
			diagonalX = blackKingX-1;
			diagonalY = blackKingY-1;
			while(diagonalX >=0 && diagonalY >= 0){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("b")) 
					break;
				if(attacker.contains("w")) {
					if(attacker.contains("B") || attacker.contains("Q")){
						attackers++;
						checked = true;
						contester = enemyTeam.get(letters[diagonalY]+(8-diagonalX));
						break;
					}
					else
						break;
				}
				diagonalX--;
				diagonalY--;
			}
		//Knight Check
			if(blackKingX+2 <= 7)
			{
				if(blackKingY+1 <=7 && this.gameBoard[blackKingX+2][blackKingY+1].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY+1]+(8-(blackKingX+2)));
				}
				if(blackKingY-1 >=0 && this.gameBoard[blackKingX+2][blackKingY-1].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY-1]+(8-(blackKingX+2)));
				}
			}
			if(blackKingX-2 >= 0)
			{
				if(blackKingY+1 <=7 && this.gameBoard[blackKingX-2][blackKingY+1].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY+1]+(8-(blackKingX-2)));
				}
	
				if(blackKingY-1 >=0 && this.gameBoard[blackKingX-2][blackKingY-1].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY-1]+(8-(blackKingX-2)));
				}
			}
			if(blackKingY+2 <= 7)
			{
				if(blackKingX+1 <=7 && this.gameBoard[blackKingX+1][blackKingY+2].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY+2]+(8-(blackKingX+1)));
				}
				if(blackKingX-1 >=0 && this.gameBoard[blackKingX-1][blackKingY+2].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY+2]+(8-(blackKingX-1)));
				}
			}	
			if(blackKingY-2 >= 0)
			{
				if(blackKingX+1 <=7 && this.gameBoard[blackKingX+1][blackKingY-2].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY-2]+(8-(blackKingX+1)));
				}
				if(blackKingX-1 >=0 && this.gameBoard[blackKingX-1][blackKingY-2].contains("wN")){
					attackers++;
					checked = true;
					contester = enemyTeam.get(letters[blackKingY-2]+(8-(blackKingX-1)));
				}
			}
		ATTACK = attackers;
		if(checked) {
			return true;
		}
		return false;
	}	
	
	/**
	 * decides whether white team has any moves left that it can make
	 * @return boolean
	 */
	public boolean whiteNoPossibleMoves() { //W.I.P
		
		String test = whitePosition;
		HashMap<String, ChessPiece> allyTeam = this.white;
		HashMap<String, ChessPiece> enemyTeam = this.black;
		//Can the King move out of the way?
		char letter = test.charAt(0);
		char letterAfter = (char)(letter+1);
		char letterBefore = (char)(letter-1);
		int number = Integer.parseInt(test.substring(1));
		
		whiteKingCheck();
		if(letterBefore >= 'a'){
			if(safeMove(test + " " + letterBefore + number, allyTeam, enemyTeam) || ((number-1 > 0) && safeMove(test + " " + letterBefore + (number-1), allyTeam, enemyTeam)) || ((number+1<=8) && safeMove(test + " " + letterBefore + (number+1), allyTeam, enemyTeam))) 
				return false;
		}
		if( ((number-1 > 0) && safeMove(test + " " + letter + (number-1), allyTeam, enemyTeam)) || ((number+1<=8) && safeMove(test + " " + letter + (number+1), allyTeam, enemyTeam))) {
			return false;
		}
		
		if(letterAfter <= 'h'){			
			if(safeMove(test + " " + letterAfter + number, allyTeam, enemyTeam) || ((number-1 > 0) && safeMove(test + " " + letterAfter + (number-1), allyTeam, enemyTeam)) || ((number+1<=8) && safeMove(test + " " + letterAfter + (number+1), allyTeam, enemyTeam))) 
				return false;
		}
		whiteKingCheck();
		//Is there more than one attacker? If so and king cannot move, checkmate.
		
		if(ATTACK > 1)
		{
			return true;
		}
		
		//Are there no attackers? Could be a stale-mate.
		if(contester==null || ATTACK == 0) {
			for(String key : allyTeam.keySet()) {
				ChessPiece defender = white.get(key);
				String[] moves = defender.getValidMoves(this.gameBoard);
				for(String spot: moves) {
					if( (defender.canMoveTo(spot, this.gameBoard) || defender.canCapture(spot, this.gameBoard)) && safeMove(defender.stringPosition()+" "+spot, allyTeam, enemyTeam)) {
						return false;
					}
				}				
			}
			return true;
		}
		

		//Can the attacker's path be blocked?
		String[] validMoves = contester.getValidMoves(this.gameBoard);
		
		for(String spot : validMoves) {
			for(String key : allyTeam.keySet()) {
				ChessPiece defender = allyTeam.get(key);
				if(defender.canMoveTo(spot, gameBoard) && safeMove(defender.stringPosition()+" "+spot, allyTeam, enemyTeam)) {
						return false;
				}
			}
		}
		//Can the attacker be captured?
		for(String key : allyTeam.keySet()) {
			ChessPiece defender = allyTeam.get(key);
			if(defender.canCapture(contester.stringPosition(), this.gameBoard) && safeMove(defender.stringPosition()+" "+contester.stringPosition(), allyTeam, enemyTeam)) {
				return false;
			}
			if(contester instanceof Pawn && defender instanceof Pawn && contester.getPosition()[0]-1 >=0 ) { //Passantable?
				char a = contester.stringPosition().charAt(0);
				a = a--;
				if( a >= 'a' && safeMove(defender.stringPosition()+" "+a+contester.stringPosition().charAt(1), allyTeam, enemyTeam))
					return false;
				a = (char)(a+2);
				
				if( a <= 'h' && safeMove(defender.stringPosition()+" "+a+contester.stringPosition().charAt(1), allyTeam, enemyTeam))
					return false;
			}
					
		}
		return true;
	}

	/**
	 * decides whether black team has any moves left that it can make
	 * @return boolean
	 */
	public boolean blackNoPossibleMoves() { //W.I.P
		String test = blackPosition;
		HashMap<String, ChessPiece> allyTeam = this.black;
		HashMap<String, ChessPiece> enemyTeam = this.white;
		//Can the King move out of the way?
		char letter = test.charAt(0);
		char letterAfter = (char)(letter+1);
		char letterBefore = (char)(letter-1);
		int number = Integer.parseInt(test.substring(1));
		
	
		blackKingCheck();

		if(letterBefore >= 'a'){
			if(safeMove(test + " " + letterBefore + number, allyTeam, enemyTeam) || ((number-1 > 0) && safeMove(test + " " + letterBefore + (number-1), allyTeam, enemyTeam)) || ((number+1<=8) && safeMove(test + " " + letterBefore + (number+1), allyTeam, enemyTeam))) 
				return false;
		}
		
		if( ((number-1 > 0) && safeMove(test + " " + letter + (number-1), allyTeam, enemyTeam)) || ((number+1<=8) && safeMove(test + " " + letter + (number+1), allyTeam, enemyTeam))) {
			return false;
		}
		
		if(letterAfter <= 'h'){			
			if(safeMove(test + " " + letterAfter + number, allyTeam, enemyTeam) || ((number-1 > 0) && safeMove(test + " " + letterAfter + (number-1), allyTeam, enemyTeam)) || ((number+1<=8) && safeMove(test + " " + letterAfter + (number+1), allyTeam, enemyTeam))) 
				return false;
		}
		blackKingCheck();
		//Is there more than one attacker? If so and king cannot move, checkmate.
		
		if(ATTACK > 1)
		{
			return true;
		}
		
		//Are there no attackers? Could be a stale-mate.
		if(contester==null || ATTACK == 0) {
			for(String key : allyTeam.keySet()) {
				ChessPiece defender = black.get(key);
				String[] moves = defender.getValidMoves(this.gameBoard);
				for(String spot: moves) {
					if( (defender.canMoveTo(spot, this.gameBoard) || defender.canCapture(spot, this.gameBoard)) && safeMove(defender.stringPosition()+" "+spot, allyTeam, enemyTeam)) {
						return false;
					}
				}
			}
			return true;
		}
		//Can the attacker's path be blocked?
		String[] validMoves = contester.getValidMoves(this.gameBoard);
		for(String spot : validMoves) {
			for(String key : allyTeam.keySet()) {
				ChessPiece defender = allyTeam.get(key);
				if(defender.canMoveTo(spot, gameBoard) && safeMove(defender.stringPosition()+" "+spot, allyTeam, enemyTeam)) {
						return false;
				}
			}
		}
		//Can the attacker be captured?
		for(String key : allyTeam.keySet()) {
			ChessPiece defender = allyTeam.get(key);
			if(defender.canCapture(contester.stringPosition(), this.gameBoard) && safeMove(defender.stringPosition()+" "+contester.stringPosition(), allyTeam, enemyTeam)) {
					return false;
			}
			if(contester instanceof Pawn && defender instanceof Pawn && contester.getPosition()[0]-1 >=0 ) { //Passantable?
				char a = contester.stringPosition().charAt(0);
				a = a--;
				if( a >= 'a' && safeMove(defender.stringPosition()+" "+a+contester.stringPosition().charAt(1), allyTeam, enemyTeam))
					return false;
				a = (char)(a+2);
				
				if( a <= 'h' && safeMove(defender.stringPosition()+" "+a+contester.stringPosition().charAt(1), allyTeam, enemyTeam))
					return false;
			}
					
		}
		return true;
	}
	
	/**
	 * handles all logic for actually executing a move. checks whether the piece moves was a pawn an promotes it accordingly
	 * @param ChessPiece p
	 * @param String source
	 * @param String target
	 * @param String[] inputs
	 * @param HashMap allyTeam
	 */
	private void move(ChessPiece p, String source, String target, String[] inputs, HashMap<String, ChessPiece> allyTeam) {
		int[] origin = p.getPosition();
		int[] dest = Parser.translate(target);
		this.gameBoard[origin[0]][origin[1]] = "";
		this.gameBoard[dest[0]][dest[1]] = p.toString();
		p.updatePosition(dest);
//		System.out.println("this should have changed");
//		System.out.println(java.util.Arrays.toString(p.getPosition()));
//		System.out.println("before " + allyTeam.keySet());
		allyTeam.remove(source);	//hm shouldn't this be initial?
		allyTeam.put(target, p);
//		System.out.println(allyTeam.keySet());
		if(p instanceof Pawn) {
			char promotionType = ((Pawn)p).wasMoved(inputs);
			if(promotionType != Parser.INVALID_CHAR) {
				allyTeam.remove(target);
				ChessPiece promoted = Parser.getNewPiece(promotionType, p);
				allyTeam.put(target, promoted);
				this.gameBoard[dest[0]][dest[1]] = promoted.toString();
				
			}
		}
	}
	
	/**
	 * Finds the integer position of white king
	 * @return int[] position of whiteKing
	 */
	public int[] getWhiteKing() {
		System.out.println("White King is at x-"+whiteKingPosition[0]+" and y-"+whiteKingPosition[1]);
		return whiteKingPosition;
	}
	/**
	 * Finds the integer position of black king
	 * @return int[] position of blackKing
	 */
	public int[] getBlackKing() {
		System.out.println("Black King is at x-"+blackKingPosition[0]+" and y-"+blackKingPosition[1]);
		return blackKingPosition;
	}
	
	/**
	 * returns whether the position on a board is an empty spot or not
	 * @param String end target position from instruction
	 * @return boolean
	 */
	private boolean emptySpot(String end) {
		int[] position = Parser.translate(end);
		if(this.gameBoard[position[0]][position[1]].isBlank() || this.gameBoard[position[0]][position[1]].contains("##")) {
			return true;
		}
		return false;
	}

	
}
