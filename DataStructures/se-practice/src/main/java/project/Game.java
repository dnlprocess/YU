package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.ChessPiece;
import project.pieces.*;
import project.ChessBoard;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Image;

public class Game {
    List<String> moves;
    ChessBoard board;
    File f;
    final static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    
    public Game(String filename) throws IOException {
        this.board = new ChessBoard();
        //throw new IOException(System.getProperty("user.dir"));
        this.moves = readPgnFile(filename);

        for (String move: moves) {
            move(move);
        }
    }

    public void manageGraphics(){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        int horizontalBound = 512;
        int verticalBound = 512;
        frame.setBounds(0,0,horizontalBound*2,verticalBound);

        JPanel jp = new JPanel(){
            @Override
            public void paint(Graphics g){
                boolean white = false;
                for(int i=0;i<8;i++){
                    white=!white;
                    for(int j=0;j<8;j++){
                        if(white){
                            g.setColor(Color.WHITE);
                        }
                        else{
                            g.setColor(Color.BLUE.darker().darker().darker());
                        }
                        g.fillRect(i*64,j*64,64,64);
                        ChessPiece temp = board.board[i][7-j];
                        if(temp!=null){
                            g.drawImage(temp.getImage(), i*64, j*64, this);
                        }
                        white=!white;
                    }
                }
            }
        };

        frame.add(jp);
        frame.setVisible(true);
    }

    private List<String> readPgnFile(String filename) throws IOException {
        List<String> moves = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            boolean startReadingMoves = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("1.")) {
                    sb.append(line);
                }
                else if (startReadingMoves) {
                    sb.append(line);
                }
            }
        }
        
        String[] tokens = sb.toString().split("\\s+");
        boolean ignore = false;
        for (String token : tokens) {
            if (ignore) {
                if (token.endsWith("}")) {
                    ignore = false;
                }
                continue;
            }
            else if (token.startsWith("{")) {
                ignore = true;
                continue;
            }
            else if (token.matches("\\d+\\.")) { //move number
                continue;
            } else if (token.matches("[a-h][1-8]")) {
                moves.add("P" + token);
            } else if (token.matches("[KQRBN][a-h]?[1-8]?[x-]?[a-h][1-8]([+#])?")) {
                token = token.replace("x", "");
                if (token.endsWith("+") || token.endsWith("#")) {
                    token = token.substring(0, token.length() - 1);
                }
                moves.add(token);
            } else if (token.matches("O-O(-O)?")) {
                moves.add(token);
            }
        }
    
        //reset graphics
        return moves;
    }

    public void backMove() {
        board.moveTree.traverseUp(1);

        //drawMove();

        //reset graphics
    }

    public void forwardMove(int move) {
        board.moveTree.traverseDown(move);

        //drawMove();

        //reset graphics
    }

    public boolean move(String move) {
        int x2 = alphabet.indexOf(move.charAt(move.length() -2));
        int y2 = Character.getNumericValue(move.charAt(move.length() -1))-1;

        Class<? extends ChessPiece> piece = getPieceClass(move.charAt(0));

        ArrayList<int[]> sourcePosition = board.findPiece(x2, y2, !board.moveTree.getMove().isWhite(), piece);

        int x1;
        int y1;

        if (sourcePosition.size() > 1) {
            Character c = move.charAt(1);
            int pos = Character.getNumericValue(c);
            int[] position;
            if (pos > 0) {
                position = findPosition(sourcePosition, pos, 1);
            }
            else {
                position = findPosition(sourcePosition, alphabet.indexOf(c), 0);
            }
            x1 = position[0];
            y1 = position[1];
        }
        else {
            x1 = sourcePosition.get(0)[0];
            y1 = sourcePosition.get(0)[1];
        }

        boolean success = board.move(x1, y1, x2, y2);
        //drawMove();

        return success;
    }

    public void addNote(String txt) {

    }

    private static Class<? extends ChessPiece> getPieceClass(Character piece) {
        switch (piece) {
            case 'K':
                return King.class;
            case 'Q':
                return Queen.class;
            case 'R':
                return Rook.class;
            case 'B':
                return Bishop.class;
            case 'N':
                return Knight.class;
            case 'P':
                return Pawn.class;
            default:
                throw new IllegalArgumentException("Invalid piece type: " + piece);
        }
    }

    private int [] findPosition(ArrayList<int[]> sourcePosition, int coordinate, int index) {
        for (int[] position: sourcePosition) {
            if (position[index] == coordinate) {
                return position;
            }
        }
        return sourcePosition.get(0);
    }

    /*
    private void drawMove() {
        int[] locations = board.moveTree.getMove().getLocations();

        ChessPiece sourcePiece = board.board[locations[0]][locations[1]];
        if(sourcePiece !=null){
            g.drawImage(sourcePiece.getImage(), locations[0], locations[1], this);
        }
        else {

        }

        ChessPiece destinationPiece = board.board[locations[2]][locations[3]];
        if(destinationPiece !=null){
            g.drawImage(destinationPiece.getImage(), locations[2], locations[3], this);
        }
        else {

        }
    }
    */
}