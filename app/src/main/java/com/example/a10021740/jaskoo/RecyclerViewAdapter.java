package com.example.a10021740.jaskoo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.a10021740.jaskoo.FragmentBad.badAdapter;
import static com.example.a10021740.jaskoo.FragmentBad.badCardList;
import static com.example.a10021740.jaskoo.FragmentBad.badRecyclerView;
import static com.example.a10021740.jaskoo.FragmentDiet.dietAdapter;
import static com.example.a10021740.jaskoo.FragmentDiet.dietRecyclerView;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoriteCardsList;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoritesAdapter;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoritesRecyclerView;
import static com.example.a10021740.jaskoo.FragmentGood.goodAdapter;
import static com.example.a10021740.jaskoo.FragmentGood.goodCardList;
import static com.example.a10021740.jaskoo.FragmentGood.goodRecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {


    private Context mCtx;
    private List<Card> cardList;
    private Button button;


    public RecyclerViewAdapter (Context mCtx, List<Card> cardList){
        this.mCtx = mCtx;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardlayout,null);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int i) {
        final Card card = cardList.get(i);

        holder.calories.setText("Calories: "+card.getCalories());
        holder.protein.setText("Protein: "+card.getProtein());
        holder.carbs.setText("Carbs: "+card.getCarbs());
        holder.fats.setText("Fats: "+card.getFat());
        holder.productName.setText(card.getProductName());

        ImageView imageView = new ImageView(mCtx);
        Picasso.get().load(card.getImageURL()).into(imageView);

        holder.productImage.setImageDrawable(imageView.getDrawable());
//        favoritesRecyclerView.setAdapter(favoritesAdapter);
//        goodRecyclerView.setAdapter(goodAdapter);
//        badRecyclerView.setAdapter(badAdapter);
//        dietRecyclerView.setAdapter(dietAdapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favoriteCardsList.contains(card)){
                    favoriteCardsList.add(card);
                    Toast.makeText(mCtx, "Item added to favorites!", Toast.LENGTH_SHORT).show();
                    favoritesRecyclerView.setAdapter(favoritesAdapter);
                }else{
                    Toast.makeText(mCtx, "Item already in favorites!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (cardList != null)
            return cardList.size();
        else
            return 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView calories, protein, carbs, fats, productName;
        public RecyclerViewHolder(View itemView){
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            calories = itemView.findViewById(R.id.Calories);
            protein = itemView.findViewById(R.id.Protein);
            carbs = itemView.findViewById(R.id.Carbs);
            fats = itemView.findViewById(R.id.Fats);
            productName = itemView.findViewById(R.id.productName);
            button = itemView.findViewById(R.id.button);
        }
    }
}
