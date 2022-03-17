package components;

import util.Parser;

public class ChessPiece {
	public int x;
	public int y;
	public int playerColor;
	public int[] vmoves;
	public int[] hmoves;
	public String ally;
	public String enemy;
	public String name;
	protected int totalMoves;
	
	protected final int BOARD_SIZE = 8;
	
	
	
	public ChessPiece(int x, int y, char type, int player, int totalMoves) {
		this.x = x;
		this.y = y;
		this.name = (player == 1)? "w"+type : "b"+type;
		this.ally = (player == 1)? "w": "b";
		this.enemy = (player == 1)? "b": "w";
		this.totalMoves = totalMoves;
		this.playerColor = player;
	}
	
	// deprecated - will probably delete later
	public String[] getValidMoves(String[][] board) {
		String[] pos = new String[totalMoves];
		int k = 0;
		for(int i = 0; i < this.totalMoves; i++) {
			int tempX = this.x + this.hmoves[i];
			int tempY = this.y + this.vmoves[i];
			if( (tempX>-1 && tempX < BOARD_SIZE) && (tempY>-1 && tempY<BOARD_SIZE) ){
				String posible_move = Parser.translate(tempX,tempY);
				pos[k] = posible_move;
				k++;
			}
		}
		String[] statement = new String[k];
		for(int i = 0; i < statement.length;i++) {
			statement[i] = pos[i];
		}
		return statement;
	}
	
	// deprecated - will probably delete later
	public String[] getValidMoves(int maxMoves, int scalar, String[][] board) {
		String[] posible = new String[maxMoves];
		int k = 0;
		for(int i = 0; i < this.totalMoves; i++) {
			for(int scale = 1; scale < scalar; scale++) {
				int tempX = this.x + this.hmoves[i]*scale;
				int tempY = this.y + this.vmoves[i]*scale;
				if( (tempX>-1 && tempX < BOARD_SIZE) && (tempY>-1 && tempY<BOARD_SIZE) ) {
					String posible_move = Parser.translate(tempX,tempY);
					posible[k] = posible_move;
					k++;
				}
			}
		}
		if(k != maxMoves) {
			return Parser.adjustArraySize(posible, k);
		}
		return posible;
	}
	
	public int[] getPosition() {
		int[] pos = {this.x, this.y};
		return pos;
	}
	public String stringPosition() {
		return Parser.translate(this.x, this.y);
	}
	
	public String toString() {
		return name+" ";
	}

	public String getName() {
		return this.name;
	}

	public boolean canCapture(String target, String[][] board) {
		return canMoveTo(target, board);
	}
	
	public boolean canCapture(int scalar, String end, String[][] board) {
		// pawn should be the only one that doesn't do this
		return canMoveTo(scalar,end,board);
	}

	public boolean canMoveTo(int scalar, String end,String[][] board) {
		for(int i = 0; i < this.totalMoves; i++) {
			for(int scale = 1; scale < scalar; scale++) {
				int tempX = this.x + this.hmoves[i]*scale;
				int tempY = this.y + this.vmoves[i]*scale;
				if( (tempX>-1 && tempX < BOARD_SIZE) && (tempY>-1 && tempY<BOARD_SIZE) ) {
					String spot = board[tempX][tempY];
					if(!spot.isBlank() && spot.contains(this.ally)) {
						break;
					}
					String posible_move = Parser.translate(tempX,tempY);
					if(posible_move.equals(end))
						return true;
					if(!spot.isBlank() && spot.contains(this.enemy) && !posible_move.equals(end))
						break;
				}
			}
		}
		return false;
	}

	public boolean canMoveTo(String target, String[][] board) {
		for(int i = 0; i < this.totalMoves; i++) {
			int tempX = this.x + this.hmoves[i];
			int tempY = this.y + this.vmoves[i];
			if( (tempX>-1 && tempX < BOARD_SIZE) && (tempY>-1 && tempY<BOARD_SIZE) ){
				String spot = board[tempX][tempY];
				if(!spot.isBlank() && spot.contains(this.ally)) {
					continue;
				}
				String posible_move = Parser.translate(tempX,tempY);
				if(posible_move.equals(target))
					return true;
			}
		}
		return false;
	}

	
	public int getTotalMoves() {
		return this.totalMoves;
	}

	public void updatePosition(int[] dest) {
		this.x = dest[0];
		this.y = dest[1];
	}




}
