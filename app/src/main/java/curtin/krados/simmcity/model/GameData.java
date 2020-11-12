package curtin.krados.simmcity.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GameData {
    //Singleton
    private static GameData sInstance;
    public static GameData get() {
        if (sInstance == null) {
            sInstance = new GameData();
        }
        return sInstance;
    }
    public static GameData recreate() {
        sInstance = new GameData();
        return sInstance;
    }

    private Settings mSettings;
    private int mGameTime = 0;
    private MutableLiveData<Integer> mMoney;
    private int mPopulation = 0;
    private MutableLiveData<Integer> mNumResidential;
    private MutableLiveData<Integer> mNumCommercial;

    private boolean mGameStarted    = false;
    private Structure mSelectedStructure;
    private int mPreviousStructureIndex;
    private boolean mDetailChecking = false;
    private boolean mDemolishing    = false;
    private boolean mGameOver       = false;

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
    public LiveData<Integer> getNumResidential() {
        return mNumResidential;
    }
    public LiveData<Integer> getNumCommercial() {
        return mNumCommercial;
    }

    public boolean isGameStarted() {
        return mGameStarted;
    }
    public Structure getSelectedStructure() {
        return mSelectedStructure;
    }
    public int getPreviousStructureIndex() {
        return mPreviousStructureIndex;
    }
    public boolean isDetailChecking() {
        return mDetailChecking;
    }
    public boolean isDemolishing() {
        return mDemolishing;
    }
    public boolean isGameOver() {
        return mGameOver;
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

    public void setGameStarted(boolean gameStarted) {
        mGameStarted = gameStarted;
    }
    public void setSelectedStructure(Structure selectedStructure) {
        mSelectedStructure = selectedStructure;
    }
    public void setPreviousStructureIndex(int previousStructureIndex) {
        mPreviousStructureIndex = previousStructureIndex;
    }
    public void setDetailChecking(boolean detailChecking) {
        mDetailChecking = detailChecking;
    }
    public void setDemolishing(boolean demolishing) {
        mDemolishing = demolishing;
    }
    public void setGameOver(boolean gameOver) {
        mGameOver = gameOver;
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
