package components;

/**
 * Extends from ChessPiece to generate King piece object.
 * @author DiegoCastellanos dac392
 * @author AbidAzad aa2177
 */
public class King extends ChessPiece{
	private final static int MOVES = 8;
	public boolean hasntMoved = true;
	public King(int x,int y, char type, int player) {
		super(x,y, type, player, MOVES);
		super.hmoves = new int[]{-1,-1,0,1,1, 1,0, -1};	// up,right,down,left
		super.vmoves = new int[]{ 0, 1,1,1,0,-1,-1,-1};	// up,right,down,left
	}
	
	/**
	 * decides whether the king is allowed to move or not
	 * @param String target
	 * @param String[][] board
	 * @return boolean
	 */
	public boolean canMoveTo(String target, String[][] board) {
		return super.canMoveTo(target, board);
	}
	/**
	 * decides whether the king can capture a target
	 * @param String target
	 * @param String[][] board
	 * @return boolean
	 */
	public boolean canCapture(String target, String[][] board) {
		return super.canCapture(target, board);
	}
	
	/**
	 * called after the king has been moved to prevent illegal castling
	 */
	public void moved() {
		hasntMoved = false;
	}

}
