import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


/**This Batch class maintains the N Commands that were parsed from the given batch file. */

public class Batch{
	
	String workingDir;
	HashMap<String,Command> cmdLookUp;
	List<Command> commandList;

/**
     * Construct the Batch.
     */
	
	public Batch(){
		workingDir = null;
		cmdLookUp = new HashMap<String,Command>();
		commandList = new ArrayList<Command>();
	}

/**
     * Maintaince of the commands.
     * Different type of commands were mapped into different ID type for the convience of the later access.
     * List was empoloyed for the CmdCommand and PipeCommand for multiple fields. 
     */

	public void addCommand(Command cmd){
		String className = cmd.getClass().getName();
		if (className.equalsIgnoreCase("WdCommand")){
			workingDir = ((WdCommand)cmd).getPath();
			cmdLookUp.put(cmd.getID(), (WdCommand)cmd);
		}
		if (className.equalsIgnoreCase("FileCommand")){
			cmdLookUp.put(cmd.getID(), (FileCommand)cmd);
		}
		if (className.equalsIgnoreCase("CmdCommand")){
			cmdLookUp.put(cmd.getID(), (CmdCommand)cmd);
			commandList.add(cmdLookUp.get(cmd.getID()));
		}
		if (className.equalsIgnoreCase("PipeCommand")){
			cmdLookUp.put(cmd.getID(), (PipeCommand)cmd);
			commandList.add(cmdLookUp.get(cmd.getID()));
		}		
	}
	
	public String getWorkingDir(){
		return workingDir;
	}
	
	public HashMap<String,Command> getCommands(){
		return cmdLookUp;
	}
 	
	public List<Command> getExecutableCommand(){
		return commandList;
	}
}