package com.vectoranimation.vector;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.vectoranimation.R;

public class VectorActivity extends AppCompatActivity {


    private ImageView ivTickCross;
    private boolean tick,isMenu=true ;
    ImageView ivMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector);

        initViews();
//        final VectorMasterView heartVector = (VectorMasterView) findViewById(R.id.heart_vector);
//        // find the correct path using name
//        final PathModel outline = heartVector.getPathModelByName("outline");
////        // set the stroke color
//        outline.setStrokeColor(Color.parseColor("#ED4337"));
////
//// set the fill color (if fill color is not set or is TRANSPARENT, then no fill is drawn)
//        outline.setFillColor(Color.parseColor("#ED4337"));

        // set trim path start (values are given in fraction of length)
//        outline.setTrimPathStart(0.0f);
//        // set trim path end (values are given in fraction of length)
//        outline.setTrimPathEnd(1f);


//        heartVector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // initialize value animator and pass start and end color values
//                final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, Color.parseColor("#ED4337"));
//                valueAnimator.setDuration(1000);
//
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        // set fillcolor and update view
//                        outline.setFillColor((Integer) valueAnimator.getAnimatedValue());
//                        heartVector.update();
//                    }
//                });
//                valueAnimator.start();
//            }
//
//        });

    }

    private void initViews() {
        ivMenu = findViewById(R.id.menu);
        ivTickCross = (ImageView) findViewById(R.id.tick_cross);
    }
    @SuppressLint("NewApi")
    public void animateMenu(View view) {

         AnimatedVectorDrawable menuDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.avd_menu);
        AnimatedVectorDrawable backDrawable = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_back);
        AnimatedVectorDrawable drawable = isMenu ? menuDrawable : backDrawable;

        ivMenu.setImageDrawable(drawable);
        drawable.start();
        isMenu = !isMenu;
    }

    @SuppressLint("NewApi")
    public void animate(View view) {

        AnimatedVectorDrawable tickToCross = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_tick_to_cross);
        AnimatedVectorDrawable crossToTick = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_cross_to_tick);
        AnimatedVectorDrawable drawable = tick ? tickToCross : crossToTick;
        ivTickCross.setImageDrawable(drawable);
        drawable.start();
        tick = !tick;
    }
}
