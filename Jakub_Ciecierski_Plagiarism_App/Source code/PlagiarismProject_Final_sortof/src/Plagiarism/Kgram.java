package Plagiarism;

import java.io.Serializable;

//Kgram and its position in the string
public class Kgram implements Serializable{
	public String kgram;
	public int startPos;
	public int endPos;
	public Kgram(String kgram,int startPos,int endPos)
	{
		this.kgram=kgram;
		this.startPos=startPos;
		this.endPos=endPos;
	}
}
