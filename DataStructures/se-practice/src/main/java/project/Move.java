package project;

public class Move {
    private final boolean isWhite;
    private final int moveNumber;
    private final Runnable doMove;
    private final Runnable undoMove;
    private int[] locations = new int[4];

    public Move(Runnable doMove, Runnable undoMove, boolean isWhiteTurn, int moveNumber, int x1, int y1, int x2, int y2) {
        this.doMove = doMove;
        this.undoMove = undoMove;
        this.isWhite = isWhiteTurn;
        this.moveNumber = moveNumber;
        this.locations[0] = x1;
        this.locations[1] = y1;
        this.locations[2] = x2;
        this.locations[3] = y2;
    }

    public Move() {
        this.isWhite = false;

        this.doMove = null;
        this.undoMove = null;
        this.moveNumber = 0;
        this.locations[0] = 0;
        this.locations[1] = 0;
        this.locations[2] = 0;
        this.locations[3] = 0;
    }

    public void doMove() {
        doMove.run();
    }

    public void undoMove() {
        undoMove.run();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getMoveNumber() {
        return this.moveNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        }

        Move otherMove = (Move) o;
        return this.hashCode() == otherMove.hashCode();
    }

    @Override
    public int hashCode() {
        int result = ((isWhite) ? 1 : 0);
        for (int position: locations) {
            result += position;
            result *= 31;
        }
        return result;
    }

    public int[] getLocations() {
        return this.locations;
    }
}