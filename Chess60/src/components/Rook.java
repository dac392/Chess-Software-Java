package components;

import util.Parser;

public class Rook extends ChessPiece{
	private static final int MOVES = 4;
	private static final int SCALAR = 8;
	private static final int MAX_MOVES = 14;
	
	public Rook(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,0,1, 0};	// up,right,down,left
		super.vmoves = new int[]{ 0,1,0,-1};	// up,right,down,left
	}
	
	public String[] getValidMoves() {
		return super.getValidMoves(MAX_MOVES, SCALAR);
	}
	public static void main(String[] args) {
		Rook test = new Rook(7,3,'R',1);
		System.out.println("Rook");
		System.out.println(test.stringPosition());
		Parser.printArray(test.getValidMoves());
	}
}
