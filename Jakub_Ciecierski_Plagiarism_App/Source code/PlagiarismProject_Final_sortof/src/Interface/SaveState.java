package Interface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//Saves the pools of projects
public class SaveState {
	
	public SaveState(String filename,MainInterface mi) throws IOException
	{
		File file = new File(filename+".data");
		ObjectOutputStream save=null;
		try 
		{
			file.createNewFile();
			save = new ObjectOutputStream(new FileOutputStream(file));
			save.writeObject(mi.explorer.projectPools);
		}
		catch (Exception e) {}
		finally{
			save.close();
		}
	}
}
