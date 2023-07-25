package SchiffeVersenken;

/* Client an das lokale Netzwerk anbinden */

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Client {
    private static HashMap<String, String> test = new HashMap<String, String>();
    //private DataModel datamodel = new DataModel();
    private Socket clientSocket = null;
    //Send Server Data
    private ObjectOutputStream output = null;
    //Get Server Data
    private ObjectInputStream input = null;

    public Client() {
        Client.test.put("Test", "Hello Server");   //Initialize Test Object
    }

    //Write Server Data -> ungetested
    //Server sendet das neue Feld zurück muss vom Spieler an Datamodel gesendet werden
    public char[][] writeServerData(HashMap<Integer, Object> data) {
        HashMap<Integer, char[][]> serverData = null;
        try {
            //Connect Client to Server
            this.clientSocket = new Socket("127.0.0.1", 7777);
            System.out.println("[CLIENT] Connected to Localhost:7777..");

            //Send Packet to Server
            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.output.writeObject(data);

            //data returned from the server
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            serverData =(HashMap<Integer, char[][]>) this.input.readObject();    //ggf deserialzing von Object zu char[][]

            System.out.println(serverData.toString());
            System.out.println("[CLIENT] Connection closed...");
            this.clientSocket.close();

        } catch(IOException ex) {
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return serverData; // später return server data
    }

    //Test Methode
    public String writeServerDataTest(HashMap<String, String> data) {
        String serverData = null;
        try {
            //Connect Client to Server
            this.clientSocket = new Socket("127.0.0.1",7777);
            System.out.println("[CLIENT] Connected..");

            this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
            //this.output.writeObject(this.datamodel.getPlaygroundMatrix());
            //test Object
            this.output.writeObject(Client.test);

            //data returend from the server
            this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            serverData = this.input.readObject().toString();
            System.out.println(serverData);
            System.out.println("[CLIENT] Connection closed...");
            this.clientSocket.close();

        } catch(IOException ex) {
            System.out.println("[I/O Error] " + ex.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null; // später return server data
    }

    public static void main(String[] args) {
        //initializiere den client
        Client client = new Client();
        client.writeServerDataTest(Client.test);
    }
}