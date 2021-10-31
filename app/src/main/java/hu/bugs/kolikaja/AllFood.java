package hu.bugs.kolikaja;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//TODO:Rotate bugfix at adapter
public class AllFood extends Fragment {

    private static final String TAG = "AllFood";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ref = db.collection("foods");
    private ListenerRegistration unsubscribe;
    private ArrayList<Food> foods;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public AllFood() {
        Log.i(TAG, "constructor");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreateView");

        if (getArguments() != null) {
            foods = getArguments().getParcelable("foods");
        } else {
            foods = new ArrayList<>();
        }

        unsubscribe = ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> docs = value.getDocuments();
                foods.clear();
                for (DocumentSnapshot document : docs) {
                    Map<String, Object> temp = document.getData();
                    Log.d(TAG, temp.toString());
                    Food food = new Food(temp);
                    foods.add(food);

                    String uId = FirebaseAuth.getInstance().getUid();
                    if (!food.getMessage().isEmpty() && uId.equals(uId)) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "69")
                                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                                .setContentTitle("New customer")
                                .setContentText(food.getMessage())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setOnlyAlertOnce(true)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify((int) food.getDate().getSeconds(), builder.build());
                    }

                    Log.d(TAG, new Food(temp).toString());
                }
                adapter.notifyDataSetChanged();
            }
        });

//        db.collection("foods")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_all_food,
                container,
                false);
        root.setTag("AllFood");
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CardAdapter(foods, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.i(TAG, "onCreateView");

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("foods", foods);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    public void onDestroy() {
        unsubscribe.remove();
        Log.d(TAG, "Unsubscribed from querySnapshot");
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}