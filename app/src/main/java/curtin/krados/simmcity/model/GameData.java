package curtin.krados.simmcity.model;

public class GameData {
    //Singleton
    private static GameData sInstance;
    public static GameData get() {
        if (sInstance == null) {
            sInstance = new GameData();
        }
        return sInstance;
    }

    //TODO constants

    private Settings mSettings;
    //private MapElement[][] map; //TODO MapData here?
    private int mMoney;
    private int mGameTime = 0; //TODO check if it should = 0
    private Structure mSelectedStructure;
    private int mPreviousStructureIndex;

    //Constructor
    private GameData() {
        mSettings = new Settings();
        mMoney    = mSettings.getInitialMoney();
    }

    //Accessors
    public Settings getSettings() {
        return mSettings;
    }
    public int getMoney() {
        return mMoney;
    }
    public int getGameTime() {
        return mGameTime;
    }
    public Structure getSelectedStructure() {
        return mSelectedStructure;
    }
    public int getPreviousStructureIndex() {
        return mPreviousStructureIndex;
    }

    //Mutators
    public void setSettings(Settings settings) {
        mSettings = settings;
    }
    public void setMoney(int money) {
        mMoney = money;
    }
    public void setGameTime(int gameTime) {
        mGameTime = gameTime;
    }
    public void setSelectedStructure(Structure selectedStructure) {
        mSelectedStructure = selectedStructure;
    }
    public void setPreviousStructureIndex(int previousStructureIndex) {
        mPreviousStructureIndex = previousStructureIndex;
    }
}
