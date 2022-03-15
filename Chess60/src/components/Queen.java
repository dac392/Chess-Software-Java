package components;

import util.Parser;

public class Queen extends ChessPiece{
	private static final int MOVES = 8;
	private static final int SCALAR = 8;
	private static final int MAX_MOVES = 27;
	public Queen(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,-1,0,1,1, 1,0, -1};	// up,right,down,left
		super.vmoves = new int[]{ 0, 1,1,1,0,-1,-1,-1};	// up,right,down,left

	}
	public String[] getValidMoves() {
		return super.getValidMoves(MAX_MOVES, SCALAR);
	}
	public static void main(String[] args) {
		System.out.println("Queen");
		Queen test = new Queen(7,3,'Q',1);
		System.out.println(test.stringPosition());
		Parser.printArray(test.getValidMoves());
	}
}
