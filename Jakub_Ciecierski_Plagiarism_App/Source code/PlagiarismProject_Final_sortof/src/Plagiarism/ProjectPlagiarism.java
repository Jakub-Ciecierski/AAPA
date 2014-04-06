package Plagiarism;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ProjectLoad.*;

//Run plagiarism check of every source file in pairs
public class ProjectPlagiarism implements Serializable {
	public Project project1;
	public Project project2;
	
	public boolean isPlagiarised;
	
	private int scoreCounter1;
	private int scoreCounter2;

	public double score1;
	public double score2;
	
	/*
	 Used hashes are used to compute the score of plagiarism corectly.
	 We dont want to compute hash h of file x that has been found
	 multiple times in file y
	 */
	List<Hash> usedHashes1 = new ArrayList<Hash>();
	List<Hash> usedHashes2 = new ArrayList<Hash>();
	
	public List<SourceFilePlagiarism> plagiarisedSourceFiles = new ArrayList<SourceFilePlagiarism>();
	
	public ProjectPlagiarism (Project project1,Project project2)
	{
		this.isPlagiarised=false;
		this.project1=project1;
		this.project2=project2;
		
		checkPlagiarism();
	}
	
	public int getPlagiarisedSourceFilesCount()
	{
		return this.plagiarisedSourceFiles.size();
	}
	
	public SourceFilePlagiarism getPlagiarisedSourceFiles(int i)
	{
		return this.plagiarisedSourceFiles.get(i);
	}
	
	//Computes the score by comparing the ratio of matches found to fingerprint size
	private void computeScore()
	{
		int max1=0;
		int max2=0;
		for(int i=0;i<project1.getSourceFileCount();i++)
		{
			max1+=project1.getSourceFile(i).fingerprint.size();
		}
		for(int i=0;i<project2.getSourceFileCount();i++)
		{
			max2+=project2.getSourceFile(i).fingerprint.size();
		}
		score1=(double)usedHashes1.size()/max1;
		score2=(double)usedHashes2.size()/max2;
	}
	
	private void checkPlagiarism()
	{
		for(int k=0;k<project1.getSourceFileCount();k++)
		{
			for(int l=0;l<project2.getSourceFileCount();l++)
			{
				boolean isPlagiarised=false;
				
				SourceFile src1=project1.getSourceFile(k);
				SourceFile src2=project2.getSourceFile(l);
				SourceFilePlagiarism sourceFilePlagiarism = new SourceFilePlagiarism(src1,src2);
				
				for(int m=0;m<src1.fingerprint.size();m++)
				{
					for(int n=0;n<src2.fingerprint.size();n++)
					{
						if(src1.fingerprint.get(m).value==src2.fingerprint.get(n).value)
						{
							isPlagiarised=true;
							this.isPlagiarised=true;
							
							int[] pos1 = new int[2];
							Hash hash1=src1.fingerprint.get(m);
							pos1[0]=src1.fingerprint.get(m).startPos;
							pos1[1]=src1.fingerprint.get(m).endPos;
							
							int[] pos2 = new int[2];
							Hash hash2=src2.fingerprint.get(n);
							pos2[0] = src2.fingerprint.get(n).startPos;
							pos2[1] = src2.fingerprint.get(n).endPos;
							
							sourceFilePlagiarism.addPlagiarisedScore(hash1,hash2,this);
						}
					}
				}
				if(isPlagiarised)
				{
					plagiarisedSourceFiles.add(sourceFilePlagiarism);
					sourceFilePlagiarism.computeScore();
				}
			}
		}
		computeScore();
	}
}
