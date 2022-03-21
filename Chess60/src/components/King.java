package components;

public class King extends ChessPiece{
	// x_position
	// y_position
	// h_moves
	// v_moves
	// name
	private final static int MOVES = 8;
	public boolean hasntMoved = true;
	public King(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,-1,0,1,1, 1,0, -1};	// up,right,down,left
		super.vmoves = new int[]{ 0, 1,1,1,0,-1,-1,-1};	// up,right,down,left
	}
	
	public boolean canMoveTo(String target, String[][] board) {
		return super.canMoveTo(target, board);
	}
	
	public boolean canCapture(String target, String[][] board) {
		return super.canCapture(target, board);
	}
	
	public static void main(String[] args) {
		King test = new King(7,4,'K',1);
		System.out.println("King");
		System.out.println(test.stringPosition());
//		Parser.printArray(test.getValidMoves());
	}
	
	public void moved() {
		hasntMoved = false;
	}

}
