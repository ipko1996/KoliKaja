package hu.bugs.kolikaja;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private ArrayList<Food> foods;
    private Context context;

    public CardAdapter(ArrayList<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card, parent, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Food temp = foods.get(position);
        holder.tvFoodName.setText(temp.getName());
        holder.tvFoodPrice.setText(String.valueOf(temp.getPrice()) + " Ft");
        Glide.with(context).load(temp.getImageUrl()).into(holder.ivFoodImage);
        holder.tvFoodLocation.setText(temp.getFoodLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FoodInfo foodInfo = new FoodInfo();
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", temp);
                foodInfo.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.slide_out
                        )
                        .replace(R.id.fragment_container, foodInfo)
                        .addToBackStack(null)
                        .commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFoodName;
        private TextView tvFoodPrice;
        private ImageView ivFoodImage;
        private TextView tvFoodLocation;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.card_food_name);
            tvFoodPrice = itemView.findViewById(R.id.card_price);
            ivFoodImage = itemView.findViewById(R.id.card_image);
            tvFoodLocation = itemView.findViewById(R.id.card_foodLocation);
        }
    }
}
