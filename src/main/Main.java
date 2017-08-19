/**
 * @author Arkadiusz Wadowski
 * @ Software Developer
 * @ Github: https://github.com/arkwad
 * @ Contact: wadowski.arkadiusz@gmail.com
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while(true)
		{
			argsList.clear();

			String name = reader.readLine();

			String[] args = name.split(" ");
			
			if (args.length >= 1)
			{
				switch ( args[0] )
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
						System.out.println("Entry added successfully!");
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
					if ( args.length > 1 )
					{

						if ( args[1].equals("--all"))
						{
							if ( this.get_client().getSpecifiedList( args[1], "", outputList))
							{
								if ( outputList.isEmpty() )
								{
									System.out.println("No records found!");
								}
								else
								{
									for ( String line : outputList )
									{
										System.out.print(line);
									}
								}
							}
							else
							{
								System.out.println("An error occured during receiving list of records!");
							}
						}
						else 
						{
							if ( 3 == args.length )
							{
								switch ( args[1] )
								{
								case "--by-id":
								{
									if ( this.get_client().getSpecifiedList(args[1], args[2], outputList) )
									{
										if ( outputList.isEmpty() )
										{
											System.out.println("No records found!");
										}
										else
										{
											for ( String line : outputList )
											{
												System.out.print(line);
											}
										}
									}
									else
									{
										System.out.println("An error occured during receiving list of records!");
									}
									break;

								}
								case "--by-name":
								{
									if ( this.get_client().getSpecifiedList(args[1], args[2], outputList) )
									{
										if ( outputList.isEmpty() )
										{
											System.out.println("No records found!");
										}
										else
										{
											for ( String line : outputList )
											{
												System.out.print(line);
											}
										}
									}
									else
									{
										System.out.println("An error occured during receiving list of records!");
									}
									break;
								}
								case "--by-surname":
								{
									if ( this.get_client().getSpecifiedList(args[1], args[2], outputList) )
									{
										if ( outputList.isEmpty() )
										{
											System.out.println("No records found!");
										}
										else
										{
											for ( String line : outputList )
											{
												System.out.print(line);
											}
										}
									}
									else
									{
										System.out.println("An error occured during receiving list of records!");
									}
									break;

								}
								default:
								{
									System.out.println("Wrong using of get command!");
									break;
								}
								}
							}
							else
							{
								System.out.println("Wrong number of arguments!");
							}
						}
					}
					else
					{
						System.out.println("Wrong number of arguments!");
					}
					break;
				}
				case "remove":
				{
					if ( 2 == args.length )
					{
						if (this.get_client().removeEntry( Integer.decode(args[1]) ) )
						{
							System.out.println("Removed successfully!");
						}
						else
						{
							System.out.println("An error occured during removing record!");
						}
					}
					else
					{
						System.out.println("Wrong number of arguments!");
					}
					break;
				}
				case "clear":
				{
					if ( this.get_client().clearAddressBook() )
					{
						System.out.println("Address Book cleared successfully!");
					}
					else
					{
						System.out.println("Error during clearing Address Book!");
					}

					String action = scan.next();

					break;
				}
				case "edit":
				{
					argsList.clear();
					List<String> outputList = new ArrayList<String>();
					
					if ( 2 == args.length )
					{
						if ( this.get_client().getSpecifiedList("--by-id", args[1], outputList) )
						{
							if ( outputList.isEmpty() )
							{
								System.out.println("No such record!");
							}
							else
							{
								argsList.add(args[1]);
								for ( String arg : utils.listOfProperties )
								{
									if ( !arg.equals( utils.listOfProperties.get(0) ) )
									{
										System.out.println("Enter " + arg);
										argsList.add( scan.next() );	
									}
								}
								if ( this.get_client().editEntry(argsList))
								{
									System.out.println("Record edited successfully!");
								}
								else
								{
									System.out.println("Error during editing!");
								}
							}
						}
					}
					else
					{
						System.out.println("Wrong number of arguments!");
					}
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
