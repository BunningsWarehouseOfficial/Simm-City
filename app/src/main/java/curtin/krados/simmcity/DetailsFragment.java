package curtin.krados.simmcity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import curtin.krados.simmcity.model.GameData;
import curtin.krados.simmcity.model.MapElement;

public class DetailsFragment extends Fragment {
    private MapElement mDetailsElement;

    private TextView mTypeText;
    private EditText mStructureNameInput;
    private TextView mCoordinatesValues;
    private Button mThumbnailButton;
    private Button mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_details, ui, false);

        //Retrieving references
        mTypeText           = view.findViewById(R.id.typeText);
        mStructureNameInput = view.findViewById(R.id.structureNameInput);
        mCoordinatesValues  = view.findViewById(R.id.coordinatesValues);
        mThumbnailButton    = view.findViewById(R.id.thumbnailButton);
        mBackButton         = view.findViewById(R.id.backButton);

        //Initialising values
        mDetailsElement = GameData.get().getDetailsElement();
        mTypeText          .setText(mDetailsElement.getStructure().getString());
        mStructureNameInput.setText(mDetailsElement.getStructure().getLabel());
        int row = mDetailsElement.getRow();
        int col = mDetailsElement.getCol();
        mCoordinatesValues.setText(getString(R.string.coordinates_value, row, col));

        //Implementing callbacks / event handlers
        mStructureNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                if (value.equals("")) { //Show the user a simple error message
                    mStructureNameInput.setError(getString(R.string.city_name_error));
                }
                else {
                    mDetailsElement.getStructure().setLabel(value);
                    mStructureNameInput.setHint(value);
                }
            }
        });
        mThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Open camera
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity) getActivity()).swapToMap();
            }
        });

        return view;
    }
}
