package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * 
 * This class is a window that holds a minesweeper Board and OptionBar
 * 
 * @author blueajo
 *
 */
public class MinesweeperWindow extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	OptionBar bar;
	Board board;
	JScrollPane boardViewer;
	
	String difficulty;
	
	/**
	 * Constructs and displays a Minesweeper game window
	 */
	public MinesweeperWindow() {
		
		this.setTitle("Minesweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setMinimumSize(new Dimension(700, 750));
		this.setPreferredSize(new Dimension(1000, 900));
		this.setMaximumSize(new Dimension(2000, 1000));
		this.pack();
		this.setLocationRelativeTo(null);
		setLayout(new BorderLayout(0, 0));
		
		this.bar = new OptionBar();
		
		this.difficulty = "MEDIUM";
			
		this.newGame();
		
		bar.setOpaque(true);
		
		this.add(bar, BorderLayout.PAGE_START);
		
		this.bar.playButton.addMouseListener(this);
		this.bar.solveButton.addMouseListener(this);
		this.bar.difficultyToggle.addMouseListener(this);
		this.bar.flagToggle.addMouseListener(this);
		
		this.setVisible(true);
	}

	/**
	 * Creates a new minesweeper game for the window
	 */
	private void newGame() {
		bar.updateMinesLeft(0);
		
		board = this.makeBoard(this.difficulty);
		boardViewer = new JScrollPane(board);
		boardViewer.setOpaque(true);
		
		this.add(boardViewer, BorderLayout.CENTER);
		this.setVisible(true);
	}

	/**
	 * creates a new board object according to this.difficulty
	 * if this.difficulty is EASY, creates a 9x9 bard with 10 mines
	 * 					 	 MEDIUM, creates a 16x16 board with 40 mines
	 * 						 HARD, creates a 30x16 board with 99 mines
	 * 
	 * @param  difficulty
	 * 		   the difficulty of the board created
	 * @throws IllegalArgumentException if difficulty is not
	 * 		   "EASY", "MEDIUM", or "HARD"
	 * @return the board created
	 */
	private Board makeBoard(String difficulty) {
		Board board;
		switch (difficulty) {
			case "EASY":	board = new Board(9, 9, 10, this.bar);
							break;
			case "MEDIUM":	board = new Board(16, 16, 40, this.bar);
							break;
			case "HARD":	board = new Board(30, 16, 99, this.bar);
							break;
			default:		throw new IllegalArgumentException();
		}
		
		return board;
	}
	
	/**
	 * toggles this.difficulty between EASY, MEDIUM, and HARD
	 */
	private void toggleDifficulty() {
		switch(this.difficulty) {
			case "EASY":	this.difficulty = "MEDIUM";
							break;
			case "MEDIUM":	this.difficulty = "HARD";
							break;
			case "HARD":	this.difficulty = "EASY";
							break;
			default:		throw new IllegalArgumentException();
		}
		
		this.bar.difficultyToggle.setText(this.difficulty);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource() instanceof JLabel) {
			JLabel button = (JLabel) e.getSource();
			button.setBackground(Color.GRAY);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() instanceof JLabel) {
			JLabel button = (JLabel) e.getSource();
			button.setBackground(Color.LIGHT_GRAY);
			
			if(button.equals(this.bar.playButton)) { // if source is play button
				bar.stopTimer();
				bar.resetTimeLabel();
				button.setText("PLAY");
				this.remove(boardViewer);
				this.newGame();
			} else if(button.equals(this.bar.difficultyToggle)) { // if source is difficulty toggle
				this.toggleDifficulty();
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
}
