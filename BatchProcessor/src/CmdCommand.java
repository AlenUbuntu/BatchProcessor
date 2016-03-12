import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**The subclass of Command for CmdCommand. 
  *Handles CmdCommand type.
   */

public class CmdCommand extends Command{
	private String id = null;
	private String path = null;
	private String args = null;
	private String inID = null;
	private String outID = null;
	private List<String> cmd = null;
	
	public void describe(){
		System.out.println("Parsing cmd ...");
	}
	
	public String getID(){
		return id;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getInID(){
		return inID;
	}
	
	public String getOutID(){
		return outID;
	}
	
	public List<String> getCmd(){
		return cmd;
	}
	
	public String getArgs(){
		return args;
	}

/**
     * Maintain the CmdCommand type command.
     * @param elem the cmd element passed from BatchParser.
     */
	
	public void parse(Element elem) throws Exception{
		describe();
		id = elem.getAttribute("id");
		
		if(id == null || id.isEmpty()){
			throw new ProcessException("Missing Id in cmd command");
		}
		//System.out.println("Id: " + id);
		
		path = elem.getAttribute("path");
		if(path == null || path.isEmpty()){
			throw new ProcessException("Missing path in cmd command");
		}
		//System.out.println("path: " + path);
		
		cmd = new ArrayList<String>();
		cmd.add(path);
		
		args = elem.getAttribute("args");
		if(!(args == null || args.isEmpty())){
			StringTokenizer tokens = new StringTokenizer(args);
			while(tokens.hasMoreTokens()){
				cmd.add(tokens.nextToken());
			}
		}
		
		//for (String t : cmd)
			//System.out.println("Args: " + t);
		
		String intmp = elem.getAttribute("in");
		if (!(intmp == null || intmp.isEmpty())){
			inID = intmp;
		}
		//System.out.println("inID: " + inID);
		
		String outtmp = elem.getAttribute("out");
		if(!(outtmp == null || outtmp.isEmpty())){
			outID = outtmp;
		}
		//System.out.println("outID: " + outID);
	}
	
}