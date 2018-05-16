package com.vectoranimation.SharedElementTransition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.vectoranimation.R;

public class BounceTransition extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        imageView = findViewById(R.id.simple_activity_a_imageView);


    }

    public void goToActivityB(View view) {

        Intent intent = new Intent(BounceTransition.this, ActivityB.class);
        ActivityOptionsCompat options= ActivityOptionsCompat.makeSceneTransitionAnimation(BounceTransition.this,imageView
                , ViewCompat.getTransitionName(imageView));
        startActivity(intent,options.toBundle());

    }
}
