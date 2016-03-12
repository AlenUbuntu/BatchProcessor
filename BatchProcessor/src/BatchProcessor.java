import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * BatchProcessor reads arguments from the command line 
 * given by the user, execute generated batch and handle 
 * any exception generated during this process
 */

public class BatchProcessor
{
	public static void main(String[] args){
		try
		{
			String filename = null;
			if(args.length == 0)
				throw new ProcessException("\nNo batch file found in the command! \nPlease enter a batch file");
			else
			{
				filename = args[0];
				Batch bat = new BatchParser(filename).buildBatch();
				BatchProcessor.executeBatch(bat);
			}
			
		}
		catch(Exception e){
			try{
				File log = new File("error.log");
				PrintWriter out = new PrintWriter(log);
				e.printStackTrace(out);
				System.out.println("Error: " + e.getMessage());
				System.out.println("       Please refer to error.log for details");
				out.close();
			}
			catch(FileNotFoundException e2){
				e2.printStackTrace();
			}
		}
	}

/** Execution of the batch 
    for either the cmd Command or the pipe Command respectively.
 */	
	private static void executeBatch(Batch batch) throws Exception{
		List<Command> commandList = batch.getExecutableCommand();
		HashMap<String,Command> cmdLookUp = batch.getCommands();
		String wkDir = batch.getWorkingDir();
		
		if(commandList.isEmpty()){
			throw new ProcessException("The batch contains no executable program");
		}
		
		ListIterator<Command> iterator = commandList.listIterator();
		
		while(iterator.hasNext()){
			Command cmd = iterator.next();
			String className = cmd.getClass().getName();
						
			if(className.equalsIgnoreCase("CmdCommand")){
				
				CmdCommand cmd1 = (CmdCommand)cmd;
				List<String> cmdArgs = cmd1.getCmd();
				String inID = cmd1.getInID();
				String outID = cmd1.getOutID();
				String inFile = null;
				String outFile = null;
								
				if(!(inID == null || inID.isEmpty()))
					if(cmdLookUp.containsKey(inID))
						inFile = ((FileCommand)cmdLookUp.get(inID)).getPath();
					else{
						throw new ProcessException("Unable to locate In FileCommand with id: " + inID);
					}
						
				if(!(outID == null || outID.isEmpty()))
					if(cmdLookUp.containsKey(outID))
						outFile = ((FileCommand)cmdLookUp.get(outID)).getPath();
					else{
						throw new ProcessException("Unable to locate OUT FileCommand with id: " + outID);
					}
					
				
				cmd1.execute(cmdArgs,wkDir,inFile,outFile);	
			}
			
			if(className.equalsIgnoreCase("PipeCommand")){
				PipeCommand cmd1 = (PipeCommand)cmd;
				List<String> cmdArgs1 = cmd1.getCommand1();
				List<String> cmdArgs2 = cmd1.getCommand2();
				String inID = cmd1.getInFileID();
				String outID = cmd1.getOutFileID();
				String inFile = null;
				String outFile = null;
								
				if(!(inID == null || inID.isEmpty()))
					inFile = ((FileCommand)cmdLookUp.get(inID)).getPath();
				if(!(outID == null || outID.isEmpty()))
					outFile = ((FileCommand)cmdLookUp.get(outID)).getPath();
				
				
				cmd1.execute(cmdArgs1,cmdArgs2,wkDir,inFile,outFile);	
			}	
		}
		
		System.out.println("Batch Execution Done!" );
	}
}
				
		