package com.vectoranimation.TransitionChooser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;
import com.vectoranimation.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShuffleActivity extends AppCompatActivity {

    LayoutInflater inflater;
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shuffle);

        titles.add("item 1");
        titles.add("item 2");
        titles.add("item 3");
        titles.add("item 4");
        titles.add("item 5");
        titles.add("item 6");

        final ViewGroup layout = (ViewGroup) findViewById(R.id.shuffle_container);
        ListView listView = (ListView) layout.findViewById(R.id.list_view);

        final MyArrayAdapter adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_2, titles);
        listView.setAdapter(adapter);

        final Button button = (Button) layout.findViewById(R.id.shuffle_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(layout, new ChangeBounds());
                Collections.shuffle(titles);
                adapter.notifyDataSetChanged();

            }
        });

    }



    public static class MyArrayAdapter extends ArrayAdapter<String> {

        List<String> titles ;
        Context mContext;


        MyArrayAdapter(Context context, int resource, List<String> titles) {
            super(context, resource,titles);

            mContext = context;
            this.titles = titles;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            }
            ((TextView) view.findViewById(android.R.id.text1)).setText(titles.get(position));
            TransitionManager.setTransitionName( view, titles.get(position));


            return view;
        }

    }
}