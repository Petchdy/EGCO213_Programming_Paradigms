//Kulwisit Sakkittiphokhin 6613114
//Thammanat Lerdwijitjarud 6613253
//Pornnubpun Rujicheep 6613257
//Pimlapas   Narasawad 6613264

package project3_6613114;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class LoginFrame extends JFrame 
{
    // components
    private JPanel            contentpane;
    private JLabel            drawpane, nameLabel, passLabel;
    private JTextField        scoreText, nameText;
    private JPasswordField    passwordText;
    private MyImageIcon       backgroundImg;    
    private MainApplication   backtomain;
    private RegisFrame        regisframe;
    private Start             startgame;

    private int framewidth  = MyConstants.FRAMEWIDTH;
    private int frameheight = MyConstants.FRAMEHEIGHT;
    private int score;
    private String name;
    private char[] password;

    //--------------------------------------------------------------------------
    public LoginFrame()
    {   
        setTitle("This is a Login frame");
	setSize(framewidth, frameheight); 
        setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        
	contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );        
        AddComponents();
    } 
    
    //--------------------------------------------------------------------------
    public void AddComponents()
    {        
	backgroundImg  = new MyImageIcon(MyConstants.FILE_1BG).resize(framewidth, frameheight);
	drawpane = new JLabel();
	drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);
        
        Font ifont = new Font("Monospaced", Font.BOLD, 30);
        Font pfont = new Font("Comic Sans MS", Font.PLAIN, 30);
        
	nameLabel = new JLabel("Username:");
        nameLabel.setFont(pfont);
        nameLabel.setBounds(100, 100, 200, 40);
        nameText = new JTextField(20);
        nameText.setFont(ifont);
        nameText.setBounds(300, 100, 250, 40);
	nameText.setEditable(true);
        
        
        passLabel = new JLabel("Password:");
        passLabel.setFont(pfont);
        passLabel.setBounds(100, 160, 200, 40);
        passwordText = new JPasswordField(20);
        passwordText.setFont(ifont);
        passwordText.setEditable(true);
        passwordText.setBounds(300, 160, 250, 40);
        passwordText.setEchoChar('*');
        
        JButton confirm_login;
        MyImageIcon login_icon = new MyImageIcon(MyConstants.FILE_LOGIN).resize(250, 250);
        confirm_login = new JButton(login_icon);
        
        confirm_login.setContentAreaFilled(false);
        confirm_login.setBorderPainted(false);
        confirm_login.setFocusPainted(false);
        confirm_login.setOpaque(false);

        confirm_login.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //check correctness of username and password
                // if password incorrect --> enter again
                confirmButton();
                
            }
        });

        JPanel control  = new JPanel();
        control.setOpaque(false);
        control.setBounds(700,200,500,500);

	control.add(nameLabel);
        control.add(nameText);
        control.add(passLabel);
        control.add(passwordText);
        control.add(confirm_login);
        contentpane.add(control, BorderLayout.WEST);
        contentpane.add(drawpane, BorderLayout.WEST);     
        validate();       
    }
    
    public void confirmButton() {
        name = nameText.getText();

        // password from JPasswordField is a char array, then convert it to a String
        char[] inputPasswordChars = passwordText.getPassword();
        String inputPassword = new String(inputPasswordChars);  // Convert to String

        try {
            File infile = new File(MyConstants.FILE_INFO);
            Scanner fileScan = new Scanner(infile);

            boolean userFound = false;

            while (fileScan.hasNext()) {
                String line = fileScan.nextLine();
                String[] cols = line.split(",");
                String fileUsername = cols[0];
                String filePassword = cols[1].trim();  

                if (fileUsername.equals(name)){
                    userFound = true;

                    // Compare inputPassword with filePassword -> if equal, go to 
                    if (inputPassword.equals(filePassword)) {
                        MyImageIcon success_icon = new MyImageIcon(MyConstants.ICON_LOGIN).resize(80, 80);
                        QuickDialog.show("Login Successful, Let's play game", "Login successful", JOptionPane.OK_OPTION, success_icon);
                        //open next frame --> start frame
                        
                        Player.getInstance(name, filePassword);
                        
                        if(startgame==null){ //change to ni file
                            System.out.println("Start the game");
                            startgame = new Start();
                            dispose();
                        }
                    } else { //password incorrect
                        MyImageIcon password_icon = new MyImageIcon(MyConstants.ICON_PASSWORD).resize(100, 100);
                        QuickDialog.show("Password is incorrect, Please enter again", "Password incorrect", JOptionPane.ERROR_MESSAGE, password_icon);
                    }
                    break; // Exit loop when the user is found
                }
            }

            if (!userFound) {
                MyImageIcon user_icon = new MyImageIcon(MyConstants.ICON_USER).resize(80, 80);
                QuickDialog.show("Username not found. Please register first", "User not found", JOptionPane.ERROR_MESSAGE, user_icon);
                //open first frame --> login/register frame
                if(backtomain==null){
                    backtomain = new MainApplication();
                    dispose();
                }
            }

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            // Clear the password array for security
            Arrays.fill(inputPasswordChars, ' ');
        }
    }

}   