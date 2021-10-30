package hu.bugs.kolikaja;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;

import java.util.Locale;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FOOD, food);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        food = getArguments().getParcelable("data");
        Log.d(TAG, food.toString());

        View root = inflater.inflate(R.layout.fragment_food_info, container, false);
        TextView name = (TextView) root.findViewById(R.id.tvFoodName);
        ImageView ivFood = (ImageView) root.findViewById(R.id.ivFood);
        TextView tvDescription = (TextView) root.findViewById(R.id.tvDescription);
        TextView tvDate = (TextView) root.findViewById(R.id.tvDate);
        TextView tvPlace = (TextView) root.findViewById(R.id.tvPlace);
        TextView tvUser = (TextView) root.findViewById(R.id.tvUser);
        TextView tvPrice = (TextView) root.findViewById(R.id.tvPrice);
        TextView tvExpiration = (TextView) root.findViewById(R.id.tvExpiration);

        name.setText(food.getName());
        Glide.with(getActivity()).load(food.getImageUrl()).into(ivFood);
        tvDescription.setText(food.getDescription());
        tvDate.setText(dateFormatter(food.getDate()));
        tvExpiration.setText(dateFormatter(food.getExpiration()));
        tvPlace.setText(food.getFoodLocation());
        tvUser.setText(food.getUserId());
        tvPrice.setText(food.getPrice() + " Ft");

        return root;
    }

    private String dateFormatter(Timestamp origin){
        String stringDate = null;
        Locale locale = Locale.getDefault();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            stringDate = DateFormat
                    .getDateTimeInstance(DateFormat.LONG,DateFormat.MEDIUM, locale)
                    .format(origin.toDate());
        }
        return stringDate;
    }
}