package Interface;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

//Used to choose the location of ziped projects
public class FileChooser {
	JFileChooser chooser;
	public FileChooser(Explorer tree,DefaultMutableTreeNode selectedNode)
	{
		JPanel panel = new JPanel();
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("zip File","zip");
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		
		int returnVal = chooser.showOpenDialog(panel);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File[] files = chooser.getSelectedFiles();
			tree.addProjectNode(files,selectedNode);
		}
	}
}
