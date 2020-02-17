package customphonedialer.abror96.customphonedialer.Tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import customphonedialer.abror96.customphonedialer.Adapter.Interested_Adapter;
import customphonedialer.abror96.customphonedialer.Model.Interested_Model;
import customphonedialer.abror96.customphonedialer.R;
import customphonedialer.abror96.customphonedialer.helperClass.DatabaseHelper;

public class Interested extends Fragment {



    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
     List<Interested_Model> interested_models;



    public Interested() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_interested, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.intrested_Recycler);
        interested_models = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        interested_models = databaseHelper.getAllUser();
        Interested_Adapter interestedAdapter = new Interested_Adapter(getContext(), interested_models);
        recyclerView.setAdapter(interestedAdapter);

    return v;

    }

}
