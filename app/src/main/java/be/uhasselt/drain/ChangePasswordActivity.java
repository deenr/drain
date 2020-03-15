package be.uhasselt.drain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText newPassword;
    private Button btnChangePassword;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = (EditText) findViewById(R.id.et_change_password);
        btnChangePassword = (Button) findViewById(R.id.btn_change_password);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    String userNewPassword = newPassword.getText().toString();
                    firebaseUser.updatePassword(userNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Password change successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Password change failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private Boolean validate() {
        String userNewPassword = newPassword.getText().toString();

        if (userNewPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
