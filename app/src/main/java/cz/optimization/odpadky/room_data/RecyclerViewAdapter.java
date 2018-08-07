package cz.optimization.odpadky.room_data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.optimization.odpadky.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<PlaceWatched> mPlacesList;
    private View.OnClickListener mClickListener;


    public RecyclerViewAdapter(List<PlaceWatched> placesWatchedList, View.OnClickListener clickListener) {
        mPlacesList = placesWatchedList;
        mClickListener = clickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_watched_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        PlaceWatched placeWatched = mPlacesList.get(position);

        holder.itemTextView.setText(placeWatched.getTitle());
        holder.itemView.setTag(placeWatched);
        holder.itemView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return
                mPlacesList.size();
    }

    public void addItems(List<PlaceWatched> placesWatchedList) {
        mPlacesList = placesWatchedList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTextView;

        RecyclerViewHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.place_watched_item_tv);

        }
    }
}