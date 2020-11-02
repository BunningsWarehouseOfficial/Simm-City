package curtin.krados.simmcity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import curtin.krados.simmcity.model.GameData;
import curtin.krados.simmcity.model.Structure;
import curtin.krados.simmcity.model.StructureData;

public class SelectorFragment extends Fragment {
    //RecyclerView ViewHolder implementation
    private class SelectorViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mLabel;

        //Constructor
        public SelectorViewHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.list_selection, parent, false));

            //Retrieving references using itemView superclass field
            mImage = itemView.findViewById(R.id.image);
            mLabel = itemView.findViewById(R.id.label);
        }

        public void bind(final Structure structure) {
            //Binding values to the view
            mImage.setImageResource(structure.getDrawableId());
            mLabel.setText(structure.getLabel());

            //Implementing callback / event handler for selecting a structure
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameData.get().setSelectedStructure(structure);

                    //TODO Highlight the selected structure
                }
            });
        }
    }

    //RecyclerView Adapter implementation
    private class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder> {
        private StructureData mStructureData;

        //Constructor
        public SelectorAdapter(StructureData structureData) {
            this.mStructureData = structureData;
        }

        @Override
        public int getItemCount() {
            return mStructureData.size();
        }

        @Override
        public SelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity()); //Retrieve context from fragment
            return new SelectorViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(SelectorViewHolder vh, int index) {
            vh.bind(mStructureData.get(index));
        }
    }

    // ========== //

    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_selector, ui, false);

        //Setting up map RecyclerView
        rv = view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                GridLayoutManager.HORIZONTAL,
                false
        ));
        SelectorAdapter adapter = new SelectorAdapter(StructureData.get());
        rv.setAdapter(adapter);

        return view;
    }
}