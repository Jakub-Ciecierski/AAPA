package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import Plagiarism.PoolPlagiarism;
import Plagiarism.ProjectPlagiarism;
import Plagiarism.SourceFilePlagiarism;
import ProjectLoad.SourceFile;

//Shows extra information about selected files, and manages the plagiarised projects
public class InfoPanel{
	MainInterface mi;
	JPanel mainPanel;
	JPanel infoPanel;
	JPanel buttonPanel;
	
	JScrollPane infoScroll;
	
	JPanel detailsPanel;
	JPanel resultsPanel;
	
	JTextArea poolInfoText;
	JTextArea projectInfoText;
	JTextArea srcInfoText;
	
	JTextArea resultsText;
	
	JButton detailsButton;
	JButton resultsButton;
	JButton runButton;
	
	JSplitPane resultsSplitPane;
	
	JTree resultsExplorer;
	JScrollPane resultsExplorerScroll;
	DefaultMutableTreeNode root;
	
	Font font;
	
	public InfoPanel(MainInterface mi)
	{
		this.mi=mi;
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		font = new Font("font",Font.BOLD,15);
		createButtonPanel();
		createInfoPanel();
	}
	
	public void createInfoPanel()
	{
		infoPanel = new JPanel();
		infoPanel.setBackground(Color.white);
		
		createDetailsPanel();
		createResultsPanel();
		
		infoPanel.add(resultsPanel);
		infoPanel.add(detailsPanel);
		
		infoScroll = new JScrollPane(infoPanel);
		mainPanel.add(infoScroll,BorderLayout.CENTER);
	}
	
	
	private void createDetailsPanel()
	{
		detailsPanel = new JPanel();
		detailsPanel.setBackground(Color.white);
		//detailsPanel.setLayout(new BorderLayout());
		poolInfoText= new JTextArea();
		projectInfoText= new JTextArea();
		srcInfoText= new JTextArea();
		
		poolInfoText.setFont(font);
		projectInfoText.setFont(font);
		srcInfoText.setFont(font);
		
		poolInfoText.setEditable(false);
		projectInfoText.setEditable(false);
		srcInfoText.setEditable(false);
		
		poolInfoText.setPreferredSize(new Dimension(300,150));
		projectInfoText.setPreferredSize(new Dimension(400,150));
		srcInfoText.setPreferredSize(new Dimension(450,150));
		
		detailsPanel.add(poolInfoText);
		detailsPanel.add(projectInfoText);
		detailsPanel.add(srcInfoText);
		
	}
	
	private void createResultsPanel()
	{
		resultsPanel = new JPanel();
		resultsPanel.setLayout(new BorderLayout());
		
		resultsPanel.setVisible(false);
		resultsPanel.setBackground(Color.red);
		
		root = new DefaultMutableTreeNode ("root");
		resultsExplorer = new JTree(root);
		
		resultsExplorer.addMouseListener(new ResultExplorerClickListener(mi.fileViewer));
		resultsExplorer.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e)
			{
				try
				{
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)resultsExplorer.getLastSelectedPathComponent();
					
					if(selectedNode.getLevel()==2)
						setProjectPlagScore(selectedNode);
					if(selectedNode.getLevel()==3)
						setSourcePlagScore(selectedNode);
				}
				catch(Exception ex)
				{}
			}
		});
		
		resultsExplorer.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		resultsExplorer.setRootVisible(false);
		
		
		resultsExplorerScroll = new JScrollPane(resultsExplorer);
		resultsExplorerScroll.setPreferredSize(new Dimension(550,150));
		resultsExplorerScroll.setBackground(Color.white);
		resultsExplorerScroll.getViewport().setBackground(Color.white);
		resultsExplorerScroll.getViewport().setBorder(null);
		resultsExplorerScroll.setViewportBorder(null);
		resultsExplorerScroll.setBorder(null);
		resultsExplorerScroll.setVisible(false);
		
		resultsText= new JTextArea();
		resultsText.setEditable(false);
		resultsText.setFont(font);
		
		resultsSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,resultsExplorerScroll,resultsText);
	
		resultsSplitPane.setResizeWeight(0.5);
		resultsSplitPane.setDividerSize(5);
		
		resultsPanel.add(resultsSplitPane,BorderLayout.CENTER);
		
		
	}
	
	public void createButtonPanel()
	{
		this.buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.cyan);
		buttonPanel.setPreferredSize(new Dimension(50,20));
		buttonPanel.setLayout(new GridLayout(1,5));
		
		detailsButton = new JButton("Details");
		resultsButton = new JButton("Results");
		
		detailsButton.setBackground(Color.white);
		resultsButton.setBackground(Color.cyan);
		
		detailsButton.setPreferredSize(new Dimension(80,15));
		resultsButton.setPreferredSize(new Dimension(80,15));
		
		detailsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				detailsPanel.setVisible(true);
				resultsPanel.setVisible(false);
				detailsButton.setBackground(Color.white);
				resultsButton.setBackground(Color.cyan);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		
		resultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				detailsPanel.setVisible(false);
				resultsPanel.setVisible(true);
				detailsButton.setBackground(Color.cyan);
				resultsButton.setBackground(Color.white);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		buttonPanel.add(detailsButton);
		buttonPanel.add(resultsButton);
		for(int i=0;i<8;i++)
		{
			JButton hidden=new JButton();
			hidden.setVisible(false);
			buttonPanel.add(hidden);
		}
		mainPanel.add(buttonPanel,BorderLayout.NORTH);
	}
	
	public void setProjectPlagScore(DefaultMutableTreeNode selectedNode)
	{
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) selectedNode.getParent();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) poolNode.getParent();
		
		PoolPlagiarism poolPlag = mi.explorer.plagiarisedPools.get(root.getIndex(poolNode));
		ProjectPlagiarism projectPlag = poolPlag.getPlagiarisedProjects(poolNode.getIndex(selectedNode));
		
		double s1 = projectPlag.score1*100;
		double s2 = projectPlag.score2*100;
		
		String name1 = projectPlag.project1.getProjectName();
		String score1 = String.format("%.2f",s1)+"%";
		
		String name2 = projectPlag.project2.getProjectName();
		String score2 = String.format("%.2f",s2)+"%";
		
		String info1 = "*******************Projects:*******************\n";
		//String info2= "\n***********************************************";
		
		String projInfo1 = name1 + ": \t" + score1;
		String projInfo2 = name2 + ": \t" + score2;
		//resultsText.setText("");
		resultsText.setText(info1 +projInfo1 + "\n" + projInfo2);
	}
	
 	public void setSourcePlagScore(DefaultMutableTreeNode selectedNode)
	{
 		DefaultMutableTreeNode projNode = (DefaultMutableTreeNode) selectedNode.getParent();
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) projNode.getParent();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) poolNode.getParent();
		
		PoolPlagiarism poolPlag = mi.explorer.plagiarisedPools.get(root.getIndex(poolNode));
		ProjectPlagiarism projectPlag = poolPlag.getPlagiarisedProjects(poolNode.getIndex(projNode));
		SourceFilePlagiarism srcPlag = projectPlag.getPlagiarisedSourceFiles(projNode.getIndex(selectedNode));
		
		setProjectPlagScore(projNode);
		
		double s1 = srcPlag.score1*100;
		double s2 = srcPlag.score2*100;
		
		String name1 = srcPlag.sourceFile1.getSourceFileName();
		String score1 = String.format("%.2f",s1)+"%";
		
		String info1 = "\n*******************Source Files:*******************\n";
		String info2= "\n***************************************************";
		
		String name2 = srcPlag.sourceFile2.getSourceFileName();
		String score2 = String.format("%.2f",s2)+"%";
		
		String srcInfo1 = name1 + ": \t" + score1;
		String srcInfo2 = name2 + ": \t" + score2;
		resultsText.append(info1+srcInfo1 + "\n" + srcInfo2+info2);
		//resultsText.setText("\n"+srcInfo1 + "\n" + srcInfo2);
	}
	
	public void setPoolInfo(DefaultMutableTreeNode selectedNode)
	{
		projectInfoText.setText(null);
		srcInfoText.setText(null);
		
		String name ="Pool name: "+ selectedNode.toString();
		String projectCount ="Projects count: " + selectedNode.getChildCount();
		String info = name +"\n"+ projectCount;
		
		poolInfoText.setText(info);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public void setProjectInfo(DefaultMutableTreeNode selectedNode)
	{
		srcInfoText.setText(null);
		
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) selectedNode.getParent();
		setPoolInfo(poolNode);
		
		String name ="Project name: "+ selectedNode.toString();
		String projectCount ="Source files count: " + selectedNode.getChildCount();
		String info = name +"\n"+ projectCount;
		
		projectInfoText.setText(info);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public void setSrcInfo(DefaultMutableTreeNode selectedNode)
	{
		DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedNode.getParent();
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) projectNode.getParent();
		setPoolInfo(poolNode);
		setProjectInfo(projectNode);
		
		int poolIndex = mi.explorer.root.getIndex(poolNode);
		int projectIndex = poolNode.getIndex(projectNode);
		int srcIndex = projectNode.getIndex(selectedNode);
		
		SourceFile src =mi.explorer.projectPools.get(poolIndex).getProject(projectIndex).getSourceFile(srcIndex);
		
		String noOfLines =Integer.toString(src.linesCount);
		String name ="Source File name: "+ selectedNode.toString();
		//String projectCount ="Source files count: " + selectedNode.getChildCount();
		String info = name +"\n" +"Lines of code: " +noOfLines;
		
		srcInfoText.setText(info);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public void setResultsInfo(PoolPlagiarism poolPlag)
	{
		if(poolPlag.isPlagiarised)
		{
			if(!resultsExplorerScroll.isVisible())
			{
				resultsExplorerScroll.setVisible(true);
				resultsSplitPane.setDividerLocation(400);
			}
			
			DefaultTreeModel model = (DefaultTreeModel) resultsExplorer.getModel();
			
			DefaultMutableTreeNode poolNode =new DefaultMutableTreeNode(poolPlag.projectPool.getPoolName());
			model.insertNodeInto(poolNode, root, root.getChildCount());
			resultsExplorer.scrollPathToVisible(new TreePath(poolNode.getPath()));
			
			for(int i=0;i<poolPlag.getPlagiarisedProjectsCount();i++)
			{
				DefaultMutableTreeNode projNode =new DefaultMutableTreeNode
						(poolPlag.getPlagiarisedProjects(i).project1.getProjectName()+" and "
								+poolPlag.getPlagiarisedProjects(i).project2.getProjectName());
				
				model.insertNodeInto(projNode, poolNode, poolNode.getChildCount());
				
				for(int j=0;j<poolPlag.getPlagiarisedProjects(i).getPlagiarisedSourceFilesCount();j++)
				{
					DefaultMutableTreeNode srcNode =new DefaultMutableTreeNode
							(poolPlag.getPlagiarisedProjects(i).getPlagiarisedSourceFiles(j).sourceFile1.getSourceFileName()
							+" and " +poolPlag.getPlagiarisedProjects(i).getPlagiarisedSourceFiles(j).sourceFile2.getSourceFileName());
					model.insertNodeInto(srcNode, projNode, projNode.getChildCount());
				}
			}
		}
	}
}
