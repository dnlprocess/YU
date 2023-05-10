package project.pieces;
import java.io.IOException;

import project.ChessPiece;

public class Pawn extends ChessPiece {

    public Pawn(boolean isWhite, int[] position){
        super(isWhite, position);
        String temp = getColorPath("pawn", this.isWhite);
        try {
            this.img=pathToScaledImage(temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLegalMove(int x1, int y1, int x2, int y2){
        if (x1 == x2 && Math.abs(y1 - y2) == 1) {
            return true;
        } else if (x1 == x2 && Math.abs(y1 - y2) == 2) {
            return true;
        } else if (Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 1) {
            return true;
        //} else if (Math.abs(x1 - x2) == (isWhite() ? -1 : 1) && y1 == y2) {
        //    return true;
        }
        return false;
    }
    
}