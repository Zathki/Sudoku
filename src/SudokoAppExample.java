/******************************************************************************
 * <p>Title: Sudoko Application Example</p>
 * <p>Description: Starts the Sudoko application</p>
 * <p>for the ENGR2710 Project</p>
 * @author Ramiro Liscano
 * @version 1.0
 */
public class SudokoAppExample {
	
	/****************************************************************************
	 * Start of execution
	 * @param args String[] (not used)
	 */
	public static void main(String args[]) {
		
		createGame();
	}
	
	public static void createGame() {
		// Create your Sudoko Game
		SudokoGUI sudokoApp = new SudokoGUI();
		SudokoGame sudokoGame = new MySudokoExample();
		sudokoApp.addPanelMouseListeners(sudokoGame.getPanelMouseListeners());
		sudokoApp.addUndoMenuListener(sudokoGame.getUndoActionListener());
		sudokoGame.loadData("sudoko.txt",sudokoApp.getSudokoPanels());
		sudokoApp.setVisible(true);
		}
}
