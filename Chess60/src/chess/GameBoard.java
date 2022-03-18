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
	int[] whiteKingPosition;
	int[] blackKingPosition;
	String whitePosition;
	String blackPosition;
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
					return true;
				}
			}
			
		}
		
		return false;
	}

	public boolean safeMove(String in, HashMap<String, ChessPiece> allyTeam, HashMap<String, ChessPiece> enemyTeam) { //Theoretically should perform a move and check if the king is safe. Regardless, should return board back to what it was before returning.
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
					enemyTeam.put(end, removed);
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

				}
			}
			
		}
		
		return true;
	}	
	//theoretically should work
	public boolean whiteKingCheck() {
		int whiteKingX = whiteKingPosition[0];
		int whiteKingY = whiteKingPosition[1];
		//Rook+Queen+King Check
			boolean first = true;
			for(int x = whiteKingX-1; x >= 0; x--) {
				String attacker = this.gameBoard[x][whiteKingY]; 
				if(attacker.contains("w")) {
					break;
				}
				if(attacker.contains("b")) {
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
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
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
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
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
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
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
					else
						break;
				}
				first = false;
			}
			
		//Pawn+King Check
			if((whiteKingX-1 >= 0)) {
				if(whiteKingY - 1 >= 0 && (this.gameBoard[whiteKingX-1][whiteKingY-1].equals("bp") ||this.gameBoard[whiteKingX-1][whiteKingY-1].equals("bK"))) 
					return true;
				if(whiteKingY + 1 >= 7 && (this.gameBoard[whiteKingX-1][whiteKingY+1].equals("bp") || this.gameBoard[whiteKingX-1][whiteKingY+1].equals("bK")))
					return true;
			}
			
			if((whiteKingX+1 <= 7)) {
				if(whiteKingY - 1 >= 0 && (this.gameBoard[whiteKingX+1][whiteKingY-1].equals("bp") || this.gameBoard[whiteKingX+1][whiteKingY-1].equals("bK"))) 
					return true;
				if(whiteKingY + 1 >= 7 && (this.gameBoard[whiteKingX+1][whiteKingY+1].equals("bp") || this.gameBoard[whiteKingX+1][whiteKingY+1].equals("bK")))
					return true;
			}
		
		//Bishop+Queen Check
			int diagonalX = whiteKingX+1;
			int diagonalY = whiteKingY+1;
			while(diagonalX <=7 && diagonalY <= 7){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("w")) 
					break;
				if(attacker.contains("b")) {
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
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
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
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
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
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
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
					else
						break;
				}
				diagonalX--;
				diagonalY--;
			}
		//Knight Check
			if(whiteKingX+2 <= 7)
			{
				if(whiteKingY+1 <=7 && this.gameBoard[whiteKingX+2][whiteKingY+1].equals("bN"))
					return true;
				if(whiteKingY-1 >=0 && this.gameBoard[whiteKingX+2][whiteKingY-1].equals("bN"))
					return true;
			}
			if(whiteKingX-2 >= 0)
			{
				if(whiteKingY+1 <=7 && this.gameBoard[whiteKingX-2][whiteKingY+1].equals("bN"))
					return true;
				if(whiteKingY-1 >=0 && this.gameBoard[whiteKingX-2][whiteKingY-1].equals("bN"))
					return true;
			}
			if(whiteKingY+2 <= 7)
			{
				if(whiteKingX+1 <=7 && this.gameBoard[whiteKingX+1][whiteKingY+2].equals("bN"))
					return true;
				if(whiteKingX-1 >=0 && this.gameBoard[whiteKingX-1][whiteKingY+2].equals("bN"))
					return true;
			}	
			if(whiteKingY-2 >= 0)
			{
				if(whiteKingX+1 <=7 && this.gameBoard[whiteKingX+1][whiteKingY-2].equals("bN"))
					return true;
				if(whiteKingX-1 >=0 && this.gameBoard[whiteKingX-1][whiteKingY-2].equals("bN"))
					return true;
			}		
		return false;
	}
	
	public boolean blackKingCheck() {	
		int blackKingX = blackKingPosition[0];
		int blackKingY = blackKingPosition[1];
		//Rook+Queen+King Check
			boolean first = true;
			for(int x = blackKingX-1; x >= 0; x--) {
				String attacker = this.gameBoard[x][blackKingY]; 
				if(attacker.contains("b")) {
					break;
				}
				if(attacker.contains("w")) {
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
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
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
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
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
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
					if(attacker.contains("R") || attacker.contains("Q"))
						return true;
					else if(first && attacker.contains("K"))
						return true;
					else
						break;
				}
				first = false;
			}
			
		//Pawn+King Check
			if((blackKingX-1 >= 0)) {
				if(blackKingY - 1 >= 0 && (this.gameBoard[blackKingX-1][blackKingY-1].equals("wp") ||this.gameBoard[blackKingX-1][blackKingY-1].equals("wK"))) 
					return true;
				if(blackKingY + 1 >= 7 && (this.gameBoard[blackKingX-1][blackKingY+1].equals("wp") || this.gameBoard[blackKingX-1][blackKingY+1].equals("wK")))
					return true;
			}
			
			if((blackKingX+1 <= 7)) {
				if(blackKingY - 1 >= 0 && (this.gameBoard[blackKingX+1][blackKingY-1].equals("wp") || this.gameBoard[blackKingX+1][blackKingY-1].equals("wK"))) 
					return true;
				if(blackKingY + 1 >= 7 && (this.gameBoard[blackKingX+1][blackKingY+1].equals("wp") || this.gameBoard[blackKingX+1][blackKingY+1].equals("wK")))
					return true;
			}
		
		//Bishop+Queen Check
			int diagonalX = blackKingX+1;
			int diagonalY = blackKingY+1;
			while(diagonalX <=7 && diagonalY <= 7){
				String attacker = this.gameBoard[diagonalX][diagonalY];
				if(attacker.contains("b")) 
					break;
				if(attacker.contains("w")) {
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
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
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
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
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
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
					if(attacker.contains("B") || attacker.contains("Q"))
						return true;
					else
						break;
				}
				diagonalX--;
				diagonalY--;
			}
		//Knight Check
			if(blackKingX+2 <= 7)
			{
				if(blackKingY+1 <=7 && this.gameBoard[blackKingX+2][blackKingY+1].equals("wN"))
					return true;
				if(blackKingY-1 >=0 && this.gameBoard[blackKingX+2][blackKingY-1].equals("wN"))
					return true;
			}
			if(blackKingX-2 >= 0)
			{
				if(blackKingY+1 <=7 && this.gameBoard[blackKingX-2][blackKingY+1].equals("wN"))
					return true;
				if(blackKingY-1 >=0 && this.gameBoard[blackKingX-2][blackKingY-1].equals("wN"))
					return true;
			}
			if(blackKingY+2 <= 7)
			{
				if(blackKingX+1 <=7 && this.gameBoard[blackKingX+1][blackKingY+2].equals("wN"))
					return true;
				if(blackKingX-1 >=0 && this.gameBoard[blackKingX-1][blackKingY+2].equals("wN"))
					return true;
			}	
			if(blackKingY-2 >= 0)
			{
				if(blackKingX+1 <=7 && this.gameBoard[blackKingX+1][blackKingY-2].equals("wN"))
					return true;
				if(blackKingX-1 >=0 && this.gameBoard[blackKingX-1][blackKingY-2].equals("wN"))
					return true;
			}		
		return false;
	}	
	
	public boolean whiteNoPossibleMoves() { //W.I.P
		String test = whitePosition;
		HashMap<String, ChessPiece> allyTeam = this.white;
		HashMap<String, ChessPiece> enemyTeam = this.black;
		//Can the King move out of the way?
		char letter = test.charAt(0);
		char letterAfter = (char)(letter+1);
		char letterBefore = (char)(letter-1);
		int number = Integer.parseInt(test.substring(1));
		
		String initial = test + " " + letterBefore + number;
		
		if(safeMove(test + " " + letterBefore + number, allyTeam, enemyTeam) || safeMove(test + " " + letterBefore + (number-1), allyTeam, enemyTeam) || safeMove(test + " " + letterBefore + (number+1), allyTeam, enemyTeam)) {
			return false;
		}
		
		if(safeMove(test + " " + letter + (number-1), allyTeam, enemyTeam) || safeMove(test + " " + letter + (number+1), allyTeam, enemyTeam)) {
			return false;
		}
		
		if(safeMove(test + " " + letterAfter + number, allyTeam, enemyTeam) || safeMove(test + " " + letterAfter + (number-1), allyTeam, enemyTeam) || safeMove(test + " " + letterAfter + (number+1), allyTeam, enemyTeam)) {
			return false;
		}
		
		//Is there more than one attacker? If so and king cannot move, checkmate.
		
		//Can the attacker's path be blocked?
		
		//Can the attacker be captured?
		return true;
	}
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
	
	public int[] getWhiteKing() {
		System.out.println("White King is at x-"+whiteKingPosition[0]+" and y-"+whiteKingPosition[1]);
		return whiteKingPosition;
	}
	public int[] getBlackKing() {
		System.out.println("Black King is at x-"+blackKingPosition[0]+" and y-"+blackKingPosition[1]);
		return blackKingPosition;
	}
	

	private boolean emptySpot(String end) {
		int[] position = Parser.translate(end);
		if(this.gameBoard[position[0]][position[1]].isBlank()) {
			return true;
		}
		return false;
	}

	
}
