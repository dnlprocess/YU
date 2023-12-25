package edu.yu.introtoalgs;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.yu.introtoalgs.QuestForOilBase;

public class QuestForOil extends QuestForOilBase{

    private class Tuple implements Comparable<Tuple> {
        int row;
        int column;
        int distTo;

        public Tuple (int row, int column, int distTo) {
            this.row = row;
            this.column = column;
            this.distTo = distTo;
        }

        @Override
        public int compareTo(Tuple other) {
            return this.distTo - other.distTo;
        }
    }

    private char[][] map;
    private int rows;//n
    private int columns;//m

    private final char safe = 'S';

    private int[] dRow = {-1, -1, 0, 1, 1, 1, 0, -1};//clockwise
    private int[] dColumn = {0, 1, 1, 1, 0, -1, -1, -1};
    /** Constructor supplies the map.
   *
   * @param map a non-null, N by M (not necessarily a square!), two-dimensional
   * matrix in which each element is either an 'S' (safe) or a 'U' (unsafe) to
   * walk on. It's the client's responsibility to ensure that the matrix isn't
   * "jagged". The client relinquishes ownership to the implementation.
   */
    public QuestForOil(char[][] map) {
        super(map);
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }

  /** Specifies the initial "start the search" square, explore the map to find
   * the maximum number of squares contiguous to that square (including the
   * "start the search" square itself).
   *
   * Note: the client is allowed to repeatedly invoke this method, e.g., with
   * different start search squares, on the same QuestForOil instance.
   *
   * @param row the row of the initial "start the search" square, 0..N-1
   * indexing.
   * @param column the column of the initial "start the search" square, 0..M-1
   * indexing.
   * @return the maximum number of squares contiguous to the inital square.
   */
    public int nContiguous(int row, int column) {
        if (!isValidSquare(row, column)) {
            return 0;
        }
        
        Queue<Tuple> q = new PriorityQueue<Tuple>();
        boolean[][] marked = new boolean[rows][columns];
        boolean[][] enqueued = new boolean[rows][columns];

        q.offer(new Tuple(row, column, 0));
        marked[row][column] = true;
        enqueued[row][column] = true;

        int count = bfs(q.peek(), marked, marked, q);

        return count;
    }

    private int bfs(Tuple position, boolean[][] marked, boolean[][] enqueued, Queue<Tuple> q) {
        //BFS makes the most sense and to prevent adding doubles can easily use 3rd map
        int count = 0;
        int rowStep;
        int columnStep;
        int distTo;

        while (!q.isEmpty()) {
            Tuple current = q.poll();

            if (!marked[current.row][current.column]) {
                continue;
            }

            count++;
            marked[current.row][current.column] = true;

            for (int i=0; i<8; i++) {//look over adjacent
                rowStep = current.row + dRow[i];
                columnStep = current.column + dColumn[i];
                if(isValidSquare(rowStep, columnStep) && !enqueued[rowStep][columnStep]) {
                    distTo = Math.abs(position.row - rowStep) + Math.abs(position.column - columnStep);
                    q.offer(new Tuple(rowStep, columnStep, distTo));
                    enqueued[rowStep][columnStep] = true;
                }
            }
        }
        return count;
    }

    private boolean isValidSquare(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns && map[row][column] == safe;
    }
}
