/**
* An exception that should be used to signal problems 
* during the process execution
*/

@SuppressWarnings("serial")
public class ProcessException extends Exception
{
	public ProcessException(String message)
	{
		super(message);
	}
	
	public ProcessException(String message, Throwable cause)
	{
		super(message,cause);
	}
}