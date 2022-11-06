package edu.northeastern.numad22fa_team12.stickItToEm;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_team12.R;

public class StickerViewHolder extends RecyclerView.ViewHolder {
    public ImageView stickerImage;
    public TextView totalUsed;
    public StickerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.stickerImage = itemView.findViewById(R.id.imageView_sticker);
        this.totalUsed = itemView.findViewById(R.id.textView7);
    }
}
