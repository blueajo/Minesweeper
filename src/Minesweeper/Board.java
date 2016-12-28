package minesweeper;

import java.awt.Dimension;
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
 *         This class is a Minesweeper Board.
 *         When instantiated, it generates a game of minesweeper
 *
 */
public class Board extends JComponent implements MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int buttonSize = 75;

	private Square[][] grid;
	private int rows, cols;
	private int numMines, numMinesFlagged;
	private int numSafe, numSafeRevealed;
	private boolean isFirstClick;
	private boolean gameOver;

	/**
	 * Constructs a square minesweeper board with length and width equal to
	 * size, and a numMines number of mines.
	 * 
	 * @param rows
	 *            the number of rows in the board
	 * @param cols
	 * 			  the number of columns in the board
	 * @param numMines
	 *            the number of mines to initialize
	 * @throws IllegalArgumentException
	 *             for invalid numbers of mines, rows, and columns
	 */
	public Board(int rows, int cols, int numMines) {

		if(rows < 3 || cols < 3) {
			throw new IllegalArgumentException("rows and cols must be at least 3");
		}
		
		if (numMines > rows * cols - 9 || numMines < 0) {
			throw new IllegalArgumentException("For a " + rows + "x" + cols +
					" board, there must be fewer than " + (rows * cols - 9) + "squares");
		}

		this.rows = rows;
		this.cols = cols;
		
		this.numMines = numMines;
		this.numMinesFlagged = 0;

		this.numSafe = (rows * cols) - this.numMines;
		this.numSafeRevealed = 0;

		this.isFirstClick = true;
		
		this.gameOver = false;

		this.setPreferredSize(new Dimension(rows * buttonSize, cols * buttonSize));

		this.grid = new Square[rows][cols];

		setLayout(new GridLayout(rows, cols));

		// Initializes the squares within the array.
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Initially, squares are declared not to be mines.
				Square current = new Square(row, col);
				this.grid[row][col] = current;

				add(current);
				current.addMouseListener(this);
			}
		}

		this.addMouseListener(this);

		this.setVisible(true);
	}

	/**
	 * The action performed the first time the user left clicks a square.
	 * Ensures that sq and none of its adjacent squares are mines.
	 * 
	 * @param sq
	 *            the Square that's clicked
	 */
	private void firstClick(Square sq) {
		this.isFirstClick = false;

		List<Square> mineOrder = new ArrayList<Square>();

		// Creates a list of squares guaranteed to not be mines.
		List<Square> sqAndAdjacent = new ArrayList<Square>();
		sqAndAdjacent = this.getAdjacent(sq);
		sqAndAdjacent.add(sq);

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				if (!sqAndAdjacent.contains(this.grid[row][col])) {
					mineOrder.add(this.grid[row][col]);
				}
			}
		}

		// Randomizes the order of the squares to select mines from.
		Collections.shuffle(mineOrder);

		// Sets the first numMines Squares to be mines.
		for (int i = 0; i < this.numMines; i++) {
			mineOrder.get(i).isMine = true;
		}

		// Sets the number of adjacent mines for each square.
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
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

		this.reveal(sq);
	}

	/**
	 * Reveals all squares in the given list.
	 * 
	 * @param squares
	 *            the list of squares
	 */
	private void reveal(List<Square> squares) {
		for (Square sq : squares) {
			this.reveal(sq);
		}
	}

	/**
	 * Reveals the given square if it is not flagged and not already revealed.
	 * 
	 * @param sq
	 *            the given square
	 */
	private void reveal(Square sq) {
		if (!sq.isFlagged && !sq.isRevealed) {
			fillSquare(sq);
			
			// If the square is a mine, then the user loses.
			if (sq.isMine) {
				sq.setBackground(Color.RED);
				this.endGame(false);
				
			} else {
				sq.isRevealed = true;
				this.numSafeRevealed++;

				// If the user has revealed every safe square, then the user wins.
				if (this.numSafeRevealed == this.numSafe) {
					this.endGame(true);
				}

				// If the square had 0 adjacent mines, reveals every adjacent
				// square.
				else if (sq.numAdjacent == 0) {
					this.reveal(this.getAdjacent(sq));
				}
			}
		}
	}
	
	/**
	 * Fills the square, revealing whether it was a mine, it was clicked,
	 * or how many mines were adjacent to it.
	 * 
	 * @param sq
	 * 			the square to be filled
	 */
	private void fillSquare(Square sq) {
		String text;
		if (sq.isMine) {
			if(!sq.getBackground().equals(Color.RED)) {
				sq.setBackground(Color.GRAY);
			}
			text = "";
		} else if(!sq.isMine && sq.isFlagged) {
			sq.setBackground(Color.CYAN);
			text = "X";
		} else {
			sq.setBackground(Color.CYAN);

			if (sq.numAdjacent > 0) {
				text = String.valueOf(sq.numAdjacent);
			} else {
				text = "";
			}
		}
		sq.setText(text);
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
			this.reveal(sq);
			
			if (sq.isMine) {
				this.endGame(false);
			} else {
				// If the square has no adjacent mines, reveals all adjacent
				// squares.
				if (sq.numAdjacent == 0) {
					this.reveal(this.getAdjacent(sq));
				}
			}
		} else if (sq.isRevealed && sq.numAdjacent == this.numAdjacentFlagged(sq)) {
			this.reveal(this.getAdjacent(sq));
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
				sq.setText("");
			} else {
				this.numMinesFlagged++;
				sq.setText("F");
			}

			// Toggle square's flagged status:
			sq.isFlagged = !sq.isFlagged;
		}
	}

	/**
	 * Runs the end of the game process. Behavior depends on if the user won.
	 * 
	 * @param wasGameWon
	 *            true if the user won the game, false if the user lost
	 */
	private void endGame(boolean wasGameWon) {
		if(!this.gameOver) {
			this.gameOver = true;
			
			for (int row = 0; row < this.rows; row++) {
				for (int col = 0; col < this.cols; col++) {
					if(!this.grid[row][col].isRevealed){
						fillSquare(this.grid[row][col]);
					}
				}
			}
			
			if(wasGameWon) {
				System.out.println("YOU WON!");
			} else {
				System.out.println("YOU LOSE");
			}
		}
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

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				output += grid[row][col].toString();
				output += " ";
			}
			output += "\n";
		}

		output += "\nAdjacent mines:\n";

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
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

		for (int i = sq.row - 1; i <= sq.row + 1; i++) {
			for (int j = sq.col - 1; j <= sq.col + 1; j++) {
				if (!(i == sq.row && j == sq.col) && isInBounds(i, j)) {
					adjacent.add(this.grid[i][j]);
				}
			}
		}

		return adjacent;
	}

	/**
	 * Returns the number of squares which have been flagged adjacent to a given
	 * square.
	 * 
	 * @param sq
	 *            the given square
	 * @return the number of adjacent squares which have been flagged
	 */
	private int numAdjacentFlagged(Square sq) {
		int count = 0;

		for (Square adjacent : this.getAdjacent(sq)) {
			if (adjacent.isFlagged) {
				count++;
			}
		}
		return count;
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
		return (row >= 0) && (row < this.rows) && (col >= 0) && (col < this.cols);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		Object clickLocation = e.getSource();
		if (clickLocation instanceof Square && !gameOver) {
			Square sq = (Square) e.getSource();

			if (sq.isRevealed) {
				sq.setBackground(Color.BLUE);
			} else {
				sq.setBackground(Color.DARK_GRAY);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int clickType = e.getButton();
		Object clickLocation = e.getSource();

		if (clickLocation instanceof Square && !gameOver) {
			Square sq = (Square) e.getSource();

			if (sq.isRevealed) {
				sq.setBackground(Color.CYAN);
			} else {
				sq.setBackground(Color.LIGHT_GRAY);
			}

			// Left click:
			if (clickType == 1 && !e.isControlDown()) {
				if (this.isFirstClick) {
					this.firstClick(sq);
				}

				else {
					this.leftClickSquare(sq);
				}
			}

			// Right click:
			else if (clickType == 3 || e.isControlDown()) {
				this.rightClickSquare(sq);
			}
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
		 * Initializes the square as not revealed, not flagged, and not a mine.
		 * 
		 * @param col
		 *            the column of the square
		 * @param row
		 *            the row of the square
		 */
		public Square(int row, int col) {
			super("", CENTER);

			this.isMine = false;
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

}
