package SchiffeVersenken;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Client {
    private HashMap<String, String> test = new HashMap<String, String>();
    private DataModel datamodel = new DataModel();

    private Socket clientSocket = null;
    //Send Server Data
    private ObjectOutputStream output = null;
    //Get Server Data
    private ObjectInputStream input = null;

    public Client() {
        this.test.put("Test", "Hello Server");   //Initialize Test Object
        try {
            this.clientSocket = new Socket("127.0.0.1",7777);
            System.out.println("[CLIENT] Connected..");
        } catch(IOException ex){
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Write Server Data -> ungetested
    //input data muss im vorfeld umgewandelt werden
    public String writeServerData() {
        String serverData = null;
        try {
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            //this.output.writeObject(this.datamodel.getPlaygroundMatrix());
            //test Object
            this.output.writeObject(this.test);

            //data returend from the server
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            System.out.println(this.input.readObject().toString());
            serverData = this.input.readObject().toString();

        } catch(IOException ex) {
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return serverData;
    }

    public static void run() {
        //initializiere den client
        Client client = new Client();
        while(true) {
            client.writeServerData();
        }
    }
}