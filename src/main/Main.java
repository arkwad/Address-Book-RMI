/**
 * 
 */
package main;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import rmi_client.Client;
import rmi_server.Server;
import utils.Utils;
 
/**
 * @brief Constructor
 *
 */
public class Main 
{
    // class's attributes
	static Registry register;
	@SuppressWarnings("unused")
	private Server myServer;
	private Client myClient;
	private Utils utils;
	
	protected Main() throws Exception
	{
		this.utils = new Utils();
		this.myServer = new Server();
		this.myClient = new Client();
	}
	
    public static void main(String[] args) throws Exception, IOException
    {
    	System.out.println("Application started...");
    	
    	// create application instance
    	Main app = new Main();
    	// run infinite loop
    	app.infinite_loop();
    }
    
    @SuppressWarnings({ "resource", "unused" })
	public void infinite_loop() throws IOException
    {
    	List <String> argsList = new ArrayList<String>();
    	
		System.out.println("Type help to get list of available commands...");
		
		Scanner scan = new Scanner(System.in);
		while(true)
		{
			String s = scan.next();
		
			switch (s.toLowerCase())
			{
				case "help":
				{
					print_commands();
					break;
				}
				case "add":
				{
					argsList.clear();
					for ( String arg : utils.listOfProperties )
					{
						if ( !arg.equals( utils.listOfProperties.get(0) ) )
						{
							System.out.println("Enter " + arg);
							argsList.add( scan.next() );	
						}
					}
					if (this.get_client().addEntry(argsList))
					{
						System.out.println("Entry added sucessfully!");
					}
					else
					{
						System.out.println("Error ocured during adding! Entry not added!");
					}
					break;
				}
				case "get":
				{
					argsList.clear();
					
					List<String> outputList = new ArrayList<String>();
					/* get switch */
					s = scan.next();
					if ( s.equals("--all"))
					{
						if ( this.get_client().getSpecifiedList(s, "", outputList))
						{
							for ( String line : outputList )
							{
								System.out.println(line);
							}
						}
						else
						{
							System.out.println("An error occured during receiving list of records!");
						}
					}
					String action = scan.next();
					
					break;
				}
				case "remove":
				{
					s = scan.next();
					String action = scan.next();
					
					break;
				}
				case "clear":
				{
					s = scan.next();
					String action = scan.next();
					
					break;
				}
				case "edit":
				{
					s = scan.next();
					String action = scan.next();
					
					break;
				}
				default:
				{
					System.out.println("Wrong syntax...");
					break;
				}	
			}
		}	
	}
  
    private static void print_commands()
    {
    	System.out.println("get - command that reads from Address Book specified records \n"
    			+ "\tSwitches: \n"
    			+ "\t\t * --by-name <name> - returns all records with name <name> \n"
    			+ "\t\t * --by-surname <surname> - returns all records with surname <surname>\n"
    			+ "\t\t * --by-id <id> - returns record with given id value <id>\n"
    			+ "\tExamples:\n"
    			+ "\t\t * get --by-name John\n"
    			+ "\t\t * get --by-surname Smith\n"
    			+ "\t\t * get --by-id 2\n"
    			+ "\t\t * get --all\n");
    	
    	System.out.println("add - command that add new record do Address Book with given values.\n"
    			+ "Values of all fiels of record are required\n"
    			+ "\tExamples:\n"
    			+ "\t\t * in> add\n"
    			+ "\t\t * out> Enter name:\n"
    			+ "\t\t * in> John\n"
    			+ "\t\t * out> Enter surname:\n"
    			+ "\t\t * in> Smith\n"
    			+ "\t\t * out> Enter age:\n"
    			+ "\t\t * in> 42\n"
    			+ "\t\t * out> Enter street:\n"
    			+ "\t\t * in> Bedford\n"
    			+ "\t\t * out> Enter building number:\n"
    			+ "\t\t * in> 24\n"
    			+ "\t\t * out> Enter flat number:\n"
    			+ "\t\t * in> 42\n"
    			+ "\t\t * out> Enter city:\n"
    			+ "\t\t * in> New York\n"
    			+ "\t\t * out> Enter post code:\n"
    			+ "\t\t * in> MK42 0AA\n"
    			+ "\t\t * out> Enter phone number:\n"
    			+ "\t\t * in> 123-456-789\n");
    	
    	System.out.println("remove -  command that remove record with given id number.\n"
    			+ "\tExamples: remove 2 \n");
    	
    	System.out.println("edit - command that edit record with given id number.\n"
    			+ "Values of all fiels of record are required\n"
    			+ "\tExamples:\n"
    			+ "\t\t * in> edit 2\n"
    			+ "\t\t * out> Enter name:\n"
    			+ "\t\t * in> John\n"
    			+ "\t\t * out> Enter surname:\n"
    			+ "\t\t * in> Smith\n"
    			+ "\t\t * out> Enter age:\n"
    			+ "\t\t * in> 42\n"
    			+ "\t\t * out> Enter street:\n"
    			+ "\t\t * in> Bedford\n"
    			+ "\t\t * out> Enter building number:\n"
    			+ "\t\t * in> 24\n"
    			+ "\t\t * out> Enter flat number:\n"
    			+ "\t\t * in> 42\n"
    			+ "\t\t * out> Enter city:\n"
    			+ "\t\t * in> New York\n"
    			+ "\t\t * out> Enter post code:\n"
    			+ "\t\t * in> MK42 0AA\n"
    			+ "\t\t * out> Enter phone number:\n"
    			+ "\t\t * in> 123-456-789\n");
    	
    	System.out.println("clear - commands that clears Address Book \n"
    			+ "\tExample: clear\n");
    	
    }

	public Client get_client()
    {
    	return this.myClient;
    }
}
