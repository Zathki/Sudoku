import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.LinkedList;
import java.util.Stack;

public class MySudokoExample implements SudokoGame {
	
	final int REMOVEVALUE = 0, ADDVALUE = 1, ADDOPTION = 2, REMOVEOPTION = 3;
	
	SudokoMouseAction[][] mouseListeners = new SudokoMouseAction[SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW][SudokoGUI.MAX_COL*SudokoGUI.MAX_COL];
	int[][] sudokoState = new int[SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW][SudokoGUI.MAX_COL*SudokoGUI.MAX_COL];
	JPanel[][] sudokoPanels;
	Square[][] data;
	String sourceName;
	Stack<LinkedList<Changes>> changes = new Stack<LinkedList<Changes>>();
	
	boolean gameOver = false;
	
	public MySudokoExample() {
		
		// Create 81 mouse listeners for the 81 squares
		// So that I can process a mouse press on any square
		// These MouseListeners do nothing right now
		
		for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
			for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
				mouseListeners[i][j] = new SudokoMouseAction(this);
			}
	}
	
	public MouseListener[][] getPanelMouseListeners() {
		return mouseListeners;
	}
	
	public void pushCommand(Square square, int n, int command, LinkedList<Changes> changedValues) {
		
		if(isLegalMove(square, n)) {
			if(command == ADDVALUE) {
				square.pushValue(n);
				changedValues.add(new Changes(square));
			} else if(command == ADDOPTION) {
				square.pushOption(n);
				changedValues.add(new Changes(square, true, n, true));
			} else if(command == REMOVEOPTION) {
				square.removeOption(n);
				changedValues.add(new Changes(square, true, n, false));
			}
		}
		if(command == REMOVEVALUE) {
			square.pushValue(0);
			changedValues.add(new Changes(square));
		}
		
		if( checkWinCondition() ) {
			gameOver = true;
			for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
				for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
					data[i][j].gameOver();
					sudokoPanels[i][j].removeMouseListener(mouseListeners[i][j]);
				}
					
		}
		removeExtraOptionValues(square, changedValues);
	}
	
	public void addChange(LinkedList<Changes> changedValues) {
		if(changedValues.size() > 0)
			changes.push(changedValues);
	}
	
	public boolean checkWinCondition() {
		
		boolean win = true;
		
		for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
			for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
				if(data[i][j].getMainValue() == 0)
					win = false;
			}
		
		return win;
	}
	
	public void optionCheckIfLegalMove(Square square, LinkedList<Changes> changedValues) {
		for(int i = 1; i <= 9; i++) {
			if(square.hasOptionValue(i)) {
				if(!isLegalMove(square, i)) {
					square.removeOption(i);
					changedValues.add( new Changes(square, true, i, false) );
				}
			}
		}
	}
	
	public void removeExtraOptionValues(Square square, LinkedList<Changes> changedValues) {
		//check all elements in same column
		for(int i = 0; i < SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; i++) {
			optionCheckIfLegalMove(data[i][square.getX()], changedValues);
		}
		//check all elements in same row
		for(int i = 0; i < SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; i++) {
			optionCheckIfLegalMove(data[square.getY()][i], changedValues);
		}
		//check all elements in same 3x3 square
		//TODO
		int quadrant = square.getQuadrant();
		for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
			for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
				if(data[i][j].getQuadrant() == quadrant) {
					optionCheckIfLegalMove(data[i][j], changedValues);
				}
			}
	}
	
	public boolean isLegalMove(Square square, int n) {
		//check all elements in same column
		for(int i = 0; i < SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; i++) {
			if(data[i][square.getX()].getMainValue() == n)
				return false;
		}
		//check all elements in same row
		for(int i = 0; i < SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; i++) {
			if(data[square.getY()][i].getMainValue() == n)
				return false;
		}
		//check all elements in same 3x3 square
		//TODO
		int quadrant = square.getQuadrant();
		for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
			for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
				if(data[i][j].getQuadrant() == quadrant && data[i][j].getMainValue() == n)
					return false;
			}
		return true;
	}
	
	public void loadData (String filename,JPanel[][] sudokoPanels){
		// Reads the Sudoko start state from the file into the Sudoko state
		// Recommended approach for doing the graphics is to create Label
		// Panels and add them to the sudokoPanels. This will automatically
		// refresh and display.
		gameOver = false;
		try {
			this.sudokoPanels = sudokoPanels;
			data = new Square[sudokoPanels.length][sudokoPanels[0].length];
			
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String fileText = in.readLine();
			int randNum = (int)(Math.random()*Integer.parseInt(fileText));
			for(int i = 0; i < randNum; i++)
				fileText = in.readLine();
			String[] panelData = fileText.split(",");
		
			for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
				for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
					int value = Integer.parseInt(panelData[i*SudokoGUI.MAX_COL*SudokoGUI.MAX_COL+j]);

					Square square = new Square(value, sudokoPanels[i][j], j, i);
					data[i][j] = square;
					
					JLabel label = new JLabel();
					label.setText(square.getMainValueAsString());
					
					label.setFont(label.getFont().deriveFont(38.0f));

					sudokoPanels[i][j].add(label);
			}
			
			for (int i=0; i<SudokoGUI.MAX_ROW*SudokoGUI.MAX_ROW; i++)
				for (int j=0; j<SudokoGUI.MAX_COL*SudokoGUI.MAX_COL; j++) {
					if(data[i][j].getMainValue() == 0)
						mouseListeners[i][j].setSquare(data[i][j]);
					else
						sudokoPanels[i][j].removeMouseListener(mouseListeners[i][j]);
				}
		
		} catch(IOException e) {
			System.out.println("Unable to load file: " + e);
		}
	}
	
	public void undo() {
		if(changes.size() > 0 && !gameOver) {
			LinkedList<Changes> changedValues = changes.pop();
			for(Changes change: changedValues) {
				if(change.isOption) {
					Square temp = data[change.square.getY()][change.square.getX()];

					if( !change.prevValue)
						temp.pushOption(change.value);
					else
						temp.removeOption(change.value);
				} else {
					data[change.square.getY()][change.square.getX()].undo();
				}
			}
		}
	}
	
	public ActionListener getUndoActionListener(){
		
		// Return an action listener to handle the Undo operation
		// does nothing right now. Need to create an action listener
		// similar to the mouse listener to process the Undo command.
		return new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            undo(); }};
	}
	
}
