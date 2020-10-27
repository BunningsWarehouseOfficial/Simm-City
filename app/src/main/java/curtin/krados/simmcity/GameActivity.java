package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    //TODO fields

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Retrieving references


        //Implementing callbacks / event handlers

    }

    //Decoupling method for starting the activity
    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, GameActivity.class);
        return intent;
    }
}