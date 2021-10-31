package hu.bugs.kolikaja;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RemoveAdapter extends RecyclerView.Adapter<RemoveAdapter.CardViewHolder> {

    private ArrayList<Food> foods;
    private Context context;

    public RemoveAdapter(ArrayList<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_remove_card, parent, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Food temp = foods.get(position);
        holder.tvFoodName.setText(temp.getName());
        holder.tvFoodPrice.setText(String.valueOf(temp.getPrice()) + " Ft");
        holder.tvFoodLocation.setText(temp.getFoodLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CollectionReference ref = FirebaseFirestore.getInstance().collection("foods");
                                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(temp.getImageUrl().toString());
                                storageRef.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully
                                                Log.e("firebasestorage", "onSuccess: deleted file");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                                Log.e("firebasestorage", "onFailure: did not delete file");
                                            }
                                        });

                                ref.whereEqualTo("userId", temp.getUserId())
                                        .whereEqualTo("fileName", temp.getFileName())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<DocumentSnapshot> doc = queryDocumentSnapshots.getDocuments();
                                                String docId = doc.get(0).getId();
                                                FirebaseFirestore
                                                        .getInstance()
                                                        .collection("foods")
                                                        .document(docId)
                                                        .delete();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

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
