package SchiffeVersenken;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MenuWindow extends JFrame
{
    private GameWindow gameWindow;
    private DataModel data;
    private JButton playButtonBot;

    //DataModel d 
    public MenuWindow(){
        //
        //this.data = d;
		this.setLayout(null);
		this.getContentPane().setBackground(Color.lightGray);
		this.setLocation(400, 400);
		this.setSize(400, 300);

        playButtonBot = new JButton("Spiel gegen Bot");
        playButtonBot.setBounds(100, 5, 110, 30);
        this.add(playButtonBot);
        playButtonBot.addActionListener(e-> openGameWindow());
		setVisible(true);
    }

    private void openGameWindow(){
        this.dispose(); //Schliesse das Menu Fenster
        gameWindow = new GameWindow(this);
    }

    public void backToMenu(){
        gameWindow.dispose(); //Schliesse das Spiel Fenster
        setVisible(true); //Zeige das Menu Fenster 
    }
}
