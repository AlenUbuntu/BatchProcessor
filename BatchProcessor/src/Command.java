import org.w3c.dom.Element;
import java.io.File;
import java.util.List;

/**Command is an abstract class that defines three abstract methods: 
  *describe(): used to print a message to the console when the Command is executed. 
  *parse(): parse and extract the information contained in the given XML Element. 
  *execute() : execute the command. 
  */

public abstract class Command{
	public abstract void describe();
	public abstract void parse(Element elem) throws Exception;
	public abstract String getID();

/**
     * Execute the command.
     * @param cmd the cmd elements from the batch file.
     * @param wkDir the working directory to the batch file.
     * @param in the input file.
     * @param out the output file.
     */
	
	public void execute(List<String> cmd, String wkDir, String in, String out) throws Exception{
		ProcessBuilder builder = new ProcessBuilder();
		
		// set process working directory
		if(!(wkDir == null || wkDir.isEmpty())){
			builder.directory(new File(wkDir));
			System.out.println("The working directory will be set to " + wkDir);
		}
		else
			throw new ProcessException("Missing Working directory Argument");
		
		// set process input file and output file
		if(!(in == null || in.isEmpty())){
			File input = new File(wkDir,in);
			builder.redirectInput(input);
			System.out.println("File Command on file: " + in);
		}
		
		System.out.println("Command: "+ getID());
		System.out.println(getID() + " Deferring Execution");
		
		if(!(out == null || out.isEmpty())){
			File output = new File(wkDir,out);
			builder.redirectOutput(output);
			System.out.println("File Command on file: " + out);

		}
		
		// set the program or cmd that this process is going to run
		builder.command(cmd);
		
		// start running by creating a process
		Process process = builder.start();
		process.waitFor();
		System.out.println(getID()+" Exits" );		
	}
}