package curtin.krados.simmcity.model.Structure;

import android.content.Context;

import curtin.krados.simmcity.StructureException;

/**
 * Represents a possible structure to be placed on the map. A structure simply contains a drawable
 * int reference, and a string label to be shown in the selector.
 */
public abstract class Structure
{
    private final int drawableId;
    private String label;

    //Constructor
    public Structure(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label      = label;
    }

    //Accessors
    public int getDrawableId()
    {
        return drawableId;
    }
    public String getLabel()
    {
        return label;
    }
    abstract public String getString();

    //Mutators
    public void setLabel(String label) {
        this.label = label;
    }
    abstract public void build(Context context) throws StructureException;
    abstract public void demolish();
}