package edu.northeastern.numad22fa_team12.webservice;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_team12.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView productName;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        this.productName = itemView.findViewById(R.id.productName);
    }
}
