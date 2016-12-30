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
 * this class is bad. Help me be less bad please!!!
 * Basically, this is the window that holds the board object
 * and the option bar (which is currently only one button
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
	
	public MinesweeperWindow() {
		
		this.setTitle("Minesweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setPreferredSize(new Dimension(1000, 900));

		setLayout(new BorderLayout(0, 0));
		
		this.bar = new OptionBar();
		
		this.difficulty = "MEDIUM";
			
		board = this.makeBoard(this.difficulty);
		boardViewer = new JScrollPane(board);
		boardViewer.setPreferredSize(new Dimension(500,500));
		boardViewer.setOpaque(true);
		
		bar.setOpaque(true);
		
		this.add(bar, BorderLayout.PAGE_START);
		this.add(boardViewer, BorderLayout.CENTER);
		
		this.bar.playButton.addMouseListener(this);
		this.bar.solveButton.addMouseListener(this);
		this.bar.difficultyToggle.addMouseListener(this);
		this.bar.flagToggle.addMouseListener(this);
		
		this.pack();
		this.setVisible(true);
	}

	public void newGame() {
		this.remove(boardViewer);
		bar.updateMinesLeft(0);
		
		board = this.makeBoard(this.difficulty);
		boardViewer = new JScrollPane(board);
		boardViewer.setPreferredSize(new Dimension(500,500));
		boardViewer.setOpaque(true);
		
		this.add(boardViewer, BorderLayout.CENTER);
		this.setVisible(true);
	}

	private Board makeBoard(String difficulty) {
		Board board;
		switch (difficulty) {
			case "EASY":	board = new Board(9, 9, 10, this.bar);
							break;
			case "MEDIUM":	board = new Board(16, 16, 40, this.bar);
							break;
			case "HARD":	board = new Board(16, 30, 99, this.bar);
							break;
			default:		throw new IllegalArgumentException();
		}
		
		return board;
	}
	
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
			if(button.equals(this.bar.playButton)) {
				bar.stopTimer();
				bar.resetTimeLabel();
				button.setText("PLAY");
				this.newGame();
			} else if(button.equals(this.bar.difficultyToggle)) {
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
