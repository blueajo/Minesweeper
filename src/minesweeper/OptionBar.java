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

/**
 * This class is an option bar object for a minesweeper game. It contains
 * 		a timer which goes off when the game starts
 * 		a mines left indicator
 * 		a play button that also displays whether a game is won/lost
 * 		a solve button
 * 		a toggle for switching the difficulty of the board
 * 		a toggle for switching on and off flagging mode
 * 
 * @author blueajo
 *
 */

public class OptionBar extends JComponent implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel timeLabel;
	private int minutesElapsed, secondsElapsed;
	private Timer timerObject;
	
	private JLabel minesLeft;
	
	JLabel playButton;
	JLabel solveButton;
	JLabel difficultyToggle;
	JLabel flagToggle;
	
	/**
	 * creates an OptionBar object
	 */
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
	
	/**
	 * initializes the time display
	 */
	public void initTimeLabel() {
		this.timeLabel = new JLabel("00:00", SwingConstants.CENTER);
		this.resetTimeLabel();
		this.initButton(this.timeLabel);
	}
	
	/**
	 * resets the time display to 0
	 */
	public void resetTimeLabel() {
		this.secondsElapsed = 0;
		this.minutesElapsed = 0;
		this.timerObject = new Timer(1000, this);
		this.timeLabel.setText("00:00");
	}
	
	/**
	 * increments the time elapsed by 1 second every second
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// clock will stop at 99:01
		if(this.minutesElapsed >= 99) {
			this.stopTimer();
			return;
		}
		
		this.secondsElapsed++;
		timeLabel.setText(this.displayTime(this.minutesElapsed, this.secondsElapsed));
	}
	
	/**
	 * formats the time elapsed for the time label
	 * 
	 * @param minutes
	 * 		  minutes elapsed
	 * @param seconds
	 * 		  seconds elapsed
	 * @return
	 *        the formatted time
	 */
	private String displayTime(int minutes, int seconds) {
		if(seconds > 59) {
			minutes += seconds / 60;
			seconds = seconds % 60;
		}
		String time = "";
		if (minutes < 10) {
			time += "0";
		}
		time += String.valueOf(minutes) + ":";
		if(seconds < 10) {
			time += "0";
		}
		time += seconds;
		return time;
		
	}
	
	/**
	 * starts the timer
	 */
	public void startTimer() {
		if(!this.timerObject.isRunning()) {
			this.timerObject.start();
		}
	}
	
	/**
	 * stops the timer
	 */
	public void stopTimer() {
		if(this.timerObject.isRunning()) {
			this.timerObject.stop();
		}
	}
	
// minesLeft
	
	/**
	 * initializes the mines left display
	 */
	public void initMinesLeft() {
		this.minesLeft = new JLabel("0", SwingConstants.CENTER);
		this.initButton(this.minesLeft);
	}
	
	/**
	 * updates mines left to mines
	 * 
	 * @param mines
	 * 		  number of mines left
	 */
	public void updateMinesLeft(int mines) {
		this.minesLeft.setText(String.valueOf(mines));
	}
	
// playButton
	
	/**
	 * initializes the play button
	 */
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
	
	/**
	 * initializes the solve button
	 */
	public void initSolveButton() {
		this.solveButton = new JLabel("SOLVE", SwingConstants.CENTER);
		this.initButton(this.solveButton);
	}
	
// difficultyToggle
	
	/**
	 * initializes the difficulty toggle
	 */
	public void initDifficultyToggle() {
		this.difficultyToggle = new JLabel("MEDIUM", SwingConstants.CENTER);
		this.initButton(this.difficultyToggle);
	}
	
// flagToggle
	
	/**
	 * initializes the flag toggle
	 */
	public void initFlagToggle() {
		this.flagToggle = new JLabel("MODE", SwingConstants.CENTER);
		this.initButton(this.flagToggle);
	}
	
// general
	
	/**
	 * common initialization for the buttons
	 * @param button
	 * 		  the button to be initialized
	 */
	public void initButton(JLabel button) {
		button.setPreferredSize(new Dimension(100, 40));
		button.setMaximumSize(new Dimension(100, 40));
		button.setBackground(Color.LIGHT_GRAY);
		button.setOpaque(true);
		button.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(button);
	}
}
