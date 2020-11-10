package curtin.krados.simmcity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
        mEmploymentText .setText(getString(R.string.employment_undefined)); //TODO Check for undefined value on pop. updated as well, like demolishing residential

        return view;
    }
}
