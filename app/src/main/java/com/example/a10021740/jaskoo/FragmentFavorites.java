package com.example.a10021740.jaskoo;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.a10021740.jaskoo.FragmentDiet.cardList;

public class FragmentFavorites extends Fragment{
    public static RecyclerView favoritesRecyclerView;
    View view;

    public static List<Card> favoriteCardsList = new ArrayList<Card>();
    public static RecyclerViewAdapter favoritesAdapter;


    public FragmentFavorites() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorites_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("favorite", "favorites view created");

        favoritesRecyclerView = view.findViewById(R.id.favoriterecyclerview);
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesRecyclerView.setNestedScrollingEnabled(true);


        favoritesAdapter = new RecyclerViewAdapter(getContext(), favoriteCardsList ); //maybe have to switch to mctx?
        favoritesRecyclerView.setAdapter(favoritesAdapter);




    }
}
