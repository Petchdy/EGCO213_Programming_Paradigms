//Kulwisit Sakkittiphokhin 6613114
//Thammanat Lerdwijitjarud 6613253
//Pornnubpun Rujicheep 6613257
//Pimlapas   Narasawad 6613264

package project3_6613114;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Player {
    private static Player player;
    private String name, password;

    // Private constructor to prevent direct instantiation
    private Player(String n, String pwd) {
        name = n;
        password = pwd;
    }

    // Method to initialize the singleton instance
    public static Player getInstance(String name, String pwd) {
        if (player == null) {
            player = new Player(name, pwd);
        }
        return player;
    }

    // Method to get the existing instance
    public static Player getInstance() {
        return player;
    }

    // Getter for name and password
    public String getName() {
        return name;
    }
    public String getPassword(){
        return password;
    }
}

public class MainApplication extends JFrame
{
    private JLabel           contentpane;
    private JButton          login_button, regis_button;
    private int framewidth   = MyConstants.FRAMEWIDTH;
    private int frameheight  = MyConstants.FRAMEHEIGHT;
    private int distanceX    = MyConstants.DISTANCE_X;
    private LoginFrame       loginframe;
    private RegisFrame       regisframe;
    
    public static void main(String[] args) {
       new MainApplication(); 
    }
    
    public MainApplication(){
        setTitle("Guess Who?");
        setSize(framewidth, frameheight); 
        setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        // add resize
        setResizable(true);
        setLayout(new BorderLayout());
        
        //set background image
        JPanel firstPanel = new JPanel(null);
	MyImageIcon background = new MyImageIcon(MyConstants.FILE_BG3).resize(framewidth, frameheight);
        contentpane = new JLabel(background);
	contentpane.setBounds(0, 0, framewidth, frameheight);
        firstPanel.add(contentpane);
        setContentPane(firstPanel);
        
        AddComponents();
        setVisible(true);
    }
    
    public void AddComponents(){
        //login
        MyImageIcon login_icon = new MyImageIcon(MyConstants.FILE_LOGIN).resize(300, 300);
        login_button = new JButton(login_icon);
        login_button.setBounds(350, 500, 300, 300);
        
        login_button.setContentAreaFilled(false);
        login_button.setBorderPainted(false);
        login_button.setFocusPainted(false);
//        login_button.setOpaque(false);
        
        // ***
        login_button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //open next frame
                if(loginframe==null){
                    System.out.println("login frame");
                    loginframe = new LoginFrame();
                    dispose();
                }
                else{
                    System.out.println("frame already exists");
                }
            }
            
        });
        
        //register
        MyImageIcon regis_icon = new MyImageIcon(MyConstants.FILE_REGIS).resize(300, 300);
        regis_button = new JButton(regis_icon);
        regis_button.setBounds(700, 500, 300, 300);
        
        regis_button.setContentAreaFilled(false);
        regis_button.setBorderPainted(false);
        regis_button.setFocusPainted(false);
//        regis_button.setOpaque(false);
        
        regis_button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("regis button");
                //open next frame
                if(regisframe==null){
                    System.out.println("regis frame");
                    regisframe = new RegisFrame();
                    dispose();
                    
                }
                else{
                    System.out.println("frame already exists");
                }
            }
        });
        contentpane.add(login_button);
        contentpane.add(regis_button);
    }
}

