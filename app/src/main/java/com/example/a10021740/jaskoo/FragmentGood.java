package com.example.a10021740.jaskoo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.example.a10021740.jaskoo.FragmentDiet.cardList;

public class FragmentGood extends Fragment {

    public static RecyclerView goodRecyclerView;
    View view;

    public static List<Card> goodCardList = new ArrayList<Card>();
    public static FlatRecyclerViewAdapter goodAdapter;


    public FragmentGood() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.good_fragment,container,false);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goodRecyclerView = view.findViewById(R.id.goodrecyclerview);
        goodRecyclerView.setHasFixedSize(true);
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        goodRecyclerView.setNestedScrollingEnabled(true);


        goodAdapter = new FlatRecyclerViewAdapter(getContext(), goodCardList ); //maybe have to switch to mctx?
        goodRecyclerView.setAdapter(goodAdapter);

    }
}
