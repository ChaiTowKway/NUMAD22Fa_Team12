package edu.northeastern.numad22fa_team12.outfitToday.occasions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_team12.R;

public class MyOccasionsAdaptor extends RecyclerView.Adapter<MyOccasionsAdaptor.MyOccasionsViewHolder> {

    private static final String TAG = "MyOccasionsAdaptor";
    private final Context context;
    private final List<String> dateOfWeekList;
    private final List<String> occasionsList;
    private OnOccasionsListener onOccasionListener;

    public MyOccasionsAdaptor(Context context, List<String> dateOfWeekList, List<String> occasionsList, OnOccasionsListener onOccasionsListener) {
        this.context = context;
        this.dateOfWeekList = dateOfWeekList;
        this.occasionsList = occasionsList;
        this.onOccasionListener = onOccasionsListener;
    }

    @NonNull
    @Override
    public MyOccasionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_item_for_recycleview, null));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.occasion_layout, parent, false);
        return new MyOccasionsViewHolder(view, onOccasionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOccasionsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.dateOfWeek.setText(dateOfWeekList.get(position));
        holder.occasion.setText(occasionsList.get(position));
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
        return dateOfWeekList.size();
    }

    public interface OnOccasionsListener {
        void onOccasionClick(int position);
    }

    public class MyOccasionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout occasionLayout;
        public TextView dateOfWeek;
        public TextView occasion;
        OnOccasionsListener onOccasionsListener;
        public MyOccasionsViewHolder(@NonNull View itemView, MyOccasionsAdaptor.OnOccasionsListener onOccasionsListener) {
            super(itemView);
            this.dateOfWeek = itemView.findViewById(R.id.textView_dayOfWeek);
            this.occasion = itemView.findViewById(R.id.textView_Occasions);
            this.onOccasionsListener = onOccasionsListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
