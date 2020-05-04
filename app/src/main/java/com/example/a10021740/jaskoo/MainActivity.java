package com.example.a10021740.jaskoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.a10021740.jaskoo.FragmentBad.badAdapter;
import static com.example.a10021740.jaskoo.FragmentBad.badCardList;
import static com.example.a10021740.jaskoo.FragmentBad.badRecyclerView;
import static com.example.a10021740.jaskoo.FragmentDiet.cardList;
import static com.example.a10021740.jaskoo.FragmentDiet.dietAdapter;
import static com.example.a10021740.jaskoo.FragmentDiet.dietRecyclerView;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoriteCardsList;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoritesAdapter;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoritesRecyclerView;
import static com.example.a10021740.jaskoo.FragmentGood.goodAdapter;
import static com.example.a10021740.jaskoo.FragmentGood.goodCardList;
import static com.example.a10021740.jaskoo.FragmentGood.goodRecyclerView;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;

    public void saveTasksToSharedPrefs(Context context, List<Card> list, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list); //tasks is an ArrayList instance variable
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public List<Card> getTasksFromSharedPrefs(Context context, List<Card> list, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(key, "");
        list = gson.fromJson(json, new TypeToken<ArrayList<Card>>(){}.getType());
        return list;
    }



    @Override
    protected void onResume() {
        super.onResume();
        cardList = getTasksFromSharedPrefs(this, cardList, "cardList");
        if (cardList == null){
            cardList = new ArrayList<>();
        }
        badCardList = getTasksFromSharedPrefs(this, badCardList, "badCardList");
        if (badCardList == null){
            badCardList = new ArrayList<>();
        }
        goodCardList = getTasksFromSharedPrefs(this, goodCardList, "goodCardList");
        if (goodCardList == null){
            goodCardList = new ArrayList<>();
        }
        favoriteCardsList = getTasksFromSharedPrefs(this, favoriteCardsList, "favoriteCardsList");
        if (favoriteCardsList == null){
            favoriteCardsList = new ArrayList<>();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTasksToSharedPrefs(this, cardList, "cardList");
        saveTasksToSharedPrefs(this, goodCardList, "goodCardList");
        saveTasksToSharedPrefs(this, badCardList,"badCardList");
        saveTasksToSharedPrefs(this, favoriteCardsList,"favoriteCardsList");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);
        appBarLayout = findViewById(R.id.appbarlayout);
        viewPager = findViewById(R.id.viewpager);

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

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentCamera(), "camera");
        adapter.AddFragment(new FragmentAlgorithm(), "Algorithm");
        adapter.AddFragment(new FragmentDiet(), "diet");
        adapter.AddFragment(new FragmentFavorites(), "favorites");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                badRecyclerView.setAdapter(badAdapter);
                goodRecyclerView.setAdapter(goodAdapter);


                if (tab.getPosition() == 1){

                    double calories = 0;
                    double proteins = 0;
                    double carbs = 0;
                    double fats = 0;

                    Log.d("cardList", cardList+"");

                    if (cardList != null) {
                        for (int x = 0; x < cardList.size(); x++) {
                            calories += Double.parseDouble(cardList.get(x).getCalories());
                            proteins += Double.parseDouble(cardList.get(x).getProtein());
                            carbs += Double.parseDouble(cardList.get(x).getCarbs());
                            fats += Double.parseDouble(cardList.get(x).getFat());
                        }
                    }

                    Log.d("CALORIES", calories+"");

                    TextView calText = findViewById(R.id.calories);
                    TextView proText = findViewById(R.id.proteins);
                    TextView carbText = findViewById(R.id.carbs);
                    TextView fatText = findViewById(R.id.fats);

                    calText.setText("Calories: "+calories);
                    proText.setText("Proteins: "+proteins);
                    carbText.setText("Carbs: "+carbs);
                    fatText.setText("Fats: "+fats);

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
