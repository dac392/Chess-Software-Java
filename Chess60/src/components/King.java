package components;

import util.Parser;

public class King extends ChessPiece{
	// x_position
	// y_position
	// h_moves
	// v_moves
	// name
	private final static int MOVES = 4;
	
	public King(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,0,1,0};	// up,right,down,left
		super.vmoves = new int[]{ 0,1,0,-1};	// up,right,down,left
	}
	public static void main(String[] args) {
		King test = new King(7,4,'K',1);
		System.out.println("King");
		System.out.println(test.stringPosition());
		Parser.printArray(test.getValidMoves());
	}
	

}