package edu.northeastern.numad22fa_team12.stickItToEm;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_team12.R;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        this.userName = itemView.findViewById(R.id.userName);
    }
}
