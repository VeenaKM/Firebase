package com.vectoranimation.TransitionChooser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;
import com.vectoranimation.R;

public class PathMotionActivity extends AppCompatActivity {

    boolean visible;
    private boolean isReturnAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.path_motion_activity);


        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.path_transitions_container);
         final Button button = (Button) transitionsContainer.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer,
                        new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
                params.gravity = isReturnAnimation ? (Gravity.LEFT | Gravity.TOP) :
                        (Gravity.BOTTOM | Gravity.RIGHT);
                isReturnAnimation=!isReturnAnimation;
                button.setLayoutParams(params);
            }
        });
    }
}