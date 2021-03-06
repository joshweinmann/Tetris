package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import javax.swing.*;

import tetris.Piece.Tetris;

public class Board extends JPanel implements ActionListener {

    /** width of game board */
    private static final int bWidth = 10;

    /** height of game board */
    private static final int bHeight = 22;

    /** timer used to regulate drop speeds */
    private Timer timer;

    /** used to know when to get next piece */
    private boolean atBottom = false;

    /** used to know when game is started */
    private boolean started = false;

    /** used to know if game is paused */
    private boolean paused = false;

    /** number of lines cleared */
    private int score = 0;

    /** current position of falling piece */
    private int curX = 0;
    private int curY = 0;

    /** bar to show score/game message */
    private final JLabel scorebar;

    /** current tetromino shape */
    private Piece curPiece;

    /** game board */
    private final Tetris[] board;

    /** high scores array **/
    private String[][] highScores = new String[10][2];

    /** current player name */
    private String playerName;

    /** color selector */
    private int color;

    /** multiplier for score */
    private double scoreMultiplier;

    /**
     * Default constructor. Sets up game
     * @param parent game object
     */
    public Board(Game parent) {
        setFocusable(true);
        curPiece = new Piece();
        timer = new Timer(parent.getSpeed(), this);
        color = parent.getColorNum();
        highScores = parent.getScores();
        playerName = parent.getPlayerName();
        timer.start();
        scorebar = parent.getStatusBar();
        board = new Tetris[bWidth * bHeight];
        addKeyListener(new TAdapter());
        clear();
    }

    /**
     * Change score multiplier based on timer speed
     */
    private void changeScoreMultiplier() {
        int timerSpeed = timer.getDelay();

        // set score multiplier
        if (timerSpeed == 100) {
            scoreMultiplier = 2;
        }
        else if (timerSpeed == 700) {
            scoreMultiplier = 0.5;
        }
        else {
            scoreMultiplier = 1;
        }
    }

    /**
     * Gets square width
     * @return int width of square
     */
    private int sqWidth() {
        return (int) getSize().getWidth() / bWidth;
    }

    /**
     * Gets square height
     * @return int height of square
     */
    private int sqHeight() {
        return (int) getSize().getWidth() / bHeight;
    }

    /**
     * Gets piece at coordinate position
     * @param x coordinate
     * @param y coordinate
     * @return position of shape
     */
    private Tetris pieceAt(int x, int y) {
        return board[(y * bWidth) + x];
    }

    /**
     * Resets game and starts a new one
     */
    public void start() {
        // check if game is paused
        if (paused) {
            return;
        }

        started = true;
        atBottom = false;
        score = 0;
        clear();

        newPiece();
        timer.start();
    }

    /**
     * Pauses the game
     */
    private void pause() {

        // check if game is already paused
        if (!started)
            return;

        paused = !paused;
        if (paused) {
            timer.stop();
            scorebar.setText("Paused");
        } else {
            timer.start();
            changeScoreMultiplier();
            scorebar.setText("Score: " + String.valueOf((int)(score * 100 * scoreMultiplier)));
        }
        repaint();
    }

    /**
     * Set colors to use for tetromino pieces
     * @param c int to choose color array
     * @return color array to use
     */
    private Color[] chooseColor(int c) {

        // set colors
        Color colors[];

        // default colors
        if (c == 0) {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(255, 0, 0),
                    new Color(0, 255, 37),
                    new Color(0, 10, 255),
                    new Color(240, 238, 0),
                    new Color(204, 0, 222),
                    new Color(0, 241, 239),
                    new Color(255, 154, 0)
            };
        }
        // dull colors
        else if (c == 1) {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(135, 0, 0),
                    new Color(0, 125, 22),
                    new Color(0, 4, 130),
                    new Color(179, 178, 0),
                    new Color(123, 0, 133),
                    new Color(0, 128, 127),
                    new Color(182, 110, 0)
            };
        }
        // black and white colors
        else if (c == 2) {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(255, 255, 255),
                    new Color(168, 168, 168),
                    new Color(21, 21, 21),
                    new Color(113, 113, 113),
                    new Color(73, 73, 73),
                    new Color(100, 100, 100),
                    new Color(220, 220, 220)
            };
        }

        // fall colors
        else if (c == 3) {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(187, 0, 0),
                    new Color(238, 236, 0),
                    new Color(242, 140, 0),
                    new Color(192, 111, 0),
                    new Color(172, 171, 0),
                    new Color(242, 0, 0),
                    new Color(154, 88, 0)
            };
        }

        // spring colors
        else if (c == 4) {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(0, 232, 255),
                    new Color(0, 255, 44),
                    new Color(0, 169, 29),
                    new Color(255, 253, 0),
                    new Color(255, 0, 254),
                    new Color(177, 0, 176),
                    new Color(0, 10, 255)
            };
        }

        // black colors
        else if (c == 5) {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(0, 0, 0),
                    new Color(0, 0, 0),
                    new Color(0, 0, 0),
                    new Color(0, 0, 0),
                    new Color(0, 0, 0),
                    new Color(0, 0, 0),
                    new Color(0, 0, 0)
            };
        }

        // set default colors
        else {
            colors = new Color[]{
                    new Color(0, 0, 0),
                    new Color(255, 0, 0),
                    new Color(0, 255, 37),
                    new Color(0, 10, 255),
                    new Color(240, 238, 0),
                    new Color(204, 0, 222),
                    new Color(0, 241, 239),
                    new Color(255, 154, 0)
            };
        }
        return colors;
    }

    /**
     * Fills color for pieces
     * @param g graphics object
     * @param x coordinate
     * @param y coordinate
     * @param piece tetromino shape
     */
    private void fillPiece(Graphics g, int x, int y, Tetris piece) {
        Color colors[] = chooseColor(color);

        // set of colors
        Color c = colors[piece.ordinal()];

        // logic to fill shapes with colors
        g.setColor(c);
        g.fillRect(x + 1, y + 1, sqWidth() - 2, sqHeight() - 2);
        g.setColor(c.brighter());
        g.drawLine(x, y + sqHeight() - 1, x, y);
        g.drawLine(x, y, x + sqWidth() - 1, y);
        g.setColor(c.darker());
        g.drawLine(x + 1, y + sqHeight() - 1, x + sqWidth() - 1, y + sqHeight() - 1);
        g.drawLine(x + sqWidth() - 1, y + sqHeight() - 1, x + sqWidth() - 1, y + 1);
    }

    /**
     * Colors game piece
     * @param g graphics objects
     */
    public void paint(Graphics g) {

        super.paint(g);

        Dimension s = getSize();

        int boardTop = (int) s.getHeight() - bHeight * sqHeight();

        for (int i = 0; i < bHeight; ++i) {
            for (int j = 0; j < bWidth; ++j) {
                Tetris shape = pieceAt(j, bHeight - i - 1);
                if (shape != Tetris.emptyPiece) {
                    fillPiece(g, j * sqWidth(), boardTop + i * sqHeight(), shape);
                }
            }
        }

        if (curPiece.getPiece() != Tetris.emptyPiece) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.adjustX(i);
                int y = curY - curPiece.adjustY(i);
                fillPiece(g, x * sqWidth(), boardTop + (bHeight - y - 1) * sqHeight(), curPiece.getPiece());
            }
        }
    }

    /**
     * Checks if space is already occupied
     * @param newPiece tetromino shape
     * @param newX x coordinate
     * @param newY y coordinate
     * @return boolean
     */
    private boolean move(Piece newPiece, int newX, int newY) {

        // try move
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.adjustX(i);
            int y = newY - newPiece.adjustY(i);

            // check if piece has room to attempt move
            if (x < 0 || x >= bWidth || y < 0 || y >= bHeight) {
                return false;
            }
            if (pieceAt(x, y) != Tetris.emptyPiece) {
                return false;
            }
        }

        // set new piece
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    /**
     * Drops piece one line down
     */
    private void moveOneLineDown() {
        if (!move(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    /**
     * Clears board of all pieces
     */
    private void clear() {
        for (int i = 0; i < bHeight * bWidth; ++i) {
            board[i] = Tetris.emptyPiece;
        }
    }

    /**
     * Removes line if full
     */
    private void removeFullLines() {
        int lines = 0;

        for (int i = bHeight - 1; i >= 0; --i) {
            boolean fullLine = true;

            for (int j = 0; j < bWidth; ++j) {
                if (pieceAt(j, i) == Tetris.emptyPiece) {
                    fullLine = false;
                    break;
                }
            }

            if (fullLine) {
                ++lines;
                for (int k = i; k < bHeight - 1; ++k) {
                    for (int j = 0; j < bWidth; ++j)
                        board[(k * bWidth) + j] = pieceAt(j, k + 1);
                }
            }
        }

        // add removed lines to score
        if (lines > 0) {
            score += lines;
            changeScoreMultiplier();
            scorebar.setText("Score: " + String.valueOf((int)(score * 100 * scoreMultiplier)));
            atBottom = true;
            curPiece.setPiece(Tetris.emptyPiece);
            repaint();
        }
    }

    /**
     * Checks if a line is full
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.adjustX(i);
            int y = curY - curPiece.adjustY(i);
            board[(y * bWidth) + x] = curPiece.getPiece();
        }
        removeFullLines();

        // check if piece is at the bottom
        if (!atBottom) {
            newPiece();
        }
    }

    /**
     * Picks new piece to drop next
     */
    private void newPiece() {

        // pick next piece
        curPiece.pickRandomPiece();
        curX = bWidth / 2 + 1;
        curY = bHeight - 1 + curPiece.minY();

        gameOver();
    }

    /**
     * Logic for when game is over
     */
    private void gameOver() {

        // check if board is full
        if (!move(curPiece, curX, curY)) {
            curPiece.setPiece(Tetris.emptyPiece);
            timer.stop();
            started = false;
            if ( Integer.parseInt(String.valueOf(score * 100)) > Integer.parseInt(highScores[9][1]) ) {
                changeScoreMultiplier();
                scorebar.setText("New Top 10 High Score!: " + String.valueOf((int)(score * 100 * scoreMultiplier)));
                highScores[9][0] = playerName;
                highScores[9][1] = Integer.toString(score * 100);
                saveScores();
            }
            else {
                changeScoreMultiplier();
                scorebar.setText("Game Over! Score: " + String.valueOf((int)(score * 100 * scoreMultiplier)));
            }

            String[] options = {"Yes", "No"};
            int x = JOptionPane.showOptionDialog(null, "Would you like to start a New Game?",
                    "Game Over!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            // check popup options
            if (x == 0) {
                new Game();
            }
            else {
                System.exit(0);
            }
        }
    }

    /**
     * Saves scores to text file to keep track of high scores
     */
    private void saveScores() {


        try {
            FileWriter writer = new FileWriter("highscores.dat");
            BufferedWriter buffer = new BufferedWriter(writer);
            for (int j = 0; j < highScores.length; j++) {
                for (int k = 0; k < 2; k++) {
                    buffer.write(highScores[j][k]);
                    if ( k==0 ) {
                        buffer.write(",");
                    } else {
                        buffer.write("\n");
                    }
                }
            }
            buffer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drops piece into lowest position
     */
    private void instantDrop() {
        int newY = curY;

        // drop piece while there is an empty line below
        while (newY > 0) {
            if (!move(curPiece, curX, newY - 1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }

    /**
     * Checks when to get new piece
     * @param e action event
     */
    public void actionPerformed(ActionEvent e) {

        // check if piece is at bottom
        if (atBottom) {
            atBottom = false;
            newPiece();
        } else {
            moveOneLineDown();
        }
    }

    /**
     * Key adapter class
     */
    class TAdapter extends KeyAdapter {

        /**
         * Checks when keys are hit to make moves
         * @param e key pressed
         */
        public void keyPressed(KeyEvent e) {

            if (!started || curPiece.getPiece() == Tetris.emptyPiece) {
                return;
            }

            // code for key pressed
            int keycode = e.getKeyCode();

            // pause game
            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            // can't do moves of game is paused
            if (paused) {
                return;
            }

            // controls for moves
            switch (keycode) {

                // move left
                case KeyEvent.VK_LEFT:
                    move(curPiece, curX - 1, curY);
                    break;

                // move right
                case KeyEvent.VK_RIGHT:
                    move(curPiece, curX + 1, curY);
                    break;

                // rotate left
                case KeyEvent.VK_DOWN:
                    move(curPiece.rotateLeft(), curX, curY);
                    break;

                // rotate right
                case KeyEvent.VK_UP:
                    move(curPiece.rotateRight(), curX, curY);
                    break;

                // instant drop to bottom
                case KeyEvent.VK_SPACE:
                    instantDrop();
                    break;

                // speed up drop
                case KeyEvent.VK_SHIFT:
                    moveOneLineDown();
                    break;

                // any other key
                default :
                    break;
            }
        }
    }
}