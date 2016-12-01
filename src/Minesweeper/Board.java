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

    public Board(int size, int numMines) {
        
        if (numMines > size * size) {
            throw new IllegalArgumentException("There cannot be more mines than squares.");
        }
        
        setTitle("Minesweeper");
        setMinimumSize(new Dimension(size * 10, size * 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Square test = new Square(10, 10);
        test.setVisible(true);
        
        ArrayList<Square> mineOrder = new ArrayList<>();
        this.grid = new Square[size][size];
        
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
                    ArrayList<Square> adjacent = this.getAdjacent(row, col);
                    
                    for (Square adjSquare : adjacent) {
                        if (!adjSquare.isMine) {
                            adjSquare.numAdjacent++;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        String output = "";
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                char text = (grid[row][col].isMine) ? 'X' : 'O';
                output += text;
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
    
    private ArrayList<Square> getAdjacent(int row, int col) {
        ArrayList<Square> adjacent = new ArrayList<Square>();
        
        for (int i = row-1; i <= row+1; i++ ) {
            for (int j = col-1; j <= col+1; j++) {
                if (i != row && j != col && isInBounds(i, j)) {
                    adjacent.add(this.grid[i][j]);
                }
            }
        }
        
        return adjacent;
    }
    
    private boolean isInBounds(int row, int col) {
        return (row >= 0) && (row < this.grid.length) && (col >= 0) && (col < this.grid.length);
    }

    private static class Square extends JButton implements ActionListener {
        private static final long serialVersionUID = 1L;
        
        public int bx, by;
        public int bWidth, bHeight;
        
        public boolean isMine;
        public boolean isRevealed;
        public boolean isFlagged;
        
        public int numAdjacent;

        public Square(int xPosition, int yPosition) {
            this.isRevealed = false;
            this.isFlagged = false;
            this.bx = xPosition;
            this.by = yPosition;
            this.bWidth = 10;
            this.bHeight = 10;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
