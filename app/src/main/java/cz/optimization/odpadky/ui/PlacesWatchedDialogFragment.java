package cz.optimization.odpadky.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.optimization.odpadky.R;
import cz.optimization.odpadky.room_data.DialogRecyclerViewAdapter;
import cz.optimization.odpadky.room_data.PlaceWatched;
import cz.optimization.odpadky.room_data.PlacesWatchedViewModel;


public class PlacesWatchedDialogFragment extends DialogFragment implements View.OnClickListener {

    private RecyclerView mDialogRecyclerView;
    private PlacesWatchedViewModel viewModel;
    private DialogRecyclerViewAdapter recyclerViewAdapter;

    private NoticeDialogListener mListener;

    private static final String placeIdDialogKey = "placeIdDialogKey";

    public PlacesWatchedDialogFragment() {
    }

    public static PlacesWatchedDialogFragment newInstance(String placeId) {
        PlacesWatchedDialogFragment frag = new PlacesWatchedDialogFragment();
        Bundle args = new Bundle();
        args.putString(placeIdDialogKey, placeId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_watched, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDialogRecyclerView = view.findViewById(R.id.recycler_view);
        mDialogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewAdapter = new DialogRecyclerViewAdapter(new ArrayList<PlaceWatched>(), this);
        viewModel = ViewModelProviders.of(this).get(PlacesWatchedViewModel.class);
        viewModel.getPlaceWatchedList().observe(getActivity(), new Observer<List<PlaceWatched>>() {
            @Override
            public void onChanged(@Nullable List<PlaceWatched> watchList) {
                recyclerViewAdapter.addItems(watchList);
            }
        });

        mDialogRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onClick(View view) {
        PlaceWatched selectedPlaceWatched = (PlaceWatched) view.getTag();
        mListener.onDialogClick(selectedPlaceWatched);
        mListener.onDialogClickCheck(selectedPlaceWatched);
        getDialog().dismiss();
    }

    // interface to send placeId to DetailActivity
    public interface NoticeDialogListener {

        void onDialogClick(PlaceWatched placeWatched);

        void onDialogClickCheck(PlaceWatched placeWatched);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}