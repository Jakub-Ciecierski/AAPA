package Interface;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

//The right-click menu
public class PopUp extends JPopupMenu implements Serializable
{
	public PopUp(final Explorer explorer,final DefaultMutableTreeNode selectedNode)
	{
		if(selectedNode.getLevel()==1)
		{
			JMenuItem addItem = new JMenuItem("Add project");
			addItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt) 
				{
					new FileChooser(explorer,selectedNode);
				}
			});
			add(addItem);
			
			JMenuItem runItem = new JMenuItem("Run plagiarism check");
			runItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt) 
				{
					JPanel inputPanel = new JPanel();
					final JTextField kText = new JTextField(5);
					final JTextField tText = new JTextField(5);
					JButton okButton = new JButton("ok");
					JButton cancelButton = new JButton("cancel");
					
					JLabel kLabel = new JLabel("k:(50>k>2)");
					JLabel tLabel = new JLabel("t:(50>t>2) (t>=k)");
					
					inputPanel.setLayout(new FlowLayout());	
					inputPanel.setBorder(new EmptyBorder(new Insets(20, 20,20,20)));
					inputPanel.add(kLabel);
					inputPanel.add(kText);
					inputPanel.add(tLabel);
					inputPanel.add(tText);
					inputPanel.add(okButton);
					inputPanel.add(cancelButton);
					
					final JFrame nameFrame = new JFrame("plagiarism");   
					nameFrame.setSize(350,150);
					nameFrame.setResizable(false);
					nameFrame.add(inputPanel);
					nameFrame.setAlwaysOnTop(true);
					nameFrame.setLocationRelativeTo(explorer.tree);
					nameFrame.setVisible(true);
						
						
					cancelButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							nameFrame.dispose();
						}
					});
						
					okButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							int k=0;
							int t=0;
							if(kText.getText()=="" && tText.getText()=="")
								return;
							k=Integer.parseInt(kText.getText());
							if(!(k>2 && k<50))
								return;
							t=Integer.parseInt(tText.getText());
							if(!(t>2 && t<50))
								return;
							if(k>t)
								return;
							explorer.runPlagiarsmCheck(selectedNode,k,t-k+1);
								try
								{
									
									nameFrame.dispose();
								}
								catch(Exception ex)
								{ex.printStackTrace();}
						}	
					});	
				}
			});
			add(runItem);
		}
		
		if(selectedNode.getLevel()==2)
		{
			JMenuItem removeItem = new JMenuItem("Remove project");
			removeItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt) 
				{
					
					explorer.removeProjectNode(selectedNode);
				}
			});
			add(removeItem);
		}
		
		if(selectedNode.getLevel()==3)
		{
			if(explorer.mi.fileViewer.filePanelLeft.isVisible() && explorer.mi.fileViewer.filePanelRight.isVisible())
			{
				JMenuItem itemLeft = new JMenuItem("Open on left");
				JMenuItem itemRight= new JMenuItem("Open on right");
				itemLeft.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt) 
					{
						explorer.openSourceFileLeft(selectedNode);
					}
				});
				itemRight.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt) 
					{
						explorer.openSourceFileRight(selectedNode);
				}
				});
				add(itemLeft);
				add(itemRight);
			}
			if((explorer.mi.fileViewer.filePanelLeft.isVisible() && !explorer.mi.fileViewer.filePanelRight.isVisible()) ||
			  (!explorer.mi.fileViewer.filePanelLeft.isVisible() && explorer.mi.fileViewer.filePanelRight.isVisible()))
			{
				JMenuItem item = new JMenuItem("Open");
				item.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt) 
					{
						explorer.openSourceFile(selectedNode);
					}
				});
				add(item);
			}
		}
	}
}
