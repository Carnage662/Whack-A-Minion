package com.example.whack_a_mole;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;

/**
 * The main activity for the Whack-A-Mole game.
 * @author Connor Hand
 * @author Zachary Scurlock
 * @author Nihaal Zaheer
 */
public class MainActivity extends AppCompatActivity {

    // Game's model
    private WhackAMoleModel gameModel;

    // Buttons and TextViews
    private Button startButton;
    private ImageButton hole1, hole2, hole3, hole4, hole5, hole6, hole7, hole8, hole9;
    private TextView scoreText;
    private TextView highScoreText;
    private TextView livesText;

    // Sound variables
    private SoundPool soundPool;
    private int belloSound;
    private int fartSound;
    private int whatSound;
    private int arghhSound;
    private int belaSound;
    private int banana2Sound;
    private int bananaSound;
    private int yes_ala_doSound;
    private int pa_poySound;
    private int yaySound;

    // Handler that controls minion spawns
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable moleSpawner = new Runnable() {
        @Override
        public void run() {
            boolean[] gameGrid = gameModel.getMoleGrid();

            // Check if any minions are still up and remove minion and life
            for (int i = 0; i < 9; ++i) {
                if (gameGrid[i]) {
                    removeMinion(i);
                    gameModel.failedTap(i);
                    if (gameModel.getLives() == 0) {
                        gameOver();
                        return;
                    }
                    livesText.setText("Lives: " + String.valueOf(gameModel.getLives()));
                    break;
                }
            }

            // Spawn in new minion.
            int spawnIndex = gameModel.spawnMinion();
            insertMinion(spawnIndex);

            // Continue spawning minions if the player still has remaining lives
            if (gameModel.getLives() > 0) {
                handler.postDelayed(this, gameModel.getDelay());
            }
        }
    };

    // Method called when an ImageButton is tapped
    private void tappedImage(int index) {

        // Do nothing if the player is out of lives
        if (gameModel.getLives() == 0) {
            return;
        }

        // If the player successfully tapped a minion, update the game model, play a random sound,
        // and remove the minion from the screen
        if (gameModel.getMoleGrid()[index]) {
            gameModel.successfulTap(index);
            Random rand2 = new Random();
            int randSound = rand2.nextInt(10);
            if (randSound == 0) {
                soundPool.play(belloSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 1) {
                soundPool.play(fartSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 2) {
                soundPool.play(whatSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 3) {
                soundPool.play(arghhSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 4) {
                soundPool.play(belaSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 5) {
                soundPool.play(banana2Sound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 6) {
                soundPool.play(bananaSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 7) {
                soundPool.play(yes_ala_doSound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 8) {
                soundPool.play(pa_poySound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            else if (randSound == 9) {
                soundPool.play(yaySound, 0.99f, 0.99f, 1, 0, 0.99f);
            }
            removeMinion(index);
        }
    }

    // Method called when the player hits the "start" button
    private void startGame() {

        // Remove any minions that are up
        for (int i = 0; i < 9; ++i) {
            removeMinion(i);
        }

        // Update the game model and UI elements
        gameModel.start();
        startButton.setVisibility(View.INVISIBLE);
        scoreText.setText("Score: 0");
        highScoreText.setText("High Score: " + String.valueOf(gameModel.getHighScore()));
        livesText.setTextColor(getResources().getColor(R.color.black));
        livesText.setText("Lives: " + String.valueOf(gameModel.getLives()));
        handler.post(moleSpawner);
    }

    // Method called when the player runs out of lives
    private void gameOver() {

        // Update UI elements and remove all callbacks from the handler
        livesText.setTextColor(getResources().getColor(R.color.red));
        livesText.setText("Game over!");
        startButton.setVisibility(View.VISIBLE);
        handler.removeCallbacksAndMessages(moleSpawner);

        // Insert a minion into each hole, looks funny
        for (int i = 0; i < 9; ++i) {
            insertMinion(i);
        }
    }

    // Method that removes a minion at a certain index from the UI only
    private void removeMinion(int index) {
        index++;
        if (index == 1) {
            hole1.setImageResource(R.drawable.hole);
        }
        else if (index == 2) {
            hole2.setImageResource(R.drawable.hole);
        }
        else if (index == 3) {
            hole3.setImageResource(R.drawable.hole);
        }
        else if (index == 4) {
            hole4.setImageResource(R.drawable.hole);
        }
        else if (index == 5) {
            hole5.setImageResource(R.drawable.hole);
        }
        else if (index == 6) {
            hole6.setImageResource(R.drawable.hole);
        }
        else if (index == 7) {
            hole7.setImageResource(R.drawable.hole);
        }
        else if (index == 8) {
            hole8.setImageResource(R.drawable.hole);
        }
        else if (index == 9) {
            hole9.setImageResource(R.drawable.hole);
        }
    }

    // Method that inserts a minion at a certain index in the UI only
    private void insertMinion(int index) {
        index++;
        if (index == 1) {
            hole1.setImageResource(R.drawable.minion);
        }
        else if (index == 2) {
            hole2.setImageResource(R.drawable.minion);
        }
        else if (index == 3) {
            hole3.setImageResource(R.drawable.minion);
        }
        else if (index == 4) {
            hole4.setImageResource(R.drawable.minion);
        }
        else if (index == 5) {
            hole5.setImageResource(R.drawable.minion);
        }
        else if (index == 6) {
            hole6.setImageResource(R.drawable.minion);
        }
        else if (index == 7) {
            hole7.setImageResource(R.drawable.minion);
        }
        else if (index == 8) {
            hole8.setImageResource(R.drawable.minion);
        }
        else if (index == 9) {
            hole9.setImageResource(R.drawable.minion);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Audio attributes object for using SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        // SoundPool object
        soundPool = new SoundPool.Builder().setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        // Loads all 10 sounds
        try {
            belloSound = soundPool.load(this, R.raw.bello, 1);
            fartSound = soundPool.load(this, R.raw.fart, 1);
            whatSound = soundPool.load(this, R.raw.what, 1);
            arghhSound = soundPool.load(this, R.raw.arghh, 1);
            belaSound = soundPool.load(this, R.raw.bela, 1);
            banana2Sound = soundPool.load(this, R.raw.banana2, 1);
            bananaSound = soundPool.load(this, R.raw.banana, 1);
            yes_ala_doSound = soundPool.load(this, R.raw.yes_ala_do, 1);
            pa_poySound = soundPool.load(this, R.raw.pa_poy, 1);
            yaySound = soundPool.load(this, R.raw.yay, 1);
        } catch (Exception e) {
            System.out.println("Error loading sound.");
            e.printStackTrace();
        }

        // Instantiate UI elements and the game model
        gameModel = new ViewModelProvider(this).get(WhackAMoleModel.class);

        startButton = findViewById(R.id.button2);
        hole1 = findViewById(R.id.imageButton1);
        hole2 = findViewById(R.id.imageButton2);
        hole3 = findViewById(R.id.imageButton3);
        hole4 = findViewById(R.id.imageButton4);
        hole5 = findViewById(R.id.imageButton5);
        hole6 = findViewById(R.id.imageButton6);
        hole7 = findViewById(R.id.imageButton7);
        hole8 = findViewById(R.id.imageButton8);
        hole9 = findViewById(R.id.imageButton9);
        scoreText = findViewById(R.id.score);
        highScoreText = findViewById(R.id.highscore);
        livesText = findViewById(R.id.lives);

        // Set UI elements
        scoreText.setTextColor(getResources().getColor(R.color.black));
        highScoreText.setTextColor(getResources().getColor(R.color.black));
        livesText.setTextColor(getResources().getColor(R.color.black));
        scoreText.setText("Score: 0");
        livesText.setText("Lives: 3");
        this.getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFFFFF"));

        // Score observer for the LiveData object in the game model
        gameModel.score.observe(this, new Observer<Integer>() {
           @Override
           public void onChanged(Integer score) {
               if (score > gameModel.getHighScore()) {
                   gameModel.setHighScore(score);
                   highScoreText.setText("High Score: " + String.valueOf(gameModel.getHighScore()));
               }
               scoreText.setText("Score: " + score);
           }
        });

        // OnClickListeners for all the buttons in the UI
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        hole1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(0);
            }
        });

        hole2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(1);
            }
        });

        hole3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(2);
            }
        });

        hole4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(3);
            }
        });

        hole5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(4);
            }
        });

        hole6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(5);
            }
        });

        hole7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(6);
            }
        });

        hole8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(7);
            }
        });

        hole9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tappedImage(8);
            }
        });
    }

    @Override
    protected void onResume() {
        // Load the stored high score into the game UI and backend
        SharedPreferences prefs = getSharedPreferences("HighScorePref", MODE_PRIVATE);
        int highScore = prefs.getInt("HighScore", 0);
        gameModel.setHighScore(highScore);
        highScoreText.setText("High Score: " + String.valueOf(gameModel.getHighScore()));
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Store the high score in disk
        SharedPreferences prefs = getSharedPreferences("HighScorePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int highScore = gameModel.getHighScore();
        editor.putInt("HighScore", highScore);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Release sound pool's memory
        soundPool.release();
        super.onDestroy();
    }
}