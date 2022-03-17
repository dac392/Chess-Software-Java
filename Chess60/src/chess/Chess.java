//Diego Castellanos dac392
package chess;

import java.io.InputStreamReader;
import java.util.Scanner;

import util.Parser;

//
public class Chess {
	GameBoard chessboard;
	boolean ongoing;
	boolean previousMoveWasValid;
	
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
		if (input.contains("draw?")) {return DRAW;}
		if (input.contains("resign")) {return RESIGN;}
			
		String[] rawInputs = input.split(" ");
		if(rawInputs.length > 1) {
			if(Parser.validInput(rawInputs[0]) && Parser.validInput(rawInputs[1])  ) {
				if(rawInputs.length == 3 && !Parser.validType(rawInputs[2])) {
					return ERROR;
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
					System.out.println("Illegal move, try again");
					game.previousMoveWasValid = false;
					break;
				case CORRECT:
					game.previousMoveWasValid = game.makeMove(input, player);
					break;
				case DRAW:	//have not implemented yet
					game.ongoing = false;
					System.out.println("fix this in post");
					break;
				case RESIGN:
					game.ongoing = false;
					game.resignPrompt(player);
					break;
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
