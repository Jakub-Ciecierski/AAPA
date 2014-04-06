package ProjectLoad;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import Plagiarism.*;

//Class project saves source files of its type
public class Project implements Serializable {
	private String projectName;
	private List<SourceFile> sourceFiles = new ArrayList<SourceFile>();
	private static final String javaSourceFile =".java";
	private boolean isJavaProject;
	private boolean isCppProject;
	
	//Creates a project
	public Project(String dir,String type) throws WrongProjectTypeException
	{
		if(type=="java")
		{
			isJavaProject=true;
			isCppProject=false;
		}
		if(type=="cpp")
		{
			isJavaProject=false;
			isCppProject=true;
		}
		try 
		{
			loadSourceFiles(dir);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getProjectType()
	{
		if(isJavaProject)
			return "java";
		if(isCppProject)
			return "cpp";
		return null;
	}
	
	public int getSourceFileCount()
	{
		return this.sourceFiles.size();
	}
	
	public SourceFile getSourceFile(int i)
	{
		try
		{
			return this.sourceFiles.get(i);
		}
		catch(Exception e)
		{
			return null;
		}	
	}
	
	public String getProjectName()
	{
		return this.projectName;
	}
	
	//Adds a source file to project
	public void addSourceFile(SourceFile sourceFile)
	{
		this.sourceFiles.add(sourceFile);
	}
	
	public void addSourceFile(List<SourceFile> sourceFiles)
	{
		this.sourceFiles.addAll(sourceFiles);
	}
	
	public void setProjectName(String projectName)
	{
		try
		{
			this.projectName=projectName.substring(0,projectName.length()-4);
		}
		catch(StringIndexOutOfBoundsException e)
		{}
	}
	//Checks if a given file is a java source file
	private boolean isJavaSource(String filename)
	{
		try
		{
			if(filename.substring(filename.length()-5,filename.length()).equals(".java"))
					return true;
		}
		catch(StringIndexOutOfBoundsException e){}
		return false;
	}
	//Checks if a given file is a cpp file
	private boolean isCppSource(String filename)
	{
		try{
			if(filename.substring(filename.length()-4,filename.length()).equals(".cpp"))
				return true;
			if(filename.substring(filename.length()-2,filename.length()).equals(".h"))
				return true;
		}
		catch(StringIndexOutOfBoundsException e){}
		return false;
	}
	
	//Loads the source files from the archive file
	//throws an exeption if no correct files have been loaded
	public void loadSourceFiles(String dir) throws IOException, WrongProjectTypeException
	{
		ZipInputStream zis = null;
		FileInputStream fis = null;
		try{
			fis = new FileInputStream (dir);
			zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			
			while(ze!=null){
				File file = new File(ze.getName());
				String path = ze.getName();
				
				String name=file.getName();
				if(isJavaProject)
				{
					if(isJavaSource(name))
					{
						if(!path.contains("/bin/"))
						{
							int c;
							String document = "";
							while((c=zis.read())!=-1)
								document+=((char)c);
							SourceFile sourceFile = new SourceFile(document,name,"java");
							this.sourceFiles.add(sourceFile);
						}
					}
					ze = zis.getNextEntry();
				}
				if(isCppProject)
				{
					if(isCppSource(name))
					{
						int c;
						String document = "";
						while((c=zis.read())!=-1)
							document+=((char)c);
						SourceFile sourceFile = new SourceFile(document,name,"cpp");
						this.sourceFiles.add(sourceFile);
					}
					ze = zis.getNextEntry();
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			zis.close();
			fis.close();
		}
		if(this.getSourceFileCount()==0)
			throw new WrongProjectTypeException();
	}
}
