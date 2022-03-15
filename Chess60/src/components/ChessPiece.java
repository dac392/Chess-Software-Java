package components;

import util.Parser;

public class ChessPiece {
	public int x;
	public int y;
	public int[] vmoves;
	public int[] hmoves;
	public String name;
	protected int totalMoves;
	
	protected final int BOARD_SIZE = 8;
	
	
	public ChessPiece(int x, int y, char type, int player, int totalMoves) {
		this.x = x;
		this.y = y;
		this.name = (player == 1)? "w"+type : "b"+type;
		this.totalMoves = totalMoves;
	}
	
	public String[] getValidMoves() {
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
	public String[] getValidMoves(int maxMoves, int scalar) {
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


}
