package hu.bugs.kolikaja;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        }else{
            foods = new ArrayList<>();
        }


        // Test data
        //foods.add(new Food("Test Kaja3", 450, Uri.parse("https://image-api.nosalty.hu/nosalty/images/recipes/qN/ou/klasszikus-toltott-kaposzta.jpeg?w=640&h=360&fit=crop&s=d644d0f2313e6641857f0a1169106ba7"), "Magi"));

        unsubscribe = ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> docs = value.getDocuments();
                foods.clear();
                for (DocumentSnapshot document : docs) {
                    Map<String, Object> temp = document.getData();
                    if (temp == null) throw new AssertionError();
                    foods.add(new Food(temp));
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