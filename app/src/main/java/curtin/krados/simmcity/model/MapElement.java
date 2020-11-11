package curtin.krados.simmcity.model;

import android.content.Context;

import curtin.krados.simmcity.BuildStructureException;
import curtin.krados.simmcity.R;

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
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private Structure structure;

    //Constructor
    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure)
    {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
    }

    //Accessors
    public boolean isBuildable()
    {
        return buildable;
    }
    public int getNorthWest()
    {
        return terrainNorthWest;
    }
    public int getSouthWest()
    {
        return terrainSouthWest;
    }
    public int getNorthEast()
    {
        return terrainNorthEast;
    }
    public int getSouthEast()
    {
        return terrainSouthEast;
    }
    /**
     * Retrieves the structure built on this map element.
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return structure;
    }

    //Mutators
    public void setStructure(Structure structure, int row, int col, Context context) throws BuildStructureException
    {
        //Check that the grid cell is one that can have structures built over it
        if (buildable) {
            GameData data = GameData.get();

            if (structure instanceof Road) {
                this.structure = structure;
                structure.build(context); //Update game values
            }
            else {
                //Check that there is a road adjacent to the desired build location
                if (roadCheck(row - 1, col) || roadCheck(row, col + 1) ||
                        roadCheck(row + 1, col) || roadCheck(row, col - 1)) {
                    this.structure = structure;
                    structure.build(context); //Update game values
                }
                else {
                    throw new BuildStructureException(context.getString(R.string.no_road_error));
                }
            }
        }
        else {
            throw new BuildStructureException(context.getString(R.string.not_buildable_error));
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
}
