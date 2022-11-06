package edu.northeastern.numad22fa_team12.stickItToEm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_team12.R;

public class StickerAdapter extends RecyclerView.Adapter<StickerViewHolder> {

    private final Context context;
    private final int[] stickersLocations;

    public StickerAdapter(Context context, int[] stickersLocations) {
        this.context = context;
        this.stickersLocations = stickersLocations;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_item_for_recycleview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        holder.stickerImage.setImageResource(stickersLocations[position]);
    }

    @Override
    public int getItemCount() {
        return stickersLocations.length;
    }
}
