package be.uhasselt.drain.ListPackage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import be.uhasselt.drain.Profiles.Drink;
import be.uhasselt.drain.Profiles.DrinkProfile;
import be.uhasselt.drain.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private ProgressDialog progressDialog;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<Drink> list;
    ListAdapter myAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DrinkProfile drinkProfile;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        list = new ArrayList<Drink>();

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
                Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference = firebaseDatabase.getReference().child("DrinkLists").child(firebaseAuth.getUid()).child("drinkList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Drink p = dataSnapshot1.getValue(Drink.class);
                    assert p != null;
                    if (p.getDay() == drinkProfile.getDay()) {
                        list.add(p);
                    }
                }
                if (getActivity() != null) {
                    myAdapter = new ListAdapter(getActivity(), list);
                    recyclerView.setAdapter(myAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Value Event Error", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


}
