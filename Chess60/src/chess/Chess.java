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
						drawCalled = true;
					}
						
					else {
						System.out.println("This game has been ended by a draw.");
						game.ongoing = false;
					}
					break;
				case RESIGN:					
					game.resignPrompt(player);
					break;
			}
			if(chessboard.whiteKingCheck()) {
				System.out.println("THE KING IS UNDER ATTACK!");
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
		if(status == CORRECT)
			return true;
		
		System.out.println("Illegal move, try again");
		return false;
	}
	private void resignPrompt(int player) {
		String p = (player == PLAYER_1)? "black":"white";
		System.out.println(p+" wins");
	}


}
