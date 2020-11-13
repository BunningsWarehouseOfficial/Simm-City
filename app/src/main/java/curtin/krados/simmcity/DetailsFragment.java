package curtin.krados.simmcity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import curtin.krados.simmcity.model.GameData.GameData;
import curtin.krados.simmcity.model.Map.MapData;
import curtin.krados.simmcity.model.Map.MapElement;

public class DetailsFragment extends Fragment {
    private static final int REQUEST_THUMBNAIL = 1;
    private static final float THUMBNAIL_SCALE = (float)1.5;

    private MapElement mDetailsElement;

    private TextView mTypeText;
    private EditText mStructureNameInput;
    private TextView mCoordinatesValues;
    private Button mThumbnailButton;
    private ImageView mImage;
    private Button mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_details, ui, false);

        //Retrieving references
        mTypeText           = view.findViewById(R.id.typeText);
        mStructureNameInput = view.findViewById(R.id.structureNameInput);
        mCoordinatesValues  = view.findViewById(R.id.coordinatesValues);
        mThumbnailButton    = view.findViewById(R.id.thumbnailButton);
        mImage              = view.findViewById(R.id.structureImage);
        mBackButton         = view.findViewById(R.id.backButton);

        //Initialising values
        mDetailsElement = GameData.get().getDetailsElement();
        mTypeText          .setText(mDetailsElement.getStructure().getString());
        mStructureNameInput.setText(mDetailsElement.getStructure().getLabel());
        int row = mDetailsElement.getRow();
        int col = mDetailsElement.getCol();
        mCoordinatesValues.setText(getString(R.string.coordinates_value, row + 1, col + 1));
        Bitmap thumbnail = mDetailsElement.getThumbnail();
        if (thumbnail != null) { //Show the current thumbnail
            mImage.setImageBitmap(thumbnail);
            mImage.setScaleX(THUMBNAIL_SCALE);
            mImage.setScaleY(THUMBNAIL_SCALE);
        }
        else { //Show structure image if there is no thumbnail
            mImage.setImageResource(mDetailsElement.getStructure().getDrawableId());
            mImage.setScaleX(3);
            mImage.setScaleY(3);
        }

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
                Intent thumbnailPhotoIntent;
                thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL) {
            Bitmap newThumbnail = (Bitmap) resultIntent.getExtras().get("data");
            mImage.setImageBitmap(newThumbnail);
            mImage.setScaleX(THUMBNAIL_SCALE);
            mImage.setScaleY(THUMBNAIL_SCALE);

            mDetailsElement.setThumbnail(newThumbnail);
            MapData.get().edit(mDetailsElement);
        }
    }
}
