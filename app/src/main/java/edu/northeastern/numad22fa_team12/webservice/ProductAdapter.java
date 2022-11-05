package edu.northeastern.numad22fa_team12.webservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private final List<Product> productList;
    private final Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getName());
        ArrayList<String> allProductInfo = productList.get(position).getALlInfo();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MakeupItemActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("allProductInfo", allProductInfo);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

}
