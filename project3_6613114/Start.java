//Kulwisit Sakkittiphokhin 6613114
//Thammanat Lerdwijitjarud 6613253
//Pornnubpun Rujicheep 6613257
//Pimlapas   Narasawad 6613264

package project3_6613114;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.*;
import javax.sound.sampled.*;
import java.util.List;
import java.util.ArrayList;

class Start extends JFrame {

    private JLabel contentpane;
    private JButton startButton;
    private JLabel infoLabel;
    private JLabel soundLabel;
    private JLabel howtoLabel;
    private JLabel scoreLabel;
    private MySoundEffect gameSound;
    private int framewidth = MyConstants.FRAMEWIDTH;
    private int frameheight = MyConstants.FRAMEHEIGHT;
    
    public Start() {
        setSize(framewidth, frameheight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gameSound = MySoundEffect.getInstance();
        gameSound.playSound(MyConstants.FILE_SONG);

        
        contentpane = new JLabel();
        setContentPane(contentpane);
        contentpane.setLayout(null);
        
        updateBackgroundImage(MyConstants.FILE_BG);
        setVisible(true);

        addStartButton();
        addInfoIcon();
        addSoundIcon();
        addHowtoIcon();
        addScoreIcon();
        
        contentpane.revalidate();
        contentpane.repaint();
        
    }

    private void updateBackgroundImage(String imagePath) {
        try {
            File file = new File(imagePath);
            Image img = ImageIO.read(file);
            MyImageIcon background = new MyImageIcon(img);
            contentpane.setIcon(background);
            contentpane.revalidate();
            contentpane.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------

    private void addStartButton() {
        try {
            File startFile = new File(MyConstants.FILE_START);
            Image startImg = ImageIO.read(startFile);
            MyImageIcon startIcon = new MyImageIcon(startImg).resize(200, 100);
            startButton = new JButton(startIcon);
            startButton.setBounds((framewidth - 200) / 2, (int) (frameheight * 0.75), 200, 100);
            startButton.setContentAreaFilled(false);
            startButton.setBorderPainted(false);
            startButton.setFocusPainted(false);
            startButton.setOpaque(false);
            startButton.addActionListener(e -> {
                new Game().setVisible(true);
                dispose();
            });
            contentpane.add(startButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------
    private void addInfoIcon() {
        try {
            File infoFile = new File(MyConstants.FILE_INFOMEM);
            Image infoImg = ImageIO.read(infoFile);
            int contentWidth = getContentPane().getWidth();
            int contentHeight = getContentPane().getHeight();
            int iconWidth = contentWidth / 20;
            int iconHeight = iconWidth;
            MyImageIcon infoIcon = new MyImageIcon(infoImg).resize(iconWidth, iconHeight);
            infoLabel = new JLabel(infoIcon);
            int x = (int) (contentWidth * 0.03);
            int y = (int) (contentHeight * 0.03);
            infoLabel.setBounds(x, y, iconWidth, iconHeight);
            // ***
            infoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showInfoPopup();
                }
            });
            contentpane.add(infoLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInfoPopup() {
        try {
            File membersFile = new File(MyConstants.FILE_MEMBERS);
            Image membersImg = ImageIO.read(membersFile);
            int imageWidth = membersImg.getWidth(null);
            int imageHeight = membersImg.getHeight(null);
            int targetWidth = 1093;
            int targetHeight = 614;
            if (imageWidth > targetWidth || imageHeight > targetHeight) {
                double widthRatio = (double) targetWidth / imageWidth;
                double heightRatio = (double) targetHeight / imageHeight;
                double scaleRatio = Math.min(widthRatio, heightRatio);
                imageWidth = (int) (imageWidth * scaleRatio);
                imageHeight = (int) (imageHeight * scaleRatio);
                membersImg = membersImg.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            }
            ImageIcon membersIcon = new ImageIcon(membersImg);
            JLabel membersLabel = new JLabel(membersIcon);
            JDialog dialog = new JDialog(this, "Members", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(membersLabel, BorderLayout.CENTER);
            dialog.setSize(targetWidth, targetHeight);
            dialog.setLocationRelativeTo(this);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------
    private void addHowtoIcon() {
        try {
            File howtoFile = new File(MyConstants.FILE_HOWTO);
            Image howtoImg = ImageIO.read(howtoFile);
            int contentWidth = getContentPane().getWidth();
            int contentHeight = getContentPane().getHeight();
            int iconWidth = contentWidth / 20;
            int iconHeight = iconWidth;
            MyImageIcon howtoIcon = new MyImageIcon(howtoImg).resize(iconWidth, iconHeight);
            howtoLabel = new JLabel(howtoIcon);
            int x = (int) (contentWidth * 0.86);
            int y = (int) (contentHeight * 0.03);
            howtoLabel.setBounds(x, y, iconWidth, iconHeight);
            howtoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showHowtoPopup();
                }
            });
            contentpane.add(howtoLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showHowtoPopup() {
        try {
            File howtoFile = new File(MyConstants.FILE_HOWTOPLAY);
            Image howtoImg = ImageIO.read(howtoFile);
            ImageIcon howtoIcon = new ImageIcon(howtoImg);
            JLabel howtoLabel = new JLabel(howtoIcon);

            JScrollPane scrollPane = new JScrollPane(howtoLabel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            JDialog dialog = new JDialog(this, "How to play", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);

            int preferredWidth = 802;
            int preferredHeight = 500;
            dialog.setSize(preferredWidth, preferredHeight);

            dialog.setLocationRelativeTo(this);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------
    private void addScoreIcon() {
        try {
            File scoreFile = new File(MyConstants.FILE_SCORE);
            Image scoreImg = ImageIO.read(scoreFile);
            int contentWidth = getContentPane().getWidth();
            int contentHeight = getContentPane().getHeight();
            int iconWidth = contentWidth / 20;
            int iconHeight = iconWidth;
            MyImageIcon scoreIcon = new MyImageIcon(scoreImg).resize(iconWidth, iconHeight);
            scoreLabel = new JLabel(scoreIcon);
            int x = (int) (contentWidth * 0.92);
            int y = (int) (contentHeight * 0.03);
            scoreLabel.setBounds(x, y, iconWidth, iconHeight);
            scoreLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showScorePopup();
                }
            });
            contentpane.add(scoreLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showScorePopup() {
        try {
            File infoFile = new File(MyConstants.FILE_INFO);
            BufferedReader reader = new BufferedReader(new FileReader(infoFile));

            List<String[]> scores = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    scores.add(new String[]{parts[0].trim(), parts[2].trim()});
                }
            }
            reader.close();
            scores.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

            String[] columnNames = {"Name", "Score"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            for (String[] score : scores) {
                tableModel.addRow(new Object[]{score[0], Integer.parseInt(score[1])});
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            JDialog dialog = new JDialog((Frame) null, "Score", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setSize(300, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading info file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //--------------------------------------------------------------------------------------------------------
    private void addSoundIcon() {
        try {
            File soundFile = new File(MyConstants.FILE_SOUND);
            Image soundImg = ImageIO.read(soundFile);
            int contentWidth = getContentPane().getWidth();
            int contentHeight = getContentPane().getHeight();
            int iconWidth = contentWidth / 20;
            int iconHeight = iconWidth;
            MyImageIcon soundIcon = new MyImageIcon(soundImg).resize(iconWidth, iconHeight);
            soundLabel = new JLabel(soundIcon);
            int x = (int) (contentWidth * 0.80);
            int y = (int) (contentHeight * 0.03);
            soundLabel.setBounds(x, y, iconWidth, iconHeight);
            soundLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showSoundPopup();
                }
            });
            contentpane.add(soundLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showSoundPopup() {
        float minVolume = gameSound.getMinimumVolume();
        float maxVolume = gameSound.getMaximumVolume();
        float currentVolume = gameSound.getVolume();
        int sliderValue = Math.round((currentVolume - minVolume) / (maxVolume - minVolume) * 100);
        JSlider volumeSlider = new JSlider(0, 100, sliderValue);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        // ***
        volumeSlider.addChangeListener(e -> {
            int newSliderValue = volumeSlider.getValue();
            float volume = minVolume + (newSliderValue / 100f) * (maxVolume - minVolume);
            gameSound.setVolume(volume);
        });
        JOptionPane.showMessageDialog(this, volumeSlider, "Adjust Volume", JOptionPane.PLAIN_MESSAGE);
    }

}

//-------------------------------------------------------------------------------------------------------

class MySoundEffect {

    private Clip clip;
    private FloatControl volumeControl;
    private static MySoundEffect instance;
    private static boolean isMusicPlaying = false; // Track music state

    private MySoundEffect() {
    }

    public static MySoundEffect getInstance() {
        if (instance == null) {
            instance = new MySoundEffect();
        }
        return instance;
    }

    public void playSound(String filePath) {
        if (isMusicPlaying) {
            return; // Skip if music is already playing
        }
        stop();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolumeTo70Percent(); // Set initial volume
            clip.start();
            isMusicPlaying = true; // Set flag to indicate music is playing
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            isMusicPlaying = false; // Reset flag
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            volumeControl.setValue(volume);
        }
    }

    public float getMinimumVolume() {
        return volumeControl.getMinimum();
    }

    public float getMaximumVolume() {
        return volumeControl.getMaximum();
    }

    public float getVolume() {
        if (volumeControl != null) {
            return volumeControl.getValue();
        }
        return 0;
    }

    private void setVolumeTo70Percent() {
        if (volumeControl != null) {
            float minVolume = volumeControl.getMinimum();
            float maxVolume = volumeControl.getMaximum();
            float initialVolume = minVolume + 0.7f * (maxVolume - minVolume);
            volumeControl.setValue(initialVolume);
        }
    }
}
