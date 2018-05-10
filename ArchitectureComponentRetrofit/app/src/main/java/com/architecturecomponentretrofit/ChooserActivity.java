package com.architecturecomponentretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.architecturecomponentretrofit.room.NewWordActivity;
import com.architecturecomponentretrofit.room.RoomActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooserActivity extends AppCompatActivity {
    @BindView(R.id.btn_retrofit)
    Button btnRetrofit;
    @BindView(R.id.btn_roomdb)
    Button btnRoomdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        ButterKnife.bind(this);

    }

    public void gotoRetrofit(View view) {
        Intent intent = new Intent(ChooserActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void gotoDatabase(View view) {
        Intent intent = new Intent(ChooserActivity.this, RoomActivity.class);
        startActivity(intent);
    }
}
