package edu.northeastern.numad22fa_team12.stickItToEm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.Sticker;

public class StickerAdapter extends RecyclerView.Adapter<StickerViewHolder> {
    private static final String TAG = "StickerAdapter";
    private final Context context;
//    private final List<Sticker> stickersLocations;
    private final List<Integer> stickersLocations;

    public StickerAdapter(Context context, List<Integer> stickersLocations) {
        this.context = context;
        this.stickersLocations = stickersLocations;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_item_for_recycleview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.stickerImage.setImageResource(Integer.parseInt(stickersLocations.get(position).getStickerID()));
//        holder.totalUsed.setText(stickersLocations.get(position).getTotalUse());
        holder.stickerImage.setImageResource(stickersLocations.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StickItToEmActivity.class);
                Bundle b = new Bundle();
//                b.putString("stickerid", stickersLocations.get(position));
                b.putInt("stickerid", stickersLocations.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickersLocations.size();
    }
}
