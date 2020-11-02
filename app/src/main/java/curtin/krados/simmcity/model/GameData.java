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

    //Mutators
    public void setSettings(Settings mSettings) {
        this.mSettings = mSettings;
    }
    public void setMoney(int mMoney) {
        this.mMoney = mMoney;
    }
    public void setGameTime(int mGameTime) {
        this.mGameTime = mGameTime;
    }
    public void setSelectedStructure(Structure mSelectedStructure) {
        this.mSelectedStructure = mSelectedStructure;
    }
}
