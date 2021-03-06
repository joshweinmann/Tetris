package tetris;

import java.util.Random;
import java.lang.Math;

public class Piece {

    /** tetrominos shapes */
    enum Tetris {emptyPiece, zPiece, sPiece, linePiece, tPiece, squarePiece, lPiece, mlPiece}

    /** tetrominos shape object */
    private Tetris pieces;

    /** coordinates to make tetromino piece */
    private final int[][] pieceCoords;

    /**
     * Default constructor sets pieceCoords with placeholder empty shape
     */
    public Piece() {
        pieceCoords = new int[4][2];
        setPiece(Tetris.emptyPiece);
    }

    /**
     * Creates new tetromino shape
     * @param shape tetromino shape
     */
    public void setPiece(Tetris shape) {

        /* array of coordinates of created piece shapes */
        int[][][] createCoord = new int[][][]{

                // empty shape
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},

                // Z shape
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},

                // S shape
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},

                // Line shape
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},

                // T shape
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},

                // Square shape
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},

                // L shape
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},

                // Mirrored L shape
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };

        // loop to set coordinates and build shapes
        for (int i = 0; i < 4 ; i++) {
            System.arraycopy(createCoord[shape.ordinal()][i], 0, pieceCoords[i], 0, 2);
        }

        // set piece
        pieces = shape;

    }

    /**
     * Returns tetris piece
     * @return Tetris piece object
     */
    public Tetris getPiece() {
        return pieces;
    }

    /**
     * Chooses a random tetromino piece
     */
    public void pickRandomPiece() {
        Random rand = new Random();
        int x = rand.nextInt(Integer.MAX_VALUE) % 7 + 1;
        Tetris[] num = Tetris.values();
        setPiece(num[x]);
    }

    /**
     * Used to adjust x coordinates when piece is rotated
     * @param i old x coordinate
     * @param x new x coordinate
     */
    private void changeX(int i, int x) {
        pieceCoords[i][0] = x;
    }

    /**
     * Used to adjust y coordinates when piece is rotated
     * @param i old y coordinate
     * @param y nre y coordinate
     */
    private void changeY(int i, int y) {
        pieceCoords[i][1] = y;
    }

    /**
     * Used to adjust x coordinates when piece is rotated
     * @param i x coordinate
     * @return new coordinates
     */
    public int adjustX(int i) {
        return pieceCoords[i][0];
    }

    /**
     * Used to adjust y coordinates when piece is rotated
     * @param i y coordinate
     * @return new coordinates
     */
    public int adjustY(int i) {
        return pieceCoords[i][1];
    }

    /**
     * Rotates the tetromino to the right
     * @return Tetris rotPiece the new piece coordinates
     */
    public Piece rotateRight() {

        // square piece doesn't change when rotated
        if (pieces == Tetris.squarePiece) {
            return this;
        }

        Piece rotPiece = new Piece();
        rotPiece.pieces = pieces;

        // change coordinates of shape from rotation
        for (int i = 0; i < 4; ++i) {
            rotPiece.changeX(i, -adjustY(i));
            rotPiece.changeY(i, adjustX(i));
        }
        return rotPiece;
    }

    /**
     * Rotates the tetromino to the left
     * @return Tetris rotPiece the new piece coordinates
     */
    public Piece rotateLeft() {

        // square piece doesn't change when rotated
        if (pieces == Tetris.squarePiece) {
            return this;
        }

        Piece rotPiece = new Piece();
        rotPiece.pieces = pieces;

        // change coordinates of shape from rotation
        for (int i = 0; i < 4; ++i) {
            rotPiece.changeX(i, adjustY(i));
            rotPiece.changeY(i, -adjustX(i));
        }
        return rotPiece;
    }

    /**
     * Sets where the piece starts from the top based on the size of the piece
     * @return int min the minimum top-position
     */
    public int minY()
    {
        int min = pieceCoords[0][1];
        for (int i = 0; i < 4; i++) {
            min = Math.min(min, pieceCoords[i][1]);
        }
        return min;
    }
}