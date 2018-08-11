package cz.optimization.odpadky.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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



    public DetailActivityRecyclerViewAdapter(List<Container> containersList) {
        mContainerList = containersList;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        Container container = mContainerList.get(position);

        String trashType = container.getTrashType();
        trashType = trashType.substring(0, 1).toUpperCase() + trashType.substring(1);
        trashType = trashType.replace("_", " ");
        holder.trashTypeTextView.setText(trashType);

        int progress = container.getProgress();
        String progressString = String.valueOf(progress) + " %";
        holder.percentageTextView.setText(progressString);
        if (progress > 90) {
            holder.percentageTextView.setTextColor(ContextCompat.getColor(holder.percentageTextView.getContext(), R.color.colorRed));
        } else if (progress < 50) {
            holder.percentageTextView.setTextColor(ContextCompat.getColor(holder.percentageTextView.getContext(), R.color.colorGreen));
        } else
            holder.percentageTextView.setTextColor(ContextCompat.getColor(holder.percentageTextView.getContext(), R.color.colorOrange));


        String cleaningString = container.getCleaning();
        int positionOfP = cleaningString.indexOf("P");
        String repeat = cleaningString.substring(1, positionOfP);
        Log.v("cleaning", repeat);
        String period = cleaningString.substring(positionOfP + 1);
        Log.v("cleaning", period);
        holder.cleaningTextView.setText(repeat + "x every " + period);

        String underground = container.getUnderground();
        if (underground.equals("true")) {
            holder.undergroundTextView.setText("underground");
        } else if (underground.equals("false")) {
            holder.undergroundTextView.setText("above-ground");
        } else {
            holder.undergroundTextView.setText("N/A");
        }



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
