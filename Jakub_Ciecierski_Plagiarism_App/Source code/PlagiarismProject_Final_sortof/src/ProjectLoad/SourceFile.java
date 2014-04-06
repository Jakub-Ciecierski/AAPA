package ProjectLoad;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import Plagiarism.*;

public class SourceFile implements Serializable  {
	//String holding the actuall document
	private String sourceFile;
	
	private String formatedSourceFile;
	private String fileName;
	
	public int linesCount;
	
	//Holds the name of object that this source file would create
	public  String objectName;
	
	public Vector<Integer> originalPosition= new Vector<Integer>();
	public List<Hash> fingerprint = new ArrayList<Hash>();
	public List<Kgram> kgrams = new ArrayList<Kgram>();
	
	private boolean isJavaSource;
	private boolean isCppSource;
	
	public SourceFile(String sourceFile,String fileName,String type)
	{
		linesCount=0;
		if(type=="java")
		{
			isJavaSource=true;
			isCppSource=false;
		}
		if(type=="cpp")
		{
			isJavaSource=false;
			isCppSource=true;
		}
		this.sourceFile=sourceFile;
		setSourceFileName(fileName);
		removeIrrelevantFeatures();
	}
	
	public String getSourceType()
	{
		if(isJavaSource)
			return "java";
		if(isCppSource)
			return "cpp";
		return null;
	}
	
	public String getSourceFileDocument()
	{
		return this.sourceFile;
	}
	
	public String getSourceFileName()
	{
		return this.fileName;
	}
	
	public void setSourceFileName(String name)
	{
		this.fileName=name;
		this.objectName=name.substring(0,name.length()-5);
	}
	
	public void addFingerprint(List<Hash> fingerprint)
	{
		this.fingerprint.addAll(fingerprint);
	}
	
	//Used to clear the fingerprint before every new plagiarism check 
	public void clearFingerprint()
	{
		fingerprint = new ArrayList<Hash>();
		kgrams = new ArrayList<Kgram>();
	}
	
	//Removes irrelevant features in preparation of hashing
	//saves the position of each character in formated string to represent it in the original one
	public void removeIrrelevantFeatures()
	{
		formatedSourceFile = "";
		for(int i=0;i<sourceFile.length();i++)
		{
			char c = sourceFile.charAt(i);
			if(c=='\n')
				linesCount++;
			if(c!=' '&& c!='\n'&& c!='\r' &&c!='\t' && c!=';' && c!=',')
			{
				formatedSourceFile+=sourceFile.charAt(i);
				originalPosition.add(i);
			}
		}
	}
	
	//Computes the k grams.
	public void computeKGrams(int k)
	{
		for(int i=0;i<formatedSourceFile.length();i++)
		{
			try
			{
				String kgram = formatedSourceFile.substring(i,i+k);
				Kgram kg= new Kgram(kgram,originalPosition.get(i),originalPosition.get(i+k-1));
				this.kgrams.add(kg);
			}
			catch(StringIndexOutOfBoundsException e){}
		}
	}
}
