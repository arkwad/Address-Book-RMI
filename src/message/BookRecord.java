/**
 * @author Arkadiusz Wadowski
 * @ Software Developer
 * @ Github: https://github.com/arkwad
 * @ Contact: wadowski.arkadiusz@gmail.com
 */
package message;

import java.io.Serializable;


/** @brief Class that specifies format of an Address Book entry.
 * 			This class is used as a part of Message object. 
 */
public class BookRecord implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public String surname;
	public String age;
	public String street;
	public String buildingNumber;
	public String flatNumber;
	public String city;
	public String postCode;
	public String phoneNumber;

	/**
	 * @brief Constructor
	 *
	 */
	public BookRecord() 
	{
		this.resetValues();
	}

	public void resetValues ()
	{
		this.id = "";
		this.name = "";
		this.surname = "";
		this.age = "";
		this.street = "";
		this.buildingNumber = "";
		this.flatNumber = "";
		this.city = "";
		this.postCode = "";
		this.phoneNumber = "";
	}
}
