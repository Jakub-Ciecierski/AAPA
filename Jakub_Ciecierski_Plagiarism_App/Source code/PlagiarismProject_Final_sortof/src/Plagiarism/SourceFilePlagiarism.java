package Plagiarism;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ProjectLoad.SourceFile;

public class SourceFilePlagiarism implements Serializable {
	public SourceFile sourceFile1;
	public SourceFile sourceFile2;

	public List<int[]> positions1 = new ArrayList<int[]>();
	public List<int[]> positions2 = new ArrayList<int[]>();
	
	/*
	 Used hashes are used to compute the score of plagiarism corectly.
	 We dont want to compute hash h of file x that has been found
	 multiple times in file y
	 */
	List<Hash> usedHashes1 = new ArrayList<Hash>();
	List<Hash> usedHashes2 = new ArrayList<Hash>();
	
	public double score1;
	public double score2;
	
	public SourceFilePlagiarism(SourceFile sourceFile1,SourceFile sourceFile2)
	{
		this.sourceFile1= sourceFile1;
		this.sourceFile2= sourceFile2;
	}
	
	//Every time a match has been detected, add the hash to usedHashes
	public void addPlagiarisedScore(Hash hash1,Hash hash2,ProjectPlagiarism projPlag)
	{
		int[] pos1 = new int[2];
		
		pos1[0]=hash1.startPos;
		pos1[1]=hash1.endPos;
		
		
		int[] pos2 = new int[2];
		pos2[0] = hash2.startPos;
		pos2[1] = hash2.endPos;
		
		positions1.add(pos1);
		positions2.add(pos2);
		
		if(!usedHashes1.contains(hash1))
			usedHashes1.add(hash1);
		if(!projPlag.usedHashes1.contains(hash1))
			projPlag.usedHashes1.add(hash1);
			
		if(!usedHashes2.contains(hash2))
			usedHashes2.add(hash2);
		if(!projPlag.usedHashes2.contains(hash2))
			projPlag.usedHashes2.add(hash2);
	}
	
	//Computes the score by comparing the ratio of matches found to fingerprint size 
	public void computeScore()
	{
		score1=(double)usedHashes1.size()/sourceFile1.fingerprint.size();
		score2=(double)usedHashes2.size()/sourceFile2.fingerprint.size();
	}
}
