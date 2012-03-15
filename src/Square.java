import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Square {
	private Stack<Integer> mainValue = new Stack<Integer>();
	private boolean options[] = new boolean [9];
	private JPanel panel;
	private int x, y, quadrant;
	
	public Square(int value, JPanel panel, int x, int y) {
		this.panel = panel;
		mainValue.push(value);
		this.x = x;
		this.y = y;
		this.quadrant = x/3+y/3*3;
		
	}
	
	public void gameOver() {
		panel.removeAll();
		panel.repaint();
		panel.setLayout(new FlowLayout());
		JLabel label = new JLabel(this.getMainValueAsString());
		panel.add(label);
		label.setFont(label.getFont().deriveFont(38.0f));
		panel.validate();
	}
	
	public void update() {
		panel.removeAll();
		panel.repaint();
		
		if(getMainValue().equals(0)) { //use options instead of value
			panel.setLayout(new GridLayout(3,3));
			for(int i = 0; i < 9; i++) {
				if(hasOptionValue(i+1)) {
					JLabel label = new JLabel(String.valueOf(i+1));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					panel.add(label);
				} else
					panel.add(new JLabel(" "));
			}
			
		} else {
			panel.setLayout(new FlowLayout());
			JLabel label = new JLabel(this.getMainValueAsString());
			panel.add(label);
			
			label.setForeground(Color.GRAY);
			label.setFont(label.getFont().deriveFont(38.0f));
		}
		panel.validate();
	}
	
	public boolean hasOptionValue(int n) {
		return options[n-1];
	}
	
	public boolean hasMainValue(int n) {
		return mainValue.peek().equals(n);
	}
	
	public boolean hasValue(int n) {
		return hasOptionValue(n) || hasMainValue(n);
	}
	
	public void pushOption(int value) {
		options[value-1]= true;
		update();
	}
	
	public void removeOptions() {
		for(int i=0; i<9; i++)
			options[i]= false;
		update();
	}
	
	public void removeOption(int n) {
		options[n-1] = false;
		update();
	}
	
	public void pushValue(int value) {
		mainValue.push(value);
		update();
	}
	
	
	public void undo() {
		if (!mainValue.empty()) {
		mainValue.pop();
		update();
		}
	}
	
	public JPanel getPanel() {return panel;}
	
	public int getX() {return x;}
	
	public int getY() {return y;}
	
	public int getQuadrant() {return quadrant;}
	
	public Integer getMainValue() {
		if(mainValue.size() != 0)
			return mainValue.peek();
		
		return 0;
	}
	
	public String getMainValueAsString() {
		if(mainValue.size() != 0) {
			if(mainValue.peek() == 0)
				return "  ";
			else
				return mainValue.peek().toString();
		}
		
		return "  ";
	}
}
