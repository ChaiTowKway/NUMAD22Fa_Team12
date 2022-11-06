package edu.northeastern.numad22fa_team12.stickItToEm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.webservice.MakeupItemActivity;

public class StickerAdapter extends RecyclerView.Adapter<StickerViewHolder> {
    private static final String TAG = "StickerAdapter";
    private final Context context;
    private final int[] stickersLocations;
    private ImageView imageView_test;

    public StickerAdapter(Context context, int[] stickersLocations, ImageView imageViewTest) {
        this.context = context;
        this.stickersLocations = stickersLocations;
        this.imageView_test = imageViewTest;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_item_for_recycleview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.stickerImage.setImageResource(stickersLocations[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                int id = stickersLocations[position];
                imageView_test.setImageDrawable(context.getDrawable(id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickersLocations.length;
    }
}
