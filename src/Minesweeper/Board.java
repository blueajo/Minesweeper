package Minesweeper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author blueajo
 *
 *         This class is a Minesweeper Board
 *
 */
public class Board extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int buttonSize = 50;

	private Square[][] grid;
	private int numMines, numMinesFlagged;
	private int numSafe, numSafeRevealed;
	private boolean initialized;

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
			throw new IllegalArgumentException("There cannot be more mines than squares.");
		}

		this.numMines = numMines;
		this.initialized = false;

		this.setTitle("Minesweeper");
		this.setMinimumSize(new Dimension(size * buttonSize, size * buttonSize));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.grid = new Square[size][size];

		setLayout(new GridLayout(size, size));

		// Initializes the squares within the array.
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				// Initially, squares are declared not to be mines.
				Square current = new Square(row, col);
				this.grid[row][col] = current;

				add(current);
				current.addMouseListener(this);
			}
		}
		
		this.addMouseListener(this);

		this.setVisible(true);

		this.pack();
	}

	/**
	 * called by actionPerformed when the first square is clicked generates a
	 * randomized Minesweeper board
	 * 
	 * @param clicked
	 */
	private void firstClick(Square clicked) {
		ArrayList<Square> mineOrder = new ArrayList<Square>();
		int size = this.grid.length;

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (!(clicked.row == row) && !(clicked.col == col)) {
					mineOrder.add(this.grid[row][col]);
				}
			}
		}

		// Randomizes the order of the squares to select mines from.
		Collections.shuffle(mineOrder);

		// Sets the first numMines Squares to be mines.
		for (int i = 0; i < numMines; i++) {
			mineOrder.get(i).isMine = true;
		}



		// Sets the number of adjacent mines for each square.
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (this.grid[row][col].isMine) {
					List<Square> adjacent = this.getAdjacent(this.grid[row][col]);

					for (Square adjSquare : adjacent) {
						if (!adjSquare.isMine) {
							adjSquare.numAdjacent++;
						}
					}
				}
			}
		}

		this.reveal(clicked);
		this.recurseReveal(clicked);

		// for testing purposes
		System.out.println(this);
	}

	public void recurseReveal(Square clicked) {
		List<Square> squares = this.getAdjacent(clicked);
		for (Square square : squares) {
			if (square.isMine) {
				return;
			}
		}

		recurseRevealHelper(clicked, this.getAdjacent(clicked));
	}

	private void recurseRevealHelper(Square clicked, List<Square> adjSquares) {

		for (Square square : adjSquares) {
			this.reveal(square);
			boolean recurseAgain = true;
			List<Square> squares = this.getAdjacent(square);
			for (Square squareTwo : squares) {
				if (squareTwo.isMine) {
					recurseAgain = false;
					break;
				}
			}
			if (recurseAgain) {
				recurseRevealHelper(square, squares);
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
		} else if (sq.isRevealed) {
			// recursively reveal
		}

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
	 * Flags the square that the user right clicks on. Does nothing if the user
	 * right clicks on an already revealed square.
	 * 
	 * @param sq
	 *            the square that the user right clicked
	 */
	public void rightClickSquare(Square sq) {
		if (!sq.isRevealed) {

			if (sq.isFlagged) {
				this.numMinesFlagged--;
			} else {
				this.numMinesFlagged++;
			}

			// Toggle square's flagged status:
			sq.isFlagged = !sq.isFlagged;
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
		sq.isRevealed = true;
		sq.setBackground(Color.CYAN);
		sq.setText("" + sq.numAdjacent);
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
		String output = "Mine layout:\n";

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid.length; col++) {
				output += grid[row][col].toString();
				output += " ";
			}
			output += "\n";
		}

		output += "\nAdjacent mines:\n";

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
	private List<Square> getAdjacent(Square sq) {
		List<Square> adjacent = new ArrayList<Square>();

		for (int i = sq.col - 1; i <= sq.col + 1; i++) {
			for (int j = sq.row - 1; j <= sq.row + 1; j++) {
				if (!(i == sq.col && j == sq.row) && isInBounds(i, j)) {
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
		return (row >= 0) && (row < this.grid.length) && (col >= 0) && (col < this.grid.length);
	}

	/**
	 * This class represents a single square on a minesweeper board.
	 */
	private static class Square extends JLabel {

		private static final long serialVersionUID = 1L;

		public int col, row;

		public boolean isMine;
		public boolean isRevealed;
		public boolean isFlagged;

		public int numAdjacent;

		/**
		 * Constructs a square with coordinates at the given x and y indices.
		 * Initializes the square as not revealed and not flagged. Initializes
		 * size to 10x10 pixels.
		 * 
		 * @param col
		 *            the column of the square
		 * @param row
		 *            the row of the square
		 */
		public Square(int row, int col) {
			super("", CENTER);

			this.isRevealed = false;
			this.isFlagged = false;
			this.col = col;
			this.row = row;
			this.numAdjacent = 0;

			this.setOpaque(true);
			this.setBackground(Color.LIGHT_GRAY);
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		/**
		 * @return "X" if the square is a mine, and "O" if it isn't
		 */
		@Override
		public String toString() {
			return (isMine) ? "X" : "O";
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		Square sq = (Square) e.getSource();
		
		if(sq.isRevealed) {
			sq.setBackground(Color.BLUE);
		} else {
			sq.setBackground(Color.DARK_GRAY);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int clickType = e.getButton();

		Square sq = (Square) e.getSource();
		
		if(sq.isRevealed) {
			sq.setBackground(Color.CYAN);
		} else {
			sq.setBackground(Color.LIGHT_GRAY);
		}
		
		if (clickType == 1) {
			this.leftClickSquare(sq);
		}

		else if (clickType == 3) {
			this.rightClickSquare(sq);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
