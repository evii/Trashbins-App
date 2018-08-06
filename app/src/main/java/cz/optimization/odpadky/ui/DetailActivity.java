package cz.optimization.odpadky.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import cz.optimization.odpadky.R;
import cz.optimization.odpadky.SelectTrashbinTypeDialogFragment;
import cz.optimization.odpadky.room_data.RecyclerViewAdapter;

public class DetailActivity extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String placeId = intent.getStringExtra("placeId");
        String title = intent.getStringExtra("title");
        String containersList = intent.getStringExtra("containersList");

        TextView placeIdTv = findViewById(R.id.id_tv);
        TextView detailTv = findViewById(R.id.detail_tv);
        TextView listTv = findViewById(R.id.containers_tv);

        placeIdTv.setText(placeId);
        detailTv.setText(title);
        listTv.setText(containersList);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_watched) {


            String[] tvshows={"Crisis","Blindspot","BlackList","Game of Thrones","Gotham","Banshee"};
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.fragment_dialog_watched, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("List");

            RecyclerView rv = (RecyclerView) convertView.findViewById(R.id.recycler_view);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(tvshows, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(DetailActivity.this, "Click na dialog ok", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });


            rv.setAdapter(adapter);
            alertDialog.show();

          /*
            FragmentManager fm = getSupportFragmentManager();
            SelectWatchedPlaceDialogFragment dialog = new SelectWatchedPlaceDialogFragment();*/

            // Creating a bundle object to store the selected item's index
          //  Bundle b = new Bundle();

            // Storing the selected item's index in the bundle object
         //   b.putInt("position", position);

            // Setting the bundle object to the dialog fragment object
         //   dialog.setArguments(b);

            /*dialog.show(fm, "watched_dialog");*/


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
