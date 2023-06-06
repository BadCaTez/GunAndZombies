package com.example.gunandzombies;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}
