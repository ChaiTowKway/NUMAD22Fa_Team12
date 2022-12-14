package edu.northeastern.numad22fa_team12.stickItToEm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.Sticker;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private static final String TAG = "StickerAdapter";
    private final Context context;
//    private final List<Sticker> stickersLocations;
    private final List<Integer> stickersLocations;
    private List<Integer> usedRecord;
    private OnStickerListener onStickerListener;
    private int mCheckedPosition;

    public StickerAdapter(Context context, List<Integer> stickersLocations, OnStickerListener onStickerListener, List<Integer> usedRecord) {
        this.context = context;
        this.stickersLocations = stickersLocations;
        this.onStickerListener = onStickerListener;
        this.usedRecord = usedRecord;
        mCheckedPosition = -1;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_item_for_recycleview, null));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item_for_recycleview, parent, false);
        return new StickerViewHolder(view, onStickerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.stickerImage.setImageResource(stickersLocations.get(position));
        String output = "Total sent: " + usedRecord.get(position);
        holder.totalUsed.setText(output);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return stickersLocations.size();
    }

    public interface OnStickerListener {
        void onStickerClick(int position);
    }

    public class StickerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView stickerImage;
        public LinearLayout stickerLayout;
        public TextView totalUsed;
        OnStickerListener onStickerListener;
        public StickerViewHolder(@NonNull View itemView, OnStickerListener onStickerListener) {
            super(itemView);
            this.stickerImage = itemView.findViewById(R.id.imageView_sticker);
            this.stickerLayout = itemView.findViewById(R.id.stickerLayout);
            this.totalUsed = itemView.findViewById(R.id.textView_numSent);
            this.onStickerListener = onStickerListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Log.d(TAG, "mCheckedPosition: " + mCheckedPosition);
            int position = getAdapterPosition();
            if (mCheckedPosition == position) {
                stickerLayout.setBackgroundColor(Color.WHITE);
                mCheckedPosition = -1;
            } else if (mCheckedPosition == -1) {
                mCheckedPosition = position;
                stickerLayout.setBackgroundColor(Color.CYAN);
                onStickerListener.onStickerClick(getAdapterPosition());
            } else {
                Toast.makeText(v.getContext(), "Need to first unselect current choice!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
