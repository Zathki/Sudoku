import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;


public class SudokoValueSelector extends JFrame implements ActionListener {

	private static final long serialVersionUID=2;
	
	private JToggleButton[] buttons = new JToggleButton[9];
	private Square square;
	private JButton setButton;
	private JRadioButton optionRadioButton, valueRadioButton;
	private static MySudokoExample program;
	
	public SudokoValueSelector(Square square, MySudokoExample program) {
		
		super("Select Value");
		setLocation(650/2,650/2);
		setPreferredSize(new Dimension(150,220));
		setResizable(false);
		
		JComponent component = getRootPane();
		for(int i = 1; i <= 9; i++)
			component.getInputMap().put(KeyStroke.getKeyStroke(Integer.toString(i)),"numPressed");
		component.getActionMap().put("numPressed", new KeyAction(this));
		
		this.square = square;
		SudokoValueSelector.program = program;
		
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.setPreferredSize(new Dimension(150, 120));
		
		for(int i = 0; i < 3; i++) {
			JPanel rowPanel = new JPanel();
			
			for(int j = 0; j < 3; j++) {
				
				int n = i*3+j;
				
				if(program.isLegalMove(square, n+1) || square.hasMainValue(n+1)) {
					buttons[n] = new JToggleButton(String.valueOf(n+1));
					if(square.hasMainValue(n+1))
						buttons[n].setSelected(true);
				} else {
					buttons[n] = new JToggleButton(String.valueOf(n+1));
					buttons[n].setEnabled(false);
				}
				buttons[n].setName("Number");
				rowPanel.add(buttons[n]);
				buttons[n].addActionListener(this);
			}
			
			optionsPanel.add(rowPanel);
		}
		outerPanel.add(optionsPanel);
		
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
		optionRadioButton = new JRadioButton("Options");
		valueRadioButton = new JRadioButton("Value", true);
		optionRadioButton.setName("Options");
		valueRadioButton.setName("Value");
		optionRadioButton.addActionListener(this);
		valueRadioButton.addActionListener(this);
		radioPanel.add(optionRadioButton);
		radioPanel.add(valueRadioButton);
		outerPanel.add(radioPanel);
		
		ButtonGroup selectionGroup = new ButtonGroup();
		selectionGroup.add( optionRadioButton );
		selectionGroup.add( valueRadioButton );
		
		JPanel buttonPanel = new JPanel();
		setButton = new JButton("Set");
		setButton.setName("Set");
		setButton.addActionListener(this);
		buttonPanel.add(setButton);
		
		outerPanel.add(buttonPanel);
		
		setContentPane(outerPanel);
	    pack();
	    setVisible(true);
	    
	    requestUpdate(square.getMainValue());
	}
	
	public void requestUpdate(int num) {
		for( int i = 0; i < buttons.length; i++) {
			
			if(valueRadioButton.isSelected() && num != i+1) {
				buttons[i].setSelected(false);
			}
		}
		updateButton();
	}
	
	public void updateButton() {
		boolean changed = false;
		
		for(int i = 0; i < buttons.length; i++) {
			if(valueRadioButton.isSelected()) {
				if(square.hasMainValue(i+1) != buttons[i].isSelected())
					changed = true;
			} else {
				if(square.hasOptionValue(i+1) != buttons[i].isSelected())
					changed = true;
			}
		}
		
		setButton.setEnabled(changed);
	}
	
	private boolean setSelectedValue() {
		LinkedList<Changes> changedValues = new LinkedList<Changes>();
		for(int i = 0; i < buttons.length; i++)
			if(buttons[i].isSelected() && buttons[i].isEnabled()) {
				program.pushCommand(square, i+1, program.ADDVALUE, changedValues);
				program.addChange(changedValues);
				return true;
		}

		program.pushCommand(square, 0, program.REMOVEVALUE, changedValues);
		program.addChange(changedValues);
		return false;
		
	}
	
	private void setSelectedOptions() {
		LinkedList<Changes> changedValues = new LinkedList<Changes>();
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i].isSelected() && !square.hasOptionValue(i+1)) {
				program.pushCommand(square, i+1, program.ADDOPTION, changedValues);
			} else if(!buttons[i].isSelected() && square.hasOptionValue(i+1)) {
				program.pushCommand(square, i+1, program.REMOVEOPTION, changedValues);
			}
		}
		program.addChange(changedValues);
	}
	
	public void actionPerformed(ActionEvent evt) {
		JComponent component = (JComponent)evt.getSource();
		
		if(component.getName().equals("Set")) {
			if(optionRadioButton.isSelected()) {
				setSelectedOptions();
			} else if(valueRadioButton.isSelected()) {
				setSelectedValue();
			}
			this.dispose();
			
		} else if(component.getName().equals("Options")) {
			display("Options");
			
	    } else if(component.getName().equals("Value")) {
	    	display("Value");
	    	
	    } else if(component.getName().equals("Number")){
	    	for(int i = 0; i < buttons.length; i++) {
	    		if(component == buttons[i]) {
	    			requestUpdate(i+1);
	    		}
	    	}
	    }
		updateButton();
	}
	
	public void keyPressed(int value) {
		for(int i = 1; i <= 9; i++) {
			if( value == i) {
				if(!buttons[i-1].isEnabled())
					return;
				
				buttons[i-1].setSelected(true);
				requestUpdate(i);
				if(setSelectedValue())
					this.dispose();
			}
		}
	}
	
	private void display(String selection) {
		for(int i = 0; i < buttons.length; i++) {
			if(selection.equals("Value")) {
				buttons[i].setSelected(square.hasMainValue(i+1));
			} else {
				buttons[i].setSelected(square.hasOptionValue(i+1));
			}
		}

		requestUpdate(square.getMainValue());
	}
}
