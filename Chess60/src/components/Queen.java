package components;

public class Queen extends ChessPiece{
	private static final int MOVES = 8;
	private static final int SCALAR = 8;
	private static final int MAX_MOVES = 27;
	public Queen(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,-1,0,1,1, 1,0, -1};	// up,right,down,left
		super.vmoves = new int[]{ 0, 1,1,1,0,-1,-1,-1};	// up,right,down,left

	}
	
	public boolean canMoveTo(String target, String[][] board) {
		return super.canMoveTo(SCALAR, target, board);
	}
	
	public boolean canCapture(String target, String[][] board) {
		return super.canCapture(SCALAR, target, board);
	}
	
	public String[] getValidMoves(String[][] board) {
		return super.getValidMoves(MAX_MOVES, SCALAR, board);
	}
	public static void main(String[] args) {
		System.out.println("Queen");
		Queen test = new Queen(7,3,'Q',1);
		System.out.println(test.stringPosition());
//		Parser.printArray(test.getValidMoves());
	}
}
