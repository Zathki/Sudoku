import java.awt.event.*;

public class SudokoMouseAction extends MouseAdapter {
	
	private static MySudokoExample program;
	private Square square;
	
	public SudokoMouseAction(MySudokoExample program) {
		SudokoMouseAction.program = program;
	}
	
	public void setSquare(Square square){
		this.square = square;
	}

	public void mouseClicked(MouseEvent arg0) {	
		SudokoValueSelector selector = new SudokoValueSelector(square, program);
		selector.updateButton();
	}
}
