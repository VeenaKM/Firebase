package com.architecturecomponents;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.click_count_text)
    TextView tvClickCount;

    ClickCounterViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(ClickCounterViewModel.class);
        displayClickCount(viewModel.getCount());

    }

    private void displayClickCount(int count) {
        tvClickCount.setText(String.valueOf(count));
    }

    @OnClick(R.id.increment_button)
    public void incrementClickCount(View view)
    {
        viewModel.setCount(viewModel.getCount()+1);
        displayClickCount(viewModel.getCount());

    }
}
