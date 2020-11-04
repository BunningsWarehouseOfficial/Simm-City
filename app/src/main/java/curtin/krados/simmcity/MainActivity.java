package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mStartButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieving references
        mStartButton    = (Button) findViewById(R.id.startButton);
        mSettingsButton = (Button) findViewById(R.id.settingsButton);

        //Implementing callbacks / event handlers
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = GameActivity.getIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SettingsActivity.getIntent(MainActivity.this);
                startActivity(intent); //TODO Block entering settings screen if there is a running game
            }
        });
    }
}

//TODO TextWatcher for input validation
//TODO Update Android Studio to 4.1
//TODO Remove unused (grey) methods
//TODO Implement JavaDocs
//TODO ContentDescriptions
//TODO Remove redundant imports