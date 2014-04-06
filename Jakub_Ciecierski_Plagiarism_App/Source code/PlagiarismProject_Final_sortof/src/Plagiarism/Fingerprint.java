package Plagiarism;
import java.io.Serializable;
import java.util.*;

import ProjectLoad.*;

/*
 	substring match at least as long as the
 	guarantee threshold t, is detected
 	
 	matches shorter than the noise threshold k
 	are not detected
 	
 	size of the window w = t-k+1
 	
 */

public class Fingerprint implements Serializable{
	public Fingerprint(Project project,int k,int w)
	{
		for(int i=0;i<project.getSourceFileCount();i++)
		{
			SourceFile src = project.getSourceFile(i);
			src.clearFingerprint();	//clear the fingerprint before every plagiarism check
			src.computeKGrams(k);
			winnowing(computeHashCodes(src.kgrams),src,w);
		}
	}
	
	/*
	 procces of winnowing.
	 	From the computed hashcodes of kgrams from each source file
	 	we create hash windows (in a similar fashion of kgrams)  of size w each.
	 	Then take the least, most right, element from each window
	 	and let it be a fingerprint of a document.
	 */
	public void winnowing (List<Hash> hashCodes,SourceFile src,int w)
	{
		List<Hash> fingerprint = new ArrayList<Hash>();
		List<List<Hash>> windows = new ArrayList<List<Hash>>();
		boolean end = false;
		for(int i=0;i<hashCodes.size();i++)
		{
			List<Hash> window = new ArrayList<Hash>();
			try
			{
				for(int j=i;j<i+w;j++)
				{
					if(i+w>hashCodes.size())
					{
						end=true;
						break;
					}
					else
						window.add(hashCodes.get(j));
				}
			}
			catch(Exception e){}
			if(end)
				break;
			windows.add(window);
		}
		
		for(int i=0;i<windows.size();i++)
		{
			Hash hash=windows.get(i).get(0);
			for(int j=0;j<windows.get(i).size();j++)
			{
				if(hash.value>=windows.get(i).get(j).value)
					hash=windows.get(i).get(j);
			}
			if(!fingerprint.contains(hash))
				fingerprint.add(hash);
		}
		src.addFingerprint(fingerprint);
	}
	
	
	public List<Hash> computeHashCodes(List<Kgram> kgrams)
	{
		List<Hash> hashCodes = new ArrayList<Hash>();
		for(int i=0;i<kgrams.size();i++)
		{
			Kgram kg = kgrams.get(i);
			Hash hash = new Hash (kg.kgram,kg.startPos,kg.endPos);
			hashCodes.add(hash);
		}
		return hashCodes;
	}
}
