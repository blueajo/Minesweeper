package minesweeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
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
public class MinesweeperWindow extends JFrame implements ActionListener {
	
	JScrollPane board1;
	
	public MinesweeperWindow() {
		this.setTitle("Minesweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setPreferredSize(new Dimension(1000, 1000));

		setLayout(new BorderLayout(0, 0));
		
		JButton play = new JButton("Play");
		play.addActionListener(this);
		play.setOpaque(true);
		board1 = new JScrollPane(new Board(16, 16, 40));
		board1.setPreferredSize(new Dimension(500,500));
		board1.setOpaque(true);
		this.add(board1, BorderLayout.CENTER);
		this.add(play, BorderLayout.PAGE_START);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.remove(board1);
		board1 = new JScrollPane(new Board(16, 16, 40));
		board1.setPreferredSize(new Dimension(500,500));
		board1.setOpaque(true);
		this.add(board1, BorderLayout.CENTER);
		this.setVisible(true);
	}
}
