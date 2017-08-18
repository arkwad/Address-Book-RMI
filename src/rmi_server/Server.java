/**
 * 
 */
package rmi_server;

/**
 * @author Arek
 *
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import rmi_interface.Interface;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import message.BookRecord;
import message.Message;
import utils.Utils;



/**
 * \brief Class that covers AdsressBooks's server functionality.
 * 		  This class implements interface provided in rmi_interface.Interface.java
 */
public class Server extends UnicastRemoteObject implements Interface
{


	/* register that makes as allow to use remote method calling */
	Registry registry;

	String fileName = "AddressBook.txt";
	String currentPath = Paths.get(".").toAbsolutePath().toString();
	Path filePath;
	File addressBook;
	FileWriter fileWritter;
	FileReader fileReader;
	Utils utils;
	/* response msg */
	Message respMsg;

	/* auto-generated stuff wtf?*/
	private static final long serialVersionUID = 1L;

	/**
	 * @brief Constructor
	 *
	 */
	public Server() throws IOException 
	{
		super();
		this.registry = LocateRegistry.createRegistry(1099);
		this.registry.rebind("Server", this);
		this.utils = new Utils();
		this.respMsg = new Message();

		/* build absolute path to file we will work with */
		this.currentPath = this.currentPath.substring(0, this.currentPath.length()-2);
		this.filePath = Paths.get(this.currentPath, this.fileName);

		this.addressBook = new File(this.filePath.toString());

		if ( !this.checkIfFileExist() )
		{
			System.out.println("File doesn't exist!");
			this.addressBook.createNewFile();
			this.fileWritter = new FileWriter(this.addressBook);
			System.out.println("File created at: " + this.filePath.toString());
		}
		else
		{
			this.fileWritter = new FileWriter(this.addressBook, true);
		}
		System.out.println("Server is ready!");
	}

	public static void main(String[] args) throws Exception, IOException
	{
		Server srv = new Server();
		Message msg = new Message();
		BookRecord br = new BookRecord();
		msg.setBookRecord(br);
		srv.addRecord(msg);
		srv.addRecord(msg);
		srv.addRecord(msg);
		msg.setSurnameProp("Wadowski");
		srv.searchRecordBySurname(msg);
	}
	/**
	 * \brief Check if file with data exist in working directory.
	 *
	 * @param[in] void
	 *
	 * @retval true - file exist
	 * @retval false - file doesn't exist
	 */
	private Boolean checkIfFileExist()
	{
		return Files.exists( this.filePath );
	}

	@Override
	public Boolean clearList() throws RemoteException 
	{
		try 
		{
			Files.write(filePath, "".getBytes());
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean addRecord(Message msg) throws RemoteException 
	{
		BookRecord record = msg.getBookRecord();

		String rawStr = this.utils.convertStructToString(record);
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);
			if ( lines.isEmpty() )
			{
				String str = "Id: 1," + rawStr;
				Files.write(filePath, str.getBytes(), StandardOpenOption.APPEND);
				return true;
			}
			else
			{
				String lastRecord = lines.get(lines.size() - 1);
				Integer maxIndex = Integer.decode(lastRecord.substring(4, lastRecord.indexOf(",",0)));
				maxIndex++;
				String toWrite = "Id: " + maxIndex.toString()+"," + rawStr;
				Files.write(filePath, toWrite.getBytes(), StandardOpenOption.APPEND);
				return true;
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean removeRecord(Message msg) throws RemoteException 
	{
		Integer indexToRemove = msg.getRecordId();
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);
			List<String> linesToRemove = new ArrayList<String>();
			if (indexToRemove <= lines.size())
			{
				Integer idx = 0;
				for (String a : lines) 
				{
					if (idx == indexToRemove - 1)
					{
						linesToRemove.add(a);
						break;
					} 
					idx++;
				}
			}
			else
			{
				return false;
			}
			lines.removeAll(linesToRemove);
			Files.write(this.filePath, lines);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Boolean editRecord(Message msg) throws RemoteException 
	{
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);
			if ( (msg.getRecordId() >= 1)&&(msg.getRecordId() <= lines.size()))
			{
				String line = this.utils.convertStructToString( msg.getBookRecord() );
				String str = "Id: " + msg.getRecordId().toString() + "," + line;
				lines.set(msg.getRecordId() - 1, str);
				Files.write(filePath, lines);
				return true;
			}
			else
			{
				return false;
			}	
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean getFullList(Message msg) throws RemoteException 
	{
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);

			List< BookRecord > tmpList = new ArrayList<BookRecord>();

			for( String line : lines )
			{
				if ( !tmpList.add( this.utils.convertStringToStruct(line) ))
				{
					msg = null;
					return false;
				}
			}
			this.respMsg.setListOfRecords(tmpList);
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean searchRecordById(Message msg) throws RemoteException 
	{
		Integer indexToSearch = msg.getRecordId();
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);
			indexToSearch--;
			this.respMsg.setBookRecord(this.utils.convertStringToStruct(lines.get(indexToSearch)));
			return true;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean searchRecordByName(Message msg) throws RemoteException 
	{
		List< BookRecord > listOfFoundRecords = new ArrayList<BookRecord>(); 
		BookRecord tempRecord = new BookRecord();
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);

			for (String line : lines )
			{
				tempRecord = this.utils.convertStringToStruct(line);

				if ( 0 == msg.getNameProp().compareTo(tempRecord.name) ) 
				{
					listOfFoundRecords.add(tempRecord);
				}
			}
			if ( !listOfFoundRecords.isEmpty() )
			{
				this.respMsg.setListOfRecords(listOfFoundRecords);
				return true;
			}
			return false;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean searchRecordBySurname(Message msg) throws RemoteException 
	{
		List< BookRecord > listOfFoundRecords = new ArrayList<BookRecord>(); 
		BookRecord tempRecord = new BookRecord();
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);

			for (String line : lines )
			{
				tempRecord = this.utils.convertStringToStruct(line);

				if ( 0 == msg.getSurnameProp().compareTo(tempRecord.surname) ) 
				{
					listOfFoundRecords.add(tempRecord);
				}
			}
			if ( !listOfFoundRecords.isEmpty() )
			{
				this.respMsg.setListOfRecords(listOfFoundRecords);
				return true;
			}
			return false;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Message getResponse() throws RemoteException 
	{
		return this.respMsg;
	}
}