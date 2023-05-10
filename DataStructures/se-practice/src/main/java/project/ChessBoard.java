package project;

import java.util.ArrayList;

import project.ChessPiece;
import project.pieces.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Image;

public class ChessBoard{
    ChessPiece[][] board;
    MoveTree moveTree;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
        this.moveTree = new MoveTree();
        initializeBoard();
    }

    private void initializeBoard() {
        board[0][0] = new Rook(true, new int[]{0, 0});
        board[1][0] = new Knight(true, new int[]{1, 0});
        board[2][0] = new Bishop(true, new int[]{2, 0});
        board[3][0] = new Queen(true, new int[]{3, 0});
        board[4][0] = new King(true, new int[]{4, 0});
        board[5][0] = new Bishop(true, new int[]{5, 0});
        board[6][0] = new Knight(true, new int[]{6, 0});
        board[7][0] = new Rook(true, new int[]{7, 0});

        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(true, new int[]{i, 1});
            board[i][6] = new Pawn(false, new int[]{i, 6});
        }
        
        board[0][7] = new Rook(false, new int[]{0, 7});
        board[1][7] = new Knight(false, new int[]{1, 7});
        board[2][7] = new Bishop(false, new int[]{2, 7});
        board[3][7] = new Queen(false, new int[]{3, 7});
        board[4][7] = new King(false, new int[]{4, 7});
        board[5][7] = new Bishop(false, new int[]{5, 7});
        board[6][7] = new Knight(false, new int[]{6, 7});
        board[7][7] = new Rook(false, new int[]{7, 7});

        moveTree.addMove(new Move());
    }

    public boolean move(int x1, int y1, int x2, int y2) {
        if (!isLegalMove(x1, y1, x2, y2, !moveTree.getMove().isWhite())) {
            return false;
        }

        createMove(x1, y1, x2, y2, (!moveTree.getMove().isWhite()));
        return true;
    }

    public boolean isPossibleMove(int x1, int y1, int x2, int y2, boolean isWhiteTurn) {
        ChessPiece piece = this.board[x1][y1];
        if (piece == null || piece.isWhite() != isWhiteTurn) {
            return false;
        }
    
        if (!piece.isLegalMove(x1, y1, x2, y2)) {
            return false;
        }
    
        //if (!(piece.getClass() == Knight.class) && !isClear(x1, y1, x2, y2, piece.getClass())) {
        //    return false;
        //}
    
        if (isCapture(x1, y1, x2, y2)) {
            if (this.board[x2][y2].isWhite() == isWhiteTurn) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<int[]> findPiece(int x2, int y2, boolean isWhiteTurn, Class<? extends ChessPiece> piece) {
        ArrayList<ChessPiece> pieces = getPiecesOfColor(isWhiteTurn);

        if (pieces.size() == 0) {
            throw new IllegalArgumentException();
        }

        ArrayList<int[]> positions = new ArrayList<>();

        for (ChessPiece currentPiece: pieces) {
            if (!(currentPiece.getClass() == piece) || !isPossibleMove(currentPiece.getPosition()[0], currentPiece.getPosition()[1], x2, y2, isWhiteTurn)) {
                continue;
            }
            positions.add(currentPiece.getPosition());
        }

        if (positions.size() == 0) {
            throw new IllegalArgumentException();
        }

        return positions;
    }

    private ArrayList<ChessPiece> getPiecesOfColor(boolean isWhiteTurn) {
        ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();

        for (int i=0; i < 8; i++) {
            for (int j=0; j < 8; j++) {
                if (this.board[i][j] != null && this.board[i][j].isWhite() == isWhiteTurn) {
                    pieces.add(this.board[i][j]);
                }
            }
        }

        return pieces;
    }

    public boolean isLegalMove(int x1, int y1, int x2, int y2, boolean isWhiteTurn) {
        if (!isPossibleMove(x1, y1, x2, y2, isWhiteTurn)) {
            return false;
        }
        
        //if (kingInCheck(this.board, getEnemyPieces(this.board, isWhiteTurn), isWhiteTurn) && putsKingInCheck(x1, y1, x2, y2, isWhiteTurn)) {
        //    return false;
        //}

        //if (putsKingInCheck(x1, y1, x2, y2, isWhiteTurn)) {
        //    if (isWhiteTurn == piece.isWhite())
        //    return false;
        //}
    
        return true;
    }

    public boolean isClear(int x1, int y1, int x2, int y2, Class<? extends ChessPiece> pieceType) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int xDir = x2 > x1 ? 1 : x2 < x1 ? -1 : 0;
        int yDir = y2 > y1 ? 1 : y2 < y1 ? -1 : 0;

        if (dx <= 1 && dy <= 1) {
            return true;
        }
        
        if (pieceType == Bishop.class) {
            if (dx == 0 || dy == 0 || dx != dy) {
                return true;
            }

            int x = x1 + xDir;
            int y = y1 + yDir;

            while (x != x2 && y != y2) {
                if (board[x][y] != null) {
                    return true;
                }
                x += xDir;
                y += yDir;
            }
        } else if (pieceType == Rook.class) {
            if ((dx == 0 && dy == 0) || (dx != 0 && dy != 0)) {
                return true;
            }

            if (dx > 0) {
                for (int x = x1 + xDir; x != x2; x += xDir) {
                    if (board[x][y1] != null) {
                        return true;
                    }
                }
            } else {
                for (int y = y1 + yDir; y != y2; y += yDir) {
                    if (board[x1][y] != null) {
                        return true;
                    }
                }
            }
        } else if (pieceType == Queen.class) {
            if ((dx == 0 && dy == 0) || (dx != 0 && dy != 0 && dx != dy)) {
                return true;
            }

            if (dx > 0 && dy == 0) {
                for (int x = x1 + xDir; x != x2; x += xDir) {
                    if (board[x][y1] != null) {
                        return true;
                    }
                }
            } else if (dx == 0 && dy > 0) {
                for (int y = y1 + yDir; y != y2; y += yDir) {
                    if (board[x1][y] != null) {
                        return true;
                    }
                }
            } else {
                int x = x1 + xDir;
                int y = y1 + yDir;

                while (x != x2 && y != y2) {
                    if (board[x][y] != null) {
                        return true;
                    }
                    x += xDir;
                    y += yDir;
                }
            }
        } else if (pieceType == King.class) {
            if (dx > 1 || dy > 1) {
                return true;
            }
        }

        return false;
    }

    public void createMove (int x1, int y1, int x2, int y2, boolean isWhiteTurn) {
        ChessPiece sourcePiece = board[x1][y1];
        ChessPiece destinationPiece = board[x2][y2];

        Runnable doMove = () -> {
            board[x1][y1] = null;
            board[x2][y2] = sourcePiece;
            board[x2][y2].setPosition(x2, y2);
        };
        Runnable undoMove = () -> {
            board[x1][y1] = sourcePiece;
            if (board[x1][y1] != null) {
                board[x1][y1].setPosition(x1, y1);
            }
            board[x2][y2] = destinationPiece;
            if (board[x2][y2] != null) {
                board[x2][y2].setPosition(x2, y2);
            }
        };

        int moveNumber = moveTree.getMove() == null? 0: moveTree.getMove().getMoveNumber();
        moveNumber += ((isWhiteTurn) ? 1 : 0);
        Move move = new Move(doMove, undoMove, isWhiteTurn, moveNumber, x1, y1, x2, y2);
        moveTree.addMove(move);

        moveTree.getMove().doMove();
    }

    private boolean isCapture(int x1, int y1, int x2, int y2) {
        if (board[x2][y2] != null) {
            return true;
        }
        return false;
    }

    private boolean putsKingInCheck(int x1, int y1, int x2, int y2, boolean isWhiteTurn) {
        ChessPiece alternateBoard[][] = new ChessPiece[8][8];

        for (int i=0; i < this.board.length; i++) {
            for (int j=0; i < this.board[i].length; j++) {
                alternateBoard[i][j] = this.board[i][j];
            }
        }

        ArrayList<ChessPiece> enemyPieces = getEnemyPieces(alternateBoard, isWhiteTurn);

        if (kingInCheck(alternateBoard, enemyPieces, isWhiteTurn)) {
            return true;
        }

        return false;
    }

    private ArrayList<ChessPiece> getEnemyPieces(ChessPiece[][] board, boolean isWhiteTurn) {
        ArrayList<ChessPiece> enemyPieces = new ArrayList<ChessPiece>();

        for (int i=0; i < this.board.length; i++) {
            for (int j=0; i < this.board[i].length; j++) {
                if (this.board[i][j] != null && this.board[i][j].isWhite() != isWhiteTurn) {
                    enemyPieces.add(this.board[i][j]);
                }
            }
        }

        return enemyPieces;
    }

    private boolean kingInCheck(ChessPiece[][] board, ArrayList<ChessPiece> enemyPieces, boolean isWhiteTurn) {
        ArrayList<ChessPiece> pieces = getPiecesOfColor(isWhiteTurn);

        ChessPiece king = null;

        for (ChessPiece piece: pieces) {
            if (piece.getClass() == King.class) king = piece;
        }
        
        for (ChessPiece piece: enemyPieces) {
            if (isPossibleMove(piece.getPosition()[0], piece.getPosition()[1], king.getPosition()[0], king.getPosition()[1], isWhiteTurn)) {
                return true;
            }
        }

        return false;
    }
}