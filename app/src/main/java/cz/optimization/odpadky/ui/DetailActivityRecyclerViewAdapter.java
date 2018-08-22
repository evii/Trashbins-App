package cz.optimization.odpadky.ui;

import android.content.Context;
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


public class DetailActivityRecyclerViewAdapter extends RecyclerView.Adapter<DetailActivityRecyclerViewAdapter.RecyclerViewHolder> {
    private List<Container> mContainerList;
    private Context mContext;

    public DetailActivityRecyclerViewAdapter(List<Container> containersList, Context context) {
        mContainerList = containersList;
        mContext = context;
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
        String cleaningFinal = repeat + mContext.getResources().getString(R.string.Xevery) + period;
        holder.cleaningTextView.setText(cleaningFinal);

        String underground = container.getUnderground();
        if (underground.equals("true")) {
            holder.undergroundTextView.setText(R.string.underground);
        } else if (underground.equals("false")) {
            holder.undergroundTextView.setText(R.string.aboveground);
        } else {
            holder.undergroundTextView.setText(R.string.NA);
        }
    }

    @Override
    public int getItemCount() {
        return
                mContainerList.size();
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
