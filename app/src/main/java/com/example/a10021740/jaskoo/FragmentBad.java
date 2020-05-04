package com.example.a10021740.jaskoo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.example.a10021740.jaskoo.FragmentDiet.cardList;
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchAPI;
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImageObject;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImagesModel;

public class FragmentBad extends Fragment {

    public static RecyclerView badRecyclerView;
    View view;

    public static List<Card> badCardList = new ArrayList<Card>();
    public static FlatRecyclerViewAdapter badAdapter;


    public FragmentBad() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bad_fragment,container,false);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("VIEW CREATED", "VIEW CREATED");

        badRecyclerView = view.findViewById(R.id.badrecyclerview);
        badRecyclerView.setHasFixedSize(true);
        badRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        badRecyclerView.setNestedScrollingEnabled(true);


        badAdapter = new FlatRecyclerViewAdapter(getContext(), badCardList ); //maybe have to switch to mctx?
        badRecyclerView.setAdapter(badAdapter);

    }
}
