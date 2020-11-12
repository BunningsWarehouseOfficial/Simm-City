package curtin.krados.simmcity.model;

import android.content.Context;

import curtin.krados.simmcity.StructureException;
import curtin.krados.simmcity.R;

public class Road extends Structure {
    //Constructor
    public Road(int drawableId, String label) {
        super(drawableId, label);
    }

    //Mutators
    @Override
    public void build(Context context) throws StructureException {
        GameData data = GameData.get();
        int newMoney = data.getMoney().getValue() - data.getSettings().getRoadBuildingCost();
        if (newMoney >= 0) {
            data.setMoney(newMoney);
        }
        else {
            throw new StructureException(context.getString(R.string.not_enough_money_error));
        }
    }
    @Override
    public void demolish() { }
}
