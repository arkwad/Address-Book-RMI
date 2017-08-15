/**
 * 
 */
package rmi_client;
/**
 * @author Arek
 *
 */

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rmi_interface.Interface;

public final class Client 
{
	Registry registry;
	Interface server;
	
	/**
	 * @brief Constructor
	 *
	 */
	public Client()  throws RemoteException, Exception, IOException
	{
		this.registry = LocateRegistry.getRegistry("127.0.0.1");
		this.server = (Interface)this.registry.lookup("Server");
        System.out.println("Client succefully found server!");
	}
	
	public Boolean clearAddressBook()
	{
		try 
		{
			return server.clearList();
		} 
		catch (RemoteException e) 
		{

			e.printStackTrace();
			return false;
		}
	}
}