package hu.bugs.kolikaja;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodInfo extends Fragment {

    private static final String TAG = "FoodInfo";
    private static final String FOOD = "food";

    private Food food;

    public FoodInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param food food content.
     * @return A new instance of fragment FoodInfo.
     */

    public static FoodInfo newInstance(Food food) {
        FoodInfo fragment = new FoodInfo();
        Bundle args = new Bundle();
        args.putParcelable(FOOD, food);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));

        if (getArguments() != null) {
            food = getArguments().getParcelable(FOOD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        food = getArguments().getParcelable("data");
        Log.d(TAG,food.toString());
        return inflater.inflate(R.layout.fragment_food_info, container, false);
    }
}