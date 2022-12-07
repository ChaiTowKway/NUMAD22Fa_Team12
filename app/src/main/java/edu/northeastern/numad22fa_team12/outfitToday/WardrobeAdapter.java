package edu.northeastern.numad22fa_team12.outfitToday;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.CategoryEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Season;
import edu.northeastern.numad22fa_team12.outfitTodayModel.SeasonEnum;

public class WardrobeAdapter extends RecyclerView.Adapter<WardrobeAdapter.WardrobeHolder> {
    private Context context;
    private List<Outfit> outfit;
    private boolean viewMyWardrobe;

    public WardrobeAdapter(Context  context , List<Outfit> outfit, boolean viewMyWardrobe){
        this.context = context;
        this.outfit = outfit;
        this.viewMyWardrobe = viewMyWardrobe;
    }
    @NonNull
    @Override
    public WardrobeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.wardrobe_item_layout, parent,false);
       return new WardrobeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WardrobeHolder holder, int position) {
        holder.setDetails(outfit.get(position));
    }

    @Override
    public int getItemCount() {
        return outfit.size();
    }

    class WardrobeHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        Button editButton;
        public WardrobeHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.outfit_item_image_view);
            textView = itemView.findViewById(R.id.outfit_description_text);
            editButton = itemView.findViewById(R.id.outfit_item_edit_button);
            // if viewing other's wardrobe, won't have the edit button
            if (!viewMyWardrobe) editButton.setVisibility(View.GONE);
        }


        public void setDetails(Outfit outfit){
            String description = "";
            String occasion = OccasionEnum.values()[outfit.getOccasionId()].toString();
            String category = CategoryEnum.values()[outfit.getCategoryId()].toString();
            String season = SeasonEnum.values()[outfit.getSeasonId()].toString();
            System.out.println(outfit.toString());
            Picasso.get().load(outfit.getUrl()).into(imageView);
            description = String.format("Category: %s\nSeason: %s\nOccasion: %s",category,season,occasion);
            textView.setText(description);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context , EditOutfitActivity.class);
                    i.putExtra("outfit" , (Parcelable) outfit);
                    context.startActivity(i);

                }
            });
        }
    }
}
