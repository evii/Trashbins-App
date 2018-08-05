package cz.optimization.odpadky.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.support.design.widget.CollapsingToolbarLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import cz.optimization.odpadky.R;

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
}
