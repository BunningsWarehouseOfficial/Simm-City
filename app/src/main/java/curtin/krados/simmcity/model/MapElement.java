package curtin.krados.simmcity.model;

import android.content.Context;

import curtin.krados.simmcity.StructureException;
import curtin.krados.simmcity.R;
import curtin.krados.simmcity.model.GameData.GameData;
import curtin.krados.simmcity.model.Structure.Road;
import curtin.krados.simmcity.model.Structure.Structure;

/**
 * Represents a single grid square in the map. Each map element has both terrain and an optional
 * structure.
 *
 * The terrain comes in four pieces, as if each grid square was further divided into its own tiny
 * 2x2 grid (north-west, north-east, south-west and south-east). Each piece of the terrain is
 * represented as an int, which is actually a drawable reference. That is, if you have both an
 * ImageView and a MapElement, you can do this:
 *
 * ImageView iv = ...;
 * MapElement me = ...;
 * iv.setImageResource(me.getNorthWest());
 *
 * This will cause the ImageView to display the grid square's north-western terrain image,
 * whatever it is.
 *
 * (The terrain is broken up like this because there are a lot of possible combinations of terrain
 * images for each grid square. If we had a single terrain image for each square, we'd need to
 * manually combine all the possible combinations of images, and we'd get a small explosion of
 * image files.)
 *
 * Meanwhile, the structure is something we want to display over the top of the terrain. Each
 * MapElement has either zero or one Structure} objects. For each grid square, we can also change
 * which structure is built on it.
 */
public class MapElement
{
    private final boolean mBuildable;
    private final int mTerrainNorthWest;
    private final int mTerrainSouthWest;
    private final int mTerrainNorthEast;
    private final int mTerrainSouthEast;
    private Structure mStructure;
    private int mRow;
    private int mCol;

    //Constructor
    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure, int row, int col)
    {
        mBuildable = buildable;
        mTerrainNorthWest = northWest;
        mTerrainNorthEast = northEast;
        mTerrainSouthWest = southWest;
        mTerrainSouthEast = southEast;
        mStructure = structure;
        mRow = row;
        mCol = col;
    }

    //Accessors
    public boolean isBuildable()
    {
        return mBuildable;
    }
    public int getNorthWest()
    {
        return mTerrainNorthWest;
    }
    public int getSouthWest()
    {
        return mTerrainSouthWest;
    }
    public int getNorthEast()
    {
        return mTerrainNorthEast;
    }
    public int getSouthEast()
    {
        return mTerrainSouthEast;
    }
    /**
     * Retrieves the structure built on this map element.
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return mStructure;
    }
    public int getRow() {
        return mRow;
    }
    public int getCol() {
        return mCol;
    }

    //Mutators
    public void buildStructure(Structure structure, Context context) throws StructureException
    {
        //Check if there is already a structure at this location
        if (mStructure != null) {
            throw new StructureException(context.getString(R.string.space_occupied_error));
        }

        //Check that the grid cell is one that can have structures built over it
        if (mBuildable) {
            GameData data = GameData.get();

            if (structure instanceof Road) {
                mStructure = structure;
                structure.build(context); //Update game values
            }
            else {
                //Check that there is a road adjacent to the desired build location
                if (roadCheck(mRow - 1, mCol) || roadCheck(mRow, mCol + 1) ||
                        roadCheck(mRow + 1, mCol) || roadCheck(mRow, mCol - 1)) {
                    structure.build(context); //Update game values
                    mStructure = structure;
                }
                else {
                    throw new StructureException(context.getString(R.string.no_road_error));
                }
            }
        }
        else {
            throw new StructureException(context.getString(R.string.not_buildable_error));
        }
    }
    public void removeStructure(Context context) throws StructureException {
        if (mStructure instanceof Road) {
            //Check if there are any adjacent non-road structures
            if (buildingCheck(mRow - 1, mCol) || buildingCheck(mRow, mCol + 1) ||
                    buildingCheck(mRow + 1, mCol) || buildingCheck(mRow, mCol - 1)) {
                throw new StructureException(context.getString(R.string.cannot_demolish_road_error));
            }
            else {
                mStructure.demolish(); //Update game values
                mStructure = null;
            }
        }
        else {
            if (mStructure != null) {
                mStructure.demolish(); //Update game values
                mStructure = null;
            }
        }
    }

    //Private Methods
    private boolean roadCheck(int row, int col) {
        boolean isRoad = false;
        //Check that the grid cell exists
        if ((row >= 0 && row < MapData.HEIGHT) && (col >= 0 && col < MapData.WIDTH)) {
            //Check that the grid cell has a road
            if (MapData.get().get(row, col).getStructure() instanceof Road) {
                isRoad = true;
            }
        }
        return isRoad;
    }
    private boolean buildingCheck(int row, int col) {
        boolean isBuilding = false;
        //Check that the grid cell exists
        if ((row >= 0 && row < MapData.HEIGHT) && (col >= 0 && col < MapData.WIDTH)) {
            //Check that the grid cell has a structure that isn't a road (i.e. a building)
            Structure structure = MapData.get().get(row, col).getStructure();
            if (structure != null && !(structure instanceof Road)) {
                isBuilding = true;
            }
        }
        return isBuilding;
    }
}
