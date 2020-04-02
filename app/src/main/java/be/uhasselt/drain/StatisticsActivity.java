package be.uhasselt.drain;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    public StatisticsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        btnSetWeek = (Button) findViewById(R.id.btn_set_week);
        btnSetMonth = (Button) findViewById(R.id.btn_set_month);
        graph = (GraphView) findViewById(R.id.graph_view);

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

                makeGraph(7);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatisticsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSetWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeGraph(7);
            }
        });

        btnSetMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeGraph(31);
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

    private void makeGraph(int amount) {

        double[] x = new double[amount];
        double[] y = new double[amount];

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

        series = new BarGraphSeries<>(data(x, y, amount));

        // styling series
        series.setColor(getResources().getColor(R.color.Blue2));
        graph.addSeries(series);

        // styling graph
        graph.getViewport().setMinX(series.getLowestValueX() - .5);
        graph.getViewport().setMaxX(series.getHighestValueX() + .5);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(series.getHighestValueY() + 500);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.setVisibility(View.VISIBLE);
    }

    public DataPoint[] data(double[] x, double[] y, int amount) {

        DataPoint[] values = new DataPoint[amount];     //creating an object of type DataPoint[] of size 'n'
        for (int i = 0; i < amount; i++) {
            DataPoint v = new DataPoint(x[i], y[i]);
            values[i] = v;
            System.out.println(v.toString());
        }
        return values;
    }
}
