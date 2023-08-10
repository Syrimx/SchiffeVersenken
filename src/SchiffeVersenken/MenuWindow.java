package SchiffeVersenken;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MenuWindow extends JFrame
{
    private GUI gameWindow;
   // private DataModel data;
    private JButton playButtonBot;
    private JButton playButtonFriend;
    private JButton exitButton;
    public MenuWindow(){
        //
        //this.data = d;
		this.setLayout(null);
		this.setTitle("Menue Schiffe versenken");
		this.getContentPane().setBackground(Color.DARK_GRAY);
		this.setLocation(400, 400);
		this.setSize(500, 400);

        playButtonBot = new JButton("Spiel gegen Bot");
        playButtonBot.setBounds(60, 230, 160, 40);
        this.add(playButtonBot);
        playButtonBot.addActionListener(e-> openGameWindowBot());
        
        playButtonFriend = new JButton("Spiel gegen Freund");
        playButtonFriend.setBounds(280, 230, 160,40);
        this.add(playButtonFriend);
        playButtonFriend.addActionListener(e -> openGameWindowFriend());
        
        exitButton = new JButton("Beenden");
        exitButton.setBounds(175,300,150,40);
        this.add(exitButton);
        exitButton.addActionListener(e ->{System.exit(0);});
        setVisible(true);
    }

    private void openGameWindowBot(){
        this.dispose(); //Schliesse das Menu Fenster
        gameWindow = new GUI(this);
    }
    
    private void openGameWindowFriend() {
    	this.dispose(); //Schliesse das Menu Fenster
        gameWindow = new GUI(this);
        //Verbindung mit Freund
    }

    public void backToMenu(){
        gameWindow.dispose(); //Schliesse das Spiel Fenster
        setVisible(true); //Zeige das Menu Fenster 
    }

    public int[] getCurrentPosition() {
        return this.gameWindow.getCurrentPosition();
    }
    
    public void refreshMaps() {
    	this.gameWindow.refreshMap();
    }
}
