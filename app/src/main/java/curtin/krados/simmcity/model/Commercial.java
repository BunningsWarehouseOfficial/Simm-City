package curtin.krados.simmcity.model;

import android.content.Context;

import curtin.krados.simmcity.BuildStructureException;
import curtin.krados.simmcity.R;

public class Commercial extends Structure {
    //Constructor
    public Commercial(int drawableId, String label) {
        super(drawableId, label);
    }

    //Mutators
    @Override
    public void build(Context context) throws BuildStructureException {
        GameData data = GameData.get();
        int newMoney = data.getMoney().getValue() - data.getSettings().getCommBuildingCost();
        if (newMoney >= 0) {
            data.setMoney(newMoney);
            data.setNumCommercial(data.getNumCommercial().getValue() + 1);
        }
        else {
            throw new BuildStructureException(context.getString(R.string.not_enough_money_error));
        }
    }
}
