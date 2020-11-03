package curtin.krados.simmcity;

import android.graphics.Color;
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
    //RecyclerView Adapter implementation
    private class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.SelectorViewHolder> {
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

                //Highlight the structure if it is selected, otherwise keep a white background
                if (structure.equals(GameData.get().getSelectedStructure())) {
                    itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
                }
                else {
                    itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                //Implementing callback / event handler for selecting a structure
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameData data = GameData.get();
                        data.setSelectedStructure(structure);

                        //Updating current selection to highlight background
                        SelectorAdapter.this.notifyItemChanged(getAdapterPosition());

                        //Updating previous selection to remove highlighted background
                        int prev = data.getPreviousStructureIndex();
                        SelectorAdapter.this.notifyItemChanged(prev);
                        data.setPreviousStructureIndex(getAdapterPosition());
                    }
                });
            }
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