package project.pieces;
import java.io.IOException;

import project.ChessPiece;

public class Queen extends ChessPiece {

    public Queen(boolean isWhite, int[] position){
        super(isWhite, position);
        String temp = getColorPath("queen", this.isWhite);
        try {
            this.img=pathToScaledImage(temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
    @Override
    public boolean isLegalMove(int x1, int y1, int x2, int y2){
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        if ((dx == dy && dx>0) || ((dx>0 && dy == 0) || (dx == 0 && dy>0))) {
            return true;
        }
        return false;
    }	
}