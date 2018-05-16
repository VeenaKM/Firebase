package com.vectoranimation.SharedElementTransition;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vectoranimation.R;
import com.vectoranimation.SharedElementTransition.ActivityA;
import com.vectoranimation.bounceAnim.BounceActivity;

public class AnimationChooserActivity extends AppCompatActivity{

    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_element_chooser);

        imageView = findViewById(R.id.simple_activity_a_imageView);


    }
    public void goToActivityA(View view) {

        Intent intent = new Intent(AnimationChooserActivity.this, ActivityA.class);
        ActivityOptionsCompat options= ActivityOptionsCompat.makeSceneTransitionAnimation(AnimationChooserActivity.this,imageView
                , ViewCompat.getTransitionName(imageView));
        startActivity(intent,options.toBundle());

    }
//    public void goToActivityB(View view) {
//        Intent intent = new Intent(AnimationChooserActivity.this, ActivityB.class);
//        startActivity(intent);
//
//    }


}
