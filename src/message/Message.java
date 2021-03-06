/**
 * @author Arkadiusz Wadowski
 * @ Software Developer
 * @ Github: https://github.com/arkwad
 * @ Contact: wadowski.arkadiusz@gmail.com
 */

package message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import message.BookRecord;

public class Message implements Serializable
{
	private static final long serialVersionUID = -8756121687865588372L;

	private String name;
	private String surname;
	private BookRecord bookRecord;
	private Integer recordId;
	private List< BookRecord > listOfRecords;
	
	/**
	 * @brief Constructor
	 *
	 */
	public Message() throws RemoteException 
	{
		this.bookRecord = new BookRecord();
	}
	
	/* set methods */
	public void setBookRecord(BookRecord bookRecord)
	{
		this.bookRecord = bookRecord;
	}
	
	public void setRecordId(Integer recordId)
	{
		this.recordId = recordId;
	}
	
	public void setListOfRecords(List< BookRecord > listOfRecords)
	{
		this.listOfRecords = listOfRecords;
	}
	public void setNameProp( String name)
	{
		this.name = name;
	}
	public void setSurnameProp( String surname)
	{
		this.surname = surname;
	}
	
	/* get methods */
	public BookRecord getBookRecord()
	{
		return this.bookRecord;
	}
	
	public Integer getRecordId()
	{
		return this.recordId;
	}
	
	public List< BookRecord >  getListOfRecords()
	{
		return this.listOfRecords ;
	}
	public String getNameProp()
	{
		return this.name;
	}
	public String getSurnameProp()
	{
		return this.surname;
	}
}
