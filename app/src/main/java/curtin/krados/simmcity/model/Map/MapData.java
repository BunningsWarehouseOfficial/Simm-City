package curtin.krados.simmcity.model.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import curtin.krados.simmcity.R;
import curtin.krados.simmcity.model.GameData.GameData;
import curtin.krados.simmcity.model.GameDbHelper;
import curtin.krados.simmcity.model.GameSchema.MapTable;
import curtin.krados.simmcity.model.Structure.Structure;

/**
 * @author David Cooper
 * @author Kristian Rados
 *
 * Based on original code by David Cooper.
 *
 * Represents the overall map, and contains a grid of MapElement objects (accessible using the
 * get(row, col) method). The two static constants WIDTH and HEIGHT indicate the size of the map.
 *
 * There is a static get() method to be used to obtain an instance (rather than calling the
 * constructor directly).
 *
 * There is also a regenerate() method. The map is randomly-generated, and this method will invoke
 * the algorithm again to replace all the map data with a new randomly-generated grid.
 */
public class MapData
{
    //Constants
    public static final int HEIGHT = GameData.get().getSettings().getMapHeight();
    public static final int WIDTH = GameData.get().getSettings().getMapWidth();
    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();
    private MapElement[][] grid;

    private SQLiteDatabase db;

    //Singleton
    private static MapData instance = null;
    public static MapData get()
    {
        if (instance == null)
        {
            instance = new MapData();
        }
        return instance;
    }

    private static MapElement[][] generateGrid()
    {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++)
        {
            for (int j = 0; j < WIDTH; j++)
            {
                heightField[i][j] =
                    rng.nextInt(HEIGHT_RANGE)
                    + INLAND_BIAS * (
                        Math.min(Math.min(i, j), Math.min(HEIGHT - i - 1, WIDTH - j - 1)) -
                        Math.min(HEIGHT, WIDTH) / 4);
            }
        }

        int[][] newHf = new int[HEIGHT][WIDTH];
        for (int s = 0; s < SMOOTHING_ITERATIONS; s++)
        {
            for (int i = 0; i < HEIGHT; i++)
            {
                for (int j = 0; j < WIDTH; j++)
                {
                    int areaSize = 0;
                    int heightSum = 0;

                    for (int areaI = Math.max(0, i - AREA_SIZE);
                            areaI < Math.min(HEIGHT, i + AREA_SIZE + 1);
                            areaI++)
                    {
                        for (int areaJ = Math.max(0, j - AREA_SIZE);
                                areaJ < Math.min(WIDTH, j + AREA_SIZE + 1);
                                areaJ++)
                        {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] grid = new MapElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++)
        {
            for (int j = 0; j < WIDTH; j++)
            {
                MapElement element;

                if (heightField[i][j] >= WATER_LEVEL)
                {
                    boolean waterN = (i == 0)          || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == WIDTH - 1)  || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == HEIGHT - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0)          || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) ||          (j == 0) ||         (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) ||          (j == WIDTH - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == HEIGHT - 1) || (j == 0) ||         (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == HEIGHT - 1) || (j == WIDTH - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN || waterE || waterS || waterW ||
                                    waterNW || waterNE || waterSW || waterSE;

                    element = new MapElement(
                            true, //Changed from !coast
                            choose(waterN, waterW, waterNW,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                            choose(waterN, waterE, waterNE,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                            choose(waterS, waterW, waterSW,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                            choose(waterS, waterE, waterSE,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                            null, i, j);
                }
                else
                {
                    element = new MapElement(false, WATER, WATER, WATER, WATER, null, i, j);
                }
                grid[i][j] = element;
            }
        }
        return grid;
    }

    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater,
        int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId)
    {
        int id;
        if (nsWater)
        {
            if (ewWater)
            {
                id = convexCoastId;
            }
            else
            {
                id = nsCoastId;
            }
        }
        else
        {
            if (ewWater)
            {
                id = ewCoastId;
            }
            else if (diagWater)
            {
                id = concaveCoastId;
            }
            else
            {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }

    //Constructor
    private MapData() { }

    //Accessors
    public MapElement get(int i, int j)
    {
        return grid[i][j];
    }

    //Mutators
    public void regenerate()
    {
        clear();
        MapElement[][] newGrid = generateGrid();;
        this.grid = newGrid;
        for (int ii = 0; ii < HEIGHT; ii++) {
            for (int jj = 0; jj < WIDTH; jj++) {
                MapElement element = newGrid[ii][jj];
                add(element);
            }
        }
    }

    //Database
    /**
     * Load the contents of the Map table. If the table is empty then generate a new grid.
     */
    public void load(Context context) {
        this.db = new GameDbHelper(context.getApplicationContext()).getWritableDatabase();
        MapCursor cursor = new MapCursor(db.query(MapTable.NAME,
                null, null, null,
                null, null, null)
        );
        try { //Iterate over query results
            int count = 0;
            cursor.moveToFirst();
            this.grid = new MapElement[HEIGHT][WIDTH];
            while(!cursor.isAfterLast()) {
                MapElement element = cursor.getMapElement(); //Load values from database row into memory
                int row = element.getRow();
                int col = element.getCol();
                grid[row][col] = element;
                cursor.moveToNext();
                count++;
            }
            if (count == 0) {
                regenerate();
            }
        }
        finally {
            cursor.close(); //Prevent app leaking resources
        }
    }
    public void add(MapElement mapElement) {
        ContentValues cv = retrieveValues(mapElement);
        //Retrieve the index using column-major order mapping
        int col = mapElement.getCol();
        int row = mapElement.getRow();
        int index = col * MapData.HEIGHT + row;
        cv.put(MapTable.Cols.ID, String.valueOf(index));

        db.insert(MapTable.NAME, null, cv);
    }
    public void edit(MapElement mapElement) {
        ContentValues cv = retrieveValues(mapElement);
        //Retrieve the index using column-major order mapping
        int index = mapElement.getCol() * MapData.HEIGHT + mapElement.getRow();
        cv.put(MapTable.Cols.ID, String.valueOf(index));

        String[] whereValue = { String.valueOf(index) };
        db.update(MapTable.NAME, cv, MapTable.Cols.ID + " = ?", whereValue);
    }
    /**
     * Clears all data from the Map table.
     */
    public void clear() {
        String[] whereValue = { String.valueOf(0) };
        db.delete(MapTable.NAME, MapTable.Cols.ID + " >= ?", whereValue);
    }

    //Private Methods
    private ContentValues retrieveValues(MapElement mapElement) {
        ContentValues cv = new ContentValues();

        //Retrieving needed values for calculation
        Structure structure = mapElement.getStructure();
        Bitmap bitmap = mapElement.getThumbnail();
        if (bitmap == null) {
            cv.putNull(MapTable.Cols.THUMBNAIL);
        }
        else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] blob = bos.toByteArray();
            cv.put(MapTable.Cols.THUMBNAIL, blob);
        }

        if (structure == null) {
            cv.putNull(MapTable.Cols.LABEL);
            cv.putNull(MapTable.Cols.DRAWABLE);
        }
        else {
            cv.put(MapTable.Cols.LABEL, structure.getLabel());
            cv.put(MapTable.Cols.DRAWABLE, structure.getDrawableId());
        }
        if (mapElement.isBuildable()) {
            cv.put(MapTable.Cols.BUILDABLE, 1); //True
        }
        else {
            cv.put(MapTable.Cols.BUILDABLE, 0); //False
        }
        cv.put(MapTable.Cols.NW, mapElement.getNorthWest());
        cv.put(MapTable.Cols.NE, mapElement.getNorthEast());
        cv.put(MapTable.Cols.SW, mapElement.getSouthWest());
        cv.put(MapTable.Cols.SE, mapElement.getSouthEast());
        return cv;
    }
}
