package com.makaryostudio.lavilo.feature.tutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makaryostudio.lavilo.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<Model> mListData;
    TutorialItemClickListener tutorialItemClickListener;
    private Context mContext;

    public Adapter(Context mContext, List<Model> mListData, TutorialItemClickListener tutorialItemClickListener) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.tutorialItemClickListener = tutorialItemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.a_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = mListData.get(position);

//        Uri uri = Uri.parse(model.getImage());

        Glide.with(mContext).load(model.getImage())
                .placeholder(R.drawable.ic_item_food_placeholder_orange_128dp)
                .into(holder.image);

        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        holder.stock.setText(model.getQuantity());

        holder.layout.setOnClickListener(v -> {

        });

        holder.delete.setOnClickListener(v -> tutorialItemClickListener.onDeleteListener(position));
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        ImageView image;
        TextView name;
        TextView price;
        TextView stock;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout_item_tutorial);
            name = itemView.findViewById(R.id.text_item_name_boi);
            image = itemView.findViewById(R.id.image_item_bo);
            price = itemView.findViewById(R.id.text_item_price_boi);
            stock = itemView.findViewById(R.id.text_item_quantity_boi);
            delete = itemView.findViewById(R.id.image_item_delete);
        }
    }
}
