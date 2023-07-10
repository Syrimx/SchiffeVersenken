package SchiffeVersenken;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Server {
    private HashMap<String, String> test = new HashMap<String, String>();
    private Controller controller = new Controller();


    private ServerSocket serverSocket = null;
    private  Socket socket = null;
    //Get Server Data
    private ObjectInputStream input = null;
    //Send Server Data
    private ObjectOutputStream output = null;

    public Server() {
        this.test.put("Test", "Hello Client");   //Initialize Test Object
        try {
            this.serverSocket = new ServerSocket(7777);
            //Get Data from Server
            this.socket = serverSocket.accept();
        } catch(IOException ex){
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Read Server Data -> ungetested
    public String readClientData() {
        byte[] data = null;
        String dataString = null;
        try {
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            //read client message
            System.out.println(this.input.readObject().toString());
            //respond object -> something done with the controller 
            //Test Respond Message
            this.output.writeObject(this.test);
        } catch(IOException ex) {
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return dataString; //-> returns the datastring
    }

    public static void run() {
        //initializiert den server
        Server server = new Server();
        while(true) {
            String clientData = server.readClientData();
            System.out.println(clientData);
        }
    }
}
