package hu.bugs.kolikaja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private ArrayList<Card> cards;
    private Context context;

    public CardAdapter(ArrayList<Card> cards, Context context) {
        this.cards = cards;
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
        Card temp = cards.get(position);

        holder.foodName.setText(temp.getFoodName());
        holder.foodPrice.setText(String.valueOf(temp.getFoodPrice()) + " Ft");
        Glide.with(context).load(temp.getFoodImage()).into(holder.foodImage);
        holder.foodLocation.setText(temp.getFoodLocation());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName;
        private TextView foodPrice;
        private ImageView foodImage;
        private TextView foodLocation;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.card_food_name);
            foodPrice = itemView.findViewById(R.id.card_price);
            foodImage = itemView.findViewById(R.id.card_image);
            foodLocation = itemView.findViewById(R.id.card_foodLocation);
        }
    }
}
