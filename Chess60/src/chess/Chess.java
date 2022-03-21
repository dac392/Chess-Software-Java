//Diego Castellanos dac392
//Abid Azad aa2177
package chess;

import java.io.InputStreamReader;
import java.util.Scanner;

import util.Parser;

//
public class Chess {
	static GameBoard  chessboard;
	boolean ongoing;
	boolean previousMoveWasValid;
	boolean whiteCheck;
	boolean blackCheck;
	public static boolean alreadytold=false;
	private static boolean  drawCalled = false;
	private static final int PLAYER_1 = 1;
	private static final int PLAYER_2 = -1;
	private static final int ERROR = 0;
	private static final int CORRECT = 1;
	private static final int DRAW = 2;
	private static final int RESIGN = 3;
	
	public Chess() {
		this.chessboard = new GameBoard();
		this.ongoing = true;
		this.previousMoveWasValid = true;
	}
	public void getBoard() {
		this.chessboard.printGameBoard();
	}
	public void getPieces() {
		this.chessboard.printHashMaps();
	}
	private int getPlayer() {
		return this.chessboard.getPlayer();
	}
	
	private void startPrompt(int player) {
		String playerPrompt = (player == PLAYER_1)? "White's move:":"Black's move:";
		if (this.previousMoveWasValid){
			this.getBoard();
		}
		if(player == PLAYER_1 && chessboard.whiteKingCheck() && !alreadytold) { 
			System.out.println("CHECK!");
			alreadytold = true;
		}
		if(player == PLAYER_2 && chessboard.blackKingCheck() && !alreadytold) {
			System.out.println("CHECK!");
			alreadytold = true;
		}
		System.out.print(playerPrompt);
		this.previousMoveWasValid = true;
	}
	private int parseInput(String input) {
		//if (input.contains("draw?")) {return DRAW;}
		if (input.contains("resign")) {return RESIGN;}
			
		String[] rawInputs = input.split(" ");
		if(drawCalled) {
			
			if(rawInputs.length > 1 || !rawInputs[0].equals("draw")) 
				return ERROR;
			
			else
				return DRAW;
			
		}
		if(rawInputs.length > 1) {
			if(Parser.validInput(rawInputs[0]) && Parser.validInput(rawInputs[1])  ) {
				
				if(rawInputs.length >= 3 ) {
					
					if(rawInputs.length > 4)
						return ERROR;
					
					if(rawInputs.length == 3 && !Parser.validType(rawInputs[2])) {
						if (rawInputs[2].equals("draw?"))
							return DRAW;
						return ERROR;
					}
					
					if(rawInputs.length == 4 ) {
						if (!Parser.validType(rawInputs[2]) || !rawInputs[3].equals("draw?"))
							return ERROR;
						return DRAW;
					}
					
				}
						
				return CORRECT;
			}
		}
		return ERROR;
	}
	

	public static void main(String[] args) {
		Chess game = new Chess();
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		
		while(game.ongoing) {
			int player = game.getPlayer();
			game.startPrompt(player);
			String input = scanner.nextLine();
			int condition = game.parseInput(input);
			switch(condition) {
				case ERROR:
					if(!drawCalled)
						System.out.println("Illegal move, try again");
					
					else
						System.out.println("You are obligated to draw.");	
					
					game.previousMoveWasValid = false;
					break;
				case CORRECT:
					game.previousMoveWasValid = game.makeMove(input, player);
					break;
				case DRAW:	//have not implemented yet
					if(!drawCalled) {
						game.previousMoveWasValid = game.makeMove(input, player);
						if(!game.previousMoveWasValid)
							break;
						drawCalled = true;
					}
						
					else {
						System.out.println("This game has been ended by a draw.");
						game.ongoing = false;
					}
					break;
				case RESIGN:					
					game.resignPrompt(player);
					game.ongoing = false;
					break;
			}
			
			
			
			if(chessboard.whiteKingCheck() && chessboard.whiteNoPossibleMoves()) {
				chessboard.printGameBoard();
				System.out.println("Checkmate! Black Wins!");
				game.ongoing = false;
			}
			else if(chessboard.blackKingCheck() && chessboard.blackNoPossibleMoves()) {
				chessboard.printGameBoard();
				System.out.println("Checkmate! White wins!");
				game.ongoing = false;
			}			
			
			else if((player == PLAYER_2 && chessboard.whiteNoPossibleMoves())||(player == PLAYER_1 && chessboard.blackNoPossibleMoves())) {
				chessboard.printGameBoard();
				System.out.println("Stalemate!");
				game.ongoing = false;
			}
//			break;
		}
		
	}
	private boolean makeMove(String input, int player) {
		int status = ERROR;
		if(player == PLAYER_1) {
			status = this.chessboard.whiteMoveManager(input);
		}else {
			status = this.chessboard.blackMoveManager(input);
		}
		if(status == CORRECT) {
			alreadytold = false;
			return true;
		}
		System.out.println("Illegal move, try again");
		return false;
	}
	private void resignPrompt(int player) {
		String p = (player == PLAYER_1)? "black":"white";
		System.out.println(p+" wins");
		
	}


}
