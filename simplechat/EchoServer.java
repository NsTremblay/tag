// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
//package ocsf.simplechat1;
import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message = msg.toString(); 
    //
    
    try
    {
	    // Checks if #login is the first command
	    if ( client.getInfo("FirstCommand") == null)
	    {
	    	if ( message.contains("#login"))
	    	{
	   	 		
	   	 		client.setInfo("LoginID" , message.substring(7));
	   	 		client.setInfo("FirstCommand", msg);
	   	 	    System.out.println(message + " from " + client.getInfo("LoginID") );
	   	 		sendToAllClients( client.getInfo("LoginID") + " has logged on.");
	    	} 
	    	else
	   		{
	    		client.sendToClient("Error: Your first command must be #login");
	    		client.sendToClient("Terminating your connection");
   	 	    	client.close();
	   		} 
	    }
	    else
	    {
	    	if ( message.contains("#login"))
	    	{
	    		client.sendToClient("The #login command can only be used once as your first command");
	    	}
	    	else
	    	{
	    	// standard message, sent with loginID as a prefix
	   			this.sendToAllClients(client.getInfo("LoginID") + " > " + msg);
	   			System.out.println("Message received: '"+ message + "' from "+ client.getInfo("LoginID"));
	   		}
	   		 
	    }
    }
    catch (IOException e)
    {
    	System.out.println("Error handling message from: " + client.getInfo("LoginID"));
    }
    
    //
	if (msg.equals("#quit")) 
  	{
  		System.out.println("Server quit");
  		this.sendToAllClients("Server quit");
  		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
  }
    if (msg.equals("#close")) 
    {
		System.out.println("closing server ... disconnecting all clients");
		this.sendToAllClients("closing server ... disconnecting all clients");
    	stopListening();
    }
    if (msg.equals("#stop")) 
    {
			stopListening();
			serverStopped();
    }
    
    if (message.startsWith("#setport"))
    {
    	if (!isListening()){
    		System.out.println("setting port");
    		String setPort = message.substring(8,message.length());
    		setPort.trim();
    		System.out.println(setPort);
    		int port = Integer.parseInt(setPort);
    		setPort(port);
    		System.out.println(getPort());
    	}
    }
    
    if (msg.equals("#start"))
    {
    	if ( !isListening() )
    	{
	    	try {
				listen();
			} catch (IOException e) {
							
				System.out.println("Error: Unable to restart server!"); 
			}
    	}
    	else {
			  System.out.println("Server already listening for clients.");
		}
    }
    
    if (msg.equals("#getport")){
    	System.out.println("Port: " + getPort());
    }
    
	System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort()); 
  
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on
    
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
