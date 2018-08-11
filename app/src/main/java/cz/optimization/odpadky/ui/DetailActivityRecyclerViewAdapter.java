package cz.optimization.odpadky.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.room_data.DialogRecyclerViewAdapter;
import cz.optimization.odpadky.room_data.PlaceWatched;


public class DetailActivityRecyclerViewAdapter extends RecyclerView.Adapter<DetailActivityRecyclerViewAdapter.RecyclerViewHolder> {
    private List<Container> mContainerList;
    private View.OnClickListener mClickListener;


    public DetailActivityRecyclerViewAdapter(List<Container> containersList, View.OnClickListener clickListener) {
        mContainerList = containersList;
        mClickListener = clickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Container container = mContainerList.get(position);

        holder.trashTypeTextView.setText(container.getTrashType());
        holder.percentageTextView.setText(String.valueOf(container.getProgress()));
        holder.cleaningTextView.setText(container.getCleaning());
        holder.undergroundTextView.setText(container.getUnderground());



        /*holder.itemView.setTag(placeWatched);
        holder.itemView.setOnClickListener(mClickListener);*/
    }

    @Override
    public int getItemCount() {
        return
                mContainerList.size();
    }

    public void addItems(List<Container> containersList) {
        mContainerList = containersList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView trashTypeTextView;
        private TextView percentageTextView;
        private TextView cleaningTextView;
        private TextView undergroundTextView;


        RecyclerViewHolder(View view) {
            super(view);
            trashTypeTextView = view.findViewById(R.id.trash_type_tv);
            percentageTextView = view.findViewById(R.id.percentage_tv);
            cleaningTextView = view.findViewById(R.id.cleaning_tv);
            undergroundTextView = view.findViewById(R.id.underground_tv);

        }
    }






}
