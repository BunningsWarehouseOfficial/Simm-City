package curtin.krados.simmcity.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import curtin.krados.simmcity.model.GameSchema.GameDataTable;
import curtin.krados.simmcity.model.GameSchema.MapTable;

public class GameDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "game.db";

    //Constructor
    public GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GameDataTable.NAME + "(" +
                GameDataTable.Cols.ID + " TEXT, " +
                GameDataTable.Cols.CITY_NAME + " TEXT, " +
                GameDataTable.Cols.MAP_WIDTH + " INTEGER, " +
                GameDataTable.Cols.MAP_HEIGHT + " INTEGER, " +
                GameDataTable.Cols.INITIAL_MONEY + " INTEGER, " +
                GameDataTable.Cols.TAX_RATE + " REAL, " +
                GameDataTable.Cols.GAME_TIME + " INTEGER, " +
                GameDataTable.Cols.MONEY + " INTEGER, " +
                GameDataTable.Cols.NUM_RESIDENTIAL + " INTEGER, " +
                GameDataTable.Cols.NUM_COMMERCIAL + " INTEGER, " +
                GameDataTable.Cols.GAME_STARTED + " INTEGER)"); //Boolean

        db.execSQL("CREATE TABLE " + MapTable.NAME + "(" +
                MapTable.Cols.ID + " TEXT, " +
                MapTable.Cols.LABEL + " TEXT, " +
                MapTable.Cols.DRAWABLE + " INTEGER, " +
                MapTable.Cols.THUMBNAIL + " BLOB, " +
                MapTable.Cols.BUILDABLE + " INTEGER, " + //Boolean
                MapTable.Cols.NW + " INTEGER, " +
                MapTable.Cols.NE + " INTEGER, " +
                MapTable.Cols.SW + " INTEGER, " +
                MapTable.Cols.SE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        //No implementation
    }
}
