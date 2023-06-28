package SchiffeVersenken;

public interface Server {
    /*Nochmal schauen, wie genau Daten hin und her gesendet werden und 
     * ob eine receiveData überhaupt notwendig ist
     * und wenn ja wann genau
     */
    
    public int[][] listenAndReceive();
    public void sendData(int[][] data);
}
