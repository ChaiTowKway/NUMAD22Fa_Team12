package edu.northeastern.numad22fa_team12.stickItToEm;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_team12.R;

public class StickerDetailViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView sentInfo;
    public TextView dateTime;

    public StickerDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        this.image = itemView.findViewById(R.id.stickerDetailInfo);
        this.sentInfo = itemView.findViewById(R.id.sendInfo);
        this.dateTime = itemView.findViewById(R.id.timeInfo);
    }
}
