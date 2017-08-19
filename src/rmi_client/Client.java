/**
 * @author Arkadiusz Wadowski
 * @ Software Developer
 * @ Github: https://github.com/arkwad
 * @ Contact: wadowski.arkadiusz@gmail.com
 */
package rmi_client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import message.BookRecord;
import message.Message;
import rmi_interface.Interface;
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

	public Boolean addEntry(List< String > values) throws RemoteException
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
	public Boolean removeEntry(Integer idx ) throws RemoteException
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

	public Boolean getSpecifiedList( String opt, String value, List<String> listOut ) throws RemoteException
	{
		Message msg = new Message();
		try
		{
			if ( opt.equals("--by-name"))
			{
				msg.setNameProp(value);
				if ( ifc.searchRecordByName(msg))
				{
					Message respMsg = ifc.getResponse();
					for ( BookRecord br : respMsg.getListOfRecords() )
					{
						listOut.add(utils.convertStructToStringV2(br));
					}
					return true;
				}
				return false;
			}
			else if ( opt.equals("--by-surname") )
			{
				msg.setSurnameProp(value);
				if ( ifc.searchRecordBySurname(msg))
				{
					Message respMsg = ifc.getResponse();
					for ( BookRecord br : respMsg.getListOfRecords() )
					{
						listOut.add(utils.convertStructToStringV2(br));
					}
					return true;
				}
				return false;
			}
			else if ( opt.equals("--by-id") )
			{
				msg.setRecordId(Integer.decode( value ));
				if ( ifc.searchRecordById(msg))
				{
					Message respMsg = ifc.getResponse();
					if ( null == respMsg.getBookRecord() )
					{
						listOut.clear();
						return true;
					}
					listOut.add(utils.convertStructToStringV2(respMsg.getBookRecord()));
					return true;
				}
				return false;
			}
			else if ( opt.equals("--all") ) 
			{
				if ( ifc.getFullList(msg))
				{
					Message respMsg = ifc.getResponse();
					for ( BookRecord br : respMsg.getListOfRecords() )
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

	public Boolean editEntry( List< String > values ) throws RemoteException
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