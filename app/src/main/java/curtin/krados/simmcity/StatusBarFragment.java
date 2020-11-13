package curtin.krados.simmcity;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import curtin.krados.simmcity.model.GameData.GameData;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusBarFragment extends Fragment {
    private static final String API_KEY = "791ef53ed9a4fa297fcaec45ca26fdb9";

    private TextView mCityNameText;
    private TextView mPopulationText;
    private TextView mDayText;
    private TextView mTemperatureText;
    private TextView mMoneyText;
    private TextView mLastIncomeText;
    private TextView mEmploymentText;
    private Button mNextDayButton;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_status_bar, ui, false);

        //Retrieving references
        mCityNameText    = view.findViewById(R.id.cityNameText);
        mPopulationText  = view.findViewById(R.id.populationText);
        mDayText         = view.findViewById(R.id.dayText);
        mTemperatureText = view.findViewById(R.id.temperatureText);
        mMoneyText       = view.findViewById(R.id.moneyText);
        mLastIncomeText  = view.findViewById(R.id.lastIncomeText);
        mEmploymentText  = view.findViewById(R.id.employmentText);
        mNextDayButton   = view.findViewById(R.id.nextDayButton);
        GameData data = GameData.get();

        //Initialising values
        mCityNameText   .setText(data.getSettings().getCityName());
        mPopulationText .setText(getString(R.string.population, 0));
        mDayText        .setText(getString(R.string.day, data.getGameTime()));
        new GetTemperature().execute();
        mMoneyText      .setText(getString(R.string.money, data.getSettings().getInitialMoney()));
        mLastIncomeText .setText(getString(R.string.last_income, '+', 0));
        mEmploymentText .setText(getString(R.string.employment_undefined));
        if (data.isGameOver()) {
            mMoneyText.setTextColor(Color.parseColor("#EF3833"));
        }

        //Implementing callbacks / event handlers
        mNextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData data = GameData.get();
                int lastIncome;

                try {
                    lastIncome = data.nextDay();
                }
                catch (ArithmeticException e) {
                    mEmploymentText.setText(getString(R.string.employment_undefined));
                    lastIncome = 0;
                }

                if (lastIncome >= 0) {
                    mLastIncomeText.setText(getString(R.string.last_income, '+', lastIncome));
                }
                else {
                    mLastIncomeText.setText(getString(R.string.last_income, '-', Math.abs(lastIncome)));
                }
                mDayText.setText(getString(R.string.day, data.getGameTime()));
                data.update();
            }
        });

        //Setting up listeners to update the UI and GameData when layout parameters change
        data.getMoney().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer isVertical) {
                GameData data = GameData.get();
                int money = data.getMoney().getValue();

                //Check for game over condition
                if (money < 0 && !data.isGameOver()) {
                    data.setGameOver(true);
                    mMoneyText.setTextColor(Color.parseColor("#EF3833"));

                    Toast toast = Toast.makeText(getActivity(), getString(R.string.game_over), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                mMoneyText.setText(getString(R.string.money, money));
                data.update();
            }
        });
        data.getNumResidential().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer isVertical) {
                GameData data = GameData.get();
                int nResidential = data.getNumResidential().getValue();
                int population = data.getSettings().getFamilySize() * nResidential;
                data.setPopulation(population);

                //Recalculate and update employment rate
                try {
                    double employment = data.getEmploymentRate();
                    //Convert double value to percentage with 1 decimal place
                    double employmentPercent = Math.round(employment * 1000.0) / 10.0;
                    mEmploymentText.setText(getString(R.string.employment, employmentPercent));
                }
                catch (ArithmeticException e) {
                    mEmploymentText.setText(getString(R.string.employment_undefined));
                }
                mPopulationText.setText(getString(R.string.population, population));
                data.update();
            }
        });
        data.getNumCommercial().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer isVertical) {
                GameData data = GameData.get();
                //Recalculate and update employment rate
                try {
                    double employment = data.getEmploymentRate();
                    //Convert double value to percentage with 1 decimal place
                    double employmentPercent = Math.round(employment * 1000.0) / 10.0;
                    mEmploymentText.setText(getString(R.string.employment, employmentPercent));
                }
                catch (ArithmeticException e) {
                    mEmploymentText.setText(getString(R.string.employment_undefined));
                }
                data.update();
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class GetTemperature extends AsyncTask<URL, String, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String data, temperature = null;
            String cityName = GameData.get().getSettings().getCityName();
            String urlString = Uri.parse("https://api.openweathermap.org/data/2.5/weather")
                               .buildUpon()
                               .appendQueryParameter("q", cityName)
                               .appendQueryParameter("appid", API_KEY)
                               .appendQueryParameter("units", "metric")
                               .build().toString();
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                try {
                    //Check that a stable connection to a valid URL has been made
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        data = IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);

                        //Parse the JSON object provided by the weather API
                        JSONObject jBase = new JSONObject(data);
                        double value = jBase.getJSONObject("main").getDouble("temp");
                        temperature = getString(R.string.temperature, value);
                    }
                    else {
                        temperature = getString(R.string.temperature_not_found);
                    }
                }
                finally {
                    conn.disconnect();
                }
            }
            catch (IOException | JSONException e) {
                temperature = getString(R.string.temperature_not_found);
            }
            return temperature;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTemperatureText.setText(s);
        }
    }
}
