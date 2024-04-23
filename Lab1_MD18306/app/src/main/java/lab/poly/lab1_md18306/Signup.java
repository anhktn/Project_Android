package lab.poly.lab1_md18306;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextInputEditText edtUsername = findViewById(R.id.edtUsername_su);
        TextInputEditText edtPassword = findViewById(R.id.edtPassword_su);
        TextInputLayout errUser = findViewById(R.id.errUser_su);
        TextInputLayout errPass = findViewById(R.id.errPass_su);
        Button btnSignup = findViewById(R.id.btnSignup_su);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                boolean val = true;
                if (email.isEmpty() && password.isEmpty()) {
                    errUser.setError("Vui lòng nhập email");
                    errPass.setError("Vui lòng nhập mật khẩu");
                } else if (email.isEmpty()) {
                    errUser.setError("Vui lòng nhập email");
                    errPass.setError(null);
                } else if (password.isEmpty()) {
                    errPass.setError("Vui lòng nhập mật khẩu");
                    errPass.setError(null);
                } else {
                    val = false;
                }

                if(val){
                    return;
                }
                signUp(email, password);
            }
        });
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, Login.class));
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
}