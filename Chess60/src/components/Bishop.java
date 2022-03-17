package components;

public class Bishop extends ChessPiece{
	private static final int MOVES = 4;
	private static final int SCALAR = 8;
	private static final int MAX_MOVES = 13;
	public Bishop(int x,int y, char type,int player) {
		super(x,y, type, player, MOVES);	//uhhh
		super.hmoves = new int[]{-1,1, 1,-1};	// up,right,down,left
		super.vmoves = new int[]{ 1,1,-1,-1};	// up,right,down,left
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
		System.out.println("Bishop");
		Bishop test = new Bishop(4,4,'B',1);
		System.out.println(test.stringPosition());
//		Parser.printArray(test.getValidMoves());
	}
}
