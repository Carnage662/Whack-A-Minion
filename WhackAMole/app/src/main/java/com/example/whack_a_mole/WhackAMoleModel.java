package com.example.whack_a_mole;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

/**
 * This class stores the information of the Whack-A-Mole game.
 * @author Connor Hand
 * @author Zachary Scurlock
 * @author Nihaal Zaheer
 */
public class WhackAMoleModel extends ViewModel {

    /**
     * MutableLiveData object that holds the score in the currently played game.
     */
    public MutableLiveData<Integer> score = new MutableLiveData<Integer>(0);

    private boolean moleGrid[];
    private long delay = 1500;
    private int lives = 3;
    private int highScore;
    private Random rand = new Random();

    /**
     * Sets private variables to what they should be when starting game.
     */
    public void start() {
        delay = 1500;
        lives = 3;
        score.setValue(0);
        moleGrid = new boolean[9];
    }

    /**
     * Removes 1 from the player's lives after they missed a minion and removes the minion from the
     * boolean representation.
     * @param index The index of the moleGrid in which the player missed.
     */
    public void failedTap(int index) {
        moleGrid[index] = false;
        lives -= 1;
    }

    /**
     * Sets the moleGrid and score of the current game after a minion has been successfully tapped.
     * Also updates the delay between minion spawns.
     * @param index The index of the array that was tapped.
     */
    public void successfulTap(int index) {
        moleGrid[index] = false;
        score.setValue(score.getValue() + 100);
        if (score.getValue() > highScore) {
            highScore = score.getValue();
        }
        updateDelay();
    }

    /**
     * Determines where a minion will spawn using Random and sets the moleGrid array to true at the
     * index where the minion is spawning.
     * @return The index where the minion has spawned.
     */
    public int spawnMinion() {
        int moleRandIndex = rand.nextInt(9);
        moleGrid[moleRandIndex] = true;
        return moleRandIndex;
    }

    /**
     * Returns the amount of lives the player has left.
     * @return The lives a player has remaining.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns the game's high score.
     * @return The game's high score.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Sets the game's high score if it is currently being beaten.
     * @param score The score that is higher than the high score.
     */
    public void setHighScore(int score) {
        highScore = score;
    }

    /**
     * Returns the current delay between minion spawns.
     * @return The delay, in milliseconds, between minion spawns.
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Returns the boolean representation of the game grid. True where there is a minion, false otherwise.
     * @return The game's boolean grid representing minion locations.
     */
    public boolean[] getMoleGrid() {
        return moleGrid;
    }

    // Method that updates the delay between minion spawns after each successful tap.
    private void updateDelay() {
        if (delay > 800) {
            delay -= 50;
        }
        else if (delay <= 800 && delay > 500) {
            delay -= 30;
        }
        else if (delay <= 500 && delay > 300) {
            delay -= 20;
        }
        else if (delay <= 300 && delay > 50) {
            delay -= 20;
        }
        else {
            delay = 50;
        }
    }
}
