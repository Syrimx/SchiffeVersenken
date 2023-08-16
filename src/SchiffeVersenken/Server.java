package SchiffeVersenken;

/* Server dem lokalen Netzwerk zugänglich machen ? */

//import SchiffeVersenken.Controller; //Controller Objekt einbinden ?
import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Server {
    public static void main(String[] args) {
    	DataModel datamodel = new DataModel();
        Controller controller = new Controller(datamodel);
        HashMap<String, String> test = new HashMap<String, String>();
        test.put("Test", "Hello Client");   //Initialize Test Object

        //initializiert den server
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            Socket socket = null;
            System.out.println("[SERVER] startet auf Localhost:7777...");
            //Get Data from Server
            while(true) {
                //Abbruchbedingung erfüllt
                if(Controller.gamePhase == 2) {
                    serverSocket.close();
                    socket.close();
                    System.out.println("[SERVER] Shutdown Server...");
                    break;
                }
                socket = serverSocket.accept();
                System.out.println("[SERVER] " + socket.getInetAddress() + " has connected to Port 7777...");

                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                //read client message
                System.out.println(input.readObject().toString());
                /*respond object -> something done with the controller */
                
                /*Test Response Message*/
                output.writeObject(test);
            }
        } catch(IOException ex){
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
