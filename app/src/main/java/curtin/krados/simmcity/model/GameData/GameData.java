package curtin.krados.simmcity.model.GameData;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import curtin.krados.simmcity.model.GameDbHelper;
import curtin.krados.simmcity.model.GameSchema.GameDataTable;
import curtin.krados.simmcity.model.Map.MapElement;
import curtin.krados.simmcity.model.Settings;
import curtin.krados.simmcity.model.Structure.Structure;

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
    private MapElement mDetailsElement;

    private SQLiteDatabase db;

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
    public MapElement getDetailsElement() {
        return mDetailsElement;
    }

    public SQLiteDatabase getDb() {
        return db;
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
    public void setDetailsElement(MapElement detailsElement) {
        mDetailsElement = detailsElement;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public int nextDay() throws ArithmeticException {
        int income;
        double employment, salary, taxRate, serviceCost, increment;
        salary      = (double)mSettings.getSalary();
        taxRate     = mSettings.getTaxRate();
        serviceCost = (double)mSettings.getServiceCost();

        try {
            //Update the player's money
            employment  = getEmploymentRate();
            increment = (double)mPopulation * (employment * salary * taxRate - serviceCost);
            income = (int)Math.round(increment);
            setMoney(mMoney.getValue() + income);
        }
        finally {
            mGameTime++;
        }

        return income;
    }

    //Database
    public boolean load(Context context) {
        boolean isFilled = false; //Whether the database actually has data in it or not
        this.db = new GameDbHelper(context.getApplicationContext()).getWritableDatabase();
        GameDataCursor cursor = new GameDataCursor(db.query(GameDataTable.NAME,
                null, null, null,
                null, null, null)
        );
        try { //Iterate over query results
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                cursor.getGameData(); //Load values from database row into memory
                cursor.moveToNext();
                isFilled = true;
            }
        }
        finally {
            cursor.close(); //Prevent app leaking resources
        }
        return isFilled;
    }
    public void add() {
        ContentValues cv = retrieveValues();
        cv.put(GameDataTable.Cols.ID, String.valueOf(0));
        db.insert(GameDataTable.NAME, null, cv);
    }
    /**
     * Updates all data in the GameData table.
     */
    public void update() {
        ContentValues cv = retrieveValues();
        String[] whereValue = { String.valueOf(0) };
        db.update(GameDataTable.NAME, cv, GameDataTable.Cols.ID + " = ?", whereValue);
    }
    /**
     * Clears all data from the GameData table.
     */
    public void clear() {
        String[] whereValue = { String.valueOf(0) };
        db.delete(GameDataTable.NAME, GameDataTable.Cols.ID + " = ?", whereValue);
    }

    //Private Methods
    private ContentValues retrieveValues() {
        ContentValues cv = new ContentValues();
        cv.put(GameDataTable.Cols.CITY_NAME, mSettings.getCityName());
        cv.put(GameDataTable.Cols.MAP_WIDTH, mSettings.getMapWidth());
        cv.put(GameDataTable.Cols.MAP_HEIGHT, mSettings.getMapHeight());
        cv.put(GameDataTable.Cols.INITIAL_MONEY, mSettings.getInitialMoney());
        cv.put(GameDataTable.Cols.TAX_RATE, mSettings.getTaxRate());
        cv.put(GameDataTable.Cols.GAME_TIME, mGameTime);
        cv.put(GameDataTable.Cols.MONEY, mMoney.getValue());
        cv.put(GameDataTable.Cols.NUM_RESIDENTIAL, mNumResidential.getValue());
        cv.put(GameDataTable.Cols.NUM_COMMERCIAL, mNumCommercial.getValue());
        if (mGameStarted) {
            cv.put(GameDataTable.Cols.GAME_STARTED, 1); //True
        }
        else {
            cv.put(GameDataTable.Cols.GAME_STARTED, 0); //False
        }
        return cv;
    }
}
