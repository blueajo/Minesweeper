package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class OptionBar extends JComponent implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel timeLabel;
	private int timeElapsed;
	private Timer timerObject;
	
	private JLabel minesLeft;
	
	JLabel playButton;
	JLabel solveButton;
	JLabel difficultyToggle;
	JLabel flagToggle;
	
	public OptionBar() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		this.initTimeLabel();
		this.initMinesLeft();
		this.initPlayButton();
		this.initSolveButton();
		this.initDifficultyToggle();
		this.initFlagToggle();
		
		this.setVisible(true);
	}
	
	// timeLabel
	public void initTimeLabel() {
		this.timeLabel = new JLabel("00:00", SwingConstants.CENTER);
		this.resetTimeLabel();
		this.initButton(this.timeLabel);
	}
	
	public void resetTimeLabel() {
		this.timeElapsed = 0;
		this.timerObject = new Timer(1000, this);
		this.timeLabel.setText("0");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.timeElapsed++;
		timeLabel.setText(String.valueOf(timeElapsed));
	}
	
	public void startTimer() {
		if(!this.timerObject.isRunning()) {
			this.timerObject.start();
		}
	}
	
	public void stopTimer() {
		if(this.timerObject.isRunning()) {
			this.timerObject.stop();
		}
	}
	
	// minesLeft
	public void initMinesLeft() {
		this.minesLeft = new JLabel("0", SwingConstants.CENTER);
		this.initButton(this.minesLeft);
	}
	
	public void updateMinesLeft(int mines) {
		this.minesLeft.setText(String.valueOf(mines));
	}
	
	// playButton
	public void initPlayButton() {
		this.playButton = new JLabel("PLAY", SwingConstants.CENTER);
		this.playButton.setPreferredSize(new Dimension(100, 40));
		this.playButton.setMaximumSize(new Dimension(2000, 40));
		this.playButton.setBackground(Color.lightGray);
		this.playButton.setOpaque(true);
		this.playButton.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(this.playButton);
	}

	// solveButton
	public void initSolveButton() {
		this.solveButton = new JLabel("SOLVE", SwingConstants.CENTER);
		this.initButton(this.solveButton);
	}
	
	// difficultyToggle
	public void initDifficultyToggle() {
		this.difficultyToggle = new JLabel("MEDIUM", SwingConstants.CENTER);
		this.initButton(this.difficultyToggle);
	}
	
	// flagToggle
	public void initFlagToggle() {
		this.flagToggle = new JLabel("MODE", SwingConstants.CENTER);
		this.initButton(this.flagToggle);
	}
	
	// general
	public void initButton(JLabel button) {
		button.setPreferredSize(new Dimension(100, 40));
		button.setMaximumSize(new Dimension(100, 40));
		button.setBackground(Color.LIGHT_GRAY);
		button.setOpaque(true);
		button.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(button);
	}
}
