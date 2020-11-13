package curtin.krados.simmcity.model.Map;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import curtin.krados.simmcity.model.GameSchema.MapTable;
import curtin.krados.simmcity.model.Structure.Structure;
import curtin.krados.simmcity.model.Structure.StructureData;

public class MapCursor extends CursorWrapper {
    public MapCursor(Cursor cursor) {
        super(cursor);
    }

    public MapElement getMapElement() {
        //Recreate the Structure object
        Structure structure = null;
        String label = getString(getColumnIndex(MapTable.Cols.LABEL));
        int drawableId = getInt(getColumnIndex(MapTable.Cols.DRAWABLE));
        if (label != null) {
            structure = StructureData.structureFactory(drawableId, label);
        }

        //Recreate the MapElement object
        Bitmap thumbnail = null;
        byte[] thumbnailBlob = getBlob(getColumnIndex(MapTable.Cols.THUMBNAIL));
        if (thumbnailBlob != null) {
            thumbnail = BitmapFactory.decodeByteArray(thumbnailBlob, 0, thumbnailBlob.length);
        }
        boolean buildable    = getColumnIndex(MapTable.Cols.BUILDABLE) == 1;
        int northWest        = getInt(getColumnIndex(MapTable.Cols.NW));
        int northEast        = getInt(getColumnIndex(MapTable.Cols.NE));
        int southWest        = getInt(getColumnIndex(MapTable.Cols.SW));
        int southEast        = getInt(getColumnIndex(MapTable.Cols.SE));

        int index = Integer.parseInt(getString(getColumnIndex(MapTable.Cols.ID)));
        int row = index % MapData.HEIGHT;
        int col = index / MapData.HEIGHT;
        return new MapElement(buildable, northWest, northEast, southWest, southEast, structure, thumbnail, row, col);
    }
}
