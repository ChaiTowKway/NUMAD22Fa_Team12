package edu.northeastern.numad22fa_team12.stickItToEm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.User;
import edu.northeastern.numad22fa_team12.webservice.MakeupItemActivity;
import edu.northeastern.numad22fa_team12.webservice.ProductViewHolder;

public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private final Context context;
    private final List<User>  userList;


    public FriendAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendViewHolder(LayoutInflater.from(context).inflate(R.layout.item_users, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
