package myapp.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientApp {
    
    public static void main(String[] args) {
        
        try {
            String[] strArray = args[0].split(":");
            Socket socket = new Socket(strArray[0], Integer.parseInt(strArray[1]));

            InputStream is = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            
            Console console = System.console();
            String messageToServer = "";
            String messageFromServer = "";

            while (!messageToServer.equalsIgnoreCase("quit")) {
                printMenu();
                messageToServer = console.readLine();
                dos.writeUTF(messageToServer);
                dos.flush();
                messageFromServer = dis.readUTF();
                if (messageFromServer.equalsIgnoreCase("-1")) {
                    System.out.println("Exiting Program... Thank you and Goodbye!");
                } else if (messageFromServer.equalsIgnoreCase("-2")) {
                    System.out.println("Illegal Operation Attempted. Please read the menu.");
                } else {
                    System.out.printf("\n\n>>> Answer = %s\n\n", messageFromServer);
                }
            }

            bos.flush();
            os.flush();
            dos.close();
            bos.close();
            os.close();
            dis.close();
            bis.close();
            is.close();
            socket.close();

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide host name and port number in args:");
            System.out.println("usage: java ... myapp.client.ClientApp <hostName:portNumber>");
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printMenu() {
        System.out.println("""

            Available commands
            ------------------
            quit: exit the program
            add: get the sum of all your integers
            max: get the maximum of all your integers
            min: get the minimum of all your integers
            product: get the product of all your integers

            Examples
            --------
            add 1 2 3
            max 4 5 6

            Enter a command, followed by space, followed by any number of integers separated by space: """);
    }
}
