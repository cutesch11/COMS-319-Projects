package Lab01;

import java.io.BufferedOutputStream;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ChatClient {

	public static void main(String[] args) throws UnknownHostException, IOException 
	{	
		// 1. CONNECT CLIENT TO THE SERVER AT PORT 1337 
		Socket socket = new Socket("localhost", 1337);
		Scanner in = new Scanner(System.in);
		System.out.print("Enter your Name: (Type in your name, then press Enter)");
		String clientName = in.nextLine();
		PrintWriter out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
		out.println(clientName);
		out.flush();
		System.out.println("Client - " + clientName + " is now connected to the server!");
		System.out.println();
		System.out.println("Menu:");
		
		// 3. SHOW CLIENT MENU OF CHOICES
		if(clientName.equals("admin"))
		{
			System.out.println("1. Broadcast message to all clients");
			System.out.println("2. List all messages");
			System.out.println("3. Delete a selected message (give a message number)");
			int choice = 0;
			
			while(choice != 1 && choice != 2 && choice != 3)
			{
				try{
					choice = in.nextInt();
					if(choice != 1 && choice != 2 && choice != 3){
						throw new NoSuchElementException();
					}
				}
				catch(NoSuchElementException e){
					System.out.println("Please enter a valid choice");
				}
				
				in.nextLine();
			}
			
			// Send action choice to server
			out.println(choice);
			out.flush();
			
			if(choice == 1)
			{
				System.out.println("Enter message to broadcast to all clients");
				String message = in.nextLine();		
				
				out.println(message);
				out.flush();
			}
			else if(choice == 2)
			{
				
			}
			else if(choice == 3)
			{
				
			}
		}
		else
		{
			System.out.println("1. Send a text message to the server");
			System.out.println("2. Send an image file to the server");
			int choice = 0;
			
			while(choice != 1 && choice != 2)
			{
				try{
					choice = in.nextInt();
					if(choice != 1 && choice != 2){
						throw new NoSuchElementException();
					}
				}
				catch(NoSuchElementException e){
					System.out.println("Please enter a valid choice");
				}
				
				in.nextLine();
			}
			
			// Send action choice to server
			out.println(choice);
			out.flush();
			
			if(choice == 1) //IF CLIENT CHOOSES TO SEND TEXT MESSAGE
			{
				System.out.print("Enter text message to send to the server: ");			
				String message = in.nextLine();
	
				//ENCRYPT MESSAGE BEFORE SENDING IT TO THE SERVER
				message = encryptMessage(message);
	
				//SEND ENCRYPTED MESSAGE TO SERVER
				out.println(message);
				out.flush();
			}
			else if(choice == 2)//CLIENT CHOOSES TO SEND IMAGE
			{
				//TODO
			}
		}	
		
		// client dies here
		in.close();
		while(true)
		{
			Scanner serverIn = new Scanner(socket.getInputStream()); 
			if(serverIn.hasNextLine())
			{
				System.out.println("Message from admin: " + serverIn.nextLine());
			}
		}
	}

	private static String encryptMessage(String s)
	{
		byte[] bytes = s.getBytes();
						
		byte encrypt = (byte)Integer.parseInt("11110000", 2);
		for(int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte)(0xff & (bytes[i]) ^ (encrypt));
		}
		
		String result = new String(bytes);

		return result;
	}
}