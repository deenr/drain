package be.uhasselt.drain;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText startValue, endValue;
    private Button betnSetValue;
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

        startValue = (EditText) findViewById(R.id.et_start_graph_value);
        endValue = (EditText) findViewById(R.id.et_end_graph_value);
        betnSetValue = (Button) findViewById(R.id.btn_set_graph_value);
        graph = (GraphView) findViewById(R.id.graph_view);
        graph.setVisibility(View.INVISIBLE);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatisticsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        betnSetValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    int startIntValue = Integer.parseInt(startValue.getText().toString());
                    int endIntValue = Integer.parseInt(endValue.getText().toString());

                    double[] x = new double[endIntValue - startIntValue + 1];
                    double[] y = new double[endIntValue - startIntValue + 1];

                    for (int i = startIntValue; i <= endIntValue; i++) {
                        double j = 0;
                        ArrayList<Drink> drinkArrayList = drinkProfile.getDrinkList();
                        for (Drink d : drinkArrayList) {
                            if (d.getDay() == i) {
                                j = j + d.getAmount();
                            }
                        }
                        x[i - startIntValue] = i;
                        y[i - startIntValue] = j;
                    }

                    series = new BarGraphSeries<>(data(x, y));

                    // styling series
                    series.setColor(getResources().getColor(R.color.Blue2));
                    graph.addSeries(series);

                    // styling graph
                    graph.getGridLabelRenderer().setNumHorizontalLabels(endIntValue - startIntValue + 1);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("Day");
                    graph.getViewport().setMinX(series.getLowestValueX());
                    graph.getViewport().setMaxX(series.getHighestValueX());
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(series.getHighestValueY() + 500);
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.setVisibility(View.VISIBLE);
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

    private Boolean validate() {
        String start = startValue.getText().toString();
        String end = endValue.getText().toString();

        if (start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "Please enter the values", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(start) <= 0) {
            Toast.makeText(this, "Start value must be 1 or bigger", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(end) > drinkProfile.getDay()) {
            Toast.makeText(this, "End value must be less days used", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(end) == Integer.parseInt(start)) {
            Toast.makeText(this, "Start value and end value can not be the same", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public DataPoint[] data(double[] x, double[] y) {
        int startIntValue = Integer.parseInt(startValue.getText().toString());
        int endIntValue = Integer.parseInt(endValue.getText().toString());

        DataPoint[] values = new DataPoint[endIntValue - startIntValue + 1];     //creating an object of type DataPoint[] of size 'n'
        for (int i = 0; i < endIntValue - startIntValue + 1; i++) {
            DataPoint v = new DataPoint(x[i], y[i]);
            values[i] = v;
            System.out.println(v.toString());
        }
        return values;
    }
}
