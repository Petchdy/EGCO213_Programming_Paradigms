//Kulwisit Sakkittiphokhin 6613114
//Thammanat Lerdwijitjarud 6613253
//Pornnubpun Rujicheep 6613257
//Pimlapas   Narasawad 6613264

package project3_6613114;

import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.swing.ImageIcon;


public class CharacterUtils {

    private static final int CHARACTER_WIDTH = 220;
    private static final int CHARACTER_HEIGHT = 290;

    public static List<MyImageIcon> loadAllCharacters() 
    {
        String line;
        List<MyImageIcon> characterImages = new ArrayList<>();
        
        File CharAttInput = new File(MyConstants.CHARACTER_INFO);
        try (Scanner fileScan = new Scanner(CharAttInput)) {
            line = fileScan.nextLine();
                for (int i = 1; i <= 120; i++) 
                {
                    line = fileScan.nextLine();
                    String [] cols = line.trim().split(",");
                    String imagePath = MyConstants.PATH + "character/" + i + ".png";
                    characterImages.add(new MyImageIcon(imagePath).resize(CHARACTER_WIDTH, CHARACTER_HEIGHT));
                    characterImages.get(i-1).setAtt(cols);
                }
            }
        catch (FileNotFoundException e) {System.out.printf("File error\n");}
        
        return characterImages;
    }

    public static List<MyImageIcon> getRandomCharacters(List<MyImageIcon> allCharacters) 
    {
        if (allCharacters.size() >= 24) 
        {
            Collections.shuffle(allCharacters);
            return allCharacters.subList(0, 24);
        }
        return allCharacters;
    }
    
}

