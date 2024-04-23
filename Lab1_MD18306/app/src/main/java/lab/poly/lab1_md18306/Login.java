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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText edtUsername = findViewById(R.id.edtUsername);
        TextInputLayout errUser = findViewById(R.id.errUser);
        TextInputEditText edtPassword = findViewById(R.id.edtPassword);
        TextInputLayout errPass = findViewById(R.id.errPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView btnSignup = findViewById(R.id.btnSignup);
        TextView tvFogotPassword = findViewById(R.id.tvForgotPass);

        //hàm khởi tạo tao 1 đối tượng FirebaseAuth
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
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
                    errUser.setError(null);
                } else {
                    val = false;
                    errUser.setError(null);
                    errPass.setError(null);
                }

                if(val){
                     return;
                 }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });

        tvFogotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, FogotPassword.class));
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Toast.makeText(this, "Người dùng đã đăng nhập", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(Login.this, MainActivity.class));
//        }
//    }

//    private void signIn(String email, String password) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(Login.this, user.getEmail(), Toast.LENGTH_SHORT).show();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(Login.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//                    }
//                });
//    }
}