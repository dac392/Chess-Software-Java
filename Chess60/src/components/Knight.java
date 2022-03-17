package components;

public class Knight extends ChessPiece{
	private final static int MOVES = 8;
//	private final static int SCALAR = 8;

	public Knight(int x,int y, char type,int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-2,-1, 1, 2, 2, 1,-1,-2};	// up,right,down,left
		super.vmoves = new int[]{ 1, 2, 2, 1,-1,-2,-2,-1};	// up,right,down,left
	}
	
	public boolean canMoveTo(String target, String[][] board) {
		return super.canMoveTo(target, board);
	}
	
	public boolean canCapture(String target, String[][] board) {
		return super.canCapture(target, board);
	}
	
	public static void main(String[] args) {
		Knight test = new Knight(5,2,'N',1);
		System.out.println("Knight");
		System.out.println(test.stringPosition());
//		Parser.printArray(test.getValidMoves());
	}
}
