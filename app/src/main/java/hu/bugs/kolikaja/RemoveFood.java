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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoveFood extends Fragment {

    private static final String TAG = "RemoveFood";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ref = db.collection("foods");
    private ListenerRegistration unsubscribe;
    private ArrayList<Food> foods;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public RemoveFood() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreateView");

        foods = new ArrayList<>();

        String uId = FirebaseAuth.getInstance().getUid();

        unsubscribe = ref
                .whereEqualTo("userId",uId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

//        ref.whereEqualTo("userId", uId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                foods.add(new Food(document.getData()));
//                            }
//                            Log.d(TAG, foods.toString());
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remove_food, container, false);

        root.setTag("RemoveFood");
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RemoveAdapter(foods, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.i(TAG, "onCreateView");

        return root;
    }
}