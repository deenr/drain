package be.uhasselt.drain;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private BarGraphSeries<DataPoint> series;

    private Button btnSetWeek, btnSetMonth;
    private GraphView graph;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DrinkProfile drinkProfile;

    RecyclerView recyclerView;

    public StatisticsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        btnSetWeek = (Button) findViewById(R.id.btn_set_week);
        btnSetMonth = (Button) findViewById(R.id.btn_set_month);
        graph = (GraphView) findViewById(R.id.graph_view);
        recyclerView = (RecyclerView) findViewById(R.id.stat_recycler_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference myRefDrink = firebaseDatabase.getReference().child("DrinkLists").child(firebaseAuth.getUid());
        myRefDrink.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drinkProfile = dataSnapshot.getValue(DrinkProfile.class);
                progressDialog.dismiss();

                if (drinkProfile.getDrinkList() != null) {
                    makeGraph(drinkProfile.getDay(), 7);
                    createStringList(drinkProfile.getDay(), 7);
                } else {
                    graph.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatisticsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        btnSetWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkProfile.getDrinkList() != null) {
                    makeGraph(drinkProfile.getDay(), 7);
                    createStringList(drinkProfile.getDay(), 7);
                }
            }
        });

        btnSetMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkProfile.getDrinkList() != null) {
                    makeGraph(drinkProfile.getDay(), 31);
                    createStringList(drinkProfile.getDay(), 31);
                }
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Statistics");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeGraph(int day, int amount) {

        double[] x = new double[amount];
        double[] y = new double[amount];

        if (amount == 7 && day <= 7) {
            for (int i = 1; i <= amount; i++) {
                double j = 0;
                ArrayList<Drink> drinkArrayList = drinkProfile.getDrinkList();
                for (Drink d : drinkArrayList) {
                    if (d.getDay() == i) {
                        j = j + d.getAmount();
                    }
                }
                x[i - 1] = i;
                y[i - 1] = j;
            }
        } else if (amount == 7) {
            for (int i = day - amount + 1; i <= day; i++) {
                double j = 0;
                ArrayList<Drink> drinkArrayList = drinkProfile.getDrinkList();
                for (Drink d : drinkArrayList) {
                    if (d.getDay() == i) {
                        j = j + d.getAmount();
                    }
                }
                x[i - day + amount - 1] = i;
                y[i - day + amount - 1] = j;
            }
        } else if (amount == 31 && day <= 31) {
            for (int i = 1; i <= amount; i++) {
                double j = 0;
                ArrayList<Drink> drinkArrayList = drinkProfile.getDrinkList();
                for (Drink d : drinkArrayList) {
                    if (d.getDay() == i) {
                        j = j + d.getAmount();
                    }
                }
                x[i - 1] = i;
                y[i - 1] = j;
            }
        } else {
            for (int i = day - amount + 1; i <= day; i++) {
                double j = 0;
                ArrayList<Drink> drinkArrayList = drinkProfile.getDrinkList();
                for (Drink d : drinkArrayList) {
                    if (d.getDay() == i) {
                        j = j + d.getAmount();
                    }
                }
                x[i - day + amount - 1] = i;
                y[i - day + amount - 1] = j;
            }
        }

        series = new BarGraphSeries<>(data(x, y, amount));

        // styling series
        series.setColor(getResources().getColor(R.color.Blue2));
        graph.addSeries(series);

        if (amount == 7 && day <= 7) {
            graph.getViewport().setMinX(series.getLowestValueX() - .5);
            graph.getViewport().setMaxX(series.getHighestValueX() + .5);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(series.getHighestValueY() + 500);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.setVisibility(View.VISIBLE);
        } else if (amount == 7) {
            graph.getViewport().setMinX(day - amount + 1 - .5);
            graph.getViewport().setMaxX(series.getHighestValueX() + .5);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(series.getHighestValueY() + 500);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.setVisibility(View.VISIBLE);
        } else if (amount == 31 && day <= 31) {
            graph.getViewport().setMinX(series.getLowestValueX() - .5);
            graph.getViewport().setMaxX(series.getHighestValueX() + .5);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(series.getHighestValueY() + 500);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.setVisibility(View.VISIBLE);
        } else {
            graph.getViewport().setMinX(day - amount + 1 - .5);
            graph.getViewport().setMaxX(series.getHighestValueX() + .5);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(series.getHighestValueY() + 500);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.setVisibility(View.VISIBLE);
        }

        // styling graph
    }

    public DataPoint[] data(double[] x, double[] y, int amount) {

        DataPoint[] values = new DataPoint[amount];     //creating an object of type DataPoint[] of size 'n'
        for (int i = 0; i < amount; i++) {
            DataPoint v = new DataPoint(x[i], y[i]);
            values[i] = v;
        }
        return values;
    }

    public void createStringList(int day, int amount) {
        ArrayList<Drink> drinkList = drinkProfile.getDrinkList();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();

        if (amount == 7 && day <= 7) {
            for (int i = 1; i <= amount; i++) {
                int value = 0;
                title.add("Day " + i);
                for (Drink d : drinkList) {
                    if (d.getDay() == i) {
                        value = value + d.getAmount();
                    }
                }
                description.add(value + " ml");
            }
        } else if (amount == 7) {
            for (int i = day - amount + 1; i <= day; i++) {
                int value = 0;
                title.add("Day " + i);
                for (Drink d : drinkList) {
                    if (d.getDay() == i) {
                        value = value + d.getAmount();
                    }
                }
                description.add(value + " ml");
            }
        } else if (amount == 31 && day <= 31) {
            for (int i = 1; i <= amount; i++) {
                int value = 0;
                title.add("Day " + i);
                for (Drink d : drinkList) {
                    if (d.getDay() == i) {
                        value = value + d.getAmount();
                    }
                }
                description.add(value + " ml");
            }
        } else {
            for (int i = day - amount + 1; i <= day; i++) {
                int value = 0;
                title.add("Day " + i);
                for (Drink d : drinkList) {
                    if (d.getDay() == i) {
                        value = value + d.getAmount();
                    }
                }
                description.add(value + " ml");
            }
        }

        StatAdapter statAdapter = new StatAdapter(StatisticsActivity.this, title, description);
        recyclerView.setAdapter(statAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(StatisticsActivity.this));
    }
}
