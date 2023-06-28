package SchiffeVersenken;

/* Interface Model <-> Controller  */

public interface Client {

    /*Nochmal schauen, wie genau Daten hin und her gesendet werden und 
     * ob eine receiveData überhaupt notwendig ist
     * und wenn ja wann genau
     */
    public int[][] connectToServerAndReceive();
    public void sendData();
}