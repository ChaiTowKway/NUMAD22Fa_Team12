package edu.northeastern.numad22fa_team12.outfitToday;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;

public class WardrobeAdapter extends RecyclerView.Adapter<WardrobeAdapter.WardrobeHolder> {
    private Context context;
    private List<Outfit> outfit;

    public WardrobeAdapter(Context  context , List<Outfit> outfit){
        this.context = context;
        this.outfit = outfit;

    }
    @NonNull
    @Override
    public WardrobeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WardrobeHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return outfit.size();
    }

    class WardrobeHolder extends RecyclerView.ViewHolder{

        public WardrobeHolder(@NonNull View itemView) {
            super(itemView);
        }


        public void setDetails(Outfit outfit){

        }
    }
}
