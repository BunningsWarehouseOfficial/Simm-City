package curtin.krados.simmcity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import curtin.krados.simmcity.model.GameData;
import curtin.krados.simmcity.model.Settings;

public class SettingsActivity extends AppCompatActivity {
    private GameData data;

    private EditText mFamilySizeInput;
    private EditText mShopSizeInput;
    private EditText mSalaryInput;
    private EditText mTaxRateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        data = GameData.get();

        //Retrieving references
        mFamilySizeInput = (EditText) findViewById(R.id.familySizeInput);
        mShopSizeInput   = (EditText) findViewById(R.id.shopSizeInput);
        mSalaryInput     = (EditText) findViewById(R.id.salaryInput);
        mTaxRateInput    = (EditText) findViewById(R.id.taxRateInput);

        //Initialise EditText values
        mFamilySizeInput.setHint(getString(R.string.family_size_value, data.getSettings().getFamilySize()));
        mShopSizeInput  .setHint(getString(R.string.shop_size_value, data.getSettings().getShopSize()));
        mSalaryInput    .setHint(getString(R.string.salary_value, data.getSettings().getSalary()));
        mTaxRateInput   .setHint(getString(R.string.tax_rate_value, data.getSettings().getTaxRate()));

        //Implementing callbacks / event handlers //FIXME doesn't work
        mFamilySizeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int value = Integer.parseInt(s.toString());
                data.getSettings().setTaxRate(value);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mShopSizeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int value = Integer.parseInt(s.toString());
                data.getSettings().setTaxRate(value);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSalaryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int value = Integer.parseInt(s.toString());
                data.getSettings().setTaxRate(value);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTaxRateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double value = Double.parseDouble(s.toString());
                data.getSettings().setTaxRate(value);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Decoupling method for starting the activity
    public static Intent getIntent(Context c) {
        Intent intent = new Intent(c, SettingsActivity.class);
        return intent;
    }
}