package Minesweeper;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Board extends JFrame {

    private static final long serialVersionUID = 1L;

    private Square[][] grid;

    /**
     * Constructs a square minesweeper board with length and width equal to
     * size, and a numMines number of mines.
     * 
     * @param size
     *            the length and width of the board
     * @param numMines
     *            the number of mines to initialize
     * @throws IllegalArgumentException
     *             if the number of mines is greater than the number of squares
     */
    public Board(int size, int numMines) {

        if (numMines > size * size) {
            throw new IllegalArgumentException(
                    "There cannot be more mines than squares.");
        }

        setTitle("Minesweeper");
        setMinimumSize(new Dimension(size * 10, size * 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Square test = new Square(10, 10);
        test.setVisible(true);

        this.grid = new Square[size][size];
        ArrayList<Square> mineOrder = new ArrayList<>();

        // Initializes the squares within the array.
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Initially, squares are declared not to be mines.
                Square current = new Square(row, col);
                this.grid[row][col] = current;

                mineOrder.add(current);
            }
        }

        // Randomizes the order of the squares to select mines from.
        Collections.shuffle(mineOrder);

        // Sets the first numMines Squares to be mines.
        for (int i = 0; i < numMines; i++) {
            mineOrder.get(i).isMine = true;
        }

        // Sets the rest of the squares to be safe.
        for (int i = numMines; i < size * size; i++) {
            mineOrder.get(i).isMine = false;
        }

        // Sets the number of adjacent mines for each square.
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.grid[row][col].isMine) {
                    ArrayList<Square> adjacent = this
                            .getAdjacent(this.grid[row][col]);

                    for (Square adjSquare : adjacent) {
                        if (!adjSquare.isMine) {
                            adjSquare.numAdjacent++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Runs through the process of the user left-clicking on a tile. If the tile
     * is flagged or has already been revealed, nothing occurs.
     * 
     * @param sq
     *            the square the user clicked on
     */
    public void leftClickSquare(Square sq) {
        if (!sq.isFlagged && !sq.isRevealed) {
            // Case when the square is a mine, and the user has lost:
            if (sq.isMine) {
                this.endGame(false);
            }

            // Case when the square is not a mine, and becomes revealed:
            else {
                this.reveal(sq);

                // If the square has no adjacent mines, reveals all adjacent
                // squares.
                if (sq.numAdjacent == 0) {
                    for (Square adjacent : this.getAdjacent(sq)) {
                        this.reveal(adjacent);
                    }
                }
            }
        }
    }

    /**
     * Reveals the given square if it is not flagged and *does graphical stuff*
     * associated with revealing it.
     * 
     * @param sq
     *            the square to reveal
     */
    private void reveal(Square sq) {
        if (!sq.isRevealed) {
            sq.isRevealed = true;

            // Do more graphical stuff

        }
    }

    /**
     * Runs the end of the game process. Behavior depends on if the user won.
     * 
     * @param wasGameWon
     *            true if the user won the game, false if the user lost
     */
    private void endGame(boolean wasGameWon) {
        // Do stuff
    }

    /**
     * Currently returns a grid composed of "X" and "O" representing mines and
     * non-mines respectively.
     * 
     * @return a 2D representation of the mine locations, indicated by "X"s
     */
    @Override
    public String toString() {
        String output = "";

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                output += grid[row][col].toString();
                output += " ";
            }
            output += "\n";
        }

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                output += this.grid[row][col].numAdjacent;
                output += " ";
            }
            output += "\n";
        }

        return output;
    }

    /**
     * Returns an ArrayList of squares that are adjacent to the given square.
     * 
     * @param sq
     *            the given square to find adjacent squares of
     * @return an ArrayList of squares that are next to (within a distance of 1)
     *         the square at the given coordinates
     */
    private ArrayList<Square> getAdjacent(Square sq) {
        ArrayList<Square> adjacent = new ArrayList<Square>();

        for (int i = sq.xPos - 1; i <= sq.xPos + 1; i++) {
            for (int j = sq.yPos - 1; j <= sq.yPos + 1; j++) {
                if ((i != sq.xPos | j != sq.yPos) && isInBounds(i, j)) {
                    adjacent.add(this.grid[i][j]);
                }
            }
        }

        return adjacent;
    }

    /**
     * Determines whether the square at the given coordinates is within the
     * grid's bounds.
     * 
     * @param row
     *            the index of the row
     * @param col
     *            the index of the column
     * @return true if the square is in the grid, false otherwise
     */
    private boolean isInBounds(int row, int col) {
        return (row >= 0) && (row < this.grid.length) && (col >= 0)
                && (col < this.grid.length);
    }

    private static class Square extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;

        public int xPos, yPos;
        public int bWidth, bHeight;

        public boolean isMine;
        public boolean isRevealed;
        public boolean isFlagged;

        public int numAdjacent;

        /**
         * Constructs a square with coordinates at the given x and y indices.
         * Initializes the square as not revealed and not flagged. Initializes
         * size to 10x10 pixels.
         * 
         * @param xPosition
         *            the x coordinate of the square
         * @param yPosition
         *            the y coordinate of the square
         */
        public Square(int xPosition, int yPosition) {
            this.isRevealed = false;
            this.isFlagged = false;
            this.xPos = xPosition;
            this.yPos = yPosition;
            this.bWidth = 10;
            this.bHeight = 10;
        }

        /**
         * @return "X" if the square is a mine, and "O" if it isn't
         */
        @Override
        public String toString() {
            return (isMine) ? "X" : "O";
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
