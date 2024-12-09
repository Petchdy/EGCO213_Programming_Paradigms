//Kulwisit Sakkittiphokhin 6613114
//Thammanat Lerdwijitjarud 6613253
//Pornnubpun Rujicheep 6613257
//Pimlapas   Narasawad 6613264

package project3_6613114;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;

public class Game extends JFrame {
    
    Player player = Player.getInstance();
    
    private int rows = 4;
    private int cols = 6;
    private int currentRow = 0;
    private int currentCol = 0;
    
    private int mainCellSize = 145;
    private int botCellSize = 40;
    
    private boolean selectionCompleted = false;
    // store user & bot selection
    private String selectionKey;
    private String selectionValue;
    private int playerGuessCount = 0;
    private int botGuessCount = 0;
    private static final int MAX_GUESS = 3;
    private int askedCount = 0;
    // count for remaining cell
    private int playerRemainingCells = 24;
    private int botRemainingCells = 24;
    // count for score
    private int score = 0;
    
    private List<MyImageIcon> characterImages;
    private MyImageIcon botTarget;
    private MyImageIcon playerTarget;
    private MyImageIcon backgroundIcon;
    
    private JPanel mainGridPanel, botGridPanel, questionPanel, guessPanel;
    private JLabel drawpane, selectingLabel;
    private JLayeredPane[][] mainCellLayers, botCellLayers;
    private JLayeredPane pinkCell1, pinkCell2;
    private JRadioButton[] questionButtons;
    private JButton askButton, guessButton;
    private JComboBox<String> comboBox;
    private JSpinner guessSpinner;
    
    public Game() {
        setTitle("Guess Who");
        setSize(MyConstants.FRAMEWIDTH, MyConstants.FRAMEHEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // Create background
        backgroundIcon = new MyImageIcon(MyConstants.FILE_BG2).resize(MyConstants.FRAMEWIDTH, MyConstants.FRAMEHEIGHT);
        drawpane = new JLabel();
        drawpane.setIcon(backgroundIcon);
        drawpane.setBounds(0, 0, MyConstants.FRAMEWIDTH, MyConstants.FRAMEHEIGHT);
        add(drawpane);

        // Add JLayeredPane elements to the grid panel
        createMainTable();

        // Add pinkCell 1, 2
        createPinkCells();
        
        // Add JLayeredPane elements to the grid panel
        createBotTable();

        // Add restart button
        addRestartButton();
        
        setVisible(true);
        
        startGame();
        
    }
    
    private void createMainTable() {
        // Create main panel with grid layout
        mainCellLayers = new JLayeredPane[rows][cols];
        mainGridPanel = new JPanel(new GridLayout(rows, cols, 1, 1));
        mainGridPanel.setOpaque(false);
        mainGridPanel.setBounds(50, 50, 900, 600); // Adjust grid panel bounds
        drawpane.add(mainGridPanel);
        // Load Character
        characterImages = CharacterUtils.getRandomCharacters(CharacterUtils.loadAllCharacters());
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                mainCellLayers[row][col] = createCell(row, col);
                mainGridPanel.add(mainCellLayers[row][col]);
            }
        }
    }

    private JLayeredPane createCell (int row, int col) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(mainCellSize, mainCellSize));

        // cellLayer (alternating colors)
        JLabel cellLayer = new JLabel();
        cellLayer.setOpaque(false);
        if(row % 2 == 0) {
            cellLayer.setIcon(new MyImageIcon(MyConstants.FILE_BBOX).resize(mainCellSize, mainCellSize)); 
        }
        else {
            cellLayer.setIcon(new MyImageIcon(MyConstants.FILE_YBOX).resize(mainCellSize, mainCellSize)); 
        }
        cellLayer.setBounds(0, 0, mainCellSize, mainCellSize);
        layeredPane.add(cellLayer, Integer.valueOf(0));

        // imageLayer
        JLabel imageLayer = new JLabel();
        imageLayer.setHorizontalAlignment(SwingConstants.CENTER);
        imageLayer.setBounds(0, 0, mainCellSize + 10, mainCellSize + 84);
        imageLayer.setIcon(characterImages.get(row * cols + col));
        layeredPane.add(imageLayer, Integer.valueOf(1));
        
        // Cross layer (will be used later)
        JLabel crossLayer = new JLabel();
        crossLayer.setHorizontalAlignment(SwingConstants.CENTER);
        crossLayer.setBounds(0, 0, mainCellSize, mainCellSize);
        crossLayer.setIcon(null);
        layeredPane.add(crossLayer, Integer.valueOf(2));

        return layeredPane;
    }
    
    private void createPinkCells () {
        // Add Pinkcell 1, 2
        pinkCell1 = new JLayeredPane();
        pinkCell2 = new JLayeredPane();
        drawpane.add(pinkCell1);
        drawpane.add(pinkCell2);
        
        // Set bounds for pinkCell1 and pinkCell2
        pinkCell1.setBounds(1070, 130, mainCellSize, mainCellSize); // Set size and position
        pinkCell2.setBounds(1070, 310, mainCellSize, mainCellSize);

        // Add child labels to pinkCell1 and pinkCell2
        JLabel cellLayer1 = new JLabel();
        JLabel cellLayer2 = new JLabel();
        cellLayer1.setIcon(new MyImageIcon(MyConstants.FILE_PBOX).resize(mainCellSize, mainCellSize));
        cellLayer2.setIcon(new MyImageIcon(MyConstants.FILE_PQQ).resize(mainCellSize, mainCellSize));

        // Set child pinkImage bounds
        cellLayer1.setBounds(0, 0, mainCellSize, mainCellSize);
        cellLayer2.setBounds(0, 0, mainCellSize, mainCellSize);

        // Add labels to their respective panes
        pinkCell1.add(cellLayer1, Integer.valueOf(0));
        pinkCell2.add(cellLayer2, Integer.valueOf(0));
        
        // Add upper layer for after use
        JLabel imageLayer1 = new JLabel();
        JLabel imageLayer2 = new JLabel();
        
        imageLayer1.setHorizontalAlignment(SwingConstants.CENTER);
        imageLayer2.setHorizontalAlignment(SwingConstants.CENTER);
        
        imageLayer1.setBounds(0, 0, mainCellSize + 10, mainCellSize + 68);
        imageLayer2.setBounds(0, 0, mainCellSize + 10, mainCellSize + 68);
        
        pinkCell1.add(imageLayer1, Integer.valueOf(1));
        pinkCell2.add(imageLayer2, Integer.valueOf(1));
        
    }
    
    private void createBotTable () {
        // Create bot grid layout
        botCellLayers = new JLayeredPane[rows][cols];
        botGridPanel = new JPanel(new GridLayout(rows, cols, 1, 1));
        botGridPanel.setOpaque(false);
        botGridPanel.setBounds(1015, 480, 260, 180);
        drawpane.add(botGridPanel);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                botCellLayers[row][col] = createBotCell();
                botGridPanel.add(botCellLayers[row][col]);
            }
        }
    }
    
    private JLayeredPane createBotCell () {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(botCellSize, botCellSize));

        // cellLayer (alternating colors)
        JLabel cellLayer = new JLabel();
        cellLayer.setOpaque(false);
        cellLayer.setIcon(new MyImageIcon(MyConstants.FILE_GBOX).resize(botCellSize, botCellSize)); 
        cellLayer.setBounds(0, 0, botCellSize, botCellSize);
        layeredPane.add(cellLayer, Integer.valueOf(0));
        
        // Cross layer (will be used later)
        JLabel crossLayer = new JLabel();
        crossLayer.setHorizontalAlignment(SwingConstants.CENTER);
        crossLayer.setBounds(0, 0, botCellSize, botCellSize);
        crossLayer.setIcon(null);
        layeredPane.add(crossLayer, Integer.valueOf(1));

        return layeredPane;
    }

    private void addCrossMark (JLabel characterLabel, int CellSize) {
        characterLabel.setIcon(new MyImageIcon(MyConstants.FILE_CROSS).resize(CellSize, CellSize));
    }
    
    private void randomPlayerTarget() {
        
        int row = new Random().nextInt(4);
        int col = new Random().nextInt(6);
        
        Component[] selectingComponents = mainCellLayers[row][col].getComponents();
        JLabel selectingCharacterComponents = (JLabel) selectingComponents[1];
        playerTarget = (MyImageIcon) selectingCharacterComponents.getIcon();
        
        System.out.println("Player Target -> " + playerTarget.getTrait());
    }
    
    private void startGame () {
        
        randomPlayerTarget();
        
        Component[] selectingComponents = mainCellLayers[currentRow][currentCol].getComponents();
        selectingLabel = (JLabel) selectingComponents[0];
        selectingLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        mainGridPanel.repaint();
        
        // ***
        addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyPressed(KeyEvent e) 
            {
                if (selectionCompleted) return;
                
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT  && currentCol > 0)        currentCol--;
                if (key == KeyEvent.VK_RIGHT && currentCol < cols - 1) currentCol++;
                if (key == KeyEvent.VK_UP    && currentRow > 0)        currentRow--;
                if (key == KeyEvent.VK_DOWN  && currentRow < rows - 1) currentRow++;
                
                selectingLabel.setBorder(null);
                Component[] selectingComponents = mainCellLayers[currentRow][currentCol].getComponents();
                selectingLabel = (JLabel) selectingComponents[0];
                selectingLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                
                if (key == KeyEvent.VK_ENTER)
                {
                    JLabel selectingCharacterComponents = (JLabel) selectingComponents[1];
                    
                    Component[] pinkCellComponents = pinkCell1.getComponents();
                    JLabel pinkImage = (JLabel) pinkCellComponents[0];
                    
                    if (selectingCharacterComponents.getIcon() instanceof MyImageIcon) 
                    {
                        
                        pinkImage.setIcon(selectingCharacterComponents.getIcon());
                        selectingLabel.setBorder(null);
                        
                        // Add staring selection
                        initializeAskAndGuess();
                        drawpane.repaint();
                        selectionCompleted = true;
                        botTarget = (MyImageIcon) selectingCharacterComponents.getIcon();

                        JOptionPane.showMessageDialog(Game.this, "You have successfully chosen character for your opponent!");
                    } else {
                        JOptionPane.showMessageDialog(Game.this, "Invalid icon type!");
                    }
                }
            }
        });
        setFocusable(true);
    }
    
    private void addRestartButton () {
        
        int padding = 60;
        int buttonWidth = 75;
        int buttonHeight = 75;
        
        try
        {
            File restartFile = new File(MyConstants.FILE_RESTART);
            Image restartImg = ImageIO.read(restartFile);
            MyImageIcon restartIcon = new MyImageIcon(restartImg).resize(75, 75);
            
            JButton restartButton = new JButton(restartIcon);
            
            restartButton.setBounds(MyConstants.FRAMEWIDTH - buttonWidth - padding + (buttonWidth - padding), buttonHeight - padding , buttonWidth, buttonHeight);
            
            restartButton.setContentAreaFilled(false);
            restartButton.setBorderPainted(false);
            restartButton.setFocusPainted(false);
            restartButton.setOpaque(false);
            
            restartButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Do you want to start a new game? The score will not be saved.", 
                        "Confirm to start a new game", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) 
                {
                    // reset game
                    resetGame();
                }
            });
            
            drawpane.add(restartButton);
        }catch(IOException  e)
        {
            e.printStackTrace();
        }
    }
    
    private void initializeAskAndGuess() {
        
        int buttonWidth = 175;
        int buttonHeight = 60;
        int xButton = 310;
        int yButton = 658;
        
        try {
            File askFile = new File(MyConstants.FILE_ASK);
            Image askImg = ImageIO.read(askFile);
            MyImageIcon askIcon = new MyImageIcon(askImg).resize(buttonWidth, buttonHeight);
            askButton = new JButton(askIcon);
            
            askFile = new File(MyConstants.FILE_GUESS);
            askImg = ImageIO.read(askFile);
            askIcon = new MyImageIcon(askImg).resize(buttonWidth, buttonHeight);
            guessButton = new JButton(askIcon);
       }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // buttons for ask and guess
        askButton.setBounds(xButton, yButton, buttonWidth, buttonHeight);
        askButton.setContentAreaFilled(false);
        askButton.setBorderPainted(false);
        askButton.setFocusPainted(false);
        askButton.setOpaque(false);
        askButton.addActionListener(e -> onAsk());

        guessButton.setBounds(xButton + 200, yButton, buttonWidth, buttonHeight);
        guessButton.setContentAreaFilled(false);
        guessButton.setBorderPainted(false);
        guessButton.setFocusPainted(false);
        guessButton.setOpaque(false);
        guessButton.addActionListener(e -> onGuess());

        // add components to frame
        drawpane.add(askButton);
        drawpane.add(guessButton);
    }
    
    private void onAsk() {
        // Create a modal JDialog for the "Ask" section
        JDialog askDialog = new JDialog(this, "Ask a Question", true); // true makes it modal
        askDialog.setSize(300, 300);
        askDialog.setLayout(new BorderLayout());
        askDialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame

        // Panel for the question buttons
        JPanel questionPanel = new JPanel(new GridLayout(7, 1)); // 7 categories
        String[] questionCategories = {"Gender", "Skin", "Hair", "Nose", "Lip", "Decoration", "Clothes"};
        JRadioButton[] questionButtons = new JRadioButton[questionCategories.length];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < questionCategories.length; i++) {
            questionButtons[i] = new JRadioButton(questionCategories[i]);
            group.add(questionButtons[i]);
            questionPanel.add(questionButtons[i]);
        }

        // Panel for ComboBox
        JPanel comboBoxPanel = new JPanel(new FlowLayout());
        JLabel comboBoxLabel = new JLabel("Select Option:");
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setEnabled(false); // Disable initially, enable when a category is selected
        comboBoxPanel.add(comboBoxLabel);
        comboBoxPanel.add(comboBox);
        
        // Listener to updateRemaining ComboBox options based on selected question
        for (JRadioButton button : questionButtons) {
            button.addActionListener(e -> {
                comboBox.removeAllItems(); // Clear previous items
                comboBox.setEnabled(true); // Enable the ComboBox

                // Populate ComboBox with relevant options
                switch (button.getText()) {
                    case "Gender":
                        comboBox.addItem("Girl");
                        comboBox.addItem("Boy");
                        break;
                    case "Skin":
                        comboBox.addItem("White");
                        comboBox.addItem("Black");
                        break;
                    case "Hair":
                        comboBox.addItem("Black");
                        comboBox.addItem("Blue");
                        comboBox.addItem("Pink");
                        comboBox.addItem("Yellow");
                        comboBox.addItem("Bald");
                        break;
                    case "Nose":
                        comboBox.addItem("Cream");
                        comboBox.addItem("Blue");
                        comboBox.addItem("Pink");
                        break;
                    case "Lip":
                        comboBox.addItem("Fang");
                        comboBox.addItem("Teeth");
                        comboBox.addItem("Mouth");
                        break;
                    case "Decoration":
                        comboBox.addItem("Glasses");
                        comboBox.addItem("Headband");
                        comboBox.addItem("Earphone");
                        comboBox.addItem("Hat");
                        comboBox.addItem("No");
                        break;
                    case "Clothes":
                        comboBox.addItem("Long");
                        comboBox.addItem("Short");
                        break;
                }
                
                selectionKey = button.getText();
                
            });
        }
        
        // Confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            if (comboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(
                        askDialog, 
                        "Please select a valid option.", 
                        "Error", 
                        JOptionPane.WARNING_MESSAGE);
            } else {
                selectionValue = comboBox.getSelectedItem().toString();
                JOptionPane.showMessageDialog(
                        askDialog, 
                        "You asked if your target -> " + selectionKey + " = " + selectionValue + " ?", 
                        "You ask", 
                        JOptionPane.INFORMATION_MESSAGE);
                askDialog.dispose(); // Close the dialog
                querryCharacter(mainCellLayers, playerTarget, mainCellSize);
                botTurn();
            }
        });
        
        // Add components to the dialog
        askDialog.add(questionPanel, BorderLayout.CENTER); // Add question buttons in the center
        askDialog.add(comboBoxPanel, BorderLayout.NORTH); // Add ComboBox at the top
        askDialog.add(confirmButton, BorderLayout.SOUTH); // Add confirm button at the bottom

        // Show the dialog
        askDialog.setVisible(true);
    }

    private void onGuess() {
        JDialog guessDialog = new JDialog(this, "Make a Guess", true); // Modal dialog
        guessDialog.setLayout(new BorderLayout());
        guessDialog.setSize(300, 200);
        guessDialog.setLocationRelativeTo(this);

        JPanel guessPanel = new JPanel(new FlowLayout());
        SpinnerModel model = new SpinnerNumberModel(1, 1, 24, 1);
        JSpinner spinner = new JSpinner(model);

        JButton confirmButton = new JButton("Confirm Guess");
        confirmButton.addActionListener(e -> {
            
            // prepare to check if player guess correct ?
            System.out.println("guess: " + model.getValue());
            int userChoose = (int) model.getValue();
            int selectedRow = (userChoose-1) / 6;
            int selectedCol = (userChoose-1) % 6;
            
            Component[] selectingComponents = mainCellLayers[selectedRow][selectedCol].getComponents();
            JLabel selectingCharacterComponents = (JLabel) selectingComponents[1];
            MyImageIcon selectedCell = (MyImageIcon) selectingCharacterComponents.getIcon();
            
            System.out.println("player guess: row = " + selectedRow + ", col = " + selectedCol);
            System.out.println("target = " + playerTarget.getTrait());
            System.out.println("choose = " + selectedCell.getTrait());
            
            // count
            playerGuessCount++;
            
            // correct
            if (playerTarget == selectedCell) {
                
                // calculate score base on asked question
                if (askedCount <= 5) { score += 20; }
                else { score += 10; }
                writeScoreToFile(score);
                endGame("Correct guess!, Score = " + score);
                return;
            }
            // incorrect
            else {
                
                // add cross mark on that incorrectly selected target
                // check if this one is marked or not
                JLabel check = (JLabel)selectingComponents[0];
                if (check.getIcon() == null) {
                    addCrossMark((JLabel) selectingComponents[0], mainCellSize);
                    updateRemaining(mainCellLayers, 1);
                }
                // if it is marked then do nothing
                
                // show how many chance player has left
                JOptionPane.showMessageDialog(
                        this, 
                        "Wrong: " + (MAX_GUESS-playerGuessCount) + " guesses left!", 
                        "Incorrect guess", 
                        JOptionPane.INFORMATION_MESSAGE);
                guessDialog.dispose(); // Close dialog after guess
                
                //run out of change? -> Bot win
                if (playerGuessCount >= MAX_GUESS) {
                    endGame("Bot win");
                    return;
                }
                
                botTurn();
            
            }
            
        });

        guessPanel.add(new JLabel("Guess character number:"));
        guessPanel.add(spinner);
        guessPanel.add(confirmButton);

        guessDialog.add(guessPanel, BorderLayout.CENTER);
        guessDialog.setVisible(true);
    }
    
    private void resetGame() {
        
        new Start().setVisible(true);
        dispose();
//        SwingUtilities.invokeLater(Start::new);
                    
    }
    
    private void updateRemaining (JLayeredPane[][] CellLayers, int count) {
        
        // player
        if (CellLayers == mainCellLayers) {
            
            // updateRemaining remaining cell
            playerRemainingCells-=count;
            // caculate score base on count
            if (count <= 5) {
                score += count * 5;
            }
            else {
                score += count * 10;
            }
        }
        // bot
        else {
            // updateRemaining remaining cell
            botRemainingCells-=count;
        }
        
    }

    private void writeScoreToFile(int finalscore){
        
        StringBuilder updatedContent = new StringBuilder();
        boolean playerUpdated = false;
        
        try{
            File infile = new File(MyConstants.FILE_INFO);
            Scanner fileScan = new Scanner(infile);
            while (fileScan.hasNext()) {
                String line = fileScan.nextLine();
                String[] cols = line.split(",");
                String fileUsername = cols[0].trim();
                String filePassword = cols[1].trim();
                int fileScore = Integer.parseInt(cols[2].trim());
                
                //update score if username matches
                if(fileUsername.equals(player.getName())){
                    playerUpdated = true;
                    fileScore += finalscore;
                    updatedContent.append(fileUsername).append(",").append(filePassword).append(",").append(fileScore).append("\n");
                }
                else{
                    updatedContent.append(line).append("\n");
                }
            }
            
            // Write updated content back to the file
            FileWriter writer = new FileWriter(MyConstants.FILE_INFO, false);
            writer.write(updatedContent.toString());
            writer.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    
    private void endGame(String whoWin) {
        
        // Close all JOptionPane dialogs
//        for (Window window : Window.getWindows()) {
//            if (window instanceof JDialog) {
//                JDialog dialog = (JDialog) window;
//                if (dialog.getContentPane().getComponentCount() > 0 &&
//                    dialog.getContentPane().getComponent(0) instanceof JOptionPane) {
//                    dialog.dispose(); // Dispose of JOptionPane dialogs
//                }
//                window.dispose();
//            }
//        }
        
        JOptionPane.showMessageDialog(
                this,
                whoWin, 
                "Game End", 
                JOptionPane.INFORMATION_MESSAGE);
    
        int result = JOptionPane.showConfirmDialog(
                this,
                "Do you want to play again?", 
                "Play Again?", 
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            dispose();
            System.exit(0);
        }
    }

    private void querryCharacter (JLayeredPane[][] CellLayers, MyImageIcon target, int CellSize) {
        
        Component[] mainCell;
        Component[] targetingCell;
        JLabel targetTopLabel;
        JLabel characterLabel;
        MyImageIcon character;
        
        int changeCount = 0;
        int flag = 0;
        
        System.out.println(selectionKey);
        System.out.println(selectionValue);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // get characterLabel from mainCellLayers
                mainCell = mainCellLayers[i][j].getComponents();
                // real targeting cell
                targetingCell = CellLayers[i][j].getComponents();
                // get top level Label of targeting mainCell
                targetTopLabel = (JLabel) targetingCell[0];
                // get character icon from main celltable
                characterLabel = (JLabel) mainCell[1];
                character = (MyImageIcon) characterLabel.getIcon();
                
                // hasn't add cross yet
                if (targetTopLabel.getIcon() == null) {
                    System.out.println(character.getTrait());
                    System.out.println(character.getTrait().get(selectionKey.toLowerCase()));
                    
                    // check if question match 
                    if (selectionValue.compareToIgnoreCase(target.getTrait().get(selectionKey.toLowerCase())) == 0) {
                        flag = 1;
                        // -> close all others
                        if (character.getTrait().get(selectionKey.toLowerCase()).compareTo(target.getTrait().get(selectionKey.toLowerCase())) != 0) {
                            addCrossMark(targetTopLabel, CellSize);
                            System.out.println("Found1");
                            // count how many changed were made
                            changeCount++;
                        }
                    }
                    // if not match
                    else {
                        flag = 0;
                        // close only that trait
                        if (character.getTrait().get(selectionKey.toLowerCase()).compareTo(selectionValue.toLowerCase()) == 0) {
                            addCrossMark(targetTopLabel, CellSize);
                            System.out.println("Found2");
                            // count how many changed were made
                            changeCount++;
                        }
                    }
                }
                
            }
        }
        
        // show dialog -> what 
        if (flag == 1) {
            JOptionPane.showMessageDialog(null, "Correct: " + selectionKey + " = " + selectionValue);
        }
        else {
            JOptionPane.showMessageDialog(null, "Wrong: " + selectionKey + " != " + selectionValue);
        }
        // updateRemaining score
        updateRemaining(CellLayers, changeCount);
        
        drawpane.repaint();
    }
    
    public void botTurn() {
        System.out.println("Bot turn!!!!!!!!!!");
        if (botRemainingCells == 1) {
            randomBotGuess();
        }
        else if (botRemainingCells <= 5) {
            int random = new Random().nextInt(2);
            if (random % 2 == 0) {
                randomBotAsk();
            }
            else {
                randomBotGuess();
            }
        }
        else {
            randomBotAsk();
        }
        
    }
    
    public void randomBotAsk() {
        
        String[] questionCategories = {"Gender", "Skin", "Hair", "Nose", "Lip", "Decoration", "Clothes"};
        int randomCat = new Random().nextInt(7);
        ArrayList<String> Types = new ArrayList<>();
        switch (questionCategories[randomCat]) {
            case "Gender":
                Types.add("Girl");
                Types.add("Boy");
                break;
            case "Skin":
                Types.add("White");
                Types.add("Black");
                break;
            case "Hair":
                Types.add("Black");
                Types.add("Blue");
                Types.add("Pink");
                Types.add("Yellow");
                Types.add("Bald");
                break;
            case "Nose":
                Types.add("Cream");
                Types.add("Blue");
                Types.add("Pink");
                break;
            case "Lip":
                Types.add("Fang");
                Types.add("Teeth");
                Types.add("Mouth");
                break;
            case "Decoration":
                Types.add("Glasses");
                Types.add("Headband");
                Types.add("Earphone");
                Types.add("Hat");
                Types.add("No");
                break;
            case "Clothes":
                Types.add("Long");
                Types.add("Short");
                break;
        }
        
        int randomType = new Random().nextInt(Types.size());
        
        selectionKey = questionCategories[randomCat];
        selectionValue = Types.get(randomType);
        
        System.out.print("Bot ask: ");
        System.out.print(selectionKey + ", ");
        System.out.println(selectionValue);
        
        JOptionPane.showMessageDialog(this,
                "Bot asked if it's target -> " + selectionKey + " = " + selectionValue + " ?", 
                "Bot ask", 
                JOptionPane.INFORMATION_MESSAGE);
        
        querryCharacter(botCellLayers, botTarget, botCellSize);
    }
    
    public void randomBotGuess() {
        
        int count = 0;
        int guess = new Random().nextInt(botRemainingCells);
        
        Component[] mainCell;
        Component[] targetingCell;
        JLabel targetTopLabel;
        JLabel characterLabel;
        MyImageIcon character;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // get characterLabel from mainCellLayers
                mainCell = mainCellLayers[i][j].getComponents();
                // real targeting cell
                targetingCell = botCellLayers[i][j].getComponents();
                // get top level Label of targeting mainCell
                targetTopLabel = (JLabel) targetingCell[0];
                // get character icon from main celltable
                characterLabel = (JLabel) mainCell[1];
                character = (MyImageIcon) characterLabel.getIcon();
                
                // hasn't add cross yet
                if (targetTopLabel.getIcon() == null) {
                    System.out.println("count = " + count + ", max = " + botRemainingCells);
                    
                    // guess this one
                    if (count == guess) {
                        
                        // updateRemaining bot guess count
                        botGuessCount++;
                        
                        int temp = (i*6)+j+1;
                        
                        System.out.println("bot guess: row = " + i + ", col = " + j + " temp = " + temp);
                        System.out.println("target = " + botTarget.getTrait());
                        System.out.println("choose = " + character.getTrait());
                        
                        // show which one bot guess
                        JOptionPane.showMessageDialog(null, 
                                    "Is number " + temp + " bot target?", 
                                    "Bot guess", 
                                    JOptionPane.INFORMATION_MESSAGE);
                        
                        // check if correct
                        if (botTarget == character) {
                            endGame("Bot guess correctly!, Bot win");
                        }
                        // if incorrect -> add mark
                        else {
                            
                            addCrossMark(targetTopLabel, botCellSize);
                            updateRemaining(botCellLayers ,1);
                            
                            // reach limit -> bot lose
                            if (botGuessCount >= MAX_GUESS) {
                                endGame("You win!, Score = " + score);
                            }
                        }
                    }
                    count++;
                }
                
            }
        }
        
        drawpane.repaint();
        
    }
}