package com.example.lab1demo.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lab1demo.MainActivity;
import com.example.lab1demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText edtEmail;

    EditText edtPass;

    ImageButton btnBack;

    Button btnSigUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmaildk);
        edtPass = findViewById(R.id.edtPassdk);
        btnBack = findViewById(R.id.btnBack);
        btnSigUp = findViewById(R.id.btnSignUp);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
        btnSigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //lAdapterManager
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Đăng Ký thất bại", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

            }
        });
    }
}