package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import Plagiarism.PoolPlagiarism;
import ProjectLoad.Project;
import ProjectLoad.ProjectPool;
import ProjectLoad.SourceFile;
import ProjectLoad.WrongProjectTypeException;

//Explorer keeps the tree of all pools and projects inside them
public class Explorer {
	MainInterface mi;
	JTree tree;
	DefaultMutableTreeNode root;
	JScrollPane treeScroll;
	JPanel explorerPanel;
	boolean leftOrRight;
	List<ProjectPool> projectPools = new ArrayList<ProjectPool>();
	List<PoolPlagiarism> plagiarisedPools = new ArrayList<PoolPlagiarism>();
	
	public Explorer(){}
	
	public Explorer (final MainInterface mi)
	{
		this.leftOrRight=true;
		this.mi=mi;
		
		this.root = new DefaultMutableTreeNode ("root");
		this.tree = new JTree(root);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.tree.setRootVisible(false);
		
		this.treeScroll = new JScrollPane(tree);
		this.treeScroll.setBorder( new EmptyBorder(new Insets(0, 0,10,0)));
		this.treeScroll.setPreferredSize(new Dimension(175,90));
		
		JMenuBar menubar=new JMenuBar();
		JMenu file = new JMenu ("Explorer");
		JMenuItem close = new JMenuItem("close");
		
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				explorerPanel.setVisible(false);
				mi.showTree.setBackground(Color.lightGray);
				mi.mainPanel.revalidate();
				mi.mainPanel.repaint();	
			}
		});
		
		file.add(close);
		menubar.add(file);
		menubar.setBackground(Color.cyan);
		
		explorerPanel = new JPanel();
		explorerPanel.setLayout(new BorderLayout());
		explorerPanel.add(treeScroll,BorderLayout.CENTER);
		explorerPanel.add(menubar,BorderLayout.NORTH);
		
		tree.addMouseListener(new ExplorerPopClickListener(this));
		//tree.addTreeSelectionListener(new ExplorerSelectionListener());
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e)
			{
				try
				{
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					if(selectedNode.getLevel()==1)
						mi.infoPanel.setPoolInfo(selectedNode);
					if(selectedNode.getLevel()==2)
						mi.infoPanel.setProjectInfo(selectedNode);
					if(selectedNode.getLevel()==3)
						mi.infoPanel.setSrcInfo(selectedNode);
				}
				catch(Exception ex)
				{}
			}
		});
	}
	
	public PoolPlagiarism getPlagiarisedPool(int i)
	{
		return plagiarisedPools.get(i);
	}
	
	public void addPoolNode(String name,String type)
	{
		ProjectPool projectpool = new ProjectPool(name,type);
		projectPools.add(projectpool);
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel(); 
		DefaultMutableTreeNode pool =new DefaultMutableTreeNode(name+" ["+type+"]");
		model.insertNodeInto(pool, root, root.getChildCount());
		
		tree.scrollPathToVisible(new TreePath(pool.getPath()));
	}
	
	public void addProjectNode(File[] files,DefaultMutableTreeNode selectedNode)
	{
		int index=root.getIndex(selectedNode);
		
		for(int i=0;i<files.length;i++)
		{
			String fileName = files[i].getName();
			String dir =files[i].getAbsolutePath();
			
			Project project=null;
			try {
				project = new Project(dir,projectPools.get(index).getPoolType());
			} catch (WrongProjectTypeException e) {return;}
			
			project.setProjectName(fileName);
			projectPools.get(index).addProject(project);
			
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel(); 
		
			DefaultMutableTreeNode projectNode =new DefaultMutableTreeNode(project.getProjectName());
			model.insertNodeInto(projectNode,selectedNode,selectedNode.getChildCount());
		
			for(int j=0;j<project.getSourceFileCount();j++)
			{
				SourceFile file = project.getSourceFile(j);
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file.getSourceFileName());
				model.insertNodeInto(fileNode,projectNode,projectNode.getChildCount());
			}
		}
		mi.infoPanel.setPoolInfo(selectedNode);
	}
	
	public void removeProjectNode(DefaultMutableTreeNode selectedNode)
	{
		DefaultMutableTreeNode poolNode=(DefaultMutableTreeNode) selectedNode.getParent();
		
		int projectIndex = poolNode.getIndex(selectedNode);
		int poolIndex=root.getIndex(poolNode);
		
		projectPools.get(poolIndex).removeProject(projectIndex);
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.removeNodeFromParent(selectedNode);
		
		mi.infoPanel.setPoolInfo(poolNode);
	}
	
	public void openSourceFileLeft(DefaultMutableTreeNode selectedNode)
	{
		DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedNode.getParent();
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) projectNode.getParent();
		
		int poolIndex = root.getIndex(poolNode);
		int projectIndex = poolNode.getIndex(projectNode);
		int fileIndex = projectNode.getIndex(selectedNode);
		
		SourceFile src = projectPools.get(poolIndex).getProject(projectIndex).getSourceFile(fileIndex);
		Project proj= projectPools.get(poolIndex).getProject(projectIndex);
		mi.fileViewer.setTextLeft(src,proj,false,null);
	}
	
	public void openSourceFileRight(DefaultMutableTreeNode selectedNode)
	{
		DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedNode.getParent();
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) projectNode.getParent();
		
		int poolIndex = root.getIndex(poolNode);
		int projectIndex = poolNode.getIndex(projectNode);
		int fileIndex = projectNode.getIndex(selectedNode);
		
		SourceFile src = projectPools.get(poolIndex).getProject(projectIndex).getSourceFile(fileIndex);
		Project proj= projectPools.get(poolIndex).getProject(projectIndex);
		
		mi.fileViewer.setTextRight(src,proj,false,null);
	}
	
	public void openSourceFile(DefaultMutableTreeNode selectedNode)
	{
		if(mi.fileViewer.filePanelLeft.isVisible() && mi.fileViewer.filePanelRight.isVisible())
		{
			if(leftOrRight)
				this.openSourceFileLeft(selectedNode);
			else
				this.openSourceFileRight(selectedNode);
			leftOrRight=!leftOrRight;
		}
		if(!mi.fileViewer.filePanelLeft.isVisible() && mi.fileViewer.filePanelRight.isVisible())
		{
			this.openSourceFileRight(selectedNode);
		}
		if(mi.fileViewer.filePanelLeft.isVisible() && !mi.fileViewer.filePanelRight.isVisible())
		{
			this.openSourceFileLeft(selectedNode);
		}
	}
	
	public void runPlagiarsmCheck(DefaultMutableTreeNode selectedNode,int k,int w)
	{
		int poolIndex = root.getIndex(selectedNode);
		PoolPlagiarism poolPlag = new PoolPlagiarism (projectPools.get(poolIndex),k,w);
		plagiarisedPools.add(poolPlag);
		mi.infoPanel.setResultsInfo(poolPlag);
	}
	
	//Used to reload the entire tree and its pools after calling LoadState
	public void reload()
	{
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		while (root.getChildCount()!=0)
		{
			model.removeNodeFromParent((MutableTreeNode) root.getChildAt(0));
		}
		for(int i=0;i<projectPools.size();i++)
		{
			ProjectPool projectPool = projectPools.get(i);
			 
			DefaultMutableTreeNode poolNode =new DefaultMutableTreeNode(projectPool.getPoolName()+" ["+projectPool.getPoolType()+"]");
			model.insertNodeInto(poolNode, root, root.getChildCount());
			tree.scrollPathToVisible(new TreePath(poolNode.getPath()));
			
			for(int j=0;j<projectPool.getProjectCount();j++)
			{
				Project project = projectPool.getProject(j);
				DefaultMutableTreeNode projectNode =new DefaultMutableTreeNode(project.getProjectName());
				
				model.insertNodeInto(projectNode,(MutableTreeNode) poolNode,poolNode.getChildCount());
				
				for(int k=0;k<project.getSourceFileCount();k++)
				{
					SourceFile file = project.getSourceFile(k);
					
					DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file.getSourceFileName());
					model.insertNodeInto(fileNode,projectNode,projectNode.getChildCount());
				}
			}
			
		}
	}
}
