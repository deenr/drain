package be.uhasselt.drain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail, userName, userPassword, userAge, userWeight;
    private Button btnRegister;
    private TextView tvDoHaveAccount;

    String email, name, password, age, weight;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    //Upload data to the datebase
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        tvDoHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUIViews() {
        userEmail = (EditText) findViewById(R.id.et_user_email);
        userName = (EditText) findViewById(R.id.et_user_name);
        userPassword = (EditText) findViewById(R.id.et_user_password);
        userAge = (EditText) findViewById(R.id.et_user_age);
        userWeight = (EditText) findViewById(R.id.et_user_weight);

        btnRegister = (Button) findViewById(R.id.btn_register);
        tvDoHaveAccount = (TextView) findViewById(R.id.do_have_account);
    }

    private Boolean validate() {
        email = userEmail.getText().toString();
        name = userName.getText().toString();
        password = userPassword.getText().toString();;
        age = userAge.getText().toString();
        weight = userWeight.getText().toString();

        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void sendEmailVerification() {
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(RegisterActivity.this,"Successfully registered, verification mail has been sent.", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this,"Verification mail hasn't been sent.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference myRefUser = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(name, email, age, weight);
        myRefUser.setValue(userProfile);

        DatabaseReference myRefDrink = firebaseDatabase.getReference().child("DrinkLists").child(firebaseAuth.getUid());
        DrinkProfile drinkProfile = new DrinkProfile(Integer.valueOf(weight));
        myRefDrink.setValue(drinkProfile);
    }
}
