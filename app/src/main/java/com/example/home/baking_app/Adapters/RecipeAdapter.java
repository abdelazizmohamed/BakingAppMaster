package com.example.home.baking_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.home.baking_app.Acivities.DetailsActivity;
import com.example.home.baking_app.Fragments.StepsFragment;
import com.example.home.baking_app.JsonModels.Recipe;
import com.example.home.baking_app.JsonModels.Step;
import com.example.home.baking_app.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder>{

    FragmentActivity fragmentActivity;
    ArrayList<Recipe> recipes;

    public RecipeAdapter(FragmentActivity context, ArrayList<Recipe> recipes) {
        this.fragmentActivity = context;
        this.recipes =  recipes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(fragmentActivity)
                .inflate(R.layout.main_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(!recipes.get(position).getImage().equals("")) {
                Picasso.with(fragmentActivity)
                        .load(R.drawable.capture)
                        .into(holder.imageView);

        }
        holder.textView.setText(recipes.get(position).getName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               Recipe recipe = recipes.get(position);
               ArrayList<Step> steps = recipes.get(position).getSteps();
               int id = recipes.get(position).getSteps().get(position).getId();

               Intent intent = new Intent(fragmentActivity, DetailsActivity.class);

               Bundle bundle = new Bundle();
               bundle.putParcelable("recipe", recipe);
               bundle.putParcelableArrayList("steps", steps);
               bundle.putInt("id", id);
               intent.putExtras(bundle);

               fragmentActivity.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return recipes== null ?0 :recipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.image);
            this.textView= itemView.findViewById(R.id.recipe_name);

        }

        @Override
        public void onClick(View v) {


        }
    }
}
