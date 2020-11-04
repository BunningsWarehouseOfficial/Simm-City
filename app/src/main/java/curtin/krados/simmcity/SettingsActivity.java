package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import curtin.krados.simmcity.model.GameData;

public class SettingsActivity extends AppCompatActivity {
    private GameData data;

    private EditText mMapWidthInput;
    private EditText mMapHeightInput;
    private EditText mInitialMoneyInput;
    private EditText mTaxRateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        data = GameData.get();

        //Retrieving references
        mMapWidthInput     = (EditText) findViewById(R.id.mapWidthInput);
        mMapHeightInput    = (EditText) findViewById(R.id.mapHeightInput);
        mInitialMoneyInput = (EditText) findViewById(R.id.initialMoneyInput);
        mTaxRateInput      = (EditText) findViewById(R.id.taxRateInput);

        //Initialise EditText default values
        mMapWidthInput    .setHint(getString(R.string.map_width_value, data.getSettings().getMapWidth()));
        mMapHeightInput   .setHint(getString(R.string.map_height_value, data.getSettings().getMapHeight()));
        mInitialMoneyInput.setHint(getString(R.string.initial_money_value, data.getSettings().getInitialMoney()));
        mTaxRateInput     .setHint(getString(R.string.tax_rate_value, data.getSettings().getTaxRate()));

        //Implementing callbacks / event handlers //TODO At end of project test this on all devices to ensure that you can't enter a negative number, because it isn't checked
        mMapWidthInput.addTextChangedListener(new TextWatcher() { //TODO Use a generic method for to remove repeated integer code
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    data.getSettings().setMapWidth(value);
                    mMapWidthInput.setHint(getString(R.string.map_width_value, value));
                }
                catch (NumberFormatException e) { //Show the user a simple error message
                    mMapWidthInput.setError(getString(R.string.map_width_error));
                }
            }
        });
        mMapHeightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    data.getSettings().setMapHeight(value);
                    mMapHeightInput.setHint(getString(R.string.map_height_value, value));
                }
                catch (NumberFormatException e) { //Show the user a simple error message
                    mMapHeightInput.setError(getString(R.string.map_height_error));
                }
            }
        });
        mInitialMoneyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    data.getSettings().setInitialMoney(value);
                    mInitialMoneyInput.setHint(getString(R.string.initial_money_value, value));
                }
                catch (NumberFormatException e) { //Show the user a simple error message
                    mInitialMoneyInput.setError(getString(R.string.initial_money_error));
                }
            }
        });
        mTaxRateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double value = Double.parseDouble(s.toString());
                    data.getSettings().setTaxRate(value);
                    mTaxRateInput.setHint(getString(R.string.tax_rate_value, value));
                }
                catch (NumberFormatException e) { //Show the user a simple error message
                    mTaxRateInput.setError(getString(R.string.tax_rate_error));
                }
            }
        });
    }

    //Decoupling method for starting the activity
    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, SettingsActivity.class);
        return intent;
    }
}