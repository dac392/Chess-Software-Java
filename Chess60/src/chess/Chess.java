//Diego Castellanos dac392
package chess;
//
public class Chess {
	GameBoard chessboard;
	public Chess() {
		this.chessboard = new GameBoard();
	}
	public void getBoard() {
		this.chessboard.printGameBoard();
	}
	public void getPieces() {
		this.chessboard.printHashMaps();
	}
	
	public static void main(String[] args) {
		Chess game = new Chess();
		game.getBoard();
//		game.getPieces();
	}

}
