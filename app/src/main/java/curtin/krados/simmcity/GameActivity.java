package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private StatusBarFragment mStatusBar;
    private MapFragment mMap;
    private SelectorFragment mSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Setting up the fragments
        FragmentManager fm = getSupportFragmentManager();
        mStatusBar = (StatusBarFragment) fm.findFragmentById(R.id.statusBarFrame);
        mMap = (MapFragment) fm.findFragmentById(R.id.mapFrame);
        mSelector = (SelectorFragment) fm.findFragmentById(R.id.selectorFrame);
        FragmentTransaction ft = fm.beginTransaction();
        if (mStatusBar == null) {
            mStatusBar = new StatusBarFragment();
            ft.add(R.id.statusBarFrame, mStatusBar);
        }
        if (mMap == null) {
            mMap = new MapFragment();
            ft.add(R.id.mapFrame, mMap);
        }
        if (mSelector == null) {
            mSelector = new SelectorFragment();
            ft.add(R.id.selectorFrame, mSelector);
        }
        ft.commit();
    }

    //Decoupling method for starting the activity
    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, GameActivity.class);
        return intent;
    }
}