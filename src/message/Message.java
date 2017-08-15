package message;
import java.util.List;

/**
 * @author Arek
 *
 */
import message.BookRecord;

public class Message 
{
	private BookRecord bookRecord;
	private Integer recordId;
	private List< BookRecord > listOfRecords;
	
	/**
	 * @brief Constructor
	 *
	 */
	public Message() 
	{
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
}
