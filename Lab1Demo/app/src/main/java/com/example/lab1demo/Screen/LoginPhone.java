package com.example.lab1demo.Screen;

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

import com.example.lab1demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhone extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;

    EditText edtPhone;
    EditText edtOtp;
    Button btnLogPhone;
    Button btnGetOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        mAuth = FirebaseAuth.getInstance();
        edtPhone = findViewById(R.id.edtPhone);
        edtOtp = findViewById(R.id.edtOTP);
        btnLogPhone = findViewById(R.id.btnLogPhone);
        btnGetOTP = findViewById(R.id.btngetOTP);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // Xác thực thành công, ghi log
                Log.e("PhoneAuth", "Verification completed");
            }

            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Xác thực thất bại, ghi log
                Log.e("PhoneAuth", "Verification failed: " + e.getMessage());
            }

            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // Mã OTP đã được gửi, ghi log
                Log.d("PhoneAuth", "Code sent: " + s);
                mVerificationId = s;
            }

        };
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhone.getText().toString().trim();
                getOTP(phoneNumber);
//                Toast.makeText(LoginPhone.this, "OKk" + phoneNumber, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onClick: " + phoneNumber);
            }
        });
        btnLogPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOtp.getText().toString().trim();
                verifyOTP(otp);
            }
        });
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
//        Toast.makeText(this, "dm" + phoneNumber, Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.verifyPhoneNumber(options);
        Log.e(TAG, "SĐT: " + phoneNumber);
    }

    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
        Log.e(TAG, "OTP huhuhuhu: " + otp);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhprivation thành công, chuy intervene sang trang Home
                            Toast.makeText(LoginPhone.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(LoginPhone.this, Home.class));
                        } else {
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(LoginPhone.this, "OTP sai", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }
}