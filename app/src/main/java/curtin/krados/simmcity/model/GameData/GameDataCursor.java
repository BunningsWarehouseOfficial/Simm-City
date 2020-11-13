package curtin.krados.simmcity.model.GameData;

import android.database.Cursor;
import android.database.CursorWrapper;

import curtin.krados.simmcity.model.GameSchema.GameDataTable;
import curtin.krados.simmcity.model.Settings;

public class GameDataCursor extends CursorWrapper {
    public GameDataCursor(Cursor cursor) {
        super(cursor);
    }

    public GameData getGameData() {
        GameData data = GameData.get();

        //Recreate the Settings object
        Settings settings = new Settings();
        settings.setCityName(getString(getColumnIndex( GameDataTable.Cols.CITY_NAME)));
        settings.setMapWidth(getInt(getColumnIndex(    GameDataTable.Cols.MAP_WIDTH)));
        settings.setMapHeight(getInt(getColumnIndex(   GameDataTable.Cols.MAP_HEIGHT)));
        settings.setInitialMoney(getInt(getColumnIndex(GameDataTable.Cols.INITIAL_MONEY)));
        settings.setTaxRate(getDouble(getColumnIndex(  GameDataTable.Cols.TAX_RATE)));

        //Recreate the GameData object
        data.setSettings(settings);
        data.setGameTime(getInt(getColumnIndex(GameDataTable.Cols.GAME_TIME)));
        data.setMoney(getInt(getColumnIndex(GameDataTable.Cols.MONEY)));
        data.setNumResidential(getInt(getColumnIndex(GameDataTable.Cols.NUM_RESIDENTIAL)));
        data.setNumCommercial(getInt(getColumnIndex(GameDataTable.Cols.NUM_COMMERCIAL)));
        data.setGameStarted(getInt(getColumnIndex(GameDataTable.Cols.GAME_STARTED)) == 1);
        return data;
    }
}
