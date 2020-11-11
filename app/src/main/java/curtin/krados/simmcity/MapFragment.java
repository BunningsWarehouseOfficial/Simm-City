package curtin.krados.simmcity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import curtin.krados.simmcity.model.GameData;
import curtin.krados.simmcity.model.MapData;
import curtin.krados.simmcity.model.MapElement;
import curtin.krados.simmcity.model.Road;
import curtin.krados.simmcity.model.Structure;

public class MapFragment extends Fragment {
    //RecyclerView Adapter implementation
    private class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder> {
        private MapData mMapData;

        //Constructor
        public MapAdapter(MapData mapData) {
            this.mMapData = mapData;
        }

        @Override
        public int getItemCount() {
            return MapData.HEIGHT * MapData.WIDTH;
        }

        @Override
        public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity()); //Retrieve context from fragment
            return new MapViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(MapViewHolder vh, int index) {
            //Retrieving the position of the cell using the column-major order mapping
            int row = index % MapData.HEIGHT;
            int col = index / MapData.HEIGHT;
            vh.bind(mMapData.get(row, col));
        }

        //RecyclerView ViewHolder implementation
        private class MapViewHolder extends RecyclerView.ViewHolder {
            ImageView mNorthWestImage;
            ImageView mSouthWestImage;
            ImageView mNorthEastImage;
            ImageView mSouthEastImage;
            ImageView mStructureImage;

            MapElement mMapElement;

            //Constructor
            public MapViewHolder(LayoutInflater li, ViewGroup parent) {
                super(li.inflate(R.layout.grid_cell, parent, false));

                //Retrieving references using itemView superclass field
                mNorthEastImage = itemView.findViewById(R.id.northEast);
                mNorthWestImage = itemView.findViewById(R.id.northWest);
                mSouthEastImage = itemView.findViewById(R.id.southEast);
                mSouthWestImage = itemView.findViewById(R.id.southWest);
                mStructureImage = itemView.findViewById(R.id.topImageView);
                mMapElement = null;

                //Setting size of the cell view
                int size = parent.getMeasuredHeight() / MapData.HEIGHT + 1;
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.width = size;
                lp.height = size;
            }

            public void bind(MapElement mapElement) {
                //Binding values to the view
                mNorthEastImage.setImageResource(mapElement.getNorthEast());
                mNorthWestImage.setImageResource(mapElement.getNorthWest());
                mSouthEastImage.setImageResource(mapElement.getSouthEast());
                mSouthWestImage.setImageResource(mapElement.getSouthWest());
                Structure elementStructure = mapElement.getStructure();
                if (elementStructure != null) {
                    mStructureImage.setVisibility(View.VISIBLE);
                    mStructureImage.setImageResource(elementStructure.getDrawableId());
                }
                else {
                    mStructureImage.setVisibility(View.INVISIBLE);
                }
                mMapElement = mapElement;

                //Implementing callback / event handler for building currently selected structure
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index, row, col;

                        //Retrieving the position of the cell using the column-major order mapping
                        index = getAdapterPosition();
                        row = index % MapData.HEIGHT;
                        col = index / MapData.HEIGHT;
                        Structure selected = GameData.get().getSelectedStructure();

                        if (selected != null) {
                            try {
                                mMapElement.setStructure(selected, row, col, getContext());

                                //Update the adapter
                                MapAdapter.this.notifyItemChanged(index);
                            }
                            catch (BuildStructureException e) {
                                Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 250);
                                toast.show(); //TODO Test y offsets on other devices for consistency
                            }
                        }
                    }
                });
            }
        }
    }

    // ========== //

    private RecyclerView mRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_map, ui, false);

        //Setting up map RecyclerView
        mRv = view.findViewById(R.id.mapRecyclerView);
        mRv.setLayoutManager(new GridLayoutManager(
                getActivity(),
                MapData.HEIGHT,
                GridLayoutManager.HORIZONTAL,
                false
        ));
        MapAdapter adapter = new MapAdapter(MapData.get());
        mRv.setAdapter(adapter);

        return view;
    }
}
