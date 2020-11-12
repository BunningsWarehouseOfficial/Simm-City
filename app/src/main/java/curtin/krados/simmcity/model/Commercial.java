package curtin.krados.simmcity.model;

import android.content.Context;

import curtin.krados.simmcity.StructureException;
import curtin.krados.simmcity.R;

public class Commercial extends Structure {
    //Constructor
    public Commercial(int drawableId, String label) {
        super(drawableId, label);
    }

    //Mutators
    @Override
    public void build(Context context) throws StructureException {
        GameData data = GameData.get();
        int newMoney = data.getMoney().getValue() - data.getSettings().getCommBuildingCost();
        if (newMoney >= 0) {
            data.setMoney(newMoney);
            data.setNumCommercial(data.getNumCommercial().getValue() + 1);
        }
        else {
            throw new StructureException(context.getString(R.string.not_enough_money_error));
        }
    }
    @Override
    public void demolish() {
        GameData data = GameData.get();
        data.setNumCommercial(data.getNumCommercial().getValue() - 1);
    }
}
