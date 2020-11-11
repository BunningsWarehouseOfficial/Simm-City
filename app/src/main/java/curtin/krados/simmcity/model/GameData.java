package curtin.krados.simmcity.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import curtin.krados.simmcity.R;

public class GameData {
    //Singleton
    private static GameData sInstance;
    public static GameData get() {
        if (sInstance == null) {
            sInstance = new GameData();
        }
        return sInstance;
    }

    private Settings mSettings;
    private int mGameTime = 0;
    private MutableLiveData<Integer> mMoney;
    private int mPopulation = 0;
    private MutableLiveData<Integer> mNumResidential;
    private MutableLiveData<Integer> mNumCommercial;
    private Structure mSelectedStructure;
    private int mPreviousStructureIndex;

    //Constructor
    private GameData() {
        mSettings = new Settings();
        mMoney = new MutableLiveData<>();
        mMoney.setValue(mSettings.getInitialMoney());

        mNumResidential = new MutableLiveData<>();
        mNumResidential.setValue(0);

        mNumCommercial = new MutableLiveData<>();
        mNumCommercial.setValue(0);
    }

    //Accessors
    public Settings getSettings() {
        return mSettings;
    }
    public int getGameTime() {
        return mGameTime;
    }
    public LiveData<Integer> getMoney() {
        return mMoney;
    }
    public int getPopulation() {
        return mPopulation;
    }
    public LiveData<Integer> getNumResidential() {
        return mNumResidential;
    }
    public LiveData<Integer> getNumCommercial() {
        return mNumCommercial;
    }
    public Structure getSelectedStructure() {
        return mSelectedStructure;
    }
    public int getPreviousStructureIndex() {
        return mPreviousStructureIndex;
    }

    public double getEmploymentRate() throws ArithmeticException {
        if (mPopulation > 0) {
            double nCommercial = (double)mNumCommercial.getValue();
            double shopSize = (double)mSettings.getShopSize();
            return Math.min(1.0, nCommercial * shopSize / (double)mPopulation);
        }
        else {
            throw new ArithmeticException("Cannot divide by zero");
        }
    }

    //Mutators
    public void setSettings(Settings settings) {
        mSettings = settings;
    }
    public void setGameTime(int gameTime) {
        mGameTime = gameTime;
    }
    public void setMoney(int money) {
    mMoney.setValue(money);
    }
    public void setPopulation(int population) {
        mPopulation = population;
    }
    public void setNumResidential(int numResidential) {
        mNumResidential.setValue(numResidential);
    }
    public void setNumCommercial(int numCommercial) {
        mNumCommercial.setValue(numCommercial);
    }
    public void setSelectedStructure(Structure selectedStructure) {
        mSelectedStructure = selectedStructure;
    }
    public void setPreviousStructureIndex(int previousStructureIndex) {
        mPreviousStructureIndex = previousStructureIndex;
    }

    public int nextDay() throws ArithmeticException {
        double employment, salary, taxRate, serviceCost, increment;
        employment  = getEmploymentRate();
        salary      = (double)mSettings.getSalary();
        taxRate     = mSettings.getTaxRate();
        serviceCost = (double)mSettings.getServiceCost();

        //Update the player's money
        increment = (double)mPopulation * (employment * salary * taxRate - serviceCost);
        int income = (int)Math.round(increment);
        setMoney(mMoney.getValue() + income);

        mGameTime++;

        return income;
    }
}
