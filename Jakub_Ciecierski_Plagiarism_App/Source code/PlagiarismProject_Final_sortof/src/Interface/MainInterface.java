package Interface;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import Plagiarism.*;
import ProjectLoad.*;

//Starts the application
public class MainInterface extends JFrame implements Serializable
{
	Explorer explorer;
	FileViewer fileViewer;
	InfoPanel infoPanel;
	
	JFrame mainFrame;
	JFrame nameFrame;
	JPanel mainPanel;

	String poolName;
	JLabel startLabel;
	
	JMenuItem showTree;
	JMenuItem showLeftTextArea;
	JMenuItem showRightTextArea;
	JMenuItem showInfoPanel;
	
	JSplitPane splitPane1;
	JSplitPane splitPane2;
	
	public MainInterface()
	{
		fileViewer = new FileViewer(this);
		explorer = new Explorer(this);
		infoPanel = new InfoPanel(this);
		initUI();
	}
	
	public Explorer getExplorer()
	{
		return this.explorer;
	}
	
	public FileViewer getFileViewer()
	{
		return this.fileViewer;
	}
	
	public InfoPanel getInfoPanel()
	{
		return this.infoPanel;
	}
	
	//Called after creating new pool.Chooses the name and type of new pool
	public void newPoolName()
	{
		final JTextField name = new JTextField(5);
		JButton okButton = new JButton("ok");
		JButton cancelButton = new JButton("cancel");
		JLabel label = new JLabel("Insert the name of new workspace");
		CheckboxGroup poolType = new CheckboxGroup();
		final Checkbox javaBox = new Checkbox("Java",poolType,true);
		final Checkbox cppBox = new Checkbox("C++",poolType,false);
		
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		inputPanel.setBorder(new EmptyBorder(new Insets(20, 20,20,20)));
		inputPanel.add(label);
		inputPanel.add(name);
		inputPanel.add(okButton);
		inputPanel.add(cancelButton);
		inputPanel.add(javaBox);
		inputPanel.add(cppBox);
		
		nameFrame = new JFrame("New pool");   
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
				String poolname=name.getText();
				if(poolname.equals(""))
					return;
				else
				{
					try
					{
						poolName = name.getText();
						nameFrame.dispose();
						if(javaBox.getState())
							explorer.addPoolNode(poolName,"java");
						if(cppBox.getState())
							explorer.addPoolNode(poolName,"cpp");
						mainPanel.revalidate();
						mainPanel.repaint();
					}
					catch(Exception ex)
					{ex.printStackTrace();}
				}
			}
		});	
	}
	
	public JMenuBar createMenuBar()
	{
		JMenuBar menubar=new JMenuBar();
		JMenu file = new JMenu ("File");
		
		JMenuItem newPool = new JMenuItem("New pool");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("Load");
		newPool.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				newPoolName();
			}
		});
		final MainInterface mi = this;
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
			
				JPanel inputPanel = new JPanel();
				final JTextField saveText = new JTextField(5);
				JButton okButton = new JButton("ok");
				JButton cancelButton = new JButton("cancel");
				JLabel label = new JLabel("new save name:");
				inputPanel.setLayout(new FlowLayout());	
				inputPanel.setBorder(new EmptyBorder(new Insets(20, 20,20,20)));
				inputPanel.add(label);
				inputPanel.add(saveText);
				inputPanel.add(okButton);
				inputPanel.add(cancelButton);
				
				nameFrame = new JFrame("New pool");   
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
						String poolname=saveText.getText();
						if(poolname.equals(""))
							return;
						else
						{
							try
							{
								if(saveText.getText()=="")
									return;
								new SaveState(saveText.getText(),mi);
								
								nameFrame.dispose();
								mainPanel.revalidate();
								mainPanel.repaint();
							}
							catch(Exception ex)
							{ex.printStackTrace();}
						}						}
				});			
			}
		});
		load.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				try
				{
					JPanel panel = new JPanel();
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("save file","data");
					chooser.setFileFilter(filter);
					//chooser.setCurrentDirectory();
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.setMultiSelectionEnabled(false);
					
					int returnVal = chooser.showOpenDialog(panel);
					if(returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = chooser.getSelectedFile();
						new LoadState(file.getName(),mi);
					}
					
				} catch (IOException e) {}
			}
		});
		file.add(newPool);
		file.addSeparator();
		file.add(save);
		file.add(load);
		
		JMenu window = new JMenu ("Window");
		showLeftTextArea = new JMenuItem("Show left text area");
		showRightTextArea = new JMenuItem("Show right text area");
		showTree= new JMenuItem("Show tree");
		showInfoPanel = new JMenuItem("Show detail info");
		
		showLeftTextArea.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				fileViewer.filePanelLeft.setVisible(!fileViewer.filePanelLeft.isVisible());
				mainPanel.revalidate();
				mainPanel.repaint();
				if(fileViewer.filePanelLeft.isVisible())
				{
					showLeftTextArea.setBackground(null);
					fileViewer.splitPane.setVisible(true);
					fileViewer.splitPane.setDividerLocation(fileViewer.splitPane.getLocation().x-fileViewer.filePanelRight.getSize().width);
				}
				else
				{
					showLeftTextArea.setBackground(Color.lightGray);
					if(!fileViewer.filePanelRight.isVisible())
						fileViewer.splitPane.setVisible(false);
				}
			}
		});
		showRightTextArea.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				fileViewer.filePanelRight.setVisible(!fileViewer.filePanelRight.isVisible());
				mainPanel.revalidate();
				mainPanel.repaint();
				if(fileViewer.filePanelRight.isVisible())
				{
					showRightTextArea.setBackground(null);
					fileViewer.splitPane.setVisible(true);
					fileViewer.splitPane.setDividerLocation(fileViewer.splitPane.getLocation().x-fileViewer.filePanelLeft.getSize().width);
				}
				else
				{
					showRightTextArea.setBackground(Color.lightGray);
					if(!fileViewer.filePanelLeft.isVisible())
						fileViewer.splitPane.setVisible(false);
				}
			}
		});
		showTree.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				explorer.explorerPanel.setVisible(!explorer.explorerPanel.isVisible());
				mainPanel.revalidate();
				mainPanel.repaint();
				if(explorer.explorerPanel.isVisible())
				{
					showTree.setBackground(null);
					splitPane1.setDividerLocation(150);
				}
				else
					showTree.setBackground(Color.lightGray);
			}
		});
		showInfoPanel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				infoPanel.mainPanel.setVisible(!infoPanel.mainPanel.isVisible());
				mainPanel.revalidate();
				mainPanel.repaint();
				if(infoPanel.mainPanel.isVisible())
				{
					showInfoPanel.setBackground(null);
					splitPane2.setDividerLocation(800);
				}
				else
					showInfoPanel.setBackground(Color.lightGray);
			}
		});
		
		window.add(showLeftTextArea);
		window.add(showRightTextArea);
		window.add(showTree);
		window.add(showInfoPanel);
		
		JMenu edit = new JMenu ("Edit");
		JMenu leftText =new JMenu("Set left text");
		JMenu rightText =new JMenu("Set right text");
		
		for(int i=5;i<25;i++)
		{
			if(i%2==0)
			{
				String s = Integer.toString(i);
				JMenuItem sizeLeft =new JMenuItem(s);
				final int j = i;
				sizeLeft.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt) 
					{
						fileViewer.setFontLeft(j);
					}
				});
				JMenuItem sizeRight = new JMenuItem(s);
				sizeRight.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt) 
					{
						fileViewer.setFontRight(j);
					}
				});
				
				leftText.add(sizeLeft);
				rightText.add(sizeRight);
			}
		}
		edit.add(leftText);
		edit.add(rightText);
		
		menubar.add(file);
		menubar.add(window);
		menubar.add(edit);
		return menubar;
	}
	
	public void initUI(){
		mainFrame = new JFrame();
		mainPanel = new JPanel (new BorderLayout());
		
		splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,explorer.explorerPanel,fileViewer.fileMainPanel);
		splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,splitPane1,infoPanel.mainPanel);
		
		splitPane1.setDividerSize(5);
		splitPane2.setDividerSize(5);
		splitPane1.setDividerLocation(150);
		splitPane2.setDividerLocation(500);
		splitPane1.setResizeWeight(0.2);
		splitPane2.setResizeWeight(0.9);
		
		mainPanel.add(createMenuBar(),BorderLayout.NORTH);
		mainPanel.add(splitPane2,BorderLayout.CENTER);
		
		mainFrame.setVisible(true);   
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(mainPanel);
		mainFrame.setSize (1000,700);
	}
	
	public static void main(String[] args)
	{
		new MainInterface();
	}

	
}
