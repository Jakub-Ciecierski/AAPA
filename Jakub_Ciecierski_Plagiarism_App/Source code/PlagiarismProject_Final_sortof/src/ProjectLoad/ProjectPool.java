package ProjectLoad;
import java.io.Serializable;
import java.util.*;
import Plagiarism.*;

//ProjectPool saves projects with a given type
public class ProjectPool implements Serializable{
	private String poolName;
	private List<Project> projects = new ArrayList<Project>();
	private boolean isJavaPool;
	private boolean isCppPool;
	
	public ProjectPool(String poolName){
		this.poolName = poolName;
	}
	
	public ProjectPool(String poolName,String type){
		this.poolName = poolName;
		if(type=="java")
		{
			isJavaPool=true;
			isCppPool=false;
		}
		if(type=="cpp")
		{
			isCppPool=true;
			isJavaPool=false;
		}
	}
	
	public ProjectPool(String poolName,Project project){
		this.poolName = poolName;
		this.projects.add(project);
	}
	
	public String getPoolType()
	{
		if(isJavaPool)
			return "java";
		if(isCppPool)
			return "cpp";
		return null;
	}
	
	public String getPoolName()
	{
		return this.poolName;
	}
	
	public int getProjectCount()
	{
		return this.projects.size();
	}
	
	public Project getProject(int i)
	{
		return this.projects.get(i);
	}
	
	
	public void setPoolName(String name)
	{
		this.poolName=name;
	}
	
	public void addProject(Project project){
		this.projects.add(project);
	}
	
	public void removeProject(Project project){
		projects.remove(project);
	}
	public void removeProject(int i){
		projects.remove(i);
	}
}
