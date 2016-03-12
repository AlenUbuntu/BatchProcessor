import org.w3c.dom.Element;
import java.io.File;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;

/**The subclass of Command for PipeCommand. 
  *Handles PipeCommand type.
   */
public class PipeCommand extends Command{
	private String id = null;
	
	private String cmd1ID = null;
	private String cmd2ID = null;
	
	private Element cmd1Node = null;
	private Element cmd2Node = null;
	
	private String inFileID = null;
	private String outFileID = null;
	
	private List<String> cmd1 = null;
	private List<String> cmd2 = null;
	
	
	public String getID(){
		return id;
	}
	
	public List<String> getCommand1(){
		return cmd1;
	}
	
	public List<String> getCommand2(){
		return cmd2;
	}
	
	public String getInFileID(){
		return inFileID;
	}
	
	public String getOutFileID(){
		return outFileID;
	}
	
	public void describe(){
		System.out.println("Parsing pipe ...");
	}

/**
     * Maintain the PipeCommand type command.
     * @param elem the pipe element passed from BatchParser.
     */
		
	public void parse(Element elem) throws Exception{
		describe();
		
		id = elem.getAttribute("id");
		if(id == null || id.isEmpty()){
			throw new ProcessException("Missing id in pipe command");
		}
		//System.out.println("id: " + id);
		
		NodeList list = elem.getChildNodes();
		for (int i = 0;i < list.getLength();i++){
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				if(cmd1Node == null)
					cmd1Node = (Element) node;
				else if(cmd2Node == null)
					cmd2Node= (Element) node;
			}
		}
		
		if(cmd1Node == null || cmd2Node == null)
			throw new ProcessException("Missing elements in pipe. Can't form pipe");
		
		cmd1ID = cmd1Node.getAttribute("id");
		cmd2ID = cmd2Node.getAttribute("id");
		if (cmd1ID == null || cmd1ID.isEmpty())
			throw new ProcessException("Missing Id in command one");
		if (cmd2ID == null || cmd2ID.isEmpty())
			throw new ProcessException("Missing Id in command two");
		
		cmd1 = new ArrayList<>();
		cmd2 = new ArrayList<>();
		
		String path = cmd1Node.getAttribute("path");
		if(path == null || path.isEmpty())
			throw new ProcessException("Missing Path in the first command of pipe");
		cmd1.add(path);
		//System.out.println(cmd1Node.getAttribute("id") + " path: " + path);
		
		String args = cmd1Node.getAttribute("args");
		if(!(args == null || args.isEmpty())){
			StringTokenizer tokens = new StringTokenizer(args);
			while (tokens.hasMoreTokens()){
				cmd1.add(tokens.nextToken());
			}
		}
		
		path = cmd2Node.getAttribute("path");
		if(path == null || path.isEmpty())
			throw new ProcessException("Missing Path in the second command of pipe");
		cmd2.add(path);
		//System.out.println(cmd2Node.getAttribute("id") + " path: " + path);
		
	    args = cmd2Node.getAttribute("args");
		if(!(args == null || args.isEmpty())){
			StringTokenizer tokens = new StringTokenizer(args);
			while (tokens.hasMoreTokens()){
				cmd2.add(tokens.nextToken());
			}
		}
		
		String Fi = cmd1Node.getAttribute("in");
		if(Fi == null || Fi.isEmpty())
			throw new ProcessException("Missing input file");
		inFileID = Fi;
		
		String Fo = cmd2Node.getAttribute("out");
		if(Fo == null || Fo.isEmpty())
			throw new ProcessException("Missing output file");
		outFileID = Fo;
	}	
	
/**
     * Execute the pipe command.
     * @param cmd1 the cmd1 elements from the batch file.
     * @param cmd2 the cmd2 elements from the batch file.
     * @param wkDir the working directory to the batch file.
     * @param inFile the input file.
     * @param outFile the output file.
     */
	public void execute(List<String> cmd1, List<String> cmd2, String wkDir, String inFile, String outFile) 
	throws Exception{
		ProcessBuilder process1 = new ProcessBuilder();
		
		// set process1's working directory
		if(!(wkDir == null || wkDir.isEmpty())){
			process1.directory(new File(wkDir));
			System.out.println("The working directory will be set to " + wkDir);
		}
		else 
			throw new ProcessException("Missing Working directory Argument");
		
		// set process1's command
		process1.command(cmd1);
		
		// set process1's input stream
		if(!(inFile == null || inFile.isEmpty())){
			File in = new File(wkDir,inFile);
			process1.redirectInput(in);
			System.out.println("File Command on file: "+inFile);
		}
		else{
			throw new ProcessException("Missing Input File Argument");
		}
		
		System.out.println("Command: "+ cmd1ID);
		
		ProcessBuilder process2 = new ProcessBuilder();
		
		// set process2's working directory
		process2.directory(new File(wkDir));
		
		// set process2's output stream
		if(!(outFile == null || outFile.isEmpty())){
			File out = new File(wkDir,outFile);
			process2.redirectOutput(out);
			System.out.println("File Command on file: "+outFile);

		}
		else{
			throw new ProcessException("Missing Output File Argument");
		}
		
		process2.command(cmd2);
		
		System.out.println("Command: "+ cmd2ID);
		
		
		// start process1 and process2
		System.out.println("Pipe Established\nStart processing ...");
		
		Process p1 = process1.start();
		Process p2 = process2.start();
		
		InputStream in = p1.getInputStream();
		OutputStream out = p2.getOutputStream();
		
		// pipe the output of process1 to input of process2
		copyStreams(in,out);
		
		
		// notify the end of process
		System.out.println("Pipe Processing Finished");
	}
	
	/**
	 * Copy the contents of the input stream to the output stream in
	 * separate thread. The thread ends when an EOF is read from the 
	 * input stream.   
	 */
	private static void copyStreams(final InputStream is, final OutputStream os){
		Runnable copyThread = (new Runnable(){
			@Override
			public void run(){
				try{
					int achar;
					while((achar = is.read())!= -1){
						os.write(achar);
					}
					os.close();
					is.close();
				}
				catch(IOException e){
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		new Thread(copyThread,"copyStream").run();
	}
	
	
	
	
	
}