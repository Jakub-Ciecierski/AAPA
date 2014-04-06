package Plagiarism;

import java.io.Serializable;

// Hash value and its position in the string
public class Hash implements Serializable{
	int startPos;
	int endPos;
	int value;
	Hash(String toHash,int startPos,int endPos)
	{
		this.value=toHash.hashCode();
		this.startPos=startPos;
		this.endPos=endPos;
	}
}
