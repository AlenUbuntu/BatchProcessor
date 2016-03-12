# BatchProcessor

This software is a Platform-Independent Java-based batch processor utilizing xml as processing guidance.

# Important Note:

This program is tested using JDK1.8.0_45. If you are using a different JDK, make sure to pre-compile the source codes since the binary executable class file is invalid in this case.

# Description:
	
	- To start up this software, Please go to the bin/ directory and type the following command in terminal (Linux) or cmd prompt (windows):
	java BatchProcessor "path to xml file"

	- WorkFlow:
		read xml file -> parse xml file -> execute cmds -> output result to bin/work/ directory


	- XML commands implemented:
	  1. wd Command

		wd
			Description:
				       Sets the batch’s working directory i.e. the directory the batch will execute within.
			Arguments:
				id   - A name that uniquely identifies the command in the batch file.
				path - The path to the working directory
	  2.file Command

		File
			Description:
				 	   Identifies a file that is contained within the batch’s working directory.
			Arguments:
				id   - A name that uniquely identifies the command in the batch file.
				path - The path to the file including its name and extension. The path will always be evaluated
					   relative to the working directory specified by the ‘wd’ command.
	  3.cmd Command

		cmd
			Description:
				 A command that will be executed in a process.
		Arguments:
				id   - A name that uniquely identifies the command in the batch file.
 				path - The path to the executable. If the path is relative, it will use the system’s executable
					   PATH to locate the executable file.
				args - This is a string that contains the arguments that will be passed to the executable
					   specified by the ‘path’ option.
				in   - This is the ID of the file (specified in a file command) that will be directed to the
					   executable process’s stdin stream.
				out  - This is the ID of the file (specified in a file command) that will be directed to the
					   executable process’s stdout stream
	   4.pipe Command

	    pipe
			Description:
				 Pipe is an interconnection between two processes (cmd). The two cmd’s identified
		Arguments:
				id   - A name that uniquely identifies the pipe in the batch file.
				Cmd  - Element Two CMD elements that define the P1 and P2 described above. Both commands will be
					   executed concurrently with P1 stdout copied to P2 stdin.
