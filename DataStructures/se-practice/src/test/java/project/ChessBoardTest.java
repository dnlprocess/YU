package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class ChessBoardTest{

	ChessBoard board = new ChessBoard();

	@Test
	public void creationTest(){
		assert(board.board[0][0]!=null);
		assert(board.board[0][3]==null);
	}

	@Test
	public void moveTest(){
		assert(board.isPossibleMove(1,0,2,2,true));
		assert(board.isLegalMove(1,0,2,2,true));
		assert(!board.move(0,0,7,3));
		assert(board.move(1,0,2,2));
		assert(board.board[1][0]==null);
		assert(board.board[2][2]!=null);
	}

	@Test
	public void undoTest(){
		assert(board.move(1,0,2,2));
		//assert(!board.isClear(7,0,2,2, ));
		assert(board.board[2][2]!=null);

		board.moveTree.traverseUp(1);
		assert(board.board[2][2]==null);
		assert(board.board[1][0]!=null);
	}
}