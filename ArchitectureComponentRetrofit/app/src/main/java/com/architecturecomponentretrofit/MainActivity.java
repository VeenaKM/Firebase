package com.architecturecomponentretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.architecturecomponentretrofit.adapter.MyAdapter;
import com.architecturecomponentretrofit.model.Datum;
import com.architecturecomponentretrofit.model.ModelInfo;
import com.architecturecomponentretrofit.repository.RetrofitRepository;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn_getItem;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;

    ArrayList<Datum> nameListArray;// = new ArrayList<String>();
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);
        nameListArray = new ArrayList<Datum>();

        RetrofitRepository.getEmpInfo();

        adapter = new MyAdapter(this,nameListArray);

        myRecyclerView.setAdapter(adapter);

        RetrofitRepository.getIntData().observe(this, ModelInfo ->{
            nameListArray.clear();
            nameListArray.addAll(ModelInfo.getData());
            adapter.notifyDataSetChanged();
        });

    }

    public void refresh(View view){

        RetrofitRepository.getEmpInfo();
        // adapter.notifyDataSetChanged();
    }
}
