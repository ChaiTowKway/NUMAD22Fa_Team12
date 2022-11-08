package edu.northeastern.numad22fa_team12.stickItToEm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.Sticker;
import edu.northeastern.numad22fa_team12.webservice.MakeupItemActivity;
import edu.northeastern.numad22fa_team12.webservice.ProductViewHolder;

public class StickerDetailAdapter extends RecyclerView.Adapter<StickerDetailViewHolder> {
    private final List<Sticker> stickerList;
    private String userName;
    private final Context context;
    private boolean isSent;


    public StickerDetailAdapter(List<Sticker> stickerList, Context context, String userName, boolean isSent) {
        this.stickerList = stickerList;
        this.userName = userName;
        this.isSent = isSent;
        this.context = context;
    }

    @NonNull
    @Override
    public StickerDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerDetailViewHolder holder, int position) {
        holder.image.setImageResource(stickerList.get(position).getStickerID());
        if (isSent) {
            // if sticker is sent by cur user
            holder.sentInfo.setText("Sent to " + stickerList.get(position).getReceivedByUser());
        } else {
            holder.sentInfo.setText("Sent by " + stickerList.get(position).getSentByUser());
        }
        holder.dateTime.setText(stickerList.get(position).getDateAndTime());
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

}

