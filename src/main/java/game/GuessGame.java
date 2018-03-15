package game;

import java.util.ArrayList;

// ---------------------------------------------------------------
// Handles all the logic behind the guessing game
// ---------------------------------------------------------------
public class GuessGame {
    private int randNum;
    private int lives;
    private int maxNum;
    private boolean gameOver;
    private int difficulty;
    private ArrayList<Integer> hist;

    public final static int VERY_EASY = 0;
    public final static int EASY = 1;
    public final static int NORMAL = 2;
    public final static int HARD = 3;
    public final static int VERY_HARD = 4;
    public final static int MINNUM = 1;

    // ---------------------------------------------------------------
    // Default Constructor;
    // Creates a new game with the difficulty set to NORMAL
    // ---------------------------------------------------------------
    public GuessGame()
    {
        setDiff(NORMAL);
        genRandomNumber();
        hist = new ArrayList<>();
        gameOver = false;
    }

    // ---------------------------------------------------------------
    // Constructor;
    // Creates a new game with the difficulty set to diff
    // ---------------------------------------------------------------
    public GuessGame(int diff)
    {
        setDiff(diff);
        genRandomNumber();
        hist = new ArrayList<>();
        gameOver = false;
    }

    // ---------------------------------------------------------------
    // Checks guess against randNum.
    // Returns -2 if guess is out of range, 0 if guess == randNum,
    // 1 if guess > randNum, and -1 if guess < randNum
    // ---------------------------------------------------------------
    public int checkGuess(int guess)
    {
        hist.add(guess);

        if (guess == randNum)
        {
            gameOver = true;
            return 0;
        }

        lives--;
        if (lives <= 0)
        {
            gameOver = true;
        }

        if (guess < MINNUM || guess > maxNum)
        {
            return -2;
        }

        return guess < randNum ? -1 : 1;
    }

    // ---------------------------------------------------------------
    // Sets the difficulty to diff and resets lives to the max for
    // the given difficulty
    // ---------------------------------------------------------------
    public void setDiff(int diff)
    {
        difficulty = diff;
        switch (difficulty)
        {
            case VERY_EASY:
                maxNum = 10;
                lives = 5;
                break;
            case EASY:
                maxNum = 50;
                lives = 5;
                break;
            case NORMAL:
                maxNum = 100;
                lives = 5;
                break;
            case HARD:
                maxNum = 500;
                lives = 7;
                break;
            case VERY_HARD:
                maxNum = 1000;
                lives = 8;
                break;
        }
    }

    // ---------------------------------------------------------------
    // Resets the game
    // ---------------------------------------------------------------
    public void reset()
    {
        gameOver = false;
        setDiff(difficulty);
        hist.clear();
        genRandomNumber();
    }

    // ---------------------------------------------------------------
    // Returns maxNum, the upper limit for randNum
    // ---------------------------------------------------------------
    public int getMax()
    {
        return maxNum;
    }

    // ---------------------------------------------------------------
    // Returns lives
    // ---------------------------------------------------------------
    public int getLives()
    {
        return lives;
    }

    // ---------------------------------------------------------------
    // Returns true if the game is over, false otherwise
    // ---------------------------------------------------------------
    public boolean getGameOver()
    {
        return gameOver;
    }

    // ---------------------------------------------------------------
    // Returns hist, an ArrayList containing all the previous guesses
    // made in the current game
    // ---------------------------------------------------------------
    public ArrayList<Integer> getHist()
    {
        return hist;
    }

    // ---------------------------------------------------------------
    // Returns randNum
    // ---------------------------------------------------------------
    public int getRandNum()
    {
        return randNum;
    }

    // ---------------------------------------------------------------
    // Returns the difficulty level as an int
    // ---------------------------------------------------------------
    public int getDifficulty()
    {
        return difficulty;
    }

    // ---------------------------------------------------------------
    // Returns the max number of lives for the current difficutly.
    // Currently unused.
    // ---------------------------------------------------------------
    public int getMaxLives()
    {
        if (difficulty <= NORMAL)
            return 5;
        else if (difficulty == HARD)
            return 7;
        else
            return 8;
    }

    // ---------------------------------------------------------------
    // Sets randNum to a random number in the interval [1, maxNum]
    // ---------------------------------------------------------------
    private void genRandomNumber()
    {
        randNum = (int)(Math.random() * maxNum) + 1;
    }
}