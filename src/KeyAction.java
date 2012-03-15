import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class KeyAction extends AbstractAction {

	SudokoValueSelector parent;
	
	public KeyAction(SudokoValueSelector parent) {
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		parent.keyPressed(Integer.parseInt(evt.getActionCommand()));
	}

}
