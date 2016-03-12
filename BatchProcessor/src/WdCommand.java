import org.w3c.dom.Element;

/**The subclass of Command for WdCommand. 
  *Handles WdCommand type.
   */

public class WdCommand extends Command{
	private String id = null;
	private String path = null;
	
	public void describe(){
		System.out.println("Parsing Wd ...");
	}
/**
  * Maintain the WdCommand type command.
  * @param elem the wd element passed from BatchParser.
  */

	public void parse(Element elem) throws Exception{
		describe();
		
		id = elem.getAttribute("id");
		if(id == null || id.isEmpty())
			throw new ProcessException("Missing Id in Wd command");
		//System.out.println("Id: " + id);
		
		path = elem.getAttribute("path");
		if(path == null || path.isEmpty())
			throw new ProcessException("Missing Path in Wd command");
		//System.out.println("Path: " + path);	
	}
	
	public String getID(){
		return id;
	}
	public String getPath(){
		return path;
	}
}