package hu.bugs.kolikaja;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFood extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AddFood";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView ivFoodPic;
    ActivityResultLauncher<Intent> getCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Bundle extras = intent.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ivFoodPic.setImageBitmap(imageBitmap);
                    }
                }
            });
    ActivityResultLauncher<Intent> getGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        final Uri selectedImage = intent.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            ivFoodPic.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            Log.i(TAG, e.toString());
                        }
                    } else {
                        Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                    }
                }
            });
    private Calendar cal;
    private TextView etFoodName, etPlace, etPrice, etDescription;
    private ProgressBar pbUpload;


    public AddFood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFood.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFood newInstance(String param1, String param2) {
        AddFood fragment = new AddFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_food, container, false);
        Button btnDatePicker = (Button) root.findViewById(R.id.btnDatePick);
        Button btnCamera = root.findViewById(R.id.btnCamera);
        Button btnGallery = root.findViewById(R.id.btnGallery);
        Button btnPublish = root.findViewById(R.id.btnPublish);
        ivFoodPic = (ImageView) root.findViewById(R.id.ivFoodPic);
        etFoodName = (EditText) root.findViewById(R.id.etFoodName);
        etPlace = (EditText) root.findViewById(R.id.etPlace);
        etPrice = (EditText) root.findViewById(R.id.etPrice);
        etDescription = (EditText) root.findViewById(R.id.etDescription);
        pbUpload = (ProgressBar) root.findViewById(R.id.pbUpload);

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etFoodName.getText().toString().isEmpty() ||
                        etPlace.getText().toString().isEmpty() ||
                        cal == null ||
                        etPrice.getText().toString().isEmpty() ||
                        ivFoodPic.getDrawable() == null
                ) {
                    Toast.makeText(getContext(), "Only description can be empty", Toast.LENGTH_SHORT).show();
                } else {
                    upload();
                }
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPictureIntent = new Intent(Intent.ACTION_PICK);
                getPictureIntent.setType("image/*");
                try {
                    getGallery.launch(getPictureIntent);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    getCamera.launch(takePictureIntent);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal = showDateTimePicker();
                String stringDate = null;
                Locale locale = Locale.getDefault();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    stringDate = DateFormat
                            .getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale)
                            .format(cal.getTime());
                }
                btnDatePicker.setText(stringDate);
                Log.d(TAG, cal.toString());

            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    public void publishFood(String imageUrl, String fileName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        String userName = user.getDisplayName();
        Timestamp timestamp = new Timestamp(cal.getTime());

        Map<String, Object> food = new HashMap<>();
        String name = etFoodName.getText().toString();
        food.put("date", Timestamp.now());
        food.put("description", etDescription.getText().toString());
        food.put("expiration", timestamp);
        food.put("foodLocation", etPlace.getText().toString());
        food.put("imageUrl", imageUrl);
        food.put("name", etFoodName.getText().toString());
        food.put("price", Long.valueOf(etPrice.getText().toString()));
        food.put("userId", uid);
        food.put("userName", userName);
        food.put("fileName", fileName);
        food.put("message", "");

        db.collection("foods")
                .add(food)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "Uploaded successfully!", Toast.LENGTH_SHORT).show();

                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.setCheckedItem(R.id.nav_all_food_btn);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

        Log.d(TAG, food.toString());
    }

    private void upload() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = System.currentTimeMillis()
                + "." + etFoodName
                .getText()
                .toString() + ".jpeg";
        StorageReference temp = storageRef.child(fileName);

        ivFoodPic.setDrawingCacheEnabled(true);
        ivFoodPic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) ivFoodPic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        temp.putBytes(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failure
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbUpload.setProgress(0);
                            }
                        }, 1000);
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        //createNewPost(imageUrl);

                                        publishFood(imageUrl, fileName);
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pbUpload.setProgress((int) progress);
                    }
                });
    }

    public Calendar showDateTimePicker() {
        Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The chosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        return date;
    }
}