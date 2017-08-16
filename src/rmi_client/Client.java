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
import java.util.List;

import message.BookRecord;
import message.Message;
import rmi_interface.Interface;
import rmi_server.Server;
import sun.awt.SunHints.Value;
import utils.Utils;

public final class Client 
{
	private Registry registry;
	private Interface ifc;
	private Utils utils;

	/**
	 * @brief Constructor
	 *
	 */
	public Client()  throws RemoteException, Exception, IOException
	{
		this.utils = new Utils();
		this.registry = LocateRegistry.getRegistry("127.0.0.1");
		this.ifc = (Interface)this.registry.lookup("Server");
		System.out.println("Client succefully found server!");
	}
	public static void main(String[] args) throws Exception, IOException
	{
		Client cl = new Client();
		cl.clearAddressBook();
	}
	public Boolean clearAddressBook()
	{
		try 
		{
			return ifc.clearList();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	public Boolean addEntry(List< String > values)
	{
		Message msg = new Message();
		BookRecord bookRec = new BookRecord();
		/* some magic numbers ;) */
		if ( 9 == values.size() )
		{
			bookRec.name = values.get(0);
			bookRec.surname = values.get(1);
			bookRec.age = values.get(2);
			bookRec.street = values.get(3);
			bookRec.buildingNumber = values.get(4);
			bookRec.flatNumber = values.get(5);
			bookRec.city = values.get(6);
			bookRec.postCode = values.get(7);
			bookRec.phoneNumber = values.get(8);

			msg.setBookRecord(bookRec);

			try 
			{
				return ifc.addRecord(msg);
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	public Boolean removeEntry(Integer idx )
	{
		Message msg = new Message();
		msg.setRecordId(idx);

		try 
		{
			return ifc.removeRecord(msg);
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	public Boolean getSpecifiedList( String opt, String value, List<String> listOut )
	{
		Message msg = new Message();
		try
		{
			if ( 0 ==  opt.compareToIgnoreCase("--by-name"))
			{
				msg.setNameProp(value);
				if ( ifc.searchRecordByName(msg))
				{
					for ( BookRecord br : msg.getListOfRecords() )
					{
						listOut.add(utils.convertStructToStringV2(br));
					}
					return true;
				}
				return false;
			}
			else if ( 0 ==  opt.compareToIgnoreCase("--by-surname"))
			{
				msg.setNameProp(value);
				if ( ifc.searchRecordBySurname(msg))
				{
					for ( BookRecord br : msg.getListOfRecords() )
					{
						listOut.add(utils.convertStructToStringV2(br));
					}
					return true;
				}
				return false;
			}
			else if ( 0 ==  opt.compareToIgnoreCase("--by-id"))
			{
				msg.setNameProp(value);
				if ( ifc.searchRecordById(msg))
				{
					for ( BookRecord br : msg.getListOfRecords() )
					{
						listOut.add(utils.convertStructToStringV2(br));
					}
					return true;
				}
				return false;
			}
			else if ( 0 ==  opt.compareToIgnoreCase("--all")) 
			{
				msg.setNameProp(value);
				if ( ifc.getFullList(msg))
				{
					for ( BookRecord br : msg.getListOfRecords() )
					{
						listOut.add(utils.convertStructToStringV2(br));
					}
					return true;
				}
				return false;
			}
			else
			{
				System.out.println("Wrong search option!");
				return false;
			}
		}
		catch (RemoteException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean editEntry( List< String > values )
	{
		Message msg = new Message();
		BookRecord bookRec = new BookRecord();
		
		if ( 10 == values.size() )
		{
			msg.setRecordId(Integer.decode(values.get(0)));
			bookRec.name = values.get(1);
			bookRec.surname = values.get(2);
			bookRec.age = values.get(3);
			bookRec.street = values.get(4);
			bookRec.buildingNumber = values.get(5);
			bookRec.flatNumber = values.get(6);
			bookRec.city = values.get(7);
			bookRec.postCode = values.get(8);
			bookRec.phoneNumber = values.get(9);

			msg.setBookRecord(bookRec);

			try 
			{
				return ifc.editRecord(msg);
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
				return false;
			}
		}
		return false;
		
		
	}
}