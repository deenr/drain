package be.uhasselt.drain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeProfileActivity extends AppCompatActivity {

    private EditText newEmail, newUsername, newWeight, newAge;
    private Button btnChange;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        newEmail = (EditText) findViewById(R.id.et_change_email);
        newUsername = (EditText) findViewById(R.id.et_change_username);
        newWeight = (EditText) findViewById(R.id.et_change_weight);
        newAge = (EditText) findViewById(R.id.et_change_age);
        btnChange = (Button) findViewById(R.id.btn_change);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference databaseRefUser = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                assert userProfile != null;
                newEmail.setText(userProfile.getUserEmail());
                newUsername.setText(userProfile.getUserName());
                newWeight.setText(userProfile.getUserWeight());
                newAge.setText(userProfile.getUserAge());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChangeProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = newEmail.getText().toString();
                String name = newUsername.getText().toString();
                String weight = newWeight.getText().toString();
                String age = newAge.getText().toString();

                UserProfile userProfile = new UserProfile(name, email, age, weight);
                databaseRefUser.setValue(userProfile);

                DatabaseReference databaseRefDrink = firebaseDatabase.getReference().child("DrinkLists").child(firebaseAuth.getUid());
                Map<String, Object> mapWeight = new HashMap<>();
                mapWeight.put("weight",Integer.parseInt(weight));
                mapWeight.put("amountPerDay",Integer.parseInt(weight)*30);
                databaseRefDrink.updateChildren(mapWeight);


                firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeProfileActivity.this, "Profile change successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangeProfileActivity.this, "Profile change failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
