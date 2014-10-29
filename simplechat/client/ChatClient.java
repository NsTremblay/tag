// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  /**
   * Question E51
   * 
   */
  String loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) throws IOException
  {
         super(host,port);
         this.loginID = loginID;
         this.clientUI = clientUI;
         try {
        	 openConnection();
        	 sendToServer("#login "+loginID);
         }catch (IOException e) {
        	 System.out.println("Cannot open connection…");
         }
  }

  /*
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {

    super(host, port); //Call the superclass constructor
    
    System.out.println("Please enter your user ID");
	BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	try{
	String message = fromConsole.readLine();
	loginID = message;
	if(message.isEmpty()){
		System.exit(0);
	}
	}catch(Exception e){
		System.out.println("Error occured when trying to read input");
		System.exit(0);
	}
    
    
    
    System.out.println("Please specify the port on which you wish to connect (returning blank will connect to default 5555): "); //E49b) text to guide the user
    BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
	
	//49b) This will see if the string entered has a value and translates the
	boolean isPort = false;
	
	//79b) This while loop will make sure that the input is a valid port number
	while(!isPort){
		try{
			String portString = bfr.readLine();//E49b) Read from the user the content
			if(!portString.isEmpty()){
				//going to try to read from the buffer
				try{
					if(Integer.parseInt(portString)<650000&&Integer.parseInt(portString)>0){
						port = Integer.parseInt(portString);
						isPort = true;
					}
				}catch(NumberFormatException nfe){
					System.out.println("Please enter a number between 1 and 65000");
				}
			}else{
				break;
			}
		}
		catch(IOException ioe){
			System.out.println("There was an exception when trying to enter the port, it will default to 5555");
			port = 5555;
			isPort = true;
			break;
		}
	}
	
	System.out.println("Trying to connect to host " + host + " on port "+port);
	
    this.clientUI = clientUI;
    try{
    	openConnection();
    	sendToServer("#login <"+loginID+">");
    }catch(IOException ioe){
    	System.out.println("There was a problem connection to the server");
    }
    System.out.println("Connection to the server was successful");
  }
**/
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }


  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI. 
   * Further parameters will be for question E50 a)
   * @param #quit will test if the user wants to quit and closes the client
   * @param #logoff if the user types this. Will disconnect from the server but will not quit
   * @param #sethost <host> if the user types in this, the method set host will be called, if the user 
   * doesn't log off an error message will appear
   * @param #setport <port> will call the set port method with the same conditions as in sethost
   * @param #login only allowed if client is not already connected
   * @param #getHost the host
   * @param #getport returns the port
   */
  //E50 a) change the messages read to execute commands on the client side
  //1)#quit
  public void handleMessageFromClientUI(String message)
  {
	String quit = "#quit";
	String logoff = "#logoff";
	String sethost = "#sethost";
	String setport = "#setport";
	String login = "#login";
	String getHosts = "#getHost";
	String getPorts = "#getport";
	
	if(message.equals(quit)){
		quit();
		return;
	}else if(message.equals(logoff)){
		uiLogOff();
		return;
	}else if(message.equals(login)){
		uiLogIn();
		return;
	}else if(message.equals(getHosts)){
		System.out.println(getHost());
		return;
	}else if(message.equals(getPorts)){
		System.out.println(getPort());
		return;
	}
	//to set something, there is a space and the first 2 words should be used
	String [] characteristics = message.split(" ");
	
	if(characteristics[0].equals(sethost)){
		uiSetHost(characteristics[1]);
		return;
	}else if(characteristics[0].equals(setport)){
		uiSetPort(characteristics[1]);
		return;
	}
	
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
    	connectionClosed();
    	clientUI.display("Could not send message to server.  Terminating client.");
    }
  }
  
  /**
   * This method will try to change the host
   * @param new host
   */
  private void uiSetHost(String newHost){
	  newHost = newHost.replace("<", "");
	  newHost = newHost.replace(">", "");
	  
	  try{
		  if(isConnected()){
			  System.out.println("To change the host, please try doing so when disconnected from the server");
		  }else{
			  setHost(newHost);
			  System.out.println("The host is now "+newHost);
		  }
	  }catch(Exception e){
		  System.out.println("There was a general exception when changing the port");
	  }
	  return;
  }
  
  /**
   * This method will try to change the port
   */
  private void uiSetPort(String newPort){
	  newPort = newPort.replace("<", "");
	  newPort = newPort.replace(">", "");
	  try{
		  int newPortInteger = Integer.parseInt(newPort);
		  if(isConnected()){
			  System.out.println("To change the port, please try doing so when disconnected from the server");
		  }else{
			  setPort(newPortInteger);
			  System.out.println("The port is now "+newPortInteger);
		  }
	  }catch(NumberFormatException nfe){
		  System.out.println("There was an error setting the new port please enter an integer between 1 and 65000");
	  }catch(Exception e){
		  System.out.println("There was a general exception when changing the port");
	  }
	  return;
  }
  
  /**
   * This method will logoffTheserver
   */
  
  private void uiLogOff(){
	  try{
			closeConnection();
		}catch(IOException ioex){
			System.out.println("There was an exception when tryin to disconnect form the server");	
		}catch(Exception e){
			System.out.println("Disconnected");
		}
	  connectionClosed();
	  return;
  }
  
  /**
   * This method will try to log in the server
   */
  
  private void uiLogIn(){
	  try{
			if(!isConnected()){
				openConnection();
				System.out.println("Connected to server successfully");
			}else{
				System.out.println("Please close connection to server before login in");
			}
		
		}catch(IOException ioe){
			System.out.println("There was an error logining in to the server");
		}
		return;
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {
    	//e50a)quit method
    	System.out.println("There was an issue closing the connection");
    	return;
    }
    System.exit(0);
  }
  
  	//E49a) Overridden methods***********************************************
	protected void connectionException(Exception exception){
		System.out.print("The server stopped working");
	}
	//E49a) added a method to see when the connection is closed
	protected void connectionClosed(){
		if(!isConnected()){
			System.out.println("The connection to the server was closed");
		}
	}
}
	
//End of ChatClient class
