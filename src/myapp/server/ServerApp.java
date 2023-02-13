package myapp.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServerApp {

    public static void main(String[] args) {
        
        try {
            String portNumber = args[0];
            ServerSocket server = new ServerSocket(Integer.parseInt(portNumber));
            System.out.printf("Server started and listening on port %s... \n", portNumber);
            Socket socket = server.accept();

            InputStream is = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            
            String messageFromClient = "";
            int computedResult;

            while (!messageFromClient.equalsIgnoreCase("quit")) {

                messageFromClient = dis.readUTF();
                computedResult = compute(messageFromClient);
                dos.writeUTF(String.valueOf(computedResult));
                dos.flush();

            }

            bos.flush();
            os.flush();
            dos.close();
            bos.close();
            os.close();
            dis.close();
            bis.close();
            is.close();
            server.close();

        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println("Please provide port number in args:");
            System.out.println("usage: java ... myapp.server.ServerApp <portNumber>");
            System.exit(0);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static int compute(String readUTF) {
        
        int result;
        String[] strArray = readUTF.split(" ");

        String operation = strArray[0];
        List<Integer> intList = new ArrayList<>();

        for (int i = 1; i < strArray.length; i++) {
            intList.add(Integer.parseInt(strArray[i].trim().toLowerCase()));
        }

        switch (operation) {
            case "add":
                result = intList.stream()
                        .reduce(0, (a,b) -> {
                        return a+b; } );
                // this should also work
                // result = intList.stream().mapToInt(Integer::valueOf).sum();
                break;
            case "max":
                intList.sort(Comparator.naturalOrder());
                result = intList.get(intList.size() - 1);
                // this should also work
                // result = Collections.max(intList);
                break;
            case "min":
                intList.sort(Comparator.naturalOrder());
                result = intList.get(0);
                // this should also work
                // result = Collections.min(intList);
                break;
            case "product":
                result = intList.stream()
                        .reduce(1, (a,b) -> {
                        return a*b; } );
                break;
            case "quit":
                System.out.println("Client has exited");
                result = -1;
                break;
            default:
                System.out.println("Illegal Operation Attempted");
                result = -2;
                break;
        }
        
        return result;
    }

}