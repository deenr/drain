package be.uhasselt.drain;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import be.uhasselt.drain.ListPackage.ListActivity;
import be.uhasselt.drain.LogRegPackage.LoginActivity;
import be.uhasselt.drain.ProfilePackage.ProfileActivity;
import be.uhasselt.drain.Profiles.Drink;
import be.uhasselt.drain.Profiles.DrinkProfile;
import be.uhasselt.drain.SettingsPackage.SettingsActivity;
import be.uhasselt.drain.StatisticsPackage.StatisticsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private Button btnBottle, btnGlass, btnCan;
    private TextView txtTodayStats, txtYesterday, txtYesterdayStats, txtWeather, txtWeatherCity, txtWeatherDegrees;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DrinkProfile drinkProfile;
    LocationManager locationManager;
    String provider;
    String urlString;

    int MY_PERMISSION = 0;

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

        txtTodayStats = (TextView) findViewById(R.id.tv_today_stats);

        txtYesterday = (TextView) findViewById(R.id.tv_yesterday);
        txtYesterdayStats = (TextView) findViewById(R.id.tv_yesterday_stats);

        txtWeather = (TextView) findViewById(R.id.tv_weather);
        txtWeatherCity = (TextView) findViewById(R.id.tv_weather_city);
        txtWeatherDegrees = (TextView) findViewById(R.id.tv_weather_degrees);

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
                if (drinkProfile.getDrinkList() != null) {
                    myRefDrink.setValue(drinkProfile);
                }
                updateStats();
                progressDialog.dismiss();
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
                updateStats();
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
                myRefDrink.setValue(drinkProfile);
                updateStats();
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
                myRefDrink.setValue(drinkProfile);
                updateStats();
                Toast.makeText(MainActivity.this, "Can added", Toast.LENGTH_SHORT).show();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            Log.e("TAG", "No Location");
        }
        setWeather(location);
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

    private void updateStats() {
        if (drinkProfile.getDrinkList() != null) {
            double amountDrankInPercentage = drinkProfile.getAmountDrankInPercentage();
            progressBar.setProgress((int) amountDrankInPercentage);

            int minimumAmount = drinkProfile.getAmountPerDay();

            int today = drinkProfile.getDay();
            int amountDrankToday = drinkProfile.getAmountOfDay(today);
            txtTodayStats.setText(amountDrankToday + " ml / " + minimumAmount + " ml");

            int yesterday = drinkProfile.getDay() - 1;
            int amountDrankYesterday = drinkProfile.getAmountOfDay(yesterday);
            if (yesterday == 0) {
                txtYesterday.setVisibility(View.INVISIBLE);
                txtYesterdayStats.setVisibility(View.INVISIBLE);
            } else {
                txtYesterdayStats.setText(amountDrankYesterday + " ml / " + minimumAmount + " ml");
                txtYesterday.setVisibility(View.VISIBLE);
                txtYesterdayStats.setVisibility(View.VISIBLE);
            }
        } else {
            int minimumAmount = drinkProfile.getAmountPerDay();
            txtTodayStats.setText(0 + " ml / " + minimumAmount + " ml");

            int yesterday = drinkProfile.getDay() - 1;
            if (yesterday == 0) {
                txtYesterday.setVisibility(View.INVISIBLE);
                txtYesterdayStats.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION);
        }

        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION);
        }

        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        setWeather(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class JsonTask extends AsyncTask<Void, Void, String> {
        String s;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                Log.e("JSON", builder.toString());
                s = builder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);

                    String city = jsonObject.getString("name");
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    String temp = mainObject.getString("temp");
                    double tempInteger = Double.parseDouble(temp);

                    txtWeatherCity.setText(city);
                    txtWeatherDegrees.setText(String.format("%.0f °C", tempInteger));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public String getWeatherURL(double lat, double lng) {
        return "https://api.openweathermap.org/data/2.5/weather" + String.format("?lat=%s&lon=%s&APPID=%s&units=metric", lat, lng, "874a37d5e38723de1afd44c12d274c3e");
    }

    public void setWeather(Location location) {
        if (location == null) {
            txtWeather.setVisibility(View.INVISIBLE);
            txtWeatherCity.setVisibility(View.INVISIBLE);
            txtWeatherDegrees.setVisibility(View.INVISIBLE);
        } else {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            urlString = getWeatherURL(lat, lng);
            txtWeather.setVisibility(View.VISIBLE);
            txtWeatherCity.setVisibility(View.VISIBLE);
            txtWeatherDegrees.setVisibility(View.VISIBLE);
            new JsonTask().execute();
        }
    }
}
