package cz.optimization.odpadky.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.room_data.RecyclerViewAdapter;

public class SelectWatchedPlaceDialogFragment extends DialogFragment {


        String[] tvshows={"Crisis","Blindspot","BlackList","Game of Thrones","Gotham","Banshee"};
        RecyclerView recyclerView;
        RecyclerViewAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView=inflater.inflate(R.layout.fragment_dialog_watched, container);

            //RECYCER
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            //ADAPTER
            adapter = new RecyclerViewAdapter(tvshows, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getContext(), "Click na dialog ok", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            recyclerView.setAdapter(adapter);

            this.getDialog().setTitle("TV Shows");

            return rootView;
        }
    }








