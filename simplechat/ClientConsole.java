// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
	  System.out.println("Please enter your user ID");
		BufferedReader fromConsole = 
		        new BufferedReader(new InputStreamReader(System.in));
		String loginID = "";
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
		
	  
    try 
    {
      client = new ChatClient(loginID, host, port, this);
    }
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
    	System.out.println("Past "+ex.toString());
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  
  //Lets put the log in info into this section
  public static void main(String[] args) 
  {

	    System.out.println("Please specify the port on which you wish to connect (returning blank will connect to default 5555): "); //E49b) text to guide the user
	    BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
		
		//49b) This will see if the string entered has a value and translates the
		boolean isPort = false;
		
		int port = DEFAULT_PORT;
		
		//79b) This while loop will make sure that the input is a valid port number
		while(!isPort){
			try{
				String portString = bfr.readLine();//E49b) Read from the user the content
				if(!portString.isEmpty()){
					//going to try to read from the buffer
					try{
						if(Integer.parseInt(portString)<65535 &&Integer.parseInt(portString)>0){
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
		  
    String host = "";
    
	try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(host, port);
    
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
