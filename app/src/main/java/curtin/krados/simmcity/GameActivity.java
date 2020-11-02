package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private StatusBarFragment statusBar;
    private MapFragment map;
    private SelectorFragment selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Setting up the fragments
        FragmentManager fm = getSupportFragmentManager();
        statusBar = (StatusBarFragment) fm.findFragmentById(R.id.statusBarFrame);
        map       = (MapFragment) fm.findFragmentById(R.id.mapFrame);
        selector  = (SelectorFragment) fm.findFragmentById(R.id.selectorFrame);
        FragmentTransaction ft = fm.beginTransaction();
        if (statusBar == null) {
            statusBar = new StatusBarFragment();
            ft.add(R.id.statusBarFrame, statusBar);
        }
        if (map == null) {
            map = new MapFragment();
            ft.add(R.id.mapFrame, map);
        }
        if (selector == null) {
            selector = new SelectorFragment();
            ft.add(R.id.selectorFrame, selector);
        }
        ft.commit();
    }

    //Decoupling method for starting the activity
    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, GameActivity.class);
        return intent;
    }
}