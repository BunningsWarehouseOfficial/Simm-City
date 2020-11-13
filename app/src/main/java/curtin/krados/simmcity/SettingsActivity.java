package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import curtin.krados.simmcity.model.GameData.GameData;
import curtin.krados.simmcity.model.Settings;

public class SettingsActivity extends AppCompatActivity {
    private GameData data;

    private EditText mCityNameInput;
    private EditText mMapWidthInput;
    private EditText mMapHeightInput;
    private EditText mInitialMoneyInput;
    private EditText mTaxRateInput;
    private Button mDefaultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        data = GameData.get();

        //Retrieving references
        mCityNameInput     = findViewById(R.id.cityNameInput);
        mMapWidthInput     = findViewById(R.id.mapWidthInput);
        mMapHeightInput    = findViewById(R.id.mapHeightInput);
        mInitialMoneyInput = findViewById(R.id.initialMoneyInput);
        mTaxRateInput      = findViewById(R.id.taxRateInput);
        mDefaultButton     = findViewById(R.id.defaultButton);

        //Initialise EditText default values
        mCityNameInput    .setHint(data.getSettings().getCityName());
        mMapWidthInput    .setHint(getString(R.string.map_width_value, data.getSettings().getMapWidth()));
        mMapHeightInput   .setHint(getString(R.string.map_height_value, data.getSettings().getMapHeight()));
        mInitialMoneyInput.setHint(getString(R.string.initial_money_value, data.getSettings().getInitialMoney()));
        mTaxRateInput     .setHint(getString(R.string.tax_rate_value, data.getSettings().getTaxRate()));

        //Implementing callbacks / event handlers //TODO At end of project test this on all devices to ensure that you can't enter a negative number, because it isn't checked
        mCityNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                if (value.equals("")) { //Show the user a simple error message
                    mCityNameInput.setError(getString(R.string.city_name_error));
                }
                else {
                    if (value.length() > Settings.MAX_NAME_LENGTH) {
                        mCityNameInput.setError(getString(R.string.city_name_length_error));
                    }
                    else {
                        data.getSettings().setCityName(value);
                        data.update();
                        mCityNameInput.setHint(value);
                    }
                }
            }
        });
        mMapWidthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    data.getSettings().setMapWidth(value);
                    data.update();
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
                    data.update();
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
                    if (!data.isGameStarted()) {
                        data.setMoney(value);
                    }
                    data.update();
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
                    if (value > 1.0) {
                        mTaxRateInput.setError(getString(R.string.tax_rate_error));
                    }
                    else {
                        data.getSettings().setTaxRate(value);
                        data.update();
                        mTaxRateInput.setHint(getString(R.string.tax_rate_value, value));
                    }
                }
                catch (NumberFormatException e) { //Show the user a simple error message
                    mTaxRateInput.setError(getString(R.string.tax_rate_error));
                }
            }
        });
        mDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings settings = new Settings();
                GameData.get().setSettings(settings);
                updateUI();
            }
        });
    }

    //Decoupling method for starting the activity
    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, SettingsActivity.class);
        return intent;
    }

    //Private Methods
    private void updateUI() {
        Settings settings = GameData.get().getSettings();
        mCityNameInput    .setHint(settings.getCityName());
        mMapWidthInput    .setHint(getString(R.string.map_width_value, settings.getMapWidth()));
        mMapHeightInput   .setHint(getString(R.string.map_width_value, settings.getMapHeight()));
        mInitialMoneyInput.setHint(getString(R.string.initial_money_value, settings.getInitialMoney()));
        mTaxRateInput     .setHint(getString(R.string.tax_rate_value, settings.getTaxRate()));
    }
}