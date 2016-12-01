package Minesweeper;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Board extends JFrame {
	
	public Board(int size, int numMines) {
		setTitle("minesweeper");
		setMinimumSize(new Dimension(size*10, size*10));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Square test = new Square(true, 10, 10);
		test.setVisible(true);
		
		
	}
	
	private static class Square extends JButton implements ActionListener {
		
		boolean isMine;
		boolean isRevealed;
		boolean isFlagged;
		int bx, by, bWidth, bHeight;
		
		
		public Square(boolean isMine, int xPosition, int yPosition) {
			this.isMine = isMine;
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
