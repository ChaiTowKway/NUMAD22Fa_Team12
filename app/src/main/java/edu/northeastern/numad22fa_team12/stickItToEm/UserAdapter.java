package edu.northeastern.numad22fa_team12.stickItToEm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final Context context;
    private final List<User>  userList;
    OnUserListener onUserListener;
    private int mCheckedPosition = -1;

    public UserAdapter(Context context, List<User> userList, OnUserListener onUserListener) {
        this.context = context;
        this.userList = userList;
        this.onUserListener = onUserListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new UserViewHolder(view, onUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.userName.setText(userList.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnUserListener {
        void onUserClick(int position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView userName;
        public LinearLayout userLayout;

        public UserViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userName);
            this.userLayout = itemView.findViewById(R.id.userLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mCheckedPosition == position) {
                userLayout.setBackgroundColor(Color.WHITE);
                mCheckedPosition = -1;
            } else if (mCheckedPosition == -1) {
                mCheckedPosition = position;
                userLayout.setBackgroundColor(Color.CYAN);
                onUserListener.onUserClick(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }


}