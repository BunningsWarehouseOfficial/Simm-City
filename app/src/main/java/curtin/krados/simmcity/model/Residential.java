package curtin.krados.simmcity.model;

import android.content.Context;

import curtin.krados.simmcity.StructureException;
import curtin.krados.simmcity.R;

public class Residential extends Structure {
    //Constructor
    public Residential(int drawableId, String label) {
        super(drawableId, label);
    }

    //Accessors
    @Override
    public String getString() {
        return "Residential";
    }

    //Mutators
    @Override
    public void build(Context context) throws StructureException {
        GameData data = GameData.get();
        int newMoney = data.getMoney().getValue() - data.getSettings().getHouseBuildingCost();
        if (newMoney >= 0) {
            data.setMoney(newMoney);
            data.setNumResidential(data.getNumResidential().getValue() + 1);
        }
        else {
            throw new StructureException(context.getString(R.string.not_enough_money_error));
        }
    }
    @Override
    public void demolish() {
        GameData data = GameData.get();
        data.setNumResidential(data.getNumResidential().getValue() - 1);
    }
}
