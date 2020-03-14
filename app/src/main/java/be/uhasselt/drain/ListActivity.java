package be.uhasselt.drain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[], s2[];
    int images[] = {R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_person};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recycler_view);

        s1 = getResources().getStringArray(R.array.daily_water);
        s2 = getResources().getStringArray(R.array.description);

        MyAdapter myAdapter = new MyAdapter(this, s1, s2, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
