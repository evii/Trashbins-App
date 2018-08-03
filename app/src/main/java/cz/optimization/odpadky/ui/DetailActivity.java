package cz.optimization.odpadky.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cz.optimization.odpadky.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String placeId = intent.getStringExtra("placeId");
        String containersList = intent.getStringExtra("containersList");

        TextView placeIdTv = findViewById(R.id.id_tv);
        TextView listTv = findViewById(R.id.detail_tv);

        placeIdTv.setText(placeId);
        listTv.setText(containersList);
    }
}
