package com.example.home.baking_app.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.home.baking_app.JsonModels.Ingredient;
import com.example.home.baking_app.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {

    Context context;
    ArrayList<Ingredient> ingredients;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        this.context= context;
        this.ingredients = ingredients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ingredient_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.MyViewHolder holder, int position) {

        holder.textView1.setText(ingredients.get(position).getQuantity());
        holder.textView2.setText(ingredients.get(position).getMeasure());
        holder.textView3.setText(ingredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredients== null ?0 : ingredients.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.textView1= itemView.findViewById(R.id.quantity);
            this.textView2= itemView.findViewById(R.id.measure);
            this.textView3= itemView.findViewById(R.id.ingredient);

        }
    }

}
