package be.uhasselt.drain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnBottle;
    private Button btnGlass;
    private Button btnCan;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DrinkProfile drinkProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        btnBottle = (Button) findViewById(R.id.btn_add_bottle);
        btnGlass = (Button) findViewById(R.id.btn_add_glass);
        btnCan = (Button) findViewById(R.id.btn_add_can);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Home");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        final DatabaseReference myRefDrink = firebaseDatabase.getReference().child("DrinkLists").child(firebaseAuth.getUid());
        myRefDrink.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drinkProfile = dataSnapshot.getValue(DrinkProfile.class);
                Calendar calendar = Calendar.getInstance();
                int dayNow = calendar.get(Calendar.DAY_OF_YEAR);
                int dayStart = drinkProfile.getStartDate();
                int day = (dayNow - dayStart) + 1;
                drinkProfile.setDay(day);
                myRefDrink.setValue(drinkProfile);
                progressDialog.dismiss();
                progressBar.setProgress((int) drinkProfile.getAmountDrankInPercentage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        btnBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drinkProfile.getDrinkList() == null) {
                    ArrayList<Drink> drinkArrayList = new ArrayList<>();
                    drinkProfile.setDrinkList(drinkArrayList);
                }
                drinkProfile.addBottle();
                myRefDrink.setValue(drinkProfile);
                progressBar.setProgress((int) drinkProfile.getAmountDrankInPercentage());
                Toast.makeText(MainActivity.this, "Bottle added", Toast.LENGTH_SHORT).show();
            }
        });

        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drinkProfile.getDrinkList() == null) {
                    ArrayList<Drink> drinkArrayList = new ArrayList<>();
                    drinkProfile.setDrinkList(drinkArrayList);
                }
                drinkProfile.addGlass();
                progressBar.setProgress((int) drinkProfile.getAmountDrankInPercentage());
                myRefDrink.setValue(drinkProfile);
                progressBar.setProgress((int) drinkProfile.getAmountDrankInPercentage());
                Toast.makeText(MainActivity.this, "Glass added", Toast.LENGTH_SHORT).show();
            }
        });

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drinkProfile.getDrinkList() == null) {
                    ArrayList<Drink> drinkArrayList = new ArrayList<>();
                    drinkProfile.setDrinkList(drinkArrayList);
                }
                drinkProfile.addCan();
                progressBar.setProgress((int) drinkProfile.getAmountDrankInPercentage());
                myRefDrink.setValue(drinkProfile);
                progressBar.setProgress((int) drinkProfile.getAmountDrankInPercentage());
                Toast.makeText(MainActivity.this, "Can added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_list:
                Intent intentList = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intentList);
                break;
            case R.id.nav_stats:
                Intent intentStats = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intentStats);
                break;

            case R.id.nav_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;

            case R.id.nav_profile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                finish();
                Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogout);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
