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

class RegisFrame extends JFrame 
{
    // components
    private JPanel            contentpane;
    private JLabel            drawpane, nameLabel, passLabel, confirmLabel;
    private JTextField        scoreText, nameText;
    private JPasswordField    passwordText, confirmText;
    private MyImageIcon       backgroundImg;
    private MyImageIcon       password_icon = new MyImageIcon(MyConstants.ICON_PASSWORD).resize(100, 100);
    private MyImageIcon       success_icon  = new MyImageIcon(MyConstants.ICON_LOGIN).resize(80, 80);
    private MainApplication   backtomain;
    private RegisFrame        regisframe;
    private Start             startgame;
    private PrintWriter write;

    private int framewidth  = MyConstants.FRAMEWIDTH;
    private int frameheight = MyConstants.FRAMEHEIGHT;
    private int score=0;
    private char[] password;
    private String name;

    //--------------------------------------------------------------------------
    public RegisFrame()
    {   
        setTitle("This is a Register frame");
	setSize(framewidth, frameheight); 
        setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        
	contentpane = (JPanel)getContentPane();
	contentpane.setLayout( new BorderLayout() );
        AddComponents();
    }
    
    public void AddComponents()
    {        
	backgroundImg  = new MyImageIcon(MyConstants.FILE_1BG).resize(framewidth, frameheight);
	drawpane = new JLabel();
	drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);
        
        Font ifont = new Font("Monospaced", Font.BOLD, 30);
        Font pfont = new Font("Comic Sans MS", Font.PLAIN, 30);
        
        // JTextField
    	nameLabel = new JLabel("Username:");
        nameLabel.setFont(pfont);
        nameLabel.setBounds(100, 100, 200, 40);
        nameText = new JTextField(20);
        nameText.setFont(ifont);
        nameText.setBounds(300, 100, 250, 40);
	nameText.setEditable(true);
        
        // JPasswordField
        passLabel = new JLabel("Password:");
        passLabel.setFont(pfont);
        passLabel.setBounds(100, 160, 200, 40);
        passwordText = new JPasswordField(20);
        passwordText.setFont(ifont);
        passwordText.setEditable(true);
        passwordText.setBounds(300, 160, 250, 40);
        
        // JPasswordField
        confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(pfont);
        confirmLabel.setBounds(100, 220, 200, 40);
        confirmText = new JPasswordField(20);
        confirmText.setFont(ifont);
        confirmText.setEditable(true);
        confirmText.setBounds(300, 220, 250, 40);
        
        JButton confirm_register;
        MyImageIcon regis_icon = new MyImageIcon(MyConstants.FILE_REGIS).resize(250, 250);
        confirm_register = new JButton(regis_icon);
        
        confirm_register.setContentAreaFilled(false);
        confirm_register.setBorderPainted(false);
        confirm_register.setFocusPainted(false);
        confirm_register.setOpaque(false);
        
        confirm_register.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                name = nameText.getText();
                //check for empthy string
                if(name.length()>0){
                    if(isUniqueName(name)){
                        //check password is equal or not
                        boolean success = confirmButton();
                        if(success==true){
                            //open next frame --> start frame
                            if(startgame==null){
                                startgame = new Start();
                                dispose();
                            }
                            else{
                                System.out.println("frame already exists");
                            }
                        }
                        else{
                            System.out.println("regis frame again");
                            passwordText.setText("");
                            confirmText.setText("");
                        }
                        
                    }
                    else{
                        QuickDialog.show("This username is already exists, Please create the new one", "Username exists", JOptionPane.ERROR_MESSAGE, password_icon);
                        new RegisFrame();
                        dispose();
                    }
                }
                else{
                     QuickDialog.show("Please create username before register", "Empthy name", JOptionPane.ERROR_MESSAGE, password_icon);
                }
                 
            }
        });

        JPanel control  = new JPanel();
        control.setOpaque(false);
        control.setBounds(700,100,500,500);

	control.add(nameLabel);
        control.add(nameText);
        control.add(passLabel);
        control.add(passwordText);
        control.add(confirmLabel);
        control.add(confirmText);
        control.add(confirm_register);
        contentpane.add(control, BorderLayout.WEST);
        contentpane.add(drawpane, BorderLayout.WEST);     
        validate();       
    }
    
    public boolean isUniqueName(String name){
        
        try{
            File infile = new File(MyConstants.FILE_INFO);
            if(infile.exists()==false){
                infile.createNewFile();
                return true;
            }
            else{
                Scanner fileScan = new Scanner(infile);
                while(fileScan.hasNext()){
                    String line = fileScan.nextLine();
                    String cols[] = line.split(",");
                    if(cols[0].equals(name)){
                        return false; //username already exist
                    }
                }
            }
        }catch(Exception e){
            System.err.println(e);
        }
        return true;
    }
    
    public boolean confirmButton(){
        //check equal of password and confirm password -> if equal go to first frame to login, else show quilk dialog and new this frame
        if(Arrays.equals(passwordText.getPassword(), confirmText.getPassword())){
            password = passwordText.getPassword();
            System.out.println();
            System.out.println("password is equal");
            String realPassword = new String(password);
            
            //write name and password to file
            try{
                FileWriter fw = new FileWriter(new File(MyConstants.FILE_INFO), true);
                PrintWriter write = new PrintWriter(fw);
                write.printf("%s, %s, %d\r\n", name, realPassword, score);
                write.close();
                System.out.println("user info is write to file");
                QuickDialog.show("Register Success, Let's play game", "Register successful", JOptionPane.OK_OPTION, success_icon);
            }catch(Exception e){
                System.err.println(e);
            }
            Player.getInstance(name, realPassword);
            return true;
        }
        else{
            QuickDialog.show("Password is not equal, Please enter again", "Password fail", JOptionPane.ERROR_MESSAGE, password_icon);
            return false;
        }
    }
}

class QuickDialog
{
    public static void show(String message, String title, int type, MyImageIcon icon)
    {
        JOptionPane.showMessageDialog(new JFrame(), message, title,
			              type, icon);
    }
}

    