import org.w3c.dom.Element;

/**The subclass of Command for FileCommand. 
  *Handles FileCommand type.
   */

public class FileCommand extends Command{
	private String id = null;
	private String path = null;
	
	public void describe(){
		System.out.println("Parsing file ...");
	}
	
/**
  * Maintain the CmdCommand type command.
  * @param elem the file element passed from BatchParser.
  */

	public void parse(Element elem) throws Exception{
		describe();
		id = elem.getAttribute("id");
		
		if(id == null || id.isEmpty()){
			throw new ProcessException("Missing Id in file command");
		}
		
		//System.out.println("Id: " + id);
		
		path = elem.getAttribute("path");
		
		if(path == null || path.isEmpty()){
			throw new ProcessException("Missing path in file command");
		}
		//System.out.println("path: " + path);
	}
	public String getID(){
		return id;
	}
	public String getPath(){
		return path;
	}	
}