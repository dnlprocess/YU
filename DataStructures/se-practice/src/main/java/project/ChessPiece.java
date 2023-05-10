package project;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public abstract class ChessPiece {
    protected boolean isWhite;
    protected int[] position;
    protected Image img;

    public ChessPiece(boolean isWhite, int[] position){
        this.isWhite = isWhite;
        this.position = position;
    }

    public Image getImage(){
        return this.img;
    }

    public String getColorPath(String path, boolean isWhite){
        if(isWhite){
            return "src/main/java/project/pieces/images/"+path+"White.png";
        }
        else{
            return "src/main/java/project/pieces/images/"+path+"Black.png";
        }
    }

    public Image pathToScaledImage(String path) throws IOException{
        return ImageIO.read(new File(path)).getScaledInstance(64,64,BufferedImage.SCALE_SMOOTH);
    }
    
    public boolean isWhite() {
        return this.isWhite;
    }

    public abstract boolean isLegalMove(int x1, int y1, int x2, int y2);

    public void setPosition(int x, int y) {
        this.position[0] = x;
        this.position[1] = y;
    }

    public int[] getPosition() {
        return this.position;
    }
}