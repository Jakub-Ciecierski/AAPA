package Interface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

//Used to listen to mouse clicks on the Explorer
public class ExplorerPopClickListener extends MouseAdapter{
	Explorer explorer;
	
	public ExplorerPopClickListener(Explorer explorer)
	{
		this.explorer=explorer;
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPop(e);
		if(e.getButton()==MouseEvent.BUTTON1)
			if(e.getClickCount()==2)
				doDoubleClick(e);
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPop(e);
	}
	
	//Used to open source Files
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
				this.explorer.openSourceFile(selectedNode);
			}
		}
		catch(Exception ex){}
	}
	
	//Shows the menu after right clicking one of the nodes in the tree
	private void doPop(MouseEvent e)
	{
		JTree tree = (JTree)e.getSource();
		TreePath selPath = tree.getPathForLocation(e.getX(),e.getY());
		tree.setSelectionPath(selPath);
		
		if(tree.isSelectionEmpty())
			return;
		try
		{
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
			
			PopUp menu = new PopUp(this.explorer,selectedNode);
			menu.show(e.getComponent(),e.getX(),e.getY());
		}
		catch(Exception ex){ex.printStackTrace();}
	}
}
