import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**This BatchParser class builds an instance of Batch containing the N Commands parsed from the XML document 
 *provided in the batch file. It visits each of the XML elements in the given XML document and generates the 
 *correct Command subclass from the element. 
 */
public class BatchParser{
	private File f = null;
	
	public BatchParser (String filename){
		System.out.println("Opening " + filename);
		f = new File(filename);
	}
	
	public Batch buildBatch() throws Exception{
		return buildBatch(f);
	}
   
 /**
     * Internal method to build the batch.
     * @param f the name of the batch file.
     */
	
	private Batch buildBatch(File f) throws Exception{
		FileInputStream fis = new FileInputStream(f);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fis);
		
		Element pnode = doc.getDocumentElement();
		NodeList nodes = pnode.getChildNodes();
		
		Batch bat = new Batch();
		
		for(int i=0;i<nodes.getLength();i++){
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				Element elem = (Element) node;
				
				Command c = buildCommand(elem);
				c.parse(elem);
				bat.addCommand(c);	
			}
		}
		fis.close();
		return bat;
	}

/** Delegate the parsed elements to the correct Command subclass. 
*/ 
	private Command buildCommand(Element elem) throws ProcessException{
		String eleName = elem.getNodeName();
		Command cmd = null;
		if(eleName == null)
			throw new ProcessException("Unable to parse command from " + elem.getTextContent());
		else if(eleName.equalsIgnoreCase("wd")){
			cmd = new WdCommand();	
		}
		else if(eleName.equalsIgnoreCase("file")){
			cmd = new FileCommand();
		}
		else if(eleName.equalsIgnoreCase("cmd")){
			cmd = new CmdCommand();
		}
		else if(eleName.equalsIgnoreCase("pipe")){
			cmd = new PipeCommand();
		}
		else{
			throw new ProcessException("Unknown command" + eleName + "from: " + elem.getBaseURI());			
		}
		return cmd;
	}
}