/**
 * EasyKeno --- program uses JFrame to create a user friendly game of
 * Keno.  The interface has to have all the nesessary functions for
 * the game to work as well as an end and an instruction button.
 * The results of the game must be recorded in KenoFile.txt
 * @author  Joshua Omori
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

  /**
   * Main class that all of the classes use
   * @param No parameters.
   * @exception Any exception
   * @return No return value
   * @extends JFrame
   */
public class EasyKeno extends JFrame {
   private final int WIDTH = 850; 
   private final int HEIGHT = 500;
   private int spots = 0; 		    // counts how many keno buttons were pressed
   private int totalMoney = 500;    // total amount of money
   private int bet = 100; 	      	// the amount bet per game (range 1-100)
   private int hitCounter = 0;      // tells how many squares you hit
   private boolean devDoor = false; // If secret passcode typed, answers shown before tiles selected
   private JLabel title = new JLabel("KENO");
   		// Main Title (Top screen)
   private JLabel spotsSelected = new JLabel("<html>" +spots+"<br>Spots<br>Selected");
		//Tells user how many spots were selected
   private JLabel betAmt = new JLabel("<html>Bet<br>amount:<br>");
		//Shows user how much they bet
   private JLabel coinPurse = new JLabel("<html>Life Savings:<br>$" + totalMoney);
		//Shows user how much money they have in total
   private JLabel bottomText = new JLabel("To play, press \"Start Game\" button at the top right");
		//Shows user what to do as well as how much they won (Bottom screen)
   private JButton startGame = new JButton("Start Game");
		//Initiates game.  Promps user for a bet amount
   private JButton randomPick = new JButton("Randomize");
		//Chooses a random set of 10 buttons to play
   private JButton confirmSquares = new JButton("Confirm Squares");
		//Available once 10 keno buttuons are pressed.  Chooses which squares are winners and ends the game
   private JButton sameBet = new JButton("Same Bet");
		//When the game ends, the user can skip the textfield by clicking this button
   private JButton rules = new JButton("How to Play");
		//The rules are shown when button is pressed in a new window
   private JButton exit = new JButton("Quit the game");
		//Prompts user that work is saved and can be viewed in KenoFile.txt.  Closes game if user confirms.
   private JButton[] KenoButtons = new JButton[40];
   		//Array of 40 buttons that represent keno squares
	
  /**
   * Help Variables
   */
   private JFrame helpScreen = new JFrame("Easy Keno Help"); // sets up a new help window.
   private final int HELP_WIDTH = 850;
   private final int MENU_HEIGHT = 500;
   private int helpPageNum = 1; // this integer keeps track of the page number
   private String s; // the text instructions that dynamically changes as the page is flipped
   private JButton helpPrevious = new JButton("Previous");
   		// this button is used to go to the previous page (disabled at page 1)
   private JButton helpNext = new JButton("Next");
   		// this button is used to go to the previous page (disabled at page 6)
   private JButton helpLetsPlay = new JButton("Let's get started");
   		// this button closes out of the help screen
   private JLabel helpTitle = new JLabel("Easy Keno Instructions:");
   		// shows user the title of the help window
   private JLabel helpPageNumL = new JLabel(Integer.toString(helpPageNum));
   		// shows user the page number
   private JLabel helpInstructions = new JLabel(s);
   		// shows user the text instructions
   
   /**
    * KenoFile exclusive variables
    */
   private int wins;	// keeps track of the number of wins
   private int losses;	// keeps track of the number of losses
   
   
  /**
   * Main method that sets the main JFrame (window)
   * @param No parameters.
   * @exception Any exception
   * @return No return value
   */ 
   public EasyKeno() {
      this.setSize(WIDTH, HEIGHT);
      this.setTitle("Easy Keno Game");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);
   }

  /**
   * method that sets up the UI (user interface) of buttons and labels
   * @param No parameters.
   * @exception Any exception
   * @return No return value
   * @throws FileNotFoundException 
   */
   public void initializeGame() throws FileNotFoundException {
     // sets all of the buttons on main JFrame to do something when clicked
      ActionListener e = new MyListener();
      startGame.addActionListener(e);
      randomPick.addActionListener(e);
      sameBet.addActionListener(e);
      confirmSquares.addActionListener(e);
      rules.addActionListener(e);
      exit.addActionListener(e);
   
      JPanel main = new JPanel();
   	
   	// What will show on the top side
      JPanel north = new JPanel();
      north.setLayout(new BorderLayout());
      title.setFont(new Font("Verdana", Font.PLAIN, 30)); //title font (30 size)
      title.setHorizontalAlignment(SwingConstants.CENTER); //Centers title text
      north.add("Center",title); //adds title to the top center
   	
   	//What will show on the bottom side
      JPanel south = new JPanel();
      south.setBackground(Color.pink); //used to emphasize importance of bottom text
      south.add(bottomText);
      bottomText.setFont(new Font("Verdana", Font.PLAIN, 16)); //medium size font
   	
   	//What will show on the left side
      JPanel west = new JPanel();
      west.setLayout(new BorderLayout());
      JPanel NW = new JPanel();	//Used to group buttons on top left corner
      NW.setLayout(new GridLayout(2,1));
      NW.add(exit); //adds exit button to NW button group
      NW.add(rules); //adds rule button to NW button group
      west.add("North", NW); //groups both buttons and add them to top left
      betAmt.setFont(new Font("Verdana", Font.PLAIN, 16)); //medium size font
      west.add("South", betAmt); //adds betAmt text bottom left
      coinPurse.setFont(new Font("Verdana", Font.PLAIN, 16)); //medium size font
      west.add("Center", coinPurse); //adds coinPurse text middle left
   	
   	//What will show on the right side
      JPanel east = new JPanel();
      east.setLayout(new BorderLayout());
      spotsSelected.setFont(new Font("Verdana", Font.PLAIN, 16)); //medium size font
      JPanel NE = new JPanel(); //Used to group buttons on top right corner
      NE.setLayout(new GridLayout(3,1));
      NE.add(startGame); //adds startGame button to NE button group
      NE.add(randomPick); //adds randomPick button to NE button group
      NE.add(sameBet); //adds sameBet button to NE button group
      randomPick.setEnabled(false);
      sameBet.setEnabled(false);
      east.add("North", NE); // adds startGame button top right
      JPanel SE = new JPanel(); //Used to group button and text on bottom right corner
      SE.setLayout(new GridLayout(2,1));
      SE.add(confirmSquares); //adds confirmSquares button to SE button group
      confirmSquares.setEnabled(false);
      SE.add(spotsSelected); //adds spotsSelected text to SE button group
      east.add("South", SE); //Groups confirmSquares button and spotsSelected text to bottom right
   	
   	//What will show in the center
      JPanel center = new JPanel();
      center.setLayout(new GridLayout(4,10));
      
      // sets keno buttons yellow and in the middle of window
      for(int i=0;i<40;i++) {
         KenoButtons[i] = new JButton(Integer.toString(i+1));
         KenoButtons[i].setBackground(Color.yellow);
         KenoButtons[i].setEnabled(false);
         KenoButtons[i].addActionListener(e);
         center.add(KenoButtons[i]);
      }
   
   	// adds all elements to the main window
      main.setLayout(new BorderLayout());
      main.add("North", north);
      main.add("South", south);
      main.add("West", west);
      main.add("East", east);
      main.add("Center", (center)); //Calls on setKenoButtons and puts it in center
      this.add(main);
   	
   	// help set up
      initializeHelp();
   	
   	// loads up money save file (if file is empty, creates new save file)
      File kenoFile = new File("KenoFile.txt");
      Scanner sc = new Scanner(kenoFile);
      try {
         String s = sc.nextLine();
         wins = Integer.parseInt(sc.nextLine());
         sc.nextLine();
         sc.nextLine();
         losses = Integer.parseInt(sc.nextLine());
         sc.nextLine();
         sc.nextLine();
         totalMoney = Integer.parseInt(sc.nextLine());
      }
      catch (NoSuchElementException NSEE){
         wins = 0;
         losses = 0;
         totalMoney = 500;
      }
      coinPurse.setText("<html>Life Savings:<br>$" + totalMoney);
   }
   
  /**
   * method that sets up the UI (user interface) for the help screen
   * @param No parameters.
   * @exception Any exception
   * @return No return value
   */
   private void initializeHelp() {
      helpScreen.setSize(HELP_WIDTH, MENU_HEIGHT);
      helpScreen.setTitle("Easy Keno Game Instructions");
   	  
      // sets all buttons to work when pressed
      ActionListener e = new MyListener();
      helpPrevious.addActionListener(e);
      helpNext.addActionListener(e);
      helpLetsPlay.addActionListener(e);
      
      // main panel for this window
      JPanel p = new JPanel();
      p.setLayout(new BorderLayout());
   	
      //What will show above (Title of help window and "back to game" button)
      JPanel up = new JPanel();
      up.add(helpTitle);
      up.add(helpLetsPlay);
      helpTitle.setFont(new Font("Verdana", Font.PLAIN, 20)); //medium size font
   	
      //What will show below (previous and next buttons + pg num)
      JPanel down = new JPanel();
      down.add(helpPrevious);
      down.add(helpNext);
      down.add(helpPageNumL);
      helpPrevious.setEnabled(false);
      
      //What will be shown in the center (help instructions)
      JPanel center = new JPanel();
      center.add(helpInstructions);
      helpInstructions.setText(setInstructions(helpPageNum)); //sets the first pg text
      helpInstructions.setHorizontalAlignment(SwingConstants.CENTER); //centers text
      helpInstructions.setFont(new Font("Verdana", Font.PLAIN, 20)); //medium size font
      
      //adds all elements to the help window
      p.add("Center", helpInstructions);
      p.add("North", up);
      p.add("South", down);
      helpScreen.add(p);
   }
  /**
   * method that sets up the UI (user interface) for the help screen
   * @param Integer page number
   * @exception Any exception
   * @return String of dynamically changing instructions
   */
   private String setInstructions(int n) {
      switch (n) {
         case 1:
            s =   "<html>The goal of this game is to get as much money as possible! To do so,";
            s = s + "<br>before the game begins, you must type how much you want to bet.<html>";
            helpInstructions.setText(s);
            return s;
         case 2:
            s =   "<html>Once the bets are complete, there will show 40 different squares which";
            s = s + "<br>can be picked.  Out of those, you can chose which spots you think will ";
            s = s + "<br>be landed on.  You can choose 10 spots! <html>";
            helpInstructions.setText(s);
            return s;
         case 3:
            s =   "<html>Once you choose 10 spots, 10 spots are chosen at random.  The more spots ";
            s = s + "<br>you choose that are landed on, the more you win!!!";
            helpInstructions.setText(s);
            return s;
         case 4:
            s =   "<html> Yellow - Buttons that you have not selected";
            s = s + "<br> Blue - Buttons that you have selected";
            s = s + "<br>";
            s = s + "<br> Green - Buttons that you have won!";
            s = s + "<br> Red - Buttons that were selected by the computer...";
            return s;
         case 5:
            s =   "<html>The prizes are as follows";
            s = s + "<br>----------------------------";
            s = s + "<br>0 = bet x 2";
            s = s + "<br>3 = bet x 1";
            s = s + "<br>4 = bet x 2";
            s = s + "<br>5 = bet x 3";
            s = s + "<br>6 = bet x 7";
            s = s + "<br>7 = bet x 30";
            s = s + "<br>8 = bet x 200";
            s = s + "<br>9 = bet x 1,000";
            s = s + "<br>10 = bet x 10,000";
            return s;
         case 6:
            s =   "<html>Good luck and may your fortunes become trueee!!! :)";
      }
      return s;
   }
   
  /**
   * private class that makes buttons work
   * @param No parameters
   * @exception Any exception
   * @return No return value
   */
   private class MyListener implements ActionListener {
      private final String SECRET_PASSCODE = "SecretPassword"; // when typed in the betAmt txtfield, cheats enabled
      private ArrayList<Integer> picker = new ArrayList<Integer>(); // used to pick a random assortment of win buttons
      ArrayList<Integer> randomizer = new ArrayList<Integer>(); //used to pick a random assortment of buttons
   
      public void actionPerformed(ActionEvent event) {
       /*
        *  These buttons starts the game.  If startGame is pressed, textField will appear on screen
        *  prompting user to enter betAmt.  If the other one is pressed, the previous bet is used.
        *  sameBet is only activated when the player has played at least once
        */
         if (event.getSource()==startGame || event.getSource()==sameBet) {
            if (event.getSource()==startGame) {
               String a = JOptionPane.showInputDialog(startGame, "Please enter bet amount:", "Bet Amount", 3);
            
               try {
                 //if the user doesn't type in anything and accepts, nothing happens
                  if (a.equals("")) {
                     return;
                  }
                  //if the user types the secret passcode, cheats are enabled
                  else if (a.equals(SECRET_PASSCODE)) {
                     devDoor = true;
                     a = JOptionPane.showInputDialog(startGame, "DEV EXIT: ENTER BET AMT");
                  }
                  else {
                     devDoor = false;
                  }
               	  // if betAmt > total money you own, error is thrown
                  if (Integer.parseInt(a) > totalMoney) {
                     EasyKenoException me = new EasyKenoException();
                     throw me;
                  }
                  bet = Integer.parseInt(a);
               }
               catch (EasyKenoException EKE) {
                  JOptionPane.showMessageDialog(startGame, "Please don't go into debt (>"+totalMoney+")", "ERROR", JOptionPane.WARNING_MESSAGE);
                  return;
               }
               catch (NumberFormatException NFE) {
                  JOptionPane.showMessageDialog(startGame, "Please enter a valid dollar amount (no , or $ or .)", "ERROR", JOptionPane.WARNING_MESSAGE);
                  return;
               }
               catch (NullPointerException NPE) {
                  return;
               }
            }
            //turns off both start buttons and turns on randomPick button (chooses 10 keno buttons for you)
            startGame.setEnabled(false);
            sameBet.setEnabled(false);
            randomPick.setEnabled(true);
            
            //subtracts bet amount and updates text
            totalMoney = totalMoney-bet;
            coinPurse.setText("<html>Life Savings:<br>$" + totalMoney);
            betAmt.setText("<html>Bet<br>amount:<br>$" + bet);
            bottomText.setText("Now, click 10 different squares!");
            
            //resets buttons and turns them clickable (blue when clicked on)
            for (int i=0;i<40;i++) {
               KenoButtons[i].setBackground(Color.yellow);
               spots=0;
               spotsSelected.setText("<html>" +spots+"<br>Spots<br>Selected");
               KenoButtons[i].setEnabled(true);
            }
            
            //chooses the random winners now and shows them as orange (for cheaters)
            if (devDoor == true) {
               for (int i=0;i<40;i++) {
                  picker.add(i);
               }
               Collections.shuffle(picker);
            	
               for (int i=0;i<10;i++) {
                  KenoButtons[picker.get(i)].setBackground(Color.orange);
               }
            }
         }
         
         /*
          * This button only activates when 10 keno buttons are blue (pressed).  If cheats are disabled,
          * it choses 10 buttons at random and everything is calculated to end the game.
          */
         else if (event.getSource()==confirmSquares) {
         
         // chooses 10 random keno squares if cheats are disabled (10 already picked if enabled)
            if (devDoor == false) {
               for (int i=0;i<40;i++) {
                  picker.add(i);

               }
               Collections.shuffle(picker);
            }
            // this code shows which tiles won (green) and which ones lost (red)
            for (int i=0;i<10;i++) {
               if(KenoButtons[picker.get(i)].getBackground() == Color.yellow ||
               		KenoButtons[picker.get(i)].getBackground() == Color.orange) {
                  KenoButtons[picker.get(i)].setBackground(Color.red);
               }
               else {
                  KenoButtons[picker.get(i)].setBackground(Color.green);
                  hitCounter++;
               }
            }
            // sets keno buttons false
            for (int i=0;i<40;i++) {
               KenoButtons[i].setEnabled(false);
            }
         
            picker.clear(); // clears the array list to be used again (very important)
         	
            payout(); // calculates the total amount as well as shows win/loss message at bottom
            
            //saves result into file (if previous result is there, overrides it to show newest w/l record and total money)
            try {
               saveIntoFile();
            } 
            catch (IOException e) {
               e.printStackTrace();
            }
            
            //resets game state back to the beginning
            hitCounter = 0;
            coinPurse.setText("<html>Life Savings:<br>$" + totalMoney);
            confirmSquares.setEnabled(false);
            randomPick.setEnabled(false);
            //Game over if you have $0 to bet (can't play again)
            if (totalMoney!=0) {
               startGame.setEnabled(true);
               startGame.setText("New Bet");
               sameBet.setEnabled(true);
            }
         
         }
         /*
          * This button picks a random assortment of 10 keno buttons to be played (for convinience)
          */
         else if (event.getSource()==randomPick) {
            spots = 0;// resets spot counter
            //sets everything to yellow and chooses random buttons
            for (int i=0;i<40;i++) {
               KenoButtons[i].setBackground(Color.yellow);
               randomizer.add(i);
            }
            Collections.shuffle(randomizer);
         	
            //if cheating, sets right colors to orange
            for (int i=0;i<10;i++) {
               if (devDoor==true) {
                  KenoButtons[picker.get(i)].setBackground(Color.orange);
               }
            }
         
            //sets random array list numbers to match up with keno square colors
            for (int i=0;i<10;i++) {
               if (KenoButtons[randomizer.get(i)].getBackground() == Color.orange) {
                  KenoButtons[randomizer.get(i)].setBackground(Color.cyan);
               }
               else {	
                  KenoButtons[randomizer.get(i)].setBackground(Color.blue);
               }
            	
            }
            spots=10;
            spotsSelected.setText("<html>" +spots+"<br>Spots<br>Selected");
            confirmSquares.setEnabled(true);
            randomizer.clear();
         }
         
         /*
          * sets up button when pressed, the rule window pops up
          */
         else if (event.getSource()==rules) {
            helpScreen.setVisible(true);
            helpScreen.setLocation(50, 50);
         }
         
         /*
          * this prompts the user if they wish to exit the game and does so if they confirm
          */
         else if (event.getSource()==exit) {
            int a = JOptionPane.showConfirmDialog(exit, "Do you wish to exit? (All work is saved under KenoFile.txt)", "exit?", 0);
            if (a == 0) {
               System.exit(0);
            }
         }
         
         /*
          * Sets the button to go to the next help page (stops at pg 6)
          */
         else if (event.getSource()==helpNext) {
            if (helpPageNum>=1 && helpPageNum<=6) {
               helpPageNum++;
               helpPageNumL.setText(Integer.toString(helpPageNum));
               helpInstructions.setText(setInstructions(helpPageNum));
               helpPrevious.setEnabled(true);
            
               if (helpPageNum==6) {
                  helpNext.setEnabled(false);
               }
            }
         }
         
         /*
          * Sets the button to go to the previous help page (can't go before 1st page)
          */
         else if (event.getSource()==helpPrevious) {
            if (helpPageNum>1 && helpPageNum<=6) {
               helpPageNum--;
               helpPageNumL.setText(Integer.toString(helpPageNum));
               helpInstructions.setText(setInstructions(helpPageNum));
               helpNext.setEnabled(true);
            
               if (helpPageNum==1) {
                  helpPrevious.setEnabled(false);
               }
            }
         }
         
         /*
          * closes help screen when pressed
          */
         else if (event.getSource()==helpLetsPlay) {
            helpScreen.setVisible(false);
         }
         
         /*
          * Sets up button presses to change to blue when yellow vice versa.  Also makes it so that
          * only 10 blue buttons can be there at a time.
          */
         else {
            for (int i=0;i<40;i++) {
               if (event.getSource()==KenoButtons[i]) {
                  if (KenoButtons[i].getBackground() == Color.yellow) {
                     if (spots==9) {
                        confirmSquares.setEnabled(true);
                     }
                     if (spots<10) {
                        KenoButtons[i].setBackground(Color.blue);
                        spots++;
                        spotsSelected.setText("<html>" +spots+"<br>Spots<br>Selected");
                     }
                  }
                  // orange and cyan are dev cheats
                  else if (KenoButtons[i].getBackground() == Color.orange) {
                     if (spots==9) {
                        confirmSquares.setEnabled(true);
                     }
                     if (spots<10) {
                        KenoButtons[i].setBackground(Color.cyan);
                        spots++;
                        spotsSelected.setText("<html>" +spots+"<br>Spots<br>Selected");
                     }
                  }
                  else if (KenoButtons[i].getBackground() == Color.cyan) {
                     KenoButtons[i].setBackground(Color.orange);
                     confirmSquares.setEnabled(false);
                     spots--;
                     spotsSelected.setText("<html>" +spots+"<br>Spots<br>Selected");
                  }
                  
                  else {
                     KenoButtons[i].setBackground(Color.yellow);
                     confirmSquares.setEnabled(false);
                     spots--;
                     spotsSelected.setText("<html>" +spots+"<br>Spots<br>Selected");
                  }
               }
            }
         }
      
      
      }
   }
	
  /**
   * method that calculate payout and tells user how much they won
   * @param No parameters.
   * @exception Any exception
   * @return No return value
   */
   private void payout() {
      switch (hitCounter) {
         case 0:
            totalMoney = totalMoney+(bet*2);
            bottomText.setText("Win! 2x (No Matches)");
            wins++;
            break;
         case 3:
            totalMoney = totalMoney+bet;
            bottomText.setText("Broke Even (3 Matches)");
            break;
         case 4:
            totalMoney = totalMoney+(bet*2);
            bottomText.setText("Win! 2x (4 Matches)");
            wins++;
            break;
         case 5:
            totalMoney = totalMoney+(bet*3);
            bottomText.setText("Win! 3x (5 Matches)");
            wins++;
            break;
         case 6:
            totalMoney = totalMoney+(bet*7);
            bottomText.setText("Win! 7x (6 Matches)");
            wins++;
            break;
         case 7:
            totalMoney = totalMoney+(bet*30);
            bottomText.setText("Win! 30x (7 Matches)");
            wins++;
            break;
         case 8:
            totalMoney = totalMoney+(bet*200);
            bottomText.setText("Win! 200x (8 Matches)");
            wins++;
            break;
         case 9:
            totalMoney = totalMoney+(bet*1000);
            bottomText.setText("Win! 1000x (9 Matches)");
            wins++;
            break;
         case 10:
            totalMoney = totalMoney+(bet*10000);
            bottomText.setText("Win! 10,000x (10 Matches) CONGRATULATIONS!!!");
            wins++;
            break;
         default:
            bottomText.setText("Nice Try");
            losses++;
            break;
      }
   }
   
  /**
   * method that calculate payout and tells user how much they won
   * @param No parameters.
   * @exception Any exception
   * @return No return vlue
   */
   private void saveIntoFile() throws IOException {
     // sets up file to be outputted
      File kenoFile = new File("KenoFile.txt");
      FileWriter fw = new FileWriter(kenoFile);
      PrintWriter pw = new PrintWriter(fw);
   	  
      // prints to keno file amt of wins and losses
      pw.println("User has won this many time(s)!" );
      pw.println(wins+"\n");
      pw.println("User has loss this many time(s)!");
      pw.println(losses+"\n");
      
      // prints to keno file the total amt of money the user has
      pw.println("Total user money:");
      pw.println(totalMoney);
      
      // closes files
      pw.close();
      fw.close();
   	
   }
}