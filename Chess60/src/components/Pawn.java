package components;

import util.Parser;

public class Pawn extends ChessPiece{
	private final static int MOVES = 2;
	private boolean in_start_position;
	
	public Pawn(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,-2};	// up,right,down,left
		super.vmoves = new int[]{ 0, 0};	// up,right,down,left
		this.in_start_position = true;	// whenever, this changes to false, change this.totalMoves to 1
	}
	
	public String[] getValidMoves() {
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
}
