/**
 * @author Arkadiusz Wadowski
 * @ Software Developer
 * @ Github: https://github.com/arkwad
 * @ Contact: wadowski.arkadiusz@gmail.com
 */
package rmi_server;

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
	
	/**
	 * @brief Clearing whole Address Book.
	 *
	 * @param[in] - void.
	 *
	 * @retval true - Cleared successfully.
	 * @retval false - Something went wrong during clearing file.
	 * 
	 */
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
	
	/**
	 * @brief Adding new record to Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - Added successfully.
	 * @retval false - Something went wrong during adding record.
	 * 
	 */
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
				String str = "Id: 1," + rawStr + "\r\n";
				Files.write(filePath, str.getBytes(), StandardOpenOption.APPEND);
				return true;
			}
			else
			{
				String lastRecord = lines.get(lines.size() - 1);
				Integer maxIndex = Integer.decode(lastRecord.substring(4, lastRecord.indexOf(",",0)));
				maxIndex++;
				String toWrite = "Id: " + maxIndex.toString()+"," + rawStr + "\r\n";
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

	/**
	 * @brief Removing record with specified ID from Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - Removed successfully.
	 * @retval false - Something went wrong during removing record.
	 * 
	 */
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
			updateIndexesOfRecords(lines);
			Files.write(this.filePath, lines);
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @brief Editing existing record in Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - Edited successfully.
	 * @retval false - Something went wrong during editing record i.e. there is no such file.
	 * 
	 */
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

	/**
	 * @brief Provides list of all record from Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - List returned without problems.
	 * @retval false - Something went wrong during collecting records from file.
	 * 
	 */
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

	/**
	 * @brief Returns record with specified ID from Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - Record returned without problems.
	 * @retval false - Something went wrong during getting record from file.
	 * 
	 */
	@Override
	public Boolean searchRecordById(Message msg) throws RemoteException 
	{
		Integer indexToSearch = msg.getRecordId();
		try 
		{
			List<String> lines = Files.readAllLines(this.filePath);
			
			if (indexToSearch > lines.size())
			{
				this.respMsg.setBookRecord(null);
				return true;
			}
			
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

	/**
	 * @brief Returns all records with specified Name field from Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - Records returned without problems.
	 * @retval false - Something went wrong during getting records from file.
	 * 
	 */
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

	/**
	 * @brief Returns all records with specified Surname field from Address Book.
	 *
	 * @param[in] - Message object.
	 *
	 * @retval true - Records returned without problems.
	 * @retval false - Something went wrong during getting records from file.
	 * 
	 */
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

	/**
	 * @brief Returns Message object which shall contains result of last proceed operation.
	 *
	 * @param[in] - void.
	 *
	 * @retval Message object.
	 * 
	 */
	@Override
	public Message getResponse() throws RemoteException 
	{
		return this.respMsg;
	}
	/**
	 * @brief Internal method that updates ID's of records after remove one from the middle.
	 *
	 * @param[in] - Current list of records with wrong ID's sequence.
	 *
	 * @retval void
	 * 
	 */
	private void updateIndexesOfRecords ( List<String> lines )
	{
		Integer idx = 1;
		for (String line : lines)
		{
			int pos = line.indexOf("Name");
			line = line.substring(pos, line.length());
			line = "Id: " + idx.toString() +","+ line;
			lines.set(idx - 1, line);
			idx ++;
		}
	}
}