package game;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.io.*;

// ---------------------------------------------------------------
// Handles the graphics for the guessing game
// ---------------------------------------------------------------
public class GameWindow extends JFrame implements ActionListener, DocumentListener {
    private JButton btnGuess;
    private JButton btnHist;
    private JButton btnReset;

    // This button is currently unused, eventually it will be used
    // to check the leaderboard
    private JButton btnLeaders;

    private JLabel lblPrompt;
    private JLabel lblLives;
    private JLabel liveCount;
    private JTextField guessField;
    private GuessGame game;
    private int diff;
    private String leaders = "";

    // ---------------------------------------------------------------
    // Constructor, it sets up literally everything
    // ---------------------------------------------------------------
    public GameWindow()
    {
        game = new GuessGame();

        // Asking what difficulty the player wants and setting it to that
        setDifficulty();

        // Setting up the buttons, labels, text field, and window
        btnGuess = new JButton("Make Guess");
        btnGuess.setEnabled(false);

        btnHist = new JButton("View Previous Guesses");
        btnHist.setEnabled(false);

        btnReset = new JButton("Reset");
        btnReset.setEnabled(false);

        btnLeaders = new JButton("View Leader Board");
        lblPrompt = new JLabel("Enter a number 1 - "
                + Integer.toString(game.getMax()) + ": ");

        lblLives = new JLabel("Current lives: ");
        liveCount = new JLabel(Integer.toString(game.getLives()));
        guessField = new JTextField(25);

        setLayout(new FlowLayout());

        add(lblPrompt);
        add(guessField);
        add(btnGuess);
        add(btnHist);
        add(btnReset);
        add(lblLives);
        add(liveCount);
        add(btnLeaders);

        btnGuess.addActionListener(this);
        btnHist.addActionListener(this);
        btnReset.addActionListener(this);
        btnLeaders.addActionListener(this);
        guessField.getDocument().addDocumentListener(this);
        guessField.addActionListener(this);

        setSize(1024,524);
        setTitle("Guessing Game Reloaded");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ---------------------------------------------------------------
    // Does the appropriate thing when an ActionEvent occurs
    // ---------------------------------------------------------------
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource().equals(btnGuess) ||
            ae.getSource().equals(guessField))
        {
            // Checking if guessField contains a number
            String str = guessField.getText();
            guessField.setText("");
            NumberFormat formatter = NumberFormat.getInstance();
            ParsePosition pos = new ParsePosition(0);
            formatter.parse(str, pos);
            if (str.length() == pos.getIndex())
            {
                // Performing the proper operations for each possible
                // result
                int guess = Integer.parseInt(str);
                int result = game.checkGuess(guess);

                // Updating the life counter as soon as there's relevant a change
                liveCount.setText(Integer.toString(game.getLives()));
                if (result == -1)
                    wrongGuess("low");
                if (result == 1)
                    wrongGuess("high");
                if (result == -2)
                    wrongGuess("invalid");

                btnHist.setEnabled(true);
                btnReset.setEnabled(true);

                if (result == 0)
                    gameEnd("win!");
                if (game.getGameOver())
                    gameEnd("lose. The number was " + game.getRandNum() + ".");
            } else
            {
                JOptionPane.showMessageDialog(this, "I don't know "+
                        "what that is, but it's not a number!", "You dipshit", JOptionPane.PLAIN_MESSAGE);
            }
        }

        if (ae.getSource().equals(btnHist))
        {
            String history = "";
            for (int i = 0; i < game.getHist().size(); i++)
            {
                history += "Guess " + (i + 1) + ": " + game.getHist().get(i) + ", ";

                if (game.getHist().get(i) > game.getMax() ||
                        game.getHist().get(i) < GuessGame.MINNUM)
                {
                    history += "invalid.\n";
                }
                else if (game.getHist().get(i) < game.getRandNum())
                {
                    history += "too low.\n";
                }
                else if (game.getHist().get(i) > game.getRandNum())
                {
                    history += "too high.\n";
                }
            }

            JOptionPane.showMessageDialog(this, history,
                    "Guess History", JOptionPane.PLAIN_MESSAGE);
        }

        if (ae.getSource().equals(btnReset))
        {
            reset();
        }

        if (ae.getSource().equals(btnLeaders))
        {
            viewLeaders();
        }
    }

    // ---------------------------------------------------------------
    // Literally only here do fulfill the DocumentListener interface
    // ---------------------------------------------------------------
    public void changedUpdate(DocumentEvent de)
    {
        return;
    }

    // ---------------------------------------------------------------
    // Enables btnGuess if there's some text in guessField
    // ---------------------------------------------------------------
    public void insertUpdate(DocumentEvent de)
    {
        btnGuess.setEnabled(true);
    }

    // ---------------------------------------------------------------
    // Disables btnGuess if guessField is empty
    // ---------------------------------------------------------------
    public void removeUpdate(DocumentEvent de)
    {
        if (guessField.getText().length() <= 0)
            btnGuess.setEnabled(false);
    }

    // ---------------------------------------------------------------
    // Alerts the player that their guess was wrong
    // ---------------------------------------------------------------
    public void wrongGuess(String str)
    {
        JOptionPane.showMessageDialog(this, "Too " + str + "!",
                                      "Wrong Number", JOptionPane.PLAIN_MESSAGE);
    }

    // ---------------------------------------------------------------
    // Alerts the player that the game has ended and prompts them to
    // play again
    // ---------------------------------------------------------------
    public void gameEnd(String str)
    {
        String[] options = {"Yes", "Change Difficulty", "No"};

        int n = JOptionPane.showOptionDialog(this, "You " + str + "\nPlay again?",
                "Game Over", JOptionPane.WARNING_MESSAGE, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (n < 2 && n >= 0)
        {
            if (n == 1)
                setDifficulty();
            reset();
        } else
            System.exit(0);
    }

    // ---------------------------------------------------------------
    // Resets the game, leaving the difficulty the same
    // ---------------------------------------------------------------
    private void reset()
    {
        game.reset();
        liveCount.setText(Integer.toString(game.getLives()));
        lblPrompt.setText("Enter a number 1 - "
                + Integer.toString(game.getMax()) + ": ");
        btnHist.setEnabled(false);
        btnReset.setEnabled(false);
    }

    // ---------------------------------------------------------------
    // Sets the game difficulty and updates the labels accordingly
    // ---------------------------------------------------------------
    private void setDifficulty()
    {
        String[] difficulties =
                {"Very Easy","Easy", "Normal","Hard","Very Hard"};

        diff = JOptionPane.showOptionDialog(this,"Choose your difficulty:",
                "Difficulty selection", JOptionPane.WARNING_MESSAGE,
                JOptionPane.PLAIN_MESSAGE, null, difficulties, difficulties[2]);

        if (diff == JOptionPane.CLOSED_OPTION)
            System.exit(0);

        // Setting up the game based on the chosen difficulty
        game.setDiff(diff);

        switch(game.getDifficulty())
        {
            case GuessGame.VERY_EASY:
                leaders = "VELeaders.txt";
                break;
            case GuessGame.EASY:
                leaders = "ELeaders.txt";
                break;
            case GuessGame.NORMAL:
                leaders = "NLeaders.txt";
                break;
            case GuessGame.HARD:
                leaders = "HLeaders.txt";
                break;
            case GuessGame.VERY_HARD:
                leaders = "VHLeaders.txt";
                break;
        }
    }

    // ---------------------------------------------------------------
    // Reads and outputs the leader board
    // ---------------------------------------------------------------
    private void viewLeaders()
    {
        String line;
        String result = "";

        try
        {
            FileReader leaderReader = new FileReader(leaders);
            BufferedReader bleaderReader = new BufferedReader(leaderReader);

            while ((line = bleaderReader.readLine()) != null)
            {
                result += line;
                result += "\n";
            }

        } catch (FileNotFoundException e)
        {
            System.err.println(leaders + " not found");
        } catch (IOException e)
        {
            System.err.println("Error reading " + leaders);
        }

        JOptionPane.showMessageDialog(this, "This is currently very broken." + result,
                "Leader Board", JOptionPane.PLAIN_MESSAGE);
    }
}