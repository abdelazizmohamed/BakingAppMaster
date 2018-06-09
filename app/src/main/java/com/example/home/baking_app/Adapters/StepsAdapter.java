package com.example.home.baking_app.Adapters;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.home.baking_app.Acivities.DetailsActivity;
import com.example.home.baking_app.Fragments.StepsFragment;
import com.example.home.baking_app.R;
import com.example.home.baking_app.JsonModels.Step;

import java.util.ArrayList;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    FragmentActivity fragmentActivity;

    ArrayList<Step> steps;

  //  GetAPI getAPI;

    public StepsAdapter(FragmentActivity context, ArrayList<Step> steps) {

        this.fragmentActivity=context;
        this.steps = steps;
      //  this.getAPI=(GetAPI)context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragmentActivity)
                .inflate(R.layout.step_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.MyViewHolder holder, int position) {
        holder.tvDesc.setText(steps.get(position).getShortDescription());

        holder.tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent=new Intent(fragmentActivity, DetailsActivity.class);
                intent.putExtra("ASS",1234);
                intent.putExtra("iid",steps.get(position).getId());
                intent.putExtra("ssteps",steps);
                fragmentActivity.startActivity(intent);
                */

                if((fragmentActivity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
                        ||(fragmentActivity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                {
                    StepsFragment stepsFragment = new StepsFragment();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("steps", steps);
                    args.putInt("id", steps.get(position).getId());
                    stepsFragment.setArguments(args);
                    FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame2, stepsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {

                    StepsFragment stepsFragment = new StepsFragment();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("steps", steps);
                    args.putInt("id", steps.get(position).getId());
                    stepsFragment.setArguments(args);
                    FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame0, stepsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps== null ?0 :steps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder   {

        private TextView tvDesc;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.tvDesc= itemView.findViewById(R.id.step);

        }


    }

}
