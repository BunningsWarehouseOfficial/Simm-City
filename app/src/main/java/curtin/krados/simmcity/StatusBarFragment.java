package curtin.krados.simmcity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import curtin.krados.simmcity.model.GameData;

public class StatusBarFragment extends Fragment {
    private TextView mCityNameText;
    private TextView mPopulationText;
    private TextView mDayText;
    private TextView mTemperatureText;
    private TextView mMoneyText;
    private TextView mLastIncomeText;
    private TextView mEmploymentText;
    private Button mNextDayButton;

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
        mDayText        .setText(getString(R.string.day, 0));
        mTemperatureText.setText(getString(R.string.temperature, 25)); //TODO Retrieve actual temperature if applicable, some other value if not
        mMoneyText      .setText(getString(R.string.money, data.getSettings().getInitialMoney()));
        mLastIncomeText .setText(getString(R.string.last_income, '+', 0));
        mEmploymentText .setText(getString(R.string.employment_undefined));

        //Implementing callbacks / event handlers
        mNextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData data = GameData.get();
                int lastIncome = data.nextDay();
                if (lastIncome >= 0) {
                    mLastIncomeText.setText(getString(R.string.last_income, '+', lastIncome));
                }
                else {
                    mLastIncomeText.setText(getString(R.string.last_income, '-', Math.abs(lastIncome)));
                }
                mDayText.setText(getString(R.string.day, data.getGameTime()));
            } //TODO Lose condition at $0
        });

        //Setting up listeners to update the UI when a layout parameter changes
        data.getMoney().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer isVertical) {
                GameData data = GameData.get();
                int money = data.getMoney().getValue();
                mMoneyText.setText(getString(R.string.money, money));
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
            }
        });

        return view;
    }
}
