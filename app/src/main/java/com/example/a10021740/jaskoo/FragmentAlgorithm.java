package com.example.a10021740.jaskoo;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.a10021740.jaskoo.FragmentBad.badAdapter;
import static com.example.a10021740.jaskoo.FragmentBad.badRecyclerView;
import static com.example.a10021740.jaskoo.FragmentDiet.cardList;
import static com.example.a10021740.jaskoo.FragmentGood.goodAdapter;
import static com.example.a10021740.jaskoo.FragmentGood.goodRecyclerView;

public class FragmentAlgorithm extends Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    public FragmentAlgorithm() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.algorithm_fragment,container,false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("algorithm", "algorithm view created");
        viewPager = view.findViewById(R.id.goodbadviewpager);
        tabLayout = view.findViewById(R.id.goodbadtablayout);

        tabLayout.setupWithViewPager(viewPager);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout)root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorPrimaryDark));
            drawable.setSize(4, 3);

            ((LinearLayout)root).setDividerPadding(10);
            ((LinearLayout)root).setDividerDrawable(drawable);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new FragmentGood(), "Good");
        adapter.AddFragment(new FragmentBad(), "Bad");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




    }
}