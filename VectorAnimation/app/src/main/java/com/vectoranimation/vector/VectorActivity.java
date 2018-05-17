package com.vectoranimation.vector;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.ClipPathModel;
import com.sdsmdg.harjot.vectormaster.models.GroupModel;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.vectoranimation.R;

import java.util.Timer;
import java.util.TimerTask;

public class VectorActivity extends AppCompatActivity {


    private FloatingActionButton mFloatingActionButton;
    private boolean tick=true,isMenu=true ;
    ImageView ivMenu,ivCar;
    VectorMasterView hourglassView,search_backView,lightningView,rainView;
    int searchBackState = 0;
    PathModel searchCircle, stem, arrowUp, arrowDown;

    float stemTrimStart = 0;
    float stemTrimEnd = 0.185f;

    float circleTrimEnd = 1;
    float translationY = 0;
    float rotation = 0;
    int state = 0;
    float arrowHeadTopTrimEnd = 0;
    float arrowHeadBottomTrimEnd = 0;

    int count = 60;


    float trimStart = 0;
    float trimEnd = 0;
    int countLightning = 60;

    float rainTrimStart_C = 0;
    float rainTrimEnd_C = 0;
    int rainCount_C = 60;
    float rainTrimStart_L = 0;
    float rainTrimEnd_L = 0;
    int rainCount_L = 55;
    float rainTrimStart_R = 0;
    float rainTrimEnd_R = 0;
    int rainCount_R = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector);

        initViews();
        final VectorMasterView heartVector = (VectorMasterView) findViewById(R.id.heart_vector);
//        // find the correct path using name
        final PathModel outline = heartVector.getPathModelByName("outline");
        // set the stroke color
        outline.setStrokeColor(Color.parseColor("#ED4337"));
////
//// set the fill color (if fill color is not set or is TRANSPARENT, then no fill is drawn)
//        outline.setFillColor(Color.parseColor("#ED4337"));

        // set trim path start (values are given in fraction of length)
//        outline.setTrimPathStart(0.0f);
//        // set trim path end (values are given in fraction of length)
//        outline.setTrimPathEnd(1f);


        heartVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialize value animator and pass start and end color values
                final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, Color.parseColor("#ED4337"));
                valueAnimator.setDuration(1000);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // set fillcolor and update view
                        outline.setFillColor((Integer) valueAnimator.getAnimatedValue());
                        heartVector.update();
                    }
                });
                valueAnimator.start();
            }

        });

    }

    private void initViews() {
        ivMenu = findViewById(R.id.menu);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        hourglassView = findViewById(R.id.hourglassView);
        search_backView = findViewById(R.id.search_backView);
        lightningView = (VectorMasterView) findViewById(R.id.lightningView);
        rainView = (VectorMasterView) findViewById(R.id.rainView);
        ivCar= findViewById(R.id.carImage);

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
        mFloatingActionButton.setImageDrawable(drawable);
        drawable.start();
        tick = !tick;
    }
    public void animateHourglass(View view) {
        final GroupModel frame = hourglassView.getGroupModelByName("hourglass_frame");
        final GroupModel fillOutlines = hourglassView.getGroupModelByName("fill_outlines");
        final GroupModel fillOutlinesPivot = hourglassView.getGroupModelByName("fill_outlines_pivot");
        final GroupModel group_fill_path = hourglassView.getGroupModelByName("group_fill_path");

        ClipPathModel mask = hourglassView.getClipPathModelByName("mask_1");


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (state == 0) {	// Slide the clip-path down by changing translationY of parent group
                    translationY += 0.3f;
                    fillOutlinesPivot.setTranslateY(translationY);
                    group_fill_path.setTranslateY(-1 * translationY);
                    if (translationY >= -12) {
                        state = 1;
                    }
                } else if (state == 1) {	// Rotate the groups by 180 degress
                    rotation += 3f;
                    frame.setRotation(rotation);
                    fillOutlines.setRotation(rotation);
                    if (rotation == 180) {
                        state = 2;
                    }
                } else if (state == 2) {	// Slide the clip-path up by changing translationY of parent group
                    translationY -= 0.3f;
                    fillOutlinesPivot.setTranslateY(translationY);
                    group_fill_path.setTranslateY(-1 * translationY);
                    if (translationY <= -24) {
                        state = 3;
                    }
                } else if (state == 3) { 	// Rotate the groups by 180 degress
                    rotation += 3f;
                    frame.setRotation(rotation);
                    fillOutlines.setRotation(rotation);
                    if (rotation == 360) {
                        rotation = 0;
                        state = 0;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hourglassView.update();		// Update the view from the UI thread
                    }
                });
            }
        }, 500, 1000 / 60);
    }
    void animateSearchToBack() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                circleTrimEnd -= 1.0f / 20;
                stemTrimStart += 0.75f / 20;
                stemTrimEnd += (1 - 0.185f) / 20;
                arrowHeadBottomTrimEnd += 1.0f / 20;
                arrowHeadTopTrimEnd += 1.0f / 20;
                if (circleTrimEnd <= 0) {
                    searchBackState = 1;
                    cancel();
                }

                searchCircle.setTrimPathEnd(circleTrimEnd);
                stem.setTrimPathEnd(stemTrimEnd);
                stem.setTrimPathStart(stemTrimStart);
                arrowUp.setTrimPathEnd(arrowHeadTopTrimEnd);
                arrowDown.setTrimPathEnd(arrowHeadBottomTrimEnd);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        search_backView.update();
                    }
                });

            }
        }, 0, 1000 / 60);
    }

    void animateBackToSearch() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                arrowHeadBottomTrimEnd -= 1.0f / 20;
                arrowHeadTopTrimEnd -= 1.0f / 20;
                stemTrimStart -= 0.75f / 20;
                stemTrimEnd -= (1 - 0.185f) / 20;
                circleTrimEnd += 1.0f / 20;
                if (circleTrimEnd >= 1) {
                    searchBackState = 0;
                    cancel();
                }

                searchCircle.setTrimPathEnd(circleTrimEnd);
                stem.setTrimPathEnd(stemTrimEnd);
                stem.setTrimPathStart(stemTrimStart);
                arrowUp.setTrimPathEnd(arrowHeadTopTrimEnd);
                arrowDown.setTrimPathEnd(arrowHeadBottomTrimEnd);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        search_backView.update();
                    }
                });
            }
        }, 0, 1000 / 60);
    }

    public void animateSearch(View view) {
        searchCircle = search_backView.getPathModelByName("search_circle");
        stem = search_backView.getPathModelByName("stem");
        arrowUp = search_backView.getPathModelByName("arrow_head_top");
        arrowDown = search_backView.getPathModelByName("arrow_head_bottom");
      
                if (searchBackState == 0) {
                    animateSearchToBack();
                } else {
                    animateBackToSearch();
                }
    }
   public void animateRain(View view) {
        final PathModel rainLeft = rainView.getPathModelByName("rain_left");
        final PathModel rainCenter = rainView.getPathModelByName("rain_center");
        final PathModel rainRight = rainView.getPathModelByName("rain_right");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rainCount_C++;
                if (rainCount_C >= 60) {
                    if (rainTrimEnd_C < 1) {
                        rainTrimEnd_C += 0.04f;
                    } else if (rainTrimEnd_C >= 1 && rainTrimStart_C < 1) {
                        rainTrimStart_C += 0.04f;
                    } else if (rainTrimEnd_C >= 1 && rainTrimStart_C >= 1) {
                        rainTrimEnd_C = 0;
                        rainTrimStart_C = 0;
                        rainCount_C = 0;
                    }
                }
                rainCount_L++;
                if (rainCount_L >= 60) {
                    if (rainTrimEnd_L < 1) {
                        rainTrimEnd_L += 0.04f;
                    } else if (rainTrimEnd_L >= 1 && rainTrimStart_L < 1) {
                        rainTrimStart_L += 0.04f;
                    } else if (rainTrimEnd_L >= 1 && rainTrimStart_L >= 1) {
                        rainTrimEnd_L = 0;
                        rainTrimStart_L = 0;
                        rainCount_L = 0;
                    }
                }
                rainCount_R++;
                if (rainCount_R >= 60) {
                    if (rainTrimEnd_R < 1) {
                        rainTrimEnd_R += 0.04f;
                    } else if (rainTrimEnd_R >= 1 && rainTrimStart_R < 1) {
                        rainTrimStart_R += 0.04f;
                    } else if (rainTrimEnd_R >= 1 && rainTrimStart_R >= 1) {
                        rainTrimEnd_R = 0;
                        rainTrimStart_R = 0;
                        rainCount_R = 0;
                    }
                }

                rainCenter.setTrimPathEnd(rainTrimEnd_C);
                rainCenter.setTrimPathStart(rainTrimStart_C);

                rainLeft.setTrimPathEnd(rainTrimEnd_L);
                rainLeft.setTrimPathStart(rainTrimStart_L);

                rainRight.setTrimPathEnd(rainTrimEnd_R);
                rainRight.setTrimPathStart(rainTrimStart_R);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rainView.update();
                    }
                });

            }
        }, 500, 1000 / 60);

    }
   public void animateLightning(View v) {
        PathModel cloudModel = lightningView.getPathModelByName("cloud");
        cloudModel.setStrokeColor(Color.parseColor("#5D5D5D"));
        final PathModel lightningModel = lightningView.getPathModelByName("lightning");
        lightningModel.setStrokeColor(Color.parseColor("#FFD700"));
        lightningModel.setTrimPathEnd(0.0f);
        lightningModel.setTrimPathStart(0.0f);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countLightning++;
                if (countLightning >= 60) {
                    if (trimEnd < 1) {
                        trimEnd += 0.04f;
                    } else if (trimEnd >= 1 && trimStart < 1) {
                        trimStart += 0.04f;
                    } else if (trimEnd >= 1 && trimStart >= 1) {
                        trimEnd = 0;
                        trimStart = 0;
                        countLightning = 0;
                    }
                    lightningModel.setTrimPathEnd(trimEnd);
                    lightningModel.setTrimPathStart(trimStart);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lightningView.update();
                        }
                    });
                }
            }
        }, 500, 1000 / 60);
    }
    @SuppressLint("NewApi")
    public void animateCar(View view) {
     AnimatedVectorDrawable carDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.avd_car_animator);

        ivCar.setImageDrawable(carDrawable);
        carDrawable.start();
    }
}
