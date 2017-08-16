/**
 * 
 */
package main;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.Scanner;
import rmi_client.Client;
import rmi_server.Server;
 
/**
 * @brief Constructor
 *
 */
public class Main 
{
    // class's attributes
	static Registry register;
	private Server myServer;
	private Client myClient;
	
	protected Main() throws Exception
	{
		this.myServer = new Server();
		this.myClient = new Client();
	}
	
    public static void main(String[] args) throws Exception, IOException
    {
    	System.out.println("Application started...");
    	
    	// create application instance
    	Main app = new Main();
    	//app.get_client().clearAddressBook();
    	
    	infinite_loop(app);
    }
    
    @SuppressWarnings("resource")
	private static void infinite_loop(Main app) throws IOException
    {
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
				case "send":
				{
					s = scan.next();
			
					break;
				}
				case "list":
				{
					s = scan.next();
					String action = scan.next();
					
					break;
				}
				case "save":
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
    	
    	System.out.println("save - commands that saves data to file based on number of record from last printed list\n"
    			+ "\tArguments: first : Spectrum or TimeHistory, second: number of record in list\n"
    			+ "\tExample: save Spectrum 2\n");
    	
    }

	public Client get_client()
    {
    	return this.myClient;
    }
}
