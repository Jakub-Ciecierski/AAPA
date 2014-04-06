package Plagiarism;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ProjectLoad.ProjectPool;

//Begins to run plagiarsm check between every project that it holds
public class PoolPlagiarism implements Serializable{
	public ProjectPool projectPool;
	public List<ProjectPlagiarism> plagiarisedProjects = new ArrayList<ProjectPlagiarism>();
	public boolean isPlagiarised;
	
	public PoolPlagiarism(ProjectPool projectPool,int k,int w)
	{
		for(int i=0;i<projectPool.getProjectCount();i++)
			new Fingerprint(projectPool.getProject(i),k,w);
		
		this.projectPool=projectPool;
		this.isPlagiarised=false;
		
		for(int i=0;i<projectPool.getProjectCount();i++)
			for(int j=i+1;j<projectPool.getProjectCount();j++)
			{
				ProjectPlagiarism projectPlag =new ProjectPlagiarism(projectPool.getProject(i),projectPool.getProject(j));
				if(projectPlag.isPlagiarised)
				{
					plagiarisedProjects.add(projectPlag);
					this.isPlagiarised=true;
				}
			}
	}
	
	public int getPlagiarisedProjectsCount()
	{
		return this.plagiarisedProjects.size();
	}
	
	public ProjectPlagiarism getPlagiarisedProjects(int i)
	{
		return this.plagiarisedProjects.get(i);
	}
}
