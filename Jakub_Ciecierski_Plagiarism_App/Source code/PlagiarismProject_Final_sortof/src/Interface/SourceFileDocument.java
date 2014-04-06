package Interface;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import ProjectLoad.Project;
import ProjectLoad.SourceFile;

//Used to store opened source files
public class SourceFileDocument {
	StyledDocument document;
	SourceFile sourceFile;
	Project project;
	String[] objects;
	
	public SourceFileDocument(SourceFile src,Project proj,JTextPane textPane)
	{
		document=new DefaultStyledDocument();
		
		this.sourceFile=src;
		this.project=proj;
		try {
			document.remove(0, document.getLength());
			document.insertString(0, sourceFile.getSourceFileDocument(),null);
			
		} catch (BadLocationException e) {}
		editDocument();
		textPane.setStyledDocument(document);
	}
	
	public void setDocument(JTextPane textPane)
	{
		textPane.setStyledDocument(document);
	}
	
	private void computeObjects()
	{
		objects = new String[project.getSourceFileCount()];
		for(int i =0;i<project.getSourceFileCount();i++)
		{
			objects[i]=project.getSourceFile(i).objectName;
		}
	}
	
	public void editPlagiarismDocument(int[] offset)
	{
		StyleContext sc = StyleContext.getDefaultStyleContext();
	    javax.swing.text.AttributeSet bgAset = (javax.swing.text.AttributeSet) sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, Color.red);
	    javax.swing.text.AttributeSet charAset = (javax.swing.text.AttributeSet) sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.white);
	    document.setCharacterAttributes(offset[0],offset[1]-offset[0],bgAset,false);
	    document.setCharacterAttributes(offset[0],offset[1]-offset[0],charAset,false);
	    
	}
	
	//Edits some of the key words and the object names from all source files 
	private void editDocument()
	{
		computeObjects();
	    StyleContext sc = StyleContext.getDefaultStyleContext();
	    javax.swing.text.AttributeSet aset = (javax.swing.text.AttributeSet) sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.magenta);
	    javax.swing.text.AttributeSet asetObject = (javax.swing.text.AttributeSet) sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.blue);
	    String txt = null;
		try {
			txt = document.getText(0, document.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	    for(int i=0;i<txt.length();i++)
	    {	
	    	try 
	    	{
	    		for(int j=0;j<objects.length;j++)
	    		{
	    			if(txt.substring(i,i+objects[j].length()).equals(objects[j]) && (txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || i==0 || txt.charAt(i-1)==';' || txt.charAt(i-1)=='\t') && (txt.charAt(i+objects[j].length())==' '||txt.charAt(i+objects[j].length())=='('))
	    				document.setCharacterAttributes(i, objects[j].length(),asetObject,false);
	    		}
				if(txt.substring(i,i+5).equals("class") && (txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || i==0 || txt.charAt(i-1)==';' || txt.charAt(i-1)=='\t') && txt.charAt(i+5)==' ' )
					document.setCharacterAttributes(i, 5, aset, false);
				if(txt.substring(i,i+6).equals("public") && (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';' || txt.charAt(i-1)=='\t') && txt.charAt(i+6)==' ')
					document.setCharacterAttributes(i, 6, aset, false);
				if(txt.substring(i,i+7).equals("private") && (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t') && txt.charAt(i+7)==' ')
					document.setCharacterAttributes(i, 7, aset, false);
				if(txt.substring(i,i+9).equals("protected") && (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t') && txt.charAt(i+9)==' ')
					document.setCharacterAttributes(i, 9, aset, false);
				if(txt.substring(i,i+3).equals("for")&& (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t'))
					document.setCharacterAttributes(i, 3, aset, false);
				if(txt.substring(i,i+2).equals("if")&& (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t'))
					document.setCharacterAttributes(i, 2, aset, false);
				if(txt.substring(i,i+5).equals("while")&& (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t'))
					document.setCharacterAttributes(i, 5, aset, false);
				if(txt.substring(i,i+3).equals("try")&& (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t'))
					document.setCharacterAttributes(i, 3, aset, false);
				if(txt.substring(i,i+5).equals("catch")&& (i==0 ||txt.charAt(i-1)==' '||txt.charAt(i-1)=='\n' || txt.charAt(i-1)==';'|| txt.charAt(i-1)=='\t'))
					document.setCharacterAttributes(i, 5, aset, false);
			} 
	    	catch (StringIndexOutOfBoundsException e) {}
	    }
	 }
	
}
