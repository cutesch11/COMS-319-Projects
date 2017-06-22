package Lab01;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
	
	
	public static void main(String[] args) throws FileNotFoundException {
		ServerSocket serverSocket = null;
		int clientNum = 0; // keeps track of how many clients were created
		ArrayList<Socket> sockets = new ArrayList<Socket>();

		// 1. CREATE A NEW SERVERSOCKET
		try 
		{
			serverSocket = new ServerSocket(1337); // provide MYSERVICE at port 1337
			System.out.println(serverSocket);
			System.out.println();
		} 
		
		catch (IOException e) 
		{
			System.out.println("Could not listen on port: 1337");
			System.exit(-1);
		}

		// 2. LOOP FOREVER - SERVER IS ALWAYS WAITING TO PROVIDE SERVICE!
		while (true) {
			Socket clientSocket = null;
			try 
			{
				Scanner clientIn;

				// 2.1 WAIT FOR CLIENT TO TRY TO CONNECT TO SERVER
				System.out.println("Waiting for client " + (clientNum + 1)
						+ " to connect!");
				clientSocket = serverSocket.accept();

				sockets.add(clientSocket);
				
				// 2.2 SPAWN A THREAD TO HANDLE CLIENT REQUEST
				System.out.println("Server got connected to Client "
						+ ++clientNum);
				System.out.println();
				
				Thread t = new Thread(new ClientHandler(clientSocket, clientNum, sockets));
				t.run();
			} 
			catch (IOException e) 
			{
				System.out.println("Accept failed: 1337");
				System.exit(-1);
			}

			// 2.3 GO BACK TO WAITING FOR OTHER CLIENTS
			// (While the thread that was created handles the connected client's
			// request)

		} // end while loop
		
	} // end of main method

}

class ClientHandler implements Runnable {
	Socket s; // this is socket on the server side that connects to the CLIENT
	int num; // keeps track of its number just for identifying purposes
	ArrayList<Socket> sockets;
	
	ClientHandler(Socket s, int n, ArrayList<Socket> sock) {
		this.s = s;
		num = n;
		this.sockets = sock;
	}

	// This is the client handling code
	@Override
	public void run() {
		//printSocketInfo(s); // just print some information at the server side about the connection
		Scanner clientIn;
		
		try {
			// 1. USE THE SOCKET TO READ WHAT THE CLIENT ISb SENDING
			clientIn = new Scanner(new BufferedInputStream(s.getInputStream())); 
			String clientName = clientIn.nextLine();
			
			// 2. DETERMINE WHAT ACTION TO TAKE DEPENDING ON WHO CLIENT IS
			if(clientName.equals("admin"))
			{
				//GET ACTION CHOICE
				int adminChoice = clientIn.nextInt();
				clientIn.nextLine();

				//IF CHOICE IS 1 BROADCAST MESSAGE TO ALL CLIENTS
				if(adminChoice == 1)
				{
					String message = clientIn.nextLine();
					
					for(Socket s: this.sockets)
					{
						PrintWriter out = new PrintWriter(s.getOutputStream(), true);
						out.println(message);
					}
				}
				else if(adminChoice == 2)
				{
					
				}
				else if(adminChoice == 3)
				{
					
				}
			}
			else
			{
				// GET ACTION CHOICE
				int choice = clientIn.nextInt();
				clientIn.nextLine();
				
				// IF CHOICE IS 1, STORE TEXT MESSAGE IN CHAT.TXT
				if(choice == 1)
				{
					String clientMessage = clientIn.nextLine();
					System.out.println("Message from Client " + num + ": "  + clientMessage);
					System.out.println();
					
					// 3. DECRYPT AND SAVE WHAT CLIENT SENT IN CHAT.TXT
					clientMessage = encryptMessage(clientMessage);
					
					FileWriter fw = new FileWriter("chat.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter write = new PrintWriter(bw);
					
					write.println(clientMessage);
					write.close();
				}
				else if(choice == 2)
				{
					
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// This handling code dies after doing all the printing
	} // end of method run()

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
	
} // end of Client Handler
