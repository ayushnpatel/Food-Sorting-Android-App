package com.example.a10021740.jaskoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.a10021740.jaskoo.FragmentBad.badAdapter;
import static com.example.a10021740.jaskoo.FragmentBad.badCardList;
import static com.example.a10021740.jaskoo.FragmentBad.badRecyclerView;
import static com.example.a10021740.jaskoo.FragmentGood.goodAdapter;
import static com.example.a10021740.jaskoo.FragmentGood.goodCardList;
import static com.example.a10021740.jaskoo.FragmentGood.goodRecyclerView;

public class FragmentDiet extends Fragment {

    View view;
    public static RecyclerView dietRecyclerView;

    public static List<Card> cardList = new ArrayList<Card>();
    public static RecyclerViewAdapter dietAdapter;
    Button button;
    String cardViewJSON;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.diet_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dietRecyclerView = view.findViewById(R.id.recyclerview);
        button = view.findViewById(R.id.button2);
        dietRecyclerView.setHasFixedSize(true);
        dietRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dietRecyclerView.setNestedScrollingEnabled(true);


        dietAdapter = new RecyclerViewAdapter(getContext(), cardList ); //maybe have to switch to mctx?
        dietRecyclerView.setAdapter(dietAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badCardList.clear();
                goodCardList.clear();
                cardList.clear();

                badRecyclerView.setAdapter(badAdapter);
                goodRecyclerView.setAdapter(goodAdapter);
                dietRecyclerView.setAdapter(dietAdapter);
            }
        });

    }
}
