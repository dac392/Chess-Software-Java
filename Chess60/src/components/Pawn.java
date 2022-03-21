package components;

import util.Parser;

public class Pawn extends ChessPiece{
	public int[] hCapture;
	public int[] vCapture;
	public boolean firstMoveDone = false;
	String test;
	private static final int MOVES = 2;
	private static final int MAX_INPUTS = 3;
	
	public Pawn(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1*this.playerColor, -2*this.playerColor};
		super.vmoves = new int[]{ 0, 0};
		this.hCapture = new int[]{-1*this.playerColor,-1*this.playerColor};
		this.vCapture = new int[]{ 1*this.playerColor,-1*this.playerColor};
		
	}
	
	public boolean canMoveTo(String target, String[][] board) {
		for(int i = 0; i < this.totalMoves; i++) {
			int tempX = this.x + this.hmoves[i];
			int tempY = this.y + this.vmoves[i];
			if( (tempX > -1 && tempX < BOARD_SIZE) && (tempY > -1 && tempY < BOARD_SIZE) ) {
				String spot = board[tempX][tempY];
				if(!spot.isBlank() && (spot.contains(this.ally)|| spot.contains(this.enemy))) {
					break;
				}
				String posible_move = Parser.translate(tempX,tempY);
				if(posible_move.equals(target))
					return true;
			}
		}
		return false;
	}
	
	public boolean canCapture(String target, String[][] board) {

		int captureMoves = 2;
		test = board[x][y];
		for(int i = 0; i < captureMoves; i++) {
			int tempX = this.x + hCapture[i];
			int tempY = this.y + vCapture[i];
			if( (tempX > -1 && tempX < BOARD_SIZE) && (tempY > -1 && tempY < BOARD_SIZE) ) {
				String spot = board[tempX][tempY];
				if(!spot.isBlank() && spot.contains(this.ally)) {
					continue;
				}
				String posible_move = Parser.translate(tempX,tempY);
				if(posible_move.equals(target))
					return true;
			}

		}
		return false;
	}
	public String[] getValidMoves() {	// UHHHHHH
		String[] posible = new String[this.totalMoves];
		
		for(int i = 0; i < this.totalMoves; i++) {
			int tempX = this.x + this.hmoves[i];
			int tempY = this.y + this.vmoves[i];
			if( (tempX > -1 && tempX < BOARD_SIZE) && (tempY > -1 && tempY < BOARD_SIZE) ) {
				String posible_move = Parser.translate(tempX,tempY);
				posible[i] = posible_move;
			}
		}
		
		return posible;
		
	}
	public static void main(String[] args) {
		Pawn test = new Pawn(6,0,'p',1);
		System.out.println(test.stringPosition());
		Parser.printArray(test.getValidMoves());
	}

	public char wasMoved(String[] input) {
		super.totalMoves = 1;
		boolean canPromote = false;
		boolean defaultPromotion = input.length < MAX_INPUTS;		
		if(this.playerColor == Parser.PLAYER_1) {
			canPromote = Parser.canPromoteWhite(this.getPosition());
		}else if(this.playerColor == Parser.PLAYER_2) {
			canPromote = Parser.canPromoteBlack(this.getPosition());
		}
		if(canPromote && defaultPromotion) {
			return Parser.DEFAULT_CHAR;
		}else if(canPromote && !defaultPromotion) {
			return input[2].charAt(0);
		}
		
		return Parser.INVALID_CHAR;
	}
	public void revert() {
		if(!firstMoveDone)
			super.totalMoves = 2;
	}

}
