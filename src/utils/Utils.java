/**
 * 
 */
package utils;

import java.util.Arrays;
import java.util.List;

import message.BookRecord;

/**
 * @brief Class that contains methods which are common for whole system  
 * 
 */
public class Utils 
{
	List<String> listOfProperties = Arrays.asList("Name: ",
												  "Surname: ",
												  "Age: ",
												  "Street: ",
												  "Building Number: ",
												  "Flat Number: ",
												  "City: ",
												  "Post Code: ",
												  "Phone Number: ");	
	/**
	 * @brief Constructor
	 */
	public Utils() 
	{
	}
	/**
	 * @brief Conversion from structure to raw String.
	 *
	 * @param[in] - BookRecord record - structure to be converted.
	 *
	 * @retval String - raw data ready to write to file.
	 */
	public String convertStructToString( BookRecord record )
	{
		String temp = "Name: " + record.name + ","
					 +"Surname: " + record.surname + ","
					 +"Age: " + record.age 
					 +"Street: " + record.street + ","
					 +"Building Number: " + record.buildingNumber + ","
					 +"Flat Number: " + record.flatNumber + ","
					 +"City: " + record.city + ","
					 +"Post Code: " + record.postCode + ","
					 +"Phone Number: " + record.phoneNumber + ",.\n";
		
		return temp;
	}
	/**
	 * @brief Conversion from raw string read from file to readable structure.
	 *
	 * @param[in] - String string - raw string from file.
	 *
	 * @retval BookRecord
	 */
	public BookRecord convertStringToStruct( String string )
	{
		BookRecord temp = new BookRecord();
		
		for (String property : listOfProperties)
		{
			int pos = string.indexOf(property) + property.length();
			int end = string.indexOf(",", pos);
			String buff = string.substring(pos, end);
			
			if( 0 == property.compareTo("Name: ") )
			{
				temp.name = buff;
			}
			else if (0 == property.compareTo("Surname: ") )
			{
				temp.surname = buff;
			}
			else if (0 == property.compareTo("Age: ") )
			{
				temp.age = buff;
			}
			else if (0 == property.compareTo("Street: ") )
			{
				temp.street = buff;
			}
			else if (0 == property.compareTo("Building Number: ") )
			{
				temp.buildingNumber = buff;
			}
			else if (0 == property.compareTo("Flat Number") )
			{
				temp.flatNumber = buff;
			}
			else if (0 == property.compareTo("City: ") )
			{
				temp.city = buff;
			}
			else if (0 == property.compareTo("Post Code: ") )
			{
				temp.postCode = buff;
			}
			else if (0 == property.compareTo("Phone Number: ") )
			{
				temp.phoneNumber = buff;
			}	
		}

		return temp;
	}
}
