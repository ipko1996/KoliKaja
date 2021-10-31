package hu.bugs.kolikaja;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("69", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        Button btnBuy = (Button) root.findViewById(R.id.btnBuy);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createNotificationChannel();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "69")
                        .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent);
                        //.setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

// notificationId is a unique int for each notification that you must define
                notificationManager.notify((int) System.currentTimeMillis(), builder.build());

            }
        });

        name.setText(food.getName());
        Glide.with(getActivity()).load(food.getImageUrl()).into(ivFood);
        tvDescription.setText(food.getDescription());
        tvDate.setText(dateFormatter(food.getDate()));
        tvExpiration.setText(dateFormatter(food.getExpiration()));
        tvPlace.setText(food.getFoodLocation());
        tvUser.setText(food.getUserName());
        tvPrice.setText(food.getPrice() + " Ft");

        return root;
    }

    private String dateFormatter(Timestamp origin) {
        String stringDate = null;
        Locale locale = Locale.getDefault();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            stringDate = DateFormat
                    .getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale)
                    .format(origin.toDate());
        }
        return stringDate;
    }
}