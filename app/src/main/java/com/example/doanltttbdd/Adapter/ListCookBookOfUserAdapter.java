package com.example.doanltttbdd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanltttbdd.Activity.DetailsFoodActivity;
import com.example.doanltttbdd.R;
import com.example.doanltttbdd.model.Cookbook;
import com.example.doanltttbdd.utils.Utils;

import java.util.List;

public class ListCookBookOfUserAdapter extends RecyclerView.Adapter<ListCookBookOfUserAdapter.MyViewHolder> {
    private Context context;
    private List<Cookbook> items;
    TextView btnDetailFood;
    private Fragment EditCookBookFragment;

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String cookbookId);
    }

    public ListCookBookOfUserAdapter(Context context, List<Cookbook> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookcook_item_view, parent, false);
        btnDetailFood = view.findViewById(R.id.btn_detail_food);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCookBookOfUserAdapter.MyViewHolder holder, int position) {
        Cookbook cookbook = items.get(position);
        holder.name_food.setText(cookbook.getRecipeName());
        Log.d("cookbook.getCreatorId()", "User Name: " + cookbook.getCreatorId());
        Utils.getUserName(cookbook.getCreatorId(), new Utils.OnUserNameFetchedListener() {
            @Override
            public void onUserNameFetched(String userName) {
                if (userName != null) {
                    holder.name_user.setText(userName);
                } else {
                    Log.d("UserName", "User not found");
                }
            }

            @Override
            public void onCancelled(String errorMessage) {
                Log.e("FirebaseQuery", errorMessage);
            }
        });
        Glide.with(context).load(cookbook.getImage()).into(holder.image_food);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(cookbook.getKey());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_food, avatar;
        TextView name_user, name_food;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_food = itemView.findViewById(R.id.image_food);
            avatar = itemView.findViewById(R.id.avatar);
            name_user = itemView.findViewById(R.id.name_user);
            name_food = itemView.findViewById(R.id.name_food);
        }
    }
}

