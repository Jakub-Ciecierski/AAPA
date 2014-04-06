package Interface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import ProjectLoad.ProjectPool;

//Loads the pools from a given path
public class LoadState {
	public LoadState(String filename,MainInterface mi) throws IOException
	{
		File file = new File(filename);
		ObjectInputStream load=null;
		try 
		{
			load = new ObjectInputStream(new FileInputStream(file));
			
			List<ProjectPool> projectPools = new ArrayList<ProjectPool>();
			projectPools = (List<ProjectPool>) load.readObject();
			
			mi.explorer.projectPools=projectPools;
			mi.explorer.reload();
			
		}
		catch (Exception e) {}
		finally{
			load.close();
		}
	}
}
