
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class LeapingFrogsPuzzle extends JFrame
{
	//initializing necessary variables
	private JButton grnBtn; 
	private JButton blkBtn; 
	private JButton ngBtn;	//for "New Game" Button
	private JLabel ngLbl;	
	private JLabel winLbl;	
	private JButton incNumToken;	
	private JButton decNumToken;
	private ArrayList<Integer> posArray = new ArrayList<Integer>();	//holds position of balls
	private ArrayList<String> tokenArray = new ArrayList<String>();	//holds whether each position is a green ball, black ball, or empty space
	private ArrayList<String> checkWinArray = new ArrayList<String>();	//array used to check whether we won
	private int diameter = 20;	//each ball is 20 units in diameter
	private int x = 120;	//first ball start position
	private int spacePos = 0;	//position space is at
	private int space = 6;	//position space is at in terms of balls, space, balls
	private int numToken;	//used to count tokens 
	private int totalTokens = 6;	//total tokens in one side
	private int victory = 0;	//number of wins
	
	public LeapingFrogsPuzzle()
	{
		super ("<Siva> Game to move tokens");
		setLayout (null);
		
		//initializing components
		grnBtn = new JButton("Green");
		grnBtn.setLocation(100, 50);
		grnBtn.setSize(100, 30);
		add(grnBtn);
		
		blkBtn = new JButton("Black");
		blkBtn.setLocation(210, 50);
		blkBtn.setSize(100, 30);
		add(blkBtn);
		
		ngBtn = new JButton("New Game");
		ngBtn.setLocation(320, 50);
		ngBtn.setSize(100, 30);
		add(ngBtn);
		
		incNumToken = new JButton("Increase Tokens");
		incNumToken.setLocation(100, 200);
		incNumToken.setSize(160, 30);
		add(incNumToken);
		
		decNumToken = new JButton("Decrease Tokens");
		decNumToken.setLocation(260, 200);
		decNumToken.setSize(160, 30);
		add(decNumToken);
		
		ngLbl = new JLabel("New game started");
		ngLbl.setForeground(Color.RED);
		ngLbl.setLocation(100, 175);
		ngLbl.setSize(120, 30);
		add(ngLbl);
		
		winLbl = new JLabel("Congratulations! You've succeeded " + victory + " times!");
		winLbl.setForeground(Color.RED);
		winLbl.setLocation(100, 175);
		winLbl.setSize(400, 30);
		
		
		ButtonHandler handler = new ButtonHandler();
		grnBtn.addActionListener(handler);
		blkBtn.addActionListener(handler);
		ngBtn.addActionListener(handler);
		incNumToken.addActionListener(handler);
		decNumToken.addActionListener(handler);
		
		initializeArrays();
		
	} 
	
	public void paint (Graphics g) { //draws array implementation  
		
		super.paint(g);
		for (int i = 0; i < (2*totalTokens + 1); i++) {
			if (tokenArray.get(i) == "g") {	//if spot is a "g" we have a green ball
				g.setColor(Color.GREEN);
				g.fillOval(posArray.get(i), 150, diameter, diameter);
			}
			else if (tokenArray.get(i) == "b") {	//if spot is a "b" we have a black ball
				g.setColor(Color.BLACK);
				g.fillOval(posArray.get(i), 150, diameter, diameter);
			}
			
		}
	}
	
	public void initializeArrays() {
		//initializes all the arrays mentioned before
		numToken = 0;
		for (int i = 0; i < (2*totalTokens + 1); i++)	//2*totalTokens+1 gives arraySize because tokens on each side + empty space
		{
			if (numToken < totalTokens) {	//numToken starts at 0 and becomes 2*totalTokens by the end
				if (i != space) {
					tokenArray.add("g");	//since balls must be opposite positions, tokenArray and checkWin are initialized
					checkWinArray.add("b");	//as opposites
				}
			}
			else if (numToken >= totalTokens) {
				if (i != space) {
					tokenArray.add("b");
					checkWinArray.add("g");
				}
				
			}
			if (i != space)
			{
				x = 120 + 20 * i;	//spacing each ball
				posArray.add(x);
			}
			else	//recording where space occurs
			{
				numToken--;
				spacePos = 120 + 20*i;
				posArray.add(spacePos);
				tokenArray.add("s");
				checkWinArray.add("s");
			}
			
			numToken++;
		}
	}
	
	private boolean checkWin()	
	{
		for (int i = 0; i < (2*totalTokens +1); i++) {	//if both arrays are equal we have won
			if (!(tokenArray.get(i) == checkWinArray.get(i))) {
				return false;
			}
		}
		return true;
	}
	private class ButtonHandler implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand() == "Green")
			{
				int arrIndex;
				int spaceIndex = tokenArray.indexOf("s");	//we get index of space array
				arrIndex = spaceIndex;
				while (tokenArray.get(arrIndex) != "g" && arrIndex > 0) {	//check for first Green ball to left of space
					arrIndex--;												//because green balls move right so they have to be left of space
				}
				if (((spaceIndex - arrIndex) <= 2) && tokenArray.get(arrIndex) == "g") {	//check if within 2 distance
					tokenArray.set(spaceIndex, "g");	
					tokenArray.set(arrIndex, "s");
				}
				repaint();
				revalidate();
				remove (ngLbl);	//remove new game label upon moving 
				if (checkWin()) {	//check win each move
					victory++;	//if win number of victory is incremented
					winLbl.setText("Congratulations! You've succeeded " + victory + " times!");	//new victory label to update # victories
					add (winLbl);
					remove(grnBtn);	//remove buttons
					remove(blkBtn);
				}
			}
			else if (event.getActionCommand() == "Black")	//same as green but right instead of left
			{
				int arrIndex;
				int spaceIndex = tokenArray.indexOf("s");
				arrIndex = spaceIndex;
				while (tokenArray.get(arrIndex) != "b" && arrIndex < tokenArray.size()-1) {
					arrIndex++;
				}
				if (((arrIndex - spaceIndex) <= 2) && tokenArray.get(arrIndex) == "b") {
					tokenArray.set(spaceIndex, "b");
					tokenArray.set(arrIndex, "s");
				}
				repaint();
				revalidate();
				remove (ngLbl);
				if (checkWin()) {
					victory++;
					winLbl.setText("Congratulations! You've succeeded " + victory + " times!");
					add (winLbl);
					remove(grnBtn);
					remove(blkBtn);
				}
			}
			else if (event.getActionCommand() == "New Game")	
			{
				tokenArray.clear();	//clears the arrays to reinitialize them
				posArray.clear();
				initializeArrays();
				repaint();
				revalidate();
				add(ngLbl);
				remove(winLbl);
				add(grnBtn);
				add(blkBtn);
			}
			else if (event.getActionCommand() == "Increase Tokens") 
			{
				if (space < 10)
				{
					space++;	//inc space position
					totalTokens++;	//inc total number of tokens on one side
				}
				//below is the same as new game except
				tokenArray.clear();		//we need to also clear tokenarray as it needs to be reinitialized
				posArray.clear();		//since our size has changed
				checkWinArray.clear();
				initializeArrays();
				repaint();
				revalidate();
				add(ngLbl);
				remove(winLbl);
				add(grnBtn);
				add(blkBtn);

			}
			else if (event.getActionCommand() == "Decrease Tokens") 
			{
				if (space > 1)
				{
					space--;
					totalTokens--;
				}
				tokenArray.clear();
				posArray.clear();
				checkWinArray.clear();
				initializeArrays();
				repaint();
				revalidate();
				add(ngLbl);
				remove(winLbl);
				add(grnBtn);
				add(blkBtn);
			}
		}
		
	}

	

} 

