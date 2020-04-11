package be.uhasselt.drain.ListPackage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import be.uhasselt.drain.R;

public class ListDetailActivity extends AppCompatActivity {

    ListDetailFragment listDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        this.listDetailFragment = (ListDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
    }
}
