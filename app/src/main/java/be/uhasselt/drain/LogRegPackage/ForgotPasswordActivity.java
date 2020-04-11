package be.uhasselt.drain.LogRegPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import be.uhasselt.drain.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button passwordReset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        passwordEmail = (EditText) findViewById(R.id.et_pw_email);
        passwordReset = (Button) findViewById(R.id.btn_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();

        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = passwordEmail.getText().toString().trim();

                if (userEmail.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Password reset email has been sent", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
