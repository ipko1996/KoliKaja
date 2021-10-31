package hu.bugs.kolikaja;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

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
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        initNavView(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_all_food_btn:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AllFood())
                                .commit();
                        break;
                    case R.id.nav_add_food_btn:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AddFood())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_remove_food_btn:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new RemoveFood())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_log_out_btn:
                        signOut();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Not implemented yet",
                                Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Else it would show AllFood fragment after rotate
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AllFood())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_all_food_btn);
        }
    }

    private void initNavView(NavigationView navigationView) {
        FirebaseAuth data = FirebaseAuth.getInstance();
        FirebaseUser user = data.getCurrentUser();
        String userName = user.getDisplayName();
        String email = user.getEmail();
        Uri photo = user.getPhotoUrl();
        View header = navigationView.getHeaderView(0);
        TextView userNameText = header.findViewById(R.id.nav_username);
        userNameText.setText(userName);
        TextView emailText = header.findViewById(R.id.nav_email);
        emailText.setText(email);
        ImageView imageView = header.findViewById(R.id.nav_image);
        Glide.with(this).load(photo).circleCrop().into(imageView);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AuthActivity.class);

        //make sure user can't go back
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen((GravityCompat.START))) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}