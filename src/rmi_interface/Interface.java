/**
 * 
 */
package rmi_interface;

/**
 * @author Arek
 *
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import message.Message;

public interface Interface extends Remote
{
	Boolean addRecord( Message msg ) throws RemoteException;
	Boolean removeRecord( Message msg ) throws RemoteException;
	Boolean editRecord( Message msg ) throws RemoteException;;
	
	Boolean clearList() throws RemoteException;
	Boolean getFullList( Message msg) throws RemoteException;
	
	Boolean searchRecordById( Message msg ) throws RemoteException;
	Boolean searchRecordByName( Message msg ) throws RemoteException;
	Boolean searchRecordBySurname( Message msg ) throws RemoteException;
	
	Message getResponse() throws RemoteException;
}
