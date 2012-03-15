
import java.awt.event.*;
import javax.swing.*;

/******************************************************************************
 * <p>Title: SudokoGame Interface</p>
 * <p>Description: Specifies what interface a Sudoko Game needs to implement</p>
 * <p>for the ENGR2710 Project</p>
 * @author Ramiro Liscano
 * @version 1.0
 */
 
public interface SudokoGame {

	/** Load the data file containing the directory, or
    establish a connection with the data source.
    @param sourceName The name of the file (data source)
                      with the phone directory entries
	 */

	void loadData(String sourceName,JPanel[][] sudokoPanels);
	public MouseListener[][] getPanelMouseListeners();
	public ActionListener getUndoActionListener();
}
