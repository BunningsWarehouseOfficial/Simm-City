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

    private Settings settings;
    //private MapElement[][] map;
    private int money;
    private int gameTime = 0; //TODO check

    //Constructor
    private GameData() {
        settings = new Settings();
        money = settings.getInitialMoney();
    }

    //Accessors
    public static GameData getsInstance() {
        return sInstance;
    }
    public Settings getSettings() {
        return settings;
    }
    //public MapElement[][] getMap() {
        //return map;
    //}
    public int getMoney() {
        return money;
    }
    public int getGameTime() {
        return gameTime;
    }

    //Mutators
    public static void setsInstance(GameData sInstance) {
        GameData.sInstance = sInstance;
    }
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    //public void setMap(MapElement[][] map) {
        //this.map = map;
    //}
    public void setMoney(int money) {
        this.money = money;
    }
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }
}
