package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import curtin.krados.simmcity.model.GameData;
import curtin.krados.simmcity.model.MapData;

public class MainActivity extends AppCompatActivity {
    private Button mStartButton;
    private Button mRestartButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieving references
        mStartButton    = findViewById(R.id.startButton);
        mRestartButton  = findViewById(R.id.restartButton);
        mSettingsButton = findViewById(R.id.settingsButton);

        //Implementing callbacks / event handlers
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData.get().setGameStarted(true);
                Intent intent = GameActivity.getIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameData.get().setGameStarted(false);
                GameData.recreate();
                MapData.get().regenerate();
                update();
            }
        });
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData data = GameData.get();
                if (data.isGameStarted()) { //FIXME remove if not needed
                    Toast.makeText(MainActivity.this, getString(R.string.settings_error), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = SettingsActivity.getIntent(MainActivity.this);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    //Private Methods
    private void update() {
        if (GameData.get().isGameStarted()) {
            //Set start button to resume
            mStartButton.setText(getString(R.string.resume_button));
            //Show restart button
            mRestartButton.setVisibility(View.VISIBLE);
            //Disable settings menu
            mSettingsButton.setEnabled(false);
        }
        else {
            //Set start button to start new game
            mStartButton.setText(getString(R.string.start_button));
            //Hide restart button
            mRestartButton.setVisibility(View.GONE);
            //Enable settings menu
            mSettingsButton.setEnabled(true);
        }
    }
}

//FIXME Grid cells on map sometimes aren't connected properly with white half cross spaces between
//TODO Some function for resetting the game/database/map
//TODO Implement OwnerName

//TODO Remove unused (grey) methods
//TODO ContentDescriptions
//TODO Remove redundant imports
//TODO Add JavaDocs class and method headers (especially the latter)
//TODO Center the text in toasts (particularly game over toast)
//TODO Green and red yesterday income text for just the monetary value
//TODO Show image of structure/thumbnail in details screen
//  Always show original structure up top and show thumbnail at bottom if there is one
//  Have bottom at very bottom below thumbnail to remove thumbnail