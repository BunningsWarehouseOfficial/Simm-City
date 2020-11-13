package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import curtin.krados.simmcity.model.GameData.GameData;
import curtin.krados.simmcity.model.Map.MapData;

public class MainActivity extends AppCompatActivity {
    private Button mStartButton;
    private Button mRestartButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameData data = GameData.get();

        //Load the GameData table; if it is empty then insert a tuple based on the data in GameData
        if (!data.load(MainActivity.this)) {
            data.add();
        }

        //Retrieving references
        mStartButton    = findViewById(R.id.startButton);
        mRestartButton  = findViewById(R.id.restartButton);
        mSettingsButton = findViewById(R.id.settingsButton);
        updateUI();

        //Implementing callbacks / event handlers
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load the MapData table
                MapData.get().load(MainActivity.this);

                GameData.get().setGameStarted(true);
                Intent intent = GameActivity.getIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Transfer database memory reference without reloading its data into new GameData
                GameData data = GameData.get();
                SQLiteDatabase db = data.getDb();
                data = GameData.recreate();
                data.setDb(db);

                MapData map = MapData.get();
                map.clear();
                map.load(MainActivity.this);
                map.regenerate(); //Regenerate game map and recreate Map table
                updateUI();
            }
        });
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData data = GameData.get();
                if (data.isGameStarted()) {
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
        updateUI();
    }

    //Private Methods
    private void updateUI() {
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