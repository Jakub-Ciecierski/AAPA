package Interface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

//Listener for the plagiarised tree in info panel
public class ResultExplorerClickListener extends MouseAdapter implements Serializable{
	FileViewer fileViewer;
	public ResultExplorerClickListener(FileViewer fileViewer)
	{
		this.fileViewer=fileViewer;
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(e.getButton()==MouseEvent.BUTTON1)
			if(e.getClickCount()==2)
				doDoubleClick(e);
	}
	
	private void doDoubleClick(MouseEvent e)
	{
		JTree tree = (JTree)e.getSource();
		TreePath selPath = tree.getPathForLocation(e.getX(),e.getY());
		tree.setSelectionPath(selPath);
		if(tree.isSelectionEmpty())
			return;
		try
		{
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
			if(selectedNode.getLevel()==3)
			{
				
				this.fileViewer.setResultsComparison(selectedNode);
			}
		}
		catch(Exception ex){}
	}
}
