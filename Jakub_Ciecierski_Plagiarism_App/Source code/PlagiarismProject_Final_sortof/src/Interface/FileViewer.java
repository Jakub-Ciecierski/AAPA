package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Style;
import javax.print.attribute.AttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;

import Plagiarism.PoolPlagiarism;
import Plagiarism.ProjectPlagiarism;
import Plagiarism.SourceFilePlagiarism;
import ProjectLoad.Project;
import ProjectLoad.SourceFile;

//Used to view source files
public class FileViewer implements Serializable {
	MainInterface mi;
	
	JPanel fileMainPanel;
	JPanel filePanelLeft;
	JPanel filePanelRight;
	
	JScrollPane scrollPaneLeft;
	JScrollPane scrollPaneRight;
	
	JTextArea fileTextLeft;
	JTextArea fileTextRight;
	
	JTextPane textLeft;
	JTextPane textRight;
	
	StyledDocument docLeft;
	StyledDocument docRight;
	
	JMenuBar menuLeft;
	JMenuBar menuRight;
	
	JSplitPane splitPane;
	
	//Keeps the opened source files
	List<SourceFileDocument> documentsLeft = new ArrayList<SourceFileDocument>();
	List<SourceFileDocument> documentsRight = new ArrayList<SourceFileDocument>();

	public FileViewer(MainInterface mi)
	{
		this.mi = mi;
		initUI();
	}
	
	private SourceFileDocument getDocumentLeft(int i)
	{
		return documentsLeft.get(i);
	}
	
	private void addDocumentLeft(SourceFileDocument document)
	{
		documentsLeft.add(document);
	}
	
	public void removeDocumentLeft(int i)
	{
		documentsLeft.remove(i);
	}
	
	private SourceFileDocument getDocumentRight(int i)
	{
		return documentsRight.get(i);
	}
	
	private void addDocumentRight(SourceFileDocument document)
	{
		documentsRight.add(document);
	}
	
	public void removeDocumentRight(int i)
	{
		documentsRight.remove(i);
	}
	
	//Set the text on left document, if it comes from plagiarised check, highlight its matches
	public void setTextLeft(final SourceFile doc,final Project proj,boolean isPlag,List<int[]> positions)
	{
		SourceFileDocument srcDoc = new SourceFileDocument(doc,proj,textLeft);
		addDocumentLeft(srcDoc);
		if(isPlag)
		{
			for(int i=0;i<positions.size();i++)
			{
				srcDoc.editPlagiarismDocument(positions.get(i));
			}
		}
		
		final String textFile = doc.getSourceFileDocument();
		
		if(menuLeft==null)
		{
			menuLeft = new JMenuBar();
			menuLeft.setBackground(Color.cyan);
		}
		final JMenu file = new JMenu(doc.getSourceFileName());
		for(int i=0;i<menuLeft.getMenuCount();i++)
		{
			menuLeft.getMenu(i).setBackground(null);
		}
		file.setOpaque(true);
		file.setBackground(Color.white);
		
		final JMenuItem open = new JMenuItem("Open");
		JMenuItem close = new JMenuItem("Close");
		JMenuItem name = new JMenuItem(doc.getSourceFileName());
		
		open.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				int pos =menuLeft.getComponentIndex(file);
				
				getDocumentLeft(pos).setDocument(textLeft);
				
				for(int i=0;i<menuLeft.getMenuCount();i++)
				{
					menuLeft.getMenu(i).setBackground(null);
				}
	
				file.setOpaque(true);
				file.setBackground(Color.white);	
			}
		});
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				int pos =menuLeft.getComponentIndex(file);
				
				StyledDocument doc = getDocumentLeft(pos).document;
				menuLeft.remove(file);
				try {
					if(getDocumentLeft(pos).document.getText(0,doc.getLength()).equals(textFile))
						getDocumentLeft(pos).document.remove(0,doc.getLength());
				} catch (BadLocationException e) {}
				filePanelLeft.revalidate();
				filePanelLeft.repaint();
				removeDocumentLeft(pos);
			}
		});
		file.add(name);
		file.addSeparator();
		file.add(open);
		file.add(close);
		menuLeft.add(file);
		filePanelLeft.add(menuLeft,BorderLayout.NORTH);
		filePanelLeft.revalidate();
		filePanelLeft.repaint();
	}
	
	//Set the text on left document, if it comes from plagiarised check, highlight its matches
	public void setTextRight(final SourceFile doc,final Project proj,boolean isPlag,List<int[]> positions)
	{
		SourceFileDocument srcDoc = new SourceFileDocument(doc,proj,textRight);
		addDocumentRight(srcDoc);
		if(isPlag)
		{
			for(int i=0;i<positions.size();i++)
			{
				srcDoc.editPlagiarismDocument(positions.get(i));
			}
		}
		
		final String textFile = doc.getSourceFileDocument();
		
		if(menuRight==null)
		{
			menuRight= new JMenuBar();
			menuRight.setBackground(Color.cyan);
		}
		final JMenu file = new JMenu(doc.getSourceFileName());
		for(int i=0;i<menuRight.getMenuCount();i++)
		{
			menuRight.getMenu(i).setBackground(null);
		}
		file.setOpaque(true);
		file.setBackground(Color.white);
		
		JMenuItem open = new JMenuItem("Open");
		JMenuItem close = new JMenuItem("Close");
		JMenuItem name = new JMenuItem(doc.getSourceFileName());
		
		open.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				int pos =menuRight.getComponentIndex(file);
				getDocumentRight(pos).setDocument(textRight);
				for(int i=0;i<menuRight.getMenuCount();i++)
				{
					menuRight.getMenu(i).setBackground(null);
				}
				file.setOpaque(true);
				file.setBackground(Color.white);
			}
		});
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt) 
			{
				int pos =menuRight.getComponentIndex(file);
				//getDocument(pos).setDocument(textLeft);
				StyledDocument doc = getDocumentRight(pos).document;
				menuRight.remove(file);
				try {
					if(getDocumentRight(pos).document.getText(0,doc.getLength()).equals(textFile))
						getDocumentRight(pos).document.remove(0,doc.getLength());
				} catch (BadLocationException e) {}
				filePanelRight.revalidate();
				filePanelRight.repaint();
				removeDocumentRight(pos);
			}
		});
		file.add(name);
		file.addSeparator();
		file.add(open);
		file.add(close);
		menuRight.add(file);
		filePanelRight.add(menuRight,BorderLayout.NORTH);
		filePanelRight.revalidate();
		filePanelRight.repaint();
	}
	
	//Called after opening the pair of plagiarised files in order to show their matches
	public void setResultsComparison(DefaultMutableTreeNode selectedNode)
	{
		DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedNode.getParent();
		DefaultMutableTreeNode poolNode = (DefaultMutableTreeNode) projectNode.getParent();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) poolNode.getParent();
		
		int poolIndex = root.getIndex(poolNode);
		int projectIndex = poolNode.getIndex(projectNode);
		int fileIndex = projectNode.getIndex(selectedNode);
		
		PoolPlagiarism plagiarisedPool = mi.getExplorer().getPlagiarisedPool(poolIndex);
		ProjectPlagiarism plagiarisedProject = plagiarisedPool.getPlagiarisedProjects(projectIndex);
		SourceFilePlagiarism plagiarisedSourceFiles = plagiarisedProject.getPlagiarisedSourceFiles(fileIndex);
		
		setTextLeft(plagiarisedSourceFiles.sourceFile1,plagiarisedProject.project1,true,plagiarisedSourceFiles.positions1);
		
		setTextRight(plagiarisedSourceFiles.sourceFile2,plagiarisedProject.project2,true,plagiarisedSourceFiles.positions2);	
	}
	
	private void initUI()
	{
		Font font= new Font("font",Font.PLAIN,14);
		
		fileMainPanel = new JPanel();
		fileMainPanel.setBorder(new EmptyBorder(new Insets(0, 10,10,10)));
		fileMainPanel.setLayout(new GridLayout(1,2));
		
		filePanelLeft = new JPanel();
		filePanelRight= new JPanel();
		//JPanel resultPanel = new JPanel();
		
		scrollPaneLeft = new JScrollPane();
		scrollPaneRight= new JScrollPane();
	
		JPanel antiWrapLeftPanel = new JPanel(new BorderLayout());
		JPanel antiWrapRightPanel = new JPanel(new BorderLayout());
	
		textLeft = new JTextPane();
		textRight= new JTextPane();
		
		textLeft.setFont(font);
		textRight.setFont(font);
		
		textLeft.setEditable(false);
		textRight.setEditable(false);
		
		filePanelLeft.setLayout(new BorderLayout());
		filePanelRight.setLayout(new BorderLayout());
		
		antiWrapLeftPanel.add(textLeft,BorderLayout.CENTER);
		antiWrapRightPanel.add(textRight,BorderLayout.CENTER);
		
		scrollPaneLeft = new JScrollPane(antiWrapLeftPanel);
		scrollPaneRight = new JScrollPane(antiWrapRightPanel);
		
		filePanelLeft.add(scrollPaneLeft,BorderLayout.CENTER);
		filePanelRight.add(scrollPaneRight,BorderLayout.CENTER);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,filePanelLeft,filePanelRight);
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerSize(5);
		
		fileMainPanel.add(splitPane);
	}
	
	public void setFontLeft(int size)
	{
		Font newFont = new Font("font",Font.PLAIN,size);
		textLeft.setFont(newFont);
	}
	
	public void setFontRight(int size)
	{
		Font newFont = new Font("font",Font.PLAIN,size);
		textRight.setFont(newFont);
	}
	
}
