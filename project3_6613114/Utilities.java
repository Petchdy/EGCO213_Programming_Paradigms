//Kulwisit Sakkittiphokhin 6613114
//Thammanat Lerdwijitjarud 6613253
//Pornnubpun Rujicheep 6613257
//Pimlapas   Narasawad 6613264

package project3_6613114;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

interface MyConstants{
    static final String PATH           = "src/main/java/project3_6613114/resources/";
    // backgrounds
    static final String BACKGROUND     = PATH + "backgrounds/";
    static final String FILE_1BG       = BACKGROUND + "backgroud_1frame.png";
    static final String FILE_BG        = BACKGROUND + "guesswho.jpg"; 
    static final String FILE_BG2       = BACKGROUND + "guesswho2.png"; 
    static final String FILE_BG3       = BACKGROUND + "guesswho3.png";
    // buttons
    static final String BUTTON         = PATH + "buttons/";
    static final String FILE_START     = BUTTON + "start.png";
    static final String FILE_REGIS     = BUTTON + "register.png";
    static final String FILE_RESTART   = BUTTON + "restart.png";
    static final String FILE_ASK       = BUTTON + "ask.png";
    static final String FILE_GUESS     = BUTTON + "guess.png";    
    static final String FILE_LOGIN     = BUTTON + "login.png";
    static final String CONFIRM_LOGIN  = BUTTON + "confirm_login.png";    
    // elements
    static final String ELEMENT        = PATH + "elements/";
    static final String FILE_BBOX      = ELEMENT + "bluebox.png";
    static final String FILE_YBOX      = ELEMENT + "yellowbox.png";
    static final String FILE_GBOX      = ELEMENT + "graybox.png";
    static final String FILE_PBOX      = ELEMENT + "pinkbox.png";
    static final String FILE_PQQ       = ELEMENT + "pinkboxQQ.png";
    static final String FILE_CROSS     = ELEMENT + "redcross.png";
    static final String ICON_PASSWORD  = ELEMENT + "denied.png";
    static final String ICON_LOGIN     = ELEMENT + "checked.png";
    static final String ICON_USER      = ELEMENT + "usernotfound.png";
    static final String FILE_INFOMEM   = ELEMENT + "info.png";
    static final String FILE_MEMBERS   = ELEMENT + "members.png";
    static final String FILE_SOUND     = ELEMENT + "sound.png";
    static final String FILE_HOWTO     = ELEMENT + "howto.png";
    static final String FILE_SCORE     = ELEMENT + "score.png";
    static final String FILE_HOWTOPLAY = ELEMENT + "howtoplay.jpg"; 
    // sound 
    static final String SOUND          = PATH + "sounds/";
    static final String FILE_SONG      = SOUND + "gamesound.wav";
    // text_files
    static final String TEXT_FILE      = PATH + "text_files/";
    static final String CHARACTER_INFO = TEXT_FILE + "characters.txt";
    static final String FILE_INFO      = TEXT_FILE + "info.txt";
    // window size
    static final int FRAMEWIDTH   = 1366;
    static final int FRAMEHEIGHT  = 768;
    static final int DISTANCE_X   = 800;
    
}

class MyImageIcon extends ImageIcon
{

    private Map<String, String> allTrait = new HashMap<>();
    
    public MyImageIcon(String fname)  { super(fname); }
    public MyImageIcon(Image image)   { super(image); }
    
    public MyImageIcon resize(int width, int height)
    {
	Image oldimg = this.getImage();
	Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new MyImageIcon(newimg);
    }
    
    public void setAtt(String Att[]) {
        
        allTrait.put("num", Att[0].trim());
        allTrait.put("gender", Att[1].trim());
        allTrait.put("skin", Att[2].trim());
        allTrait.put("hair", Att[3].trim());
        allTrait.put("nose", Att[4].trim());
        allTrait.put("lip", Att[5].trim());
        allTrait.put("decoration", Att[6].trim());
        allTrait.put("clothes", Att[7].trim());
        
    }
    
    public Map<String, String> getTrait() {
        return allTrait;
    }
    
}
