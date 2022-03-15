package components;

import util.Parser;

public class Bishop extends ChessPiece{
	private static final int MOVES = 4;
	private static final int SCALAR = 8;
	private static final int MAX_MOVES = 13;
	public Bishop(int x,int y, char type,int player) {
		super(x,y, type, player, MOVES);	//uhhh
		super.hmoves = new int[]{-1,1, 1,-1};	// up,right,down,left
		super.vmoves = new int[]{ 1,1,-1,-1};	// up,right,down,left
	}
	public String[] getValidMoves() {
		return super.getValidMoves(MAX_MOVES, SCALAR);
	}
	public static void main(String[] args) {
		System.out.println("Bishop");
		Bishop test = new Bishop(4,4,'B',1);
		System.out.println(test.stringPosition());
		Parser.printArray(test.getValidMoves());
	}
}
